package com.jamie.android_ros.arcore_ros.ros.publishers;

import com.jamie.android_ros.arcore_ros.ros.data_structures.ImuData;
import com.jamie.android_ros.arcore_ros.ros.sensors.SensorListener;

interface MessagePublisher<T> {
    void publish(T msg);
}
