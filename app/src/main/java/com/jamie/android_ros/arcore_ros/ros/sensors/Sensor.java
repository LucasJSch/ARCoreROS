package com.jamie.android_ros.arcore_ros.ros.sensors;

public interface Sensor<T> {
    void start();

    void stop();

    void registerListener(SensorListener<T> listener);

    void unregisterListener(SensorListener<T> listener);

    void notifyListeners(T data);
}
