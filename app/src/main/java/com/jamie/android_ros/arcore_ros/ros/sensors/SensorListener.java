package com.jamie.android_ros.arcore_ros.ros.sensors;

/**
 * Listeners to sensor data will have their {@link #onDataReceived} method called each time a new
 * measurement/data is available.
 *
 * @author lorsi96 2020
 * @since 12-07-2020
 */
public interface SensorListener<T> {
    void onDataReceived(T data);
}
