package com.jamie.android_ros.arcore_ros.ros.converters;

/** Converts raw data as obtained from a specific sensor/source to a ros message.
 * {@code M} stands for a ROS message.
 * {@code D} stands from Raw data.
 * */
public interface DataToRosMessageConverter<M, D> {
    public M toRosMessage(D rawData, M baseMessage);
}
