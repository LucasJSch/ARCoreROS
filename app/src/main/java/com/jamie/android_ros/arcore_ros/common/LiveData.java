package com.jamie.android_ros.arcore_ros.common;

import java.util.HashSet;
import java.util.Set;

/** *
 * Simplified observer pattern to wrap a {@code T} type and have observers notified each time
 * it changes.
 *
 * @author lorsi96 2020
 * @since 12-07-2020
 */
public class LiveData<T> {
    private T data;
    private Set<Observer<T>> listeners = new HashSet<>();

    public synchronized void setValue(T data) {
        this.data = data;
        notifyChanged();
    }

    public T getValue() {
        return this.data;
    }

    public void observe(Observer<T> onChanged) {
        listeners.add(onChanged);
    }

    public void removeObserver(Observer onChanged) {
        listeners.remove(onChanged);
    }

    private void notifyChanged() {
        for(Observer<T> listener: listeners) {
            listener.onChanged(data);
        }
    }

    public interface Observer<T> {
        void onChanged(T data);
    }
}
