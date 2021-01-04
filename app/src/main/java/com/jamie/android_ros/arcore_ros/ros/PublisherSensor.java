package com.jamie.android_ros.arcore_ros.ros;

import android.util.Log;

import com.jamie.android_ros.arcore_ros.ros.publishers.MessagePublisher;
import com.jamie.android_ros.arcore_ros.ros.sensors.Sensor;

/** Provides an encapsulated instance and simplified interface of a {@code Sensor} and a
 * {@code MessagePublisher} that work together.
 *
 * @author lorsi96 2020
 * @since 12-07-2020
 */
public class PublisherSensor {
    private static final String TAG = PublisherSensor.class.getSimpleName();

    MessagePublisher mp;
    Sensor s;
    public PublisherSensor(Sensor s, MessagePublisher mp) {
        Log.d(TAG, "Created.");
        this.s = s;
        this.mp = mp;
    }
    void startPublishing() {
        Log.d(TAG, "Started publishing");
        s.registerListener(mp);
        s.start();
    }

    void stopPublishing() {
        Log.d(TAG, "Stopped publishing");
        s.unregisterListener(mp);
        s.stop();
    }
}
