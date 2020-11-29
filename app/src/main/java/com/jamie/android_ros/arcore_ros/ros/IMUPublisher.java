package com.jamie.android_ros.arcore_ros.ros;
import com.jamie.android_ros.arcore_ros.ros.converters.ImuMessageConverter;
import com.jamie.android_ros.arcore_ros.ros.data_structures.ImuData;

import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import sensor_msgs.Imu;
import static com.jamie.android_ros.arcore_ros.ros.Utilities.makeDiagonal3x3Matrix;

/**
 * Created by jamiecho on 2/4/17.
 */


public class IMUPublisher{
    private static final double[] LINEAR_ACCELERATION_COVARIANCE = makeDiagonal3x3Matrix(
            2e-4, 3e-4, 3e-4);
    private static final double[] ANGULAR_VELOCITY_COVARIANCE = makeDiagonal3x3Matrix(
            1e-6, 1e-6, 1e-6);
    private static final double[] ORIENTATION_COVARIANCE = makeDiagonal3x3Matrix(
            0.001, 0.001, 0.001);

    private final Publisher<Imu> publisher;
    private final ImuMessageConverter converter;
    private Imu msg;
    private boolean updated;

    public IMUPublisher(ConnectedNode connectedNode, ImuMessageConverter converter) {
        this.converter = converter;
        this.publisher = connectedNode.newPublisher("android/imu", sensor_msgs.Imu._TYPE);
        this.msg = publisher.newMessage();
        updateMessageCovariance(
                LINEAR_ACCELERATION_COVARIANCE,
                ANGULAR_VELOCITY_COVARIANCE,
                ORIENTATION_COVARIANCE
        );
        updated = false;
    }

    public void update(float[] linAcc, float[] angVel, float[] orientation) {
        updated = true;
        msg = converter.toRosMessage(new ImuData(linAcc, angVel, orientation), msg);
    }


    public void publish() {
        //only publish when data got updated
        if(updated){
            updated = false;
            Utilities.setHeader(msg.getHeader()); // populate header
            publisher.publish(msg);
        }
    }

    //TODO : Implement covariance updates
    private void updateMessageCovariance(double[] lc, double[] ac, double[] oc){
        msg.setLinearAccelerationCovariance(lc);
        msg.setAngularVelocityCovariance(ac);
        msg.setOrientationCovariance(oc);
    }
}