package com.jamie.android_ros.arcore_ros.ros.converters;

import com.google.ar.core.Pose;
import com.jamie.android_ros.arcore_ros.common.Utilities;

import geometry_msgs.PoseWithCovariance;
import nav_msgs.Odometry;


/**
 * Converts {@code Pose} objects to {@code Odometry} ROS messages.
 *
 * @author lorsi96 2020
 * @since 12-07-2020
 */
public class OdometryMessageConverter implements DataToRosMessageConverter<Odometry, Pose> {

    @Override
    public Odometry toRosMessage(Pose rawData, Odometry baseMessage) {
        geometry_msgs.Pose p = baseMessage.getPose().getPose();
        float[] txn = rawData.getTranslation();
        float[] rxn = rawData.getRotationQuaternion();

        p.getPosition().setX(txn[0]);
        p.getPosition().setY(txn[1]);
        p.getPosition().setZ(txn[2]);

        p.getOrientation().setX(rxn[0]);
        p.getOrientation().setY(rxn[1]);
        p.getOrientation().setZ(rxn[2]);
        p.getOrientation().setW(rxn[3]);


        PoseWithCovariance pWc = baseMessage.getPose();
        pWc.setPose(p); // TODO: Update covariance.
        baseMessage.setPose(pWc);
        Utilities.setHeader(baseMessage.getHeader(), "odom");
        return baseMessage;
    }
}
