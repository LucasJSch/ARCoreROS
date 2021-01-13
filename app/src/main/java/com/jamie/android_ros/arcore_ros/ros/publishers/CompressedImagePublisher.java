package com.jamie.android_ros.arcore_ros.ros.publishers;


import android.util.Log;

import com.google.ar.core.Frame;
import com.jamie.android_ros.arcore_ros.common.Utilities;
import com.jamie.android_ros.arcore_ros.ros.converters.CompressedImageMessageConverter;

import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import sensor_msgs.CompressedImage;

/**
 * Publishes {@code CompressedImage} messages based on given {@code Frame} updates.
 *
 * @author Lucas Scheinkerman (LucasJSch)
 * @since 1-04-2021
 */
public class CompressedImagePublisher  implements MessagePublisher<Frame> {
    private static final String TAG = CompressedImagePublisher.class.getSimpleName();
    private final Publisher<CompressedImage> publisher;
    private final CompressedImageMessageConverter converter;
    private CompressedImage msg;

    public CompressedImagePublisher(ConnectedNode connectedNode,
                                    CompressedImageMessageConverter converter,
                                    String topic) {
        this.converter = converter;
        this.publisher = connectedNode.newPublisher(topic, sensor_msgs.CompressedImage._TYPE);
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
