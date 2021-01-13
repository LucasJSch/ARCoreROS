package com.jamie.android_ros.arcore_ros.ros.publishers;

import android.util.Log;

import com.google.ar.core.Frame;
import com.jamie.android_ros.arcore_ros.common.Utilities;
import com.jamie.android_ros.arcore_ros.ros.converters.CameraInfoMessageConverter;

import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import sensor_msgs.CameraInfo;

/**
 * Publishes {@code CameraInfo} messages based on given {@code Frame} updates.
 *
 * @author Lucas Scheinkerman (LucasJSch)
 * @since 1-04-2021
 */
public class CameraInfoPublisher implements MessagePublisher<Frame> {
    private static final String TAG = CameraInfoPublisher.class.getSimpleName();
    private final Publisher<CameraInfo> publisher;
    private final CameraInfoMessageConverter converter;
    private CameraInfo msg;

    public CameraInfoPublisher(ConnectedNode connectedNode,
                               CameraInfoMessageConverter converter,
                                    String topic) {
        this.converter = converter;
        this.publisher = connectedNode.newPublisher(topic, sensor_msgs.CameraInfo._TYPE);
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
