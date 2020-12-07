package com.jamie.android_ros.arcore_ros.ros.sensors;

import com.google.ar.core.Camera;
import com.google.ar.core.Frame;
import com.google.ar.core.Pose;
import com.google.ar.core.TrackingState;
import com.jamie.android_ros.arcore_ros.common.LiveData;
import com.jamie.android_ros.arcore_ros.ros.converters.OdometryMessageConverter;

import java.time.Duration;

import javax.annotation.Nullable;

/** Sensor that retrieves {@code Pose} updates.
 *
 * As suggested for ARCore dependent sensors, it observes a {@code Frame} object via this codebase's
 * {@code LiveData} class.
 *
 * @author lorsi96 2020
 * @since 12-07-2020
 */
public class OdometrySensor extends BaseSensor<Pose> implements LiveData.Observer<Frame> {
    private final Duration samplingPeriod;
    private final LiveData<Frame> liveFrame;
    private @Nullable Pose deviceToPhysical;

    public OdometrySensor(LiveData<Frame> liveFrame,
                          Duration samplingPeriod) {
        this.liveFrame = liveFrame;
        this.samplingPeriod = samplingPeriod;
    }

    @Override
    public void start() {
        liveFrame.observe(this);
    }

    @Override
    public void stop() {
        liveFrame.removeObserver(this);
    }

    @Override
    public void onChanged(Frame frame) {
        Camera camera = liveFrame.getValue().getCamera();
        if(camera.getTrackingState() == TrackingState.TRACKING){
            Pose pose = camera.getPose();
            if(deviceToPhysical == null) {
                deviceToPhysical = pose.inverse();
            }
            if(deviceToPhysical != null){
                Pose cameraPose = deviceToPhysical.compose(pose);
                notifyListeners(cameraPose);
            }
        }
    }
}
