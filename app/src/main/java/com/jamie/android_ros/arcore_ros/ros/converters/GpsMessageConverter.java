package com.jamie.android_ros.arcore_ros.ros.converters;

import android.location.Location;

import com.jamie.android_ros.arcore_ros.common.Utilities;

import sensor_msgs.NavSatFix;
import sensor_msgs.NavSatStatus;

/**
 * Converts {@code Location} objects to {@code NavSatFix} location messages.
 *
 * @author lorsi96 2020
 * @since 12-07-2020
 */
public class GpsMessageConverter implements DataToRosMessageConverter<NavSatFix, Location> {

    @Override
    public NavSatFix toRosMessage(Location location, NavSatFix msg) {
        msg.getStatus().setService(NavSatStatus.SERVICE_GPS);
        msg.getStatus().setService(NavSatStatus.STATUS_FIX);
        msg.setLatitude(location.getLatitude());
        msg.setLongitude(location.getLongitude());
        msg.setAltitude(location.getAltitude());
        updateCovariance(msg, location.getAccuracy());
        return msg;
    }

    private void updateCovariance(NavSatFix msg, double a){
        msg.setPositionCovariance(Utilities.makeDiagonal3x3Matrix(a*a, a*a, a*a));
        // TODO: Despite known, can be improved.
        msg.setPositionCovarianceType(NavSatFix.COVARIANCE_TYPE_DIAGONAL_KNOWN);
    }
}
