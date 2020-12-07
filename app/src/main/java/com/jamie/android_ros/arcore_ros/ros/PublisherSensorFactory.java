package com.jamie.android_ros.arcore_ros.ros;

import android.content.Context;

import com.google.ar.core.Frame;
import com.jamie.android_ros.arcore_ros.common.LiveData;
import com.jamie.android_ros.arcore_ros.ros.converters.GpsMessageConverter;
import com.jamie.android_ros.arcore_ros.ros.converters.ImuMessageConverter;
import com.jamie.android_ros.arcore_ros.ros.converters.OdometryMessageConverter;
import com.jamie.android_ros.arcore_ros.ros.publishers.GpsPublisher;
import com.jamie.android_ros.arcore_ros.ros.publishers.ImuPublisher;
import com.jamie.android_ros.arcore_ros.ros.publishers.OdometryPublisher;
import com.jamie.android_ros.arcore_ros.ros.sensors.GpsSensor;
import com.jamie.android_ros.arcore_ros.ros.sensors.ImuSensor;
import com.jamie.android_ros.arcore_ros.ros.sensors.OdometrySensor;

import org.ros.node.ConnectedNode;

/** Creates {@link PublisherSensor} instances. */
public class PublisherSensorFactory {
    public static PublisherSensor createImu(ConnectedNode node, Context context) {
        return new PublisherSensor(
                new ImuSensor(context, null),
                new ImuPublisher(node, new ImuMessageConverter(), "android/imu"));
    }

    public static PublisherSensor createGps(ConnectedNode node, Context context) {
        return new PublisherSensor(
                new GpsSensor(context, 10_000),
                new GpsPublisher(node, new GpsMessageConverter(), "android/gps"));
    }

    public static PublisherSensor createOdom(ConnectedNode node, LiveData<Frame> liveFrame) {
        return new PublisherSensor(
                new OdometrySensor(liveFrame, null),
                new OdometryPublisher(node, new OdometryMessageConverter(), "android/odom" ));
    }
}
