package com.jamie.android_ros.arcore_ros.ros.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.GuardedBy;
import android.util.Log;

import com.google.common.collect.ImmutableList;
import com.jamie.android_ros.arcore_ros.ros.data_structures.ImuData;

import java.time.Duration;
import java.util.List;

import static com.jamie.android_ros.arcore_ros.common.Utilities.map;

/** Sensor that retrieves {@code ImuData} updates.
 *
 * @author lorsi96 2020
 * @since 12-07-2020
 */
public class ImuSensor extends BaseSensor<ImuData> {
    private static final short LINEAR_ACCELERATION_RECEIVED = 0b100;
    private static final short ROTATION_RECEIVED = 0b010;
    private static final short ANGULAR_VELOCITY_RECEIVED = 0b001;
    private static final short IMU_DATA_READY = 0b111;
    private static final ImmutableList<Integer> SUPPORTED_SENSORS = ImmutableList.of(
            Sensor.TYPE_LINEAR_ACCELERATION, Sensor.TYPE_ROTATION_VECTOR, Sensor.TYPE_GYROSCOPE
    );
    private static final String TAG = ImuSensor.class.getSimpleName();
    private final SensorListener sensorListener = new SensorListener();
    private final SensorManager mSensorManager;
    private final List<Sensor> sensors;
    private final Duration samplingPeriod; // TODO: use.

    @GuardedBy("this")
    private short imuSensorFlags = 0b000;

    @GuardedBy("this")
    private ImuData dataContainer = new ImuData();

    public ImuSensor(Context context, Duration samplingPeriod) {
        this.samplingPeriod = samplingPeriod;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensors = map(SUPPORTED_SENSORS, mSensorManager::getDefaultSensor);
    }

    @Override
    public void start() {
        Log.d(TAG, "Started");
        for(Sensor s : sensors) {
            mSensorManager.registerListener(sensorListener, s,  SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    synchronized void updateImuData(SensorEvent event) {
        Log.v(TAG, String.format("ImuData received - State %d", imuSensorFlags));
        switch(event.sensor.getType()) {
            case Sensor.TYPE_LINEAR_ACCELERATION:
                imuSensorFlags |= LINEAR_ACCELERATION_RECEIVED;
                dataContainer.setLinearAcceleration(event.values);
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                imuSensorFlags |= ROTATION_RECEIVED;
                dataContainer.setOrientation(event.values);
                break;
            case Sensor.TYPE_GYROSCOPE:
                imuSensorFlags |= ANGULAR_VELOCITY_RECEIVED;
                dataContainer.setAngularVelocity(event.values);
                break;
            default:
                break;
        }
    }

    @Override
    public void stop() {
        mSensorManager.unregisterListener(sensorListener);
    }

    class SensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            updateImuData(event);
            if(imuSensorFlags == IMU_DATA_READY) {
                notifyListeners(dataContainer);
                imuSensorFlags = 0b000; // Clear.
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {} // TODO: Covariance updates.
    }
}
