package com.joelimyx.flipvicefeed.Notifications;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

/**
 * Created by KorbBookProReturns on 11/30/16.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final int NOTIFICATION_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        mBuilder.setContentTitle("Notification Alert, Click Me!");
        mBuilder.setContentText("Hi, This is Android Notification Detail!");
        mBuilder.setAutoCancel(true);

        NotificationManager notificationMan = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationMan.notify(NOTIFICATION_ID,mBuilder.build());


    }
}
