package com.jamie.android_ros.arcore_ros.ros.publishers;

import com.jamie.android_ros.arcore_ros.ros.data_structures.ImuData;
import com.jamie.android_ros.arcore_ros.ros.sensors.SensorListener;

/**
 * Receives sensor data, represented by {@code D} and publishes it to a ROS topic having previuosly
 * converted it to a proper ROS message by using a {@code DataToRosMessageConverter}.
 *
 * TODO: Message publishers should receive a message directly. Currently they are unnecessarily
 * coupled with converters.
 *
 * @author lorsi96 2020
 * @since 12-07-2020
 */
public interface MessagePublisher<D> extends SensorListener<D> {
    void publish(D msg);
}
