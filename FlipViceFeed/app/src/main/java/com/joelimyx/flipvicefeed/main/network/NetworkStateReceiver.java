package com.joelimyx.flipvicefeed.main.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by ds on 12/1/16.
 */

public class NetworkStateReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkStateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        ConnectivityManager conn = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();

        if (networkInfo == null ) {
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show();
        }

        if (networkInfo != null && !networkInfo.isConnected()) {
            Toast.makeText(context, "Lost Connectivity", Toast.LENGTH_SHORT).show();

        }
    }
}