package com.karzansoft.fastvmi.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.karzansoft.fastvmi.Services.ImageSyncService;
import com.karzansoft.fastvmi.Utils.NetworkUtil;

/**
 * Created by Yasir on 5/16/2016.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        int networkStatus= NetworkUtil.getConnectivityStatus(context);
        if(networkStatus==NetworkUtil.TYPE_WIFI)
        {
            try{
            Intent serviceIntent=new Intent(context, ImageSyncService.class);
                ContextCompat.startForegroundService(context,serviceIntent);
            }catch (Exception ex){ex.printStackTrace();}
        }else {
            Log.e("Network Status",""+NetworkUtil.getConnectivityStatusString(context));
        }
    }

}
