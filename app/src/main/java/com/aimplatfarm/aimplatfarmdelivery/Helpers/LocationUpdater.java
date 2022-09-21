package com.aimplatfarm.aimplatfarmdelivery.Helpers;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApiToJson;
import com.aimplatfarm.aimplatfarmdelivery.Models.ApiResponse;
import com.aimplatfarm.aimplatfarmdelivery.Models.Location;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class LocationUpdater {

    private static String token;
    public static double lat = 0.0;
    public static double longi = 0.0;

    public static void getCurrentLocation(Context context) {
        if (token == null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", MODE_PRIVATE);
            token = sharedPreferences.getString("token", "");
        }
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(context)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);

                        LocationServices.getFusedLocationProviderClient(context)
                                .removeLocationUpdates(this);
                        if (locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            lat = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            longi = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            Location location = new Location();
                            location.setLatitude(String.valueOf(lat));
                            location.setLongitude(String.valueOf(longi));
                            updateLocation(location, context);
                            String data = String.format("Latitude :%s - Longitude: %s", lat, longi);

                        }
                    }
                }, Looper.getMainLooper());
    }



    // update location
    public static void updateLocation(Location location, Context context) {
        Call<ApiResponse> call = new DeliveryApiToJson().updateLocation(token, location);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                } else {
                    Toast.makeText(context, "Couldn't update location", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, "Couldn't update location", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
