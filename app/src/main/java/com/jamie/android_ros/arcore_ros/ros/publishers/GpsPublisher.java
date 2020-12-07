package com.jamie.android_ros.arcore_ros.ros.publishers;

import android.location.Location;

import com.jamie.android_ros.arcore_ros.common.Utilities;
import com.jamie.android_ros.arcore_ros.ros.converters.GpsMessageConverter;

import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import sensor_msgs.NavSatFix;
import sensor_msgs.NavSatStatus;

/**
 * Publishes {@code NavSatFix} messages based on given {@code Location} updates.
 * @author lorsi96 2020
 * @since 12-07-2020
 */
public class GpsPublisher implements MessagePublisher<Location> {

    private final Publisher<NavSatFix> publisher;
    private final GpsMessageConverter converter;
    private NavSatFix msg;

    public GpsPublisher(ConnectedNode connectedNode, GpsMessageConverter converter, String topic) {
        this.converter = converter;
        this.publisher = connectedNode.newPublisher(topic, NavSatFix._TYPE);
        this.msg = publisher.newMessage();
        initialize();
    }

    private void initialize(){
        msg.getStatus().setService(NavSatStatus.SERVICE_GPS);
        msg.getStatus().setService(NavSatStatus.STATUS_FIX);
    }

    @Override
    public void publish(Location location) {
        msg = converter.toRosMessage(location, msg);
        Utilities.setHeader(msg.getHeader()); // populate header
        publisher.publish(msg);
    }

    @Override
    public void onDataReceived(Location data) {
        publish(data);
    }
}
