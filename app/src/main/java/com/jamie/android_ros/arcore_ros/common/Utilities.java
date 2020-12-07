package com.jamie.android_ros.arcore_ros.common;

import org.ros.message.Time;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import std_msgs.Header;

/**
 * Created by jamiecho on 2/4/17.
 * Updated by lorsi96 on 7/12/20.
 */
public class Utilities {
    public static void setHeader(Header h, String frame){
        // utility function to populate the header
        // timestamp obtained by current time
        // frame-id is all going to be "android"
        h.setStamp(Time.fromMillis(System.currentTimeMillis()));
        h.setFrameId(frame);
    }
    public static void setHeader(Header h){
        setHeader(h, "android");
    }

    public static double[] makeDiagonal3x3Matrix(double e11, double e22, double e33) {
        return new double[] {
                e11, 0, 0,
                0, e22, 0,
                0, 0, e33
        };
    }

    public static <T, R> List<R> map(
            List<T> transformTarget, Function<T, R> transform
    ) {
        return transformTarget.stream().map(transform).collect(Collectors.toList());
    }
}
