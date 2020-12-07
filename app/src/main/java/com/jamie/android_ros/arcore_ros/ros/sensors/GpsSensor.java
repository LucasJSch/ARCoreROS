package com.jamie.android_ros.arcore_ros.ros.sensors;


import android.content.Context;
import android.location.Location;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

/** Gps Sensor that retrieves {@code Location} updates
 *
 * @author lorsi96 2020
 * @since 12-07-2020
 */
public class GpsSensor extends BaseSensor<Location> {
    private static String TAG = GpsSensor.class.getSimpleName();

    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;
    private Location location;
    private Context context;
    private long intervalForGps;

    public GpsSensor(Context context, long sampleRateMs) {
        this.context = context;
        this.intervalForGps = sampleRateMs;
    }

    @Override
    public void start() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(intervalForGps);
        mLocationRequest.setFastestInterval(intervalForGps/2);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                location = locationResult.getLastLocation();
                onGPSChanged(location);
            }
        };
        Looper.prepare();
        fusedLocationClient.requestLocationUpdates(
                mLocationRequest, locationCallback, Looper.getMainLooper());

    }

    @Override
    public void stop() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }


    private void onGPSChanged(Location location) {
        if (location.getAltitude() != 0) {
            this.location = location;
        } else {
            // essentially, use data from barometer
            this.location.setLatitude(location.getLatitude());
            this.location.setLongitude(location.getLongitude());
        }
        notifyListeners(location);
    }
}
