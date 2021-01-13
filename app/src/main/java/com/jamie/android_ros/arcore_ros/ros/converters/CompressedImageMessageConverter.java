package com.jamie.android_ros.arcore_ros.ros.converters;

import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Log;

import com.google.ar.core.Frame;
import com.google.ar.core.exceptions.NotYetAvailableException;

import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.ros.internal.message.MessageBuffers;
import org.ros.message.Time;

import java.nio.ByteBuffer;

import sensor_msgs.CompressedImage;

/**
 * Converts {@code Frame} objects to {@code CompressedImage} ROS messages.
 *
 * @author Lucas Scheinkerman (LucasJSch)
 * @since 1-04-2021
 */
public class CompressedImageMessageConverter
        implements DataToRosMessageConverter<CompressedImage, Frame> {

    private ChannelBufferOutputStream stream = new ChannelBufferOutputStream(
            MessageBuffers.dynamicBuffer());
    private static final String TAG = CompressedImageMessageConverter.class.getSimpleName();

    @Override
    public CompressedImage toRosMessage(Frame frame, CompressedImage baseMessage) {
        android.media.Image image;
        try {
            image = frame.acquireCameraImage();
        } catch (NotYetAvailableException e) {
            Log.w(TAG, e.getMessage());
            return baseMessage;
        }
        baseMessage.setFormat("jpeg");
        baseMessage.getHeader().setFrameId("camera");
        baseMessage.getHeader().setStamp(Time.fromMillis(frame.getTimestamp()));
        SetImageToCompressedJPEGInBuffer(image);
        baseMessage.setData(stream.buffer().copy());
        // Clear the buffer and release the image.
        stream.buffer().clear();
        image.close();
        return baseMessage;
    }

    private byte[] YUV_420_888toNV21(android.media.Image image) {
        byte[] nv21;
        ByteBuffer yBuffer = image.getPlanes()[0].getBuffer();
        ByteBuffer uBuffer = image.getPlanes()[1].getBuffer();
        ByteBuffer vBuffer = image.getPlanes()[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        nv21 = new byte[ySize + uSize + vSize];

        // U and V channels are swapped.
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        return nv21;
    }

    private void SetImageToCompressedJPEGInBuffer(android.media.Image image) {
        Rect rect = new Rect(0, 0, image.getWidth(), image.getHeight());
        byte bytes[] = YUV_420_888toNV21(image);
        YuvImage yuvImage =
                new YuvImage(bytes, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        yuvImage.compressToJpeg(rect, 20, stream);
    }
}
