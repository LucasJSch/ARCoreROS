package com.jamie.android_ros.arcore_ros.ros.sensors;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

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
