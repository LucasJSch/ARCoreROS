package com.jamie.android_ros.arcore_ros.ros;

import android.content.Context;
import android.util.Log;

import com.google.ar.core.Frame;
import com.jamie.android_ros.arcore_ros.common.LiveData;

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMainExecutor;

import java.util.HashSet;
import java.util.Set;

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
        publisherSensors.add(PublisherSensorFactory.createCameraInfo(n, liveFrame));
        publisherSensors.add(PublisherSensorFactory.createPointcloud(n, liveFrame));

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