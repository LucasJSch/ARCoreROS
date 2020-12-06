/*
 * Copyright (C) 2014 Jamie Cho.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.jamie.android_ros.arcore_ros.ros;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.jamie.android_ros.arcore_ros.ros.converters.GpsMessageConverter;
import com.jamie.android_ros.arcore_ros.ros.converters.ImuMessageConverter;
import com.jamie.android_ros.arcore_ros.ros.data_structures.ImuData;
import com.jamie.android_ros.arcore_ros.ros.publishers.GpsPublisher;
import com.jamie.android_ros.arcore_ros.ros.publishers.ImuPublisher;
import com.jamie.android_ros.arcore_ros.ros.sensors.GpsSensor;
import com.jamie.android_ros.arcore_ros.ros.sensors.ImuSensor;
import com.jamie.android_ros.arcore_ros.ros.sensors.SensorListener;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;
import org.ros.node.topic.Publisher;

import java.util.function.Consumer;

/**
 * A simple {@link Publisher} {@link NodeMain}.
 */
// TODO : consider filtering barometric altitude and GPS altitude

public class SensorPublisher extends AbstractNodeMain {

    // Imu sensor dependencies.
    private ImuSensor imuSensor;
    private SensorListener<ImuData> imuPublishAsListener;
    private ImuMessageConverter imuConverter = new ImuMessageConverter();
    private ImuPublisher imuPublisher;

    private GpsSensor gpsSensor;
    private SensorListener<Location> gpsPublishAsListener;
    private GpsMessageConverter gpsConverter = new GpsMessageConverter();
    private GpsPublisher gpsPublisher;

    // manage gps information separately
    private OdomPublisher odomPublisher;

    public SensorPublisher(Context mContext, NodeMainExecutor n) {
        imuSensor = new ImuSensor(mContext, null); // TODO: update duration.
        gpsSensor = new GpsSensor(mContext, 10_000);
    }

    /* Odom Callback */
    public void onOdomChanged(float[] txn, float[] rxn) {
        odomPublisher.update(txn, rxn);
    }

    public void unregisterListeners() {
        gpsSensor.unregisterListener(gpsPublishAsListener);
        imuSensor.unregisterListener(imuPublishAsListener);
    }

    /* ROS Related Stuff */
    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("android_sensors");
    }

    @Override
    public void onStart(final ConnectedNode connectedNode) {
        imuPublisher = new ImuPublisher(connectedNode, new ImuMessageConverter(), "android/imu");
        imuPublishAsListener = imuPublisher::publish;
        gpsPublisher = new GpsPublisher(connectedNode, new GpsMessageConverter() , "android/gps");
        gpsPublishAsListener = gpsPublisher::publish;

        odomPublisher = new OdomPublisher(connectedNode);

        imuSensor.registerListener(imuPublishAsListener);
        imuSensor.start();
        gpsSensor.registerListener(gpsPublishAsListener);
        gpsSensor.start();

        connectedNode.executeCancellableLoop(new CancellableLoop() {
            @Override
            protected void setup() {}

            @Override
            protected void loop() throws InterruptedException {
                // basically, keep on publishing if data exists
                odomPublisher.publish();
                //TODO : implement and check publication flags
            }
        });
    }
}