package com.jamie.android_ros.arcore_ros.ros.converters;

import android.location.Location;

import sensor_msgs.NavSatFix;
import sensor_msgs.NavSatStatus;

public class GpsMessageConverter implements DataToRosMessageConverter<NavSatFix, Location> {

    @Override
    public NavSatFix toRosMessage(Location location, NavSatFix msg) {
        msg.getStatus().setService(NavSatStatus.SERVICE_GPS);
        msg.getStatus().setService(NavSatStatus.STATUS_FIX);
        msg.setLatitude(location.getLatitude());
        msg.setLongitude(location.getLongitude());
        msg.setAltitude(location.getAltitude());
        //location.getSpeed(); TODO : figure out value of this?
        //location.getBearing();
        updateCovariance(msg, location.getAccuracy());
        return msg;
    }

    private void updateCovariance(NavSatFix msg, double a){
        msg.setPositionCovariance(new double[]{
                a*a,0,0,
                0,a*a,0,
                0,0,a*a});
        msg.setPositionCovarianceType(NavSatFix.COVARIANCE_TYPE_DIAGONAL_KNOWN); //known-ish
    }
}
