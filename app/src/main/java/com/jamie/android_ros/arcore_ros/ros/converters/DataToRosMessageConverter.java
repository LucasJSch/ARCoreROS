package com.jamie.android_ros.arcore_ros.ros.converters;

/** Converts raw data as obtained from a specific sensor/source to a ros message.
 * {@code M} stands for a ROS message.
 * {@code D} stands from Raw data.
 *
 * @author lorsi96 2020
 * @since 12-07-2020
 */
public interface DataToRosMessageConverter<M, D> {
    M toRosMessage(D rawData, M baseMessage);
}
