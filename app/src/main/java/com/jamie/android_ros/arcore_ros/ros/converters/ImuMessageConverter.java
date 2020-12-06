package com.jamie.android_ros.arcore_ros.ros.converters;

import com.jamie.android_ros.arcore_ros.ros.data_structures.ImuData;

import sensor_msgs.Imu;

import static com.jamie.android_ros.arcore_ros.ros.Utilities.makeDiagonal3x3Matrix;

public class ImuMessageConverter implements DataToRosMessageConverter<Imu, ImuData> {
    private static final double[] LINEAR_ACCELERATION_COVARIANCE = makeDiagonal3x3Matrix(
            2e-4, 3e-4, 3e-4);
    private static final double[] ANGULAR_VELOCITY_COVARIANCE = makeDiagonal3x3Matrix(
            1e-6, 1e-6, 1e-6);
    private static final double[] ORIENTATION_COVARIANCE = makeDiagonal3x3Matrix(
            0.001, 0.001, 0.001);

    public ImuMessageConverter() {}

    @Override
    public Imu toRosMessage(ImuData rawData, Imu baseMessage) {
        float[] linAcc = rawData.getLinearAcceleration();
        float[] angVel = rawData.getAngularVelocity();
        float[] orientation = rawData.getOrientation();

        baseMessage.getLinearAcceleration().setX(linAcc[0]);
        baseMessage.getLinearAcceleration().setY(linAcc[1]);
        baseMessage.getLinearAcceleration().setZ(linAcc[2]);

        baseMessage.getAngularVelocity().setX(angVel[0]);
        baseMessage.getAngularVelocity().setY(angVel[1]);
        baseMessage.getAngularVelocity().setZ(angVel[2]);

        baseMessage.getOrientation().setW(orientation[0]);
        baseMessage.getOrientation().setX(orientation[1]);
        baseMessage.getOrientation().setY(orientation[2]);
        baseMessage.getOrientation().setZ(orientation[3]);
        updateMessageCovariance(baseMessage);
        return baseMessage;
    }

    private static void updateMessageCovariance(Imu msg) {
            msg.setLinearAccelerationCovariance(LINEAR_ACCELERATION_COVARIANCE);
            msg.setAngularVelocityCovariance(ANGULAR_VELOCITY_COVARIANCE);
            msg.setOrientationCovariance(ORIENTATION_COVARIANCE);
        }
}
