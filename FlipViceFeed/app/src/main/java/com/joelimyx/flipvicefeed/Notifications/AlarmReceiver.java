package com.joelimyx.flipvicefeed.Notifications;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.joelimyx.flipvicefeed.R;

/**
 * Created by KorbBookProReturns on 11/30/16.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final int NOTIFICATION_ID = 0;
    public static final String PACKAGE_NAME = "com.joelimyx.flipvicefeed";
    public static final String SEE_NEW_STORIES = "New stories in: ";
    Notification mNotification;

    @Override
    public void onReceive(Context context, Intent intent) {

        RemoteViews customNotificationView = new RemoteViews(PACKAGE_NAME,R.layout.notification_bar_remoteview);

        customNotificationView.setImageViewResource(R.id.remote_image_background,R.drawable.remote_notification_test_image);
        customNotificationView.setImageViewResource(R.id.remote_edgefade,R.drawable.edge_fades);
        customNotificationView.setTextViewText(R.id.remote_textshadow,SEE_NEW_STORIES+"Stuff");
        customNotificationView.setTextViewText(R.id.remote_text,SEE_NEW_STORIES+"Stuff");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        mNotification = mBuilder.setSmallIcon(R.drawable.ic_vooz)
                .setContentTitle(SEE_NEW_STORIES+"Stuff")
                .setContent(customNotificationView)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationMan = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationMan.notify(NOTIFICATION_ID,mNotification);


    }
}
