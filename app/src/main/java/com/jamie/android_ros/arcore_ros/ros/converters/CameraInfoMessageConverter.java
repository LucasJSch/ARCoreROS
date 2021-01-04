package com.jamie.android_ros.arcore_ros.ros.converters;

import android.util.Log;

import com.google.ar.core.CameraIntrinsics;
import com.google.ar.core.Frame;
import com.google.ar.core.exceptions.NotYetAvailableException;

import org.ros.message.Time;

import sensor_msgs.CameraInfo;

public class CameraInfoMessageConverter implements DataToRosMessageConverter<CameraInfo, Frame> {

    private static final String TAG = CameraInfoMessageConverter.class.getSimpleName();
    private boolean alreadyHaveCameraInfo = false;
    private int width;
    private int height;

    @Override
    public CameraInfo toRosMessage(Frame frame, CameraInfo baseMessage) {
        if (!alreadyHaveCameraInfo) {
            android.media.Image image;
            try {
                image = frame.acquireCameraImage();
            } catch (NotYetAvailableException e) {
                Log.w(TAG, e.getMessage());
                return baseMessage;
            }
            width = image.getWidth();
            height = image.getHeight();
            image.close();
            alreadyHaveCameraInfo = true;
        }

        baseMessage.getHeader().setFrameId("camera");
        baseMessage.getHeader().setStamp(Time.fromMillis(frame.getTimestamp()));
        baseMessage.setWidth(width);
        baseMessage.setHeight(height);
        CameraIntrinsics intrinsics = frame.getCamera().getImageIntrinsics();
        double[] K = {intrinsics.getFocalLength()[0], 0, intrinsics.getPrincipalPoint()[0],
                      0, intrinsics.getFocalLength()[1], intrinsics.getPrincipalPoint()[1],
                      0, 0, 1};
        double[] R = {1, 0, 0,
                      0, 1, 0,
                      0, 0, 1};
        double[] P = {intrinsics.getFocalLength()[0], 0, intrinsics.getPrincipalPoint()[0], 0,
                      0, intrinsics.getFocalLength()[1], intrinsics.getPrincipalPoint()[1], 0,
                      0, 0, 1, 0};
        baseMessage.setK(K);
        baseMessage.setR(R);
        baseMessage.setP(P);

        return baseMessage;
    }
}
