package com.joelimyx.flipvicefeed.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.joelimyx.flipvicefeed.R;

/**
 * Created by KorbBookProReturns on 12/2/16.
 */


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationService extends JobService {
    public static final String SEE_NEW_STORIES = "New stories in ";
    public static final String PACKAGE_NAME = "com.joelimyx.flipvicefeed";
    public static final int NOTIFICATION_ID = 0;
    private Notification mNotification;
    private AsyncTask mTask;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        mTask = new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                RemoteViews customNotificationView = new RemoteViews(PACKAGE_NAME, R.layout.notification_bar_remoteview);

                customNotificationView.setImageViewResource(R.id.remote_image_background,0);
                customNotificationView.setImageViewResource(R.id.remote_edgefade,R.drawable.edge_fades);
                customNotificationView.setTextViewText(R.id.remote_textshadow,SEE_NEW_STORIES);
                customNotificationView.setTextViewText(R.id.remote_text,SEE_NEW_STORIES);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());

                Notification notification = mBuilder.setSmallIcon(R.drawable.ic_vooz)
                        .setContentTitle(SEE_NEW_STORIES)
                        .setContent(customNotificationView)
                        .setAutoCancel(true)
                        .build();

                NotificationManager notificationMan = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationMan.notify(NOTIFICATION_ID,notification);
            }
        }.execute();

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if(mTask != null && mTask.getStatus().equals(AsyncTask.Status.RUNNING)){
            mTask.cancel(false);
        }
        return false;
    }
}
