package com.aimplatfarm.aimplatfarmdelivery.BackgroundService;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MyLocationService extends BroadcastReceiver {
    public static final String ACTION_PROCESS_UPDATE = "com.aimplatfarm.aimplatfarmdelivery.BackgroundService.UPDATE_LOCATION";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent !=null){
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATE.equals(action)){
                Toast.makeText(context, "This is background task", Toast.LENGTH_SHORT).show();
            }
        }
    }
}