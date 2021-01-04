package com.jamie.android_ros.arcore_ros.ros.sensors;

import com.google.ar.core.Camera;
import com.google.ar.core.Frame;
import com.google.ar.core.TrackingState;
import com.jamie.android_ros.arcore_ros.common.LiveData;

public class CompressedImageCameraSensor extends BaseSensor<Frame>  implements LiveData.Observer<Frame> {

    private final LiveData<Frame> liveFrame;

    public CompressedImageCameraSensor(LiveData<Frame> liveFrame) {
        this.liveFrame = liveFrame;
    }

    @Override
    public void start() {
        liveFrame.observe(this);
    }

    @Override
    public void stop() {
        liveFrame.removeObserver(this);
    }

    @Override
    public void onChanged(Frame frame) {
        Camera camera = liveFrame.getValue().getCamera();
        if(camera.getTrackingState() == TrackingState.TRACKING){
            notifyListeners(frame);
        }
    }
}
