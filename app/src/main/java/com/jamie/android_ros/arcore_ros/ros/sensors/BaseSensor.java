package com.jamie.android_ros.arcore_ros.ros.sensors;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

/** Common interface for sensors/data collector classes.
 *
 * They provide registering and unregistering listeners. The sensor must then collect {@code T} data
 * and call {@link #notifyListeners} when a new measurement is available.
 *
 * @author lorsi96 2020
 * @since 12-07-2020
 */
public abstract class BaseSensor<T> implements Sensor<T> {
    private final Set<SensorListener<T>> listeners = new HashSet<>();

    @Override
    public void registerListener(SensorListener<T> listener) {
        listeners.add(listener);
    }

    @Override
    public void unregisterListener(SensorListener<T> listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners(T data) {
        for(SensorListener<T> l: listeners) {
            l.onDataReceived(data);
        }
    }
}
