package com.jamie.android_ros.arcore_ros.ros.publishers;


import android.util.Log;

import com.google.ar.core.Frame;
import com.jamie.android_ros.arcore_ros.common.Utilities;
import com.jamie.android_ros.arcore_ros.ros.converters.DepthImageMessageConverter;

import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import sensor_msgs.Image;

/**
 * Publishes {@code Image} messages based on given {@code Frame} updates.
 *
 * @author Lucas Scheinkerman (LucasJSch)
 * @since 1-04-2021
 */
public class DepthImagePublisher  implements MessagePublisher<Frame> {
    private static final String TAG = CompressedImagePublisher.class.getSimpleName();
    private final Publisher<Image> publisher;
    private final DepthImageMessageConverter converter;
    private Image msg;

    public DepthImagePublisher(ConnectedNode connectedNode,
                                    DepthImageMessageConverter converter,
                                    String topic) {
        this.converter = converter;
        this.publisher = connectedNode.newPublisher(topic, sensor_msgs.Image._TYPE);
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
