package com.cleanerapp.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;

public class LocationHelper {

    public interface LocationCallback {
        void onLocationReceived(double latitude, double longitude);
        void onLocationFailed(String error);
    }

    private final Context context;
    private final FusedLocationProviderClient fusedLocationClient;

    public LocationHelper(Context context) {
        this.context = context;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void getCurrentLocation(LocationCallback callback) {
        if (!hasLocationPermission()) {
            callback.onLocationFailed("Location permission not granted");
            return;
        }

        try {
            CancellationTokenSource cancellationToken = new CancellationTokenSource();
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationToken.getToken())
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            callback.onLocationReceived(location.getLatitude(), location.getLongitude());
                        } else {
                            // Fallback to last known location
                            getLastKnownLocation(callback);
                        }
                    })
                    .addOnFailureListener(e -> callback.onLocationFailed(e.getMessage()));
        } catch (SecurityException e) {
            callback.onLocationFailed("Security exception: " + e.getMessage());
        }
    }

    private void getLastKnownLocation(LocationCallback callback) {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            callback.onLocationReceived(location.getLatitude(), location.getLongitude());
                        } else {
                            callback.onLocationFailed("Unable to get location. Please enable GPS.");
                        }
                    })
                    .addOnFailureListener(e -> callback.onLocationFailed(e.getMessage()));
        } catch (SecurityException e) {
            callback.onLocationFailed("Security exception: " + e.getMessage());
        }
    }

    public static float distanceBetween(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0] / 1000f; // return in km
    }
}
