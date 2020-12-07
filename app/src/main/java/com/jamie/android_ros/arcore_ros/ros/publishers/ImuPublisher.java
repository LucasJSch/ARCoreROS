package com.jamie.android_ros.arcore_ros.ros.publishers;
import android.util.Log;

import com.jamie.android_ros.arcore_ros.common.Utilities;
import com.jamie.android_ros.arcore_ros.ros.converters.ImuMessageConverter;
import com.jamie.android_ros.arcore_ros.ros.data_structures.ImuData;

import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import sensor_msgs.Imu;

/**
 * Updated and refactored by lorsi96 on 7-12-20.
 * Created by jamiecho on 2/4/17.
 */
public class ImuPublisher implements MessagePublisher<ImuData> {
    private static final String TAG = ImuPublisher.class.getSimpleName();
    private final Publisher<Imu> publisher;
    private final ImuMessageConverter converter;
    private Imu msg;

    public ImuPublisher(ConnectedNode connectedNode, ImuMessageConverter converter, String topic) {
        this.converter = converter;
        this.publisher = connectedNode.newPublisher(topic, sensor_msgs.Imu._TYPE);
        this.msg = publisher.newMessage();
    }
    public void publish(ImuData data) {
        Log.v(TAG, "Publishing");
        msg = converter.toRosMessage(data, msg);
        Utilities.setHeader(msg.getHeader()); // populate header
        publisher.publish(msg);
    }

    @Override
    public void onDataReceived(ImuData data) {
        publish(data);
    }
}