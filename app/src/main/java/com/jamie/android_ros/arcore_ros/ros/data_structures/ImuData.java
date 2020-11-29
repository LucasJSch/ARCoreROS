package com.jamie.android_ros.arcore_ros.ros.data_structures;

public class ImuData {
    private final float[] linearAcceleration;
    private final float[] angularVelocity;
    private final float[] orientation;

    public ImuData(float[] linearAcceleration, float[] angularVelocity, float[] orientation) {
        this.linearAcceleration = linearAcceleration;
        this.angularVelocity = angularVelocity;
        this.orientation = orientation;
    }

    public float[] getLinearAcceleration() {
        return linearAcceleration;
    };

    public float[] getAngularVelocity() {
        return angularVelocity;
    }

    public float[] getOrientation(){
        return orientation;
    }
}

// order is x,y,z,w in ROS
