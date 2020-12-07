package com.jamie.android_ros.arcore_ros.ros.sensors;

/**
 * Sensor interface to be implemented by raw sensors.
 *
 * @author lorsi96 2020
 * @since 12-07-2020
 */
public interface Sensor<T> {
    void start();

    void stop();

    void registerListener(SensorListener<T> listener);

    void unregisterListener(SensorListener<T> listener);

    void notifyListeners(T data);
}
