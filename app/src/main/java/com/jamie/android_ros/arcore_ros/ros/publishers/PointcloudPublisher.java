package com.jamie.android_ros.arcore_ros.ros.publishers;

import android.util.Log;

import com.google.ar.core.Frame;
import com.jamie.android_ros.arcore_ros.common.Utilities;
import com.jamie.android_ros.arcore_ros.ros.converters.PointcloudMessageConverter;

import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import sensor_msgs.PointCloud2;

/**
 * Publishes {@code Pointcloud2} messages based on given {@code Frame} updates.
 *
 * @authors elector102, lorsi96 and LucasJSch
 * @since 1-04-2021
 */
public class PointcloudPublisher implements MessagePublisher<Frame> {
    private static final String TAG = CompressedImagePublisher.class.getSimpleName();
    private final Publisher<PointCloud2> publisher;
    private final PointcloudMessageConverter converter;
    private PointCloud2 msg;

    public PointcloudPublisher(ConnectedNode connectedNode,
                               PointcloudMessageConverter converter,
                               String topic) {
        this.converter = converter;
        this.publisher = connectedNode.newPublisher(topic, sensor_msgs.PointCloud2._TYPE);
        this.msg = publisher.newMessage();
    }

    @Override
    public void publish(Frame frame) {
        Log.v(TAG, "Publishing");
        msg = converter.toRosMessage(frame, msg);
        Utilities.setHeader(msg.getHeader());
        publisher.publish(msg);
    }

    @Override
    public void onDataReceived(Frame frame) {
        publish(frame);
    }
}
