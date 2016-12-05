package com.joelimyx.flipvicefeed.notifications;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.main.main.MainActivity;

/**
 * Created by KorbBookProReturns on 11/30/16.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final int NOTIFICATION_ID = 0;
    public static final String PACKAGE_NAME = "com.joelimyx.flipvicefeed";
    public static final String NEW_STORIES_DEFAULT = "Check out the latest stories!";
    public static final String SEE_NEW_STORIES = "New stories in: ";

    @Override
    public void onReceive(Context context, Intent intent) {

        //-------------------------------
        //  When an alarm is received, if the api is 21 or above, it sends it to a job scheduler
        //  so it pulls the latest article's image and uses that as the background of the notification.
        //  But it'll only show a notification if there's internet, and it'll try again every 15 minutes.
        //-------------------------------

        if(Build.VERSION.SDK_INT >= 21){
            JobScheduler jobScheduler = (JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

            PersistableBundle internetCheckBundle = new PersistableBundle();

            JobInfo notificationJob = new JobInfo.Builder(NOTIFICATION_ID,new ComponentName(context,NotificationService.class))
                    .setExtras(internetCheckBundle)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setBackoffCriteria(60000*15,JobInfo.BACKOFF_POLICY_LINEAR)
                    .build();

            jobScheduler.schedule(notificationJob);


        }else{
            //-------------------------------
            //  If the api is below 21, JobScheduler doesn't work, so instead it just sends
            //  a default notification without the current image in the background, and links
            //  to the main page.
            //-------------------------------
            RemoteViews customNotificationView = new RemoteViews(PACKAGE_NAME,R.layout.notification_bar_remoteview);

            customNotificationView.setImageViewResource(R.id.remote_image_background,0);
            customNotificationView.setImageViewResource(R.id.remote_edgefade,R.drawable.edge_fades);
            customNotificationView.setTextViewText(R.id.remote_textshadow,NEW_STORIES_DEFAULT);
            customNotificationView.setTextViewText(R.id.remote_text,NEW_STORIES_DEFAULT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

            Intent defaultIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

            Notification notification = mBuilder.setSmallIcon(R.drawable.ic_vooz)
                    .setContentTitle(NEW_STORIES_DEFAULT)
                    .setContent(customNotificationView)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build();

            NotificationManager notificationMan = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            notificationMan.notify(NOTIFICATION_ID,notification);
        }

    }
}
