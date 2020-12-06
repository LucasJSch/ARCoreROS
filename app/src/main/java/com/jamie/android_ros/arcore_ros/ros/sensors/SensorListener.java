package com.jamie.android_ros.arcore_ros.ros.sensors;

public interface SensorListener<T> {
    void onDataReceived(T data);
}
