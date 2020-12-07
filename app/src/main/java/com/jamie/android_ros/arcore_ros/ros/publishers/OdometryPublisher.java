package com.jamie.android_ros.arcore_ros.ros.publishers;

import com.google.ar.core.Pose;
import com.jamie.android_ros.arcore_ros.common.Utilities;
import com.jamie.android_ros.arcore_ros.ros.converters.OdometryMessageConverter;

import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import nav_msgs.Odometry;


/**
 * Publishes {@code Odometry} messages based on given {@code Pose} updates.
 *
 * @author lorsi96 2020
 * @since 12-07-2020
 */
public class OdometryPublisher implements MessagePublisher<Pose> {
    private static final String HEADER_FRAME = "odom";
    private static final String CHILD_FRAME = "android";
    private final Publisher<Odometry> publisher;
    private final OdometryMessageConverter converter;
    private Odometry msg;

    public OdometryPublisher(ConnectedNode connectedNode, OdometryMessageConverter converter, String topic) {
        this.converter = converter;
        this.publisher = connectedNode.newPublisher(topic, Odometry._TYPE);
        this.msg = publisher.newMessage();
    }

    @Override
    public void publish(Pose pose) {
        msg = converter.toRosMessage(pose, msg);
        Utilities.setHeader(msg.getHeader(), HEADER_FRAME);
        msg.setChildFrameId(CHILD_FRAME);
        publisher.publish(msg);
    }

    @Override
    public void onDataReceived(Pose data) {
        publish(data);
    }
}
