package com.aimplatfarm.aimplatfarmdelivery.map;


import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.aimplatfarm.aimplatfarmdelivery.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;


import java.util.List;


public class UpdateLocationService extends Service {
    private NotificationManager mNM;
    private int NOTIFICATION = R.string.local_service_started;
    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();


    LocationRequest mLocationRequest;
    public static final String MY_ACTION = "action.SOME_ACTION";
    private static final int MY_JOB_INTENT_SERVICE_ID = 500;
    boolean isThreadOn = false;
    FusedLocationProviderClient mFusedLocationClient;
    LatLng latLng1;
    public static UpdateLocationService updateLocationService;

    public class LocalBinder extends Binder {
        UpdateLocationService getService() {
            return UpdateLocationService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        updateLocationService = this;
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        showNotification();
        locationUpdate();

    }
    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.local_service_started);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this,this.getClass()), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.splash_photo)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("Location update running")  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1,
                restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 10000,
                restartServicePendingIntent);
        super.onTaskRemoved(rootIntent);

    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
     //   onTaskRemoved(intent);

        locationUpdate();
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this,"Location Service Stopped", Toast.LENGTH_SHORT).show();
    }
    public void locationUpdate() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); // two minute interval
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback,
                        Looper.myLooper());
            } else {
                //   UpdateLocationService.this.stopSelf();

            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback,
                    Looper.myLooper());
        }
    }


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);

                System.out.println("location..........." + location.getLatitude() + " " + location.getLongitude());
               // Toast.makeText(getApplicationContext(), location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_SHORT).show();

            }
        }
    };
    private final int SPLASH_TIME = 500;

    private void startSplashTimer() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                        Toast.makeText(UpdateLocationService.this, "location stop", Toast.LENGTH_SHORT).show();
                    }
                }, SPLASH_TIME);
    }

    //    public void Update_driver_location_API(final double vLa, final double vLo) {
//        try {
//            if (Utility.isConnectingToInternet(UpdateLocationService.this)) {
//
//                String  tripId=preferenceUtills.getData(Constant.TRIPID);
//                apiInterface = APIClient.getParentClient().create(ApiInterface.class);
//                Call<LogoutModel> call = apiInterface.save_latitude_longitude(""+ChaperoneIdd,vLa,vLo,"6122",tripId);
//                Log.d("TAG", "callApiLogout: "+ChaperoneIdd);
//                call.enqueue(new Callback<LogoutModel>() {
//                    @Override
//                    public void onResponse(Call<LogoutModel> call, Response<LogoutModel> response) {
//
//                        LogoutModel logoutModel = response.body();
//
//                        if (logoutModel.getStatus() == 200) {
//
//                            //  Toast.makeText(UpdateLocationService.this, ""+logoutModel.getMessage(), Toast.LENGTH_SHORT).show();
//                            Log.d("TAG", "onResponse: "+logoutModel.getMessage());
//
//                        }
//                        else {
//                            Toast.makeText(UpdateLocationService.this, ""+ logoutModel.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<LogoutModel> call, Throwable t) {
//                        //  Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                        Log.d("TAG", "onFailure: "+t.getMessage());
//
//                    }
//                });
//
//            }
//            else {
//                Toast.makeText(UpdateLocationService.this, "Please Connect Internet !", Toast.LENGTH_SHORT).show();
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }



}
