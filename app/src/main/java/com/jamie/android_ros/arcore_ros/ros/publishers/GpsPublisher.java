package com.jamie.android_ros.arcore_ros.ros.publishers;

import android.location.Location;

import com.jamie.android_ros.arcore_ros.ros.Utilities;
import com.jamie.android_ros.arcore_ros.ros.converters.GpsMessageConverter;
import com.jamie.android_ros.arcore_ros.ros.sensors.SensorListener;

import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import sensor_msgs.NavSatFix;
import sensor_msgs.NavSatStatus;

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
}
