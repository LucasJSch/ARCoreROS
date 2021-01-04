package com.jamie.android_ros.arcore_ros.ros.converters;

import android.util.Log;

import com.google.ar.core.Frame;
import com.google.ar.core.exceptions.NotYetAvailableException;

import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.ros.internal.message.MessageBuffers;
import org.ros.message.Time;

import java.io.IOException;
import java.nio.ByteBuffer;

import sensor_msgs.Image;

public class DepthImageMessageConverter
        implements DataToRosMessageConverter<Image, Frame> {

    private ChannelBufferOutputStream stream = new ChannelBufferOutputStream(
            MessageBuffers.dynamicBuffer());
    private static final String TAG = DepthImageMessageConverter.class.getSimpleName();

    @Override
    public Image toRosMessage(Frame frame, Image baseMessage) {
        android.media.Image depthImage;
        try {
            depthImage = frame.acquireDepthImage();
        } catch (NotYetAvailableException e) {
            Log.w(TAG, e.getMessage());
            return baseMessage;
        } catch (IllegalStateException e){
            Log.w(TAG, "Depth image is not supported on this device.");
            return baseMessage;
        }

        baseMessage.setIsBigendian((byte) 1);
        baseMessage.setWidth(depthImage.getWidth());
        baseMessage.setHeight(depthImage.getHeight());
        baseMessage.getHeader().setFrameId("camera");
        baseMessage.getHeader().setStamp(Time.fromMillis(frame.getTimestamp()));
        baseMessage.setEncoding("mono16");
        // The step is the amount of bytes in one row of the image.
        // Since the depth image is in int16 format, each pixel is represented with 2 bytes.
        baseMessage.setStep(depthImage.getWidth() * 2);
        SetDepthImageInBuffer(depthImage);
        baseMessage.setData(stream.buffer().copy());
        // Clear the buffer and release the image.
        stream.buffer().clear();
        depthImage.close();
        return baseMessage;
    }

    private void SetDepthImageInBuffer(android.media.Image image) {
        ByteBuffer byteBuffer =  image.getPlanes()[0].getBuffer();
        stream = (ChannelBufferOutputStream) ChannelBuffers.copiedBuffer(byteBuffer);
        ByteBuffer buffer =  image.getPlanes()[0].getBuffer();
        int size = buffer.remaining();
        byte[] byteArray = new byte[size];
        buffer.get(byteArray, 0, size);
        try {
            stream.write(byteArray);
        } catch (IOException e) {
            Log.w(TAG, "Exception writing depth image: " + e.getMessage());
            stream.buffer().clear();
        }
    }

}
