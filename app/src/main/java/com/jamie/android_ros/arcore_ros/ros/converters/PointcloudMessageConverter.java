package com.jamie.android_ros.arcore_ros.ros.converters;

import android.util.Log;

import com.google.ar.core.Frame;
import com.google.ar.core.PointCloud;

import org.jboss.netty.buffer.ChannelBuffer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import sensor_msgs.PointCloud2;

/**
 * Converts {@code Frame} objetcs into {@code Pointcloud2} ROS messages.
 *
 * @authors elector102, lorsi96 and LucasJSch
 * @since 1-04-2021
 */
public class PointcloudMessageConverter
        implements DataToRosMessageConverter<PointCloud2, Frame> {
    private static final String TAG = PointcloudMessageConverter.class.getSimpleName();
    private ChannelBuffer cb;
    private FloatBuffer fb;

    @Override
    public PointCloud2 toRosMessage(Frame frame, PointCloud2 baseMessage) {
        if (cb != null && fb != null) {
            cb.clear();
            fb.clear();
        }
        try (PointCloud pointCloud = frame.acquirePointCloud()){
            FloatBuffer fb = pointCloud.getPoints();
            float[] farray = new float[fb.remaining()];
            fb.get(farray);
            byte[] ba = floatArrayToByteArray(farray);
            ChannelBuffer cb = baseMessage.getData();
            cb.writeBytes(ba);
            baseMessage.setData(cb);
        } catch (Exception e) {
            Log.w(TAG, e.getMessage());
        }
        return baseMessage;
    }

    public static byte[] floatArrayToByteArray(float[] values){
        ByteBuffer buffer = ByteBuffer.allocate(4 * values.length);
        for (float value : values){
            buffer.putFloat(value);
        }
        return buffer.array();
    }

}
