package com.jamie.android_ros.arcore_ros.ros.data_structures;


/**
 * Wraps {@code float[]} data, as received from Android's sensor manager into a single
 * {@code ImuData} structure.
 *
 * @author lorsi96 2020
 * @since 12-07-2020
 */
public class ImuData {
    private float[] linearAcceleration; // x, y, z.
    private float[] angularVelocity; // x, y, z.
    private float[] orientation;  // w, x, y, z.

    public ImuData() {
    }

    public ImuData(float[] linearAcceleration, float[] angularVelocity, float[] orientation) {
        this.linearAcceleration = linearAcceleration;
        this.angularVelocity = angularVelocity;
        this.orientation = orientation;
    }

    public void setAngularVelocity(float[] angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public void setLinearAcceleration(float[] linearAcceleration) {
        this.linearAcceleration = linearAcceleration;
    }

    public void setOrientation(float[] orientation) {
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
