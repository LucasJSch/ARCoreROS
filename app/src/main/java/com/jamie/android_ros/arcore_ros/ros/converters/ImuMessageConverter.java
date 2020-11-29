package com.jamie.android_ros.arcore_ros.ros.converters;

import com.jamie.android_ros.arcore_ros.ros.data_structures.ImuData;

import sensor_msgs.Imu;

public class ImuMessageConverter implements DataToRosMessageConverter<Imu, ImuData> {

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

        return baseMessage;
    }
}
