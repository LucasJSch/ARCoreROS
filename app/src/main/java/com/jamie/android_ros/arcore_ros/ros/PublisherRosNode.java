package com.jamie.android_ros.arcore_ros.ros;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.ar.core.Frame;
import com.google.ar.core.Pose;
import com.jamie.android_ros.arcore_ros.common.LiveData;
import com.jamie.android_ros.arcore_ros.ros.converters.DataToRosMessageConverter;
import com.jamie.android_ros.arcore_ros.ros.converters.GpsMessageConverter;
import com.jamie.android_ros.arcore_ros.ros.converters.ImuMessageConverter;
import com.jamie.android_ros.arcore_ros.ros.converters.OdometryMessageConverter;
import com.jamie.android_ros.arcore_ros.ros.data_structures.ImuData;
import com.jamie.android_ros.arcore_ros.ros.publishers.GpsPublisher;
import com.jamie.android_ros.arcore_ros.ros.publishers.ImuPublisher;
import com.jamie.android_ros.arcore_ros.ros.publishers.MessagePublisher;
import com.jamie.android_ros.arcore_ros.ros.publishers.OdometryPublisher;
import com.jamie.android_ros.arcore_ros.ros.sensors.GpsSensor;
import com.jamie.android_ros.arcore_ros.ros.sensors.ImuSensor;
import com.jamie.android_ros.arcore_ros.ros.sensors.OdometrySensor;
import com.jamie.android_ros.arcore_ros.ros.sensors.Sensor;
import com.jamie.android_ros.arcore_ros.ros.sensors.SensorListener;

import org.jboss.netty.handler.execution.OrderedDownstreamThreadPoolExecutor;
import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;
import org.ros.node.topic.Publisher;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * ROS Node wrapping class that is in charge of instantiating all of the {@link PublisherSensor}
 * that the application will require, as well as providing a ROS Node through which they'll send
 * their messages.
 *
 * @author lorsi96 2020
 * @since 12-07-2020
 */
public class PublisherRosNode extends AbstractNodeMain {
    private static final String TAG = PublisherRosNode.class.getSimpleName();
    private final Context mContext;
    private final LiveData<Frame> liveFrame;

    private Set<PublisherSensor> publisherSensors = new HashSet<>();

    public PublisherRosNode(Context mContext, LiveData<Frame> liveFrame, NodeMainExecutor n) {
        this.mContext = mContext;
        this.liveFrame = liveFrame;
    }

    /* ROS Related Stuff */
    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("android_sensors");
    }

    @Override
    public void onStart(final ConnectedNode n) {
        Log.d(TAG, "Started");
        publisherSensors.add(PublisherSensorFactory.createImu(n, mContext));
        publisherSensors.add(PublisherSensorFactory.createGps(n ,mContext));
        publisherSensors.add(PublisherSensorFactory.createOdom(n, liveFrame));
        publisherSensors.add(PublisherSensorFactory.createCompressedImageCamera(n, liveFrame));
        publisherSensors.add(PublisherSensorFactory.createDepthImage(n, liveFrame));

        for(PublisherSensor s: publisherSensors) {
            s.startPublishing();
        }
    }

    @Override
    public void onShutdown(Node node) {
        super.onShutdown(node);
        for(PublisherSensor s: publisherSensors) {
            s.stopPublishing();
        }

    }
}