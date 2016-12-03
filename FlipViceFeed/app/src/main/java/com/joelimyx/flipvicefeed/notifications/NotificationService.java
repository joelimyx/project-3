package com.joelimyx.flipvicefeed.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.classes.GsonArticle;
import com.joelimyx.flipvicefeed.classes.Item;
import com.joelimyx.flipvicefeed.classes.TopicObject;
import com.joelimyx.flipvicefeed.classes.VolleySingleton;
import com.joelimyx.flipvicefeed.database.AlarmSQLHelper;

import java.util.List;

import static android.content.ContentValues.TAG;

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
    private List<Item> mArticleList;
    private TopicObject mRandomFavorite;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

                List<TopicObject> favoriteTopics = AlarmSQLHelper.getInstance(getApplicationContext()).getFavoriteTopics();
                mRandomFavorite = favoriteTopics.get((int)(Math.random()*favoriteTopics.size()));

                String url = "http://vice.com/api/getlatest/category/"+mRandomFavorite;
                mArticleList.clear();

                StringRequest request = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                GsonArticle gsonArticle = new Gson().fromJson(response,GsonArticle.class);
                                mArticleList = gsonArticle.getData().getItems();

                                RemoteViews customNotificationView = new RemoteViews(PACKAGE_NAME, R.layout.notification_bar_remoteview);

                                customNotificationView.setImageViewUri(R.id.remote_image_background, Uri.parse(mArticleList.get(0).getThumb()));
                                customNotificationView.setImageViewResource(R.id.remote_edgefade,R.drawable.edge_fades);
                                customNotificationView.setTextViewText(R.id.remote_textshadow,SEE_NEW_STORIES+mRandomFavorite);
                                customNotificationView.setTextViewText(R.id.remote_text,SEE_NEW_STORIES+mRandomFavorite);

                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());

                                Notification notification = mBuilder.setSmallIcon(R.drawable.ic_vooz)
                                        .setContentTitle(SEE_NEW_STORIES)
                                        .setContent(customNotificationView)
                                        .setAutoCancel(true)
                                        .build();

                                NotificationManager notificationMan = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationMan.notify(NOTIFICATION_ID,notification);

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        });

                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

        jobFinished(jobParameters, true);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
