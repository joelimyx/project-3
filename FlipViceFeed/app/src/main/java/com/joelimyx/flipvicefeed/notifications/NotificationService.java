package com.joelimyx.flipvicefeed.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.classes.GsonArticle;
import com.joelimyx.flipvicefeed.classes.Item;
import com.joelimyx.flipvicefeed.classes.TopicObject;
import com.joelimyx.flipvicefeed.classes.VolleySingleton;
import com.joelimyx.flipvicefeed.database.AlarmSQLHelper;
import com.joelimyx.flipvicefeed.main.main.MainActivity;

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
    private List<Item> mArticleList;
    private TopicObject mRandomFavorite;
    private String imageUrl;
    private String url;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {


        //Start task to load random favorite topic from db
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                List<TopicObject> favoriteTopics = AlarmSQLHelper.getInstance(getApplicationContext()).getFavoriteTopics();
                mRandomFavorite = favoriteTopics.get((int)(Math.random()*favoriteTopics.size()));
                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                //With the favorite topic, setup a call to get the latest in that category
                url = "http://vice.com/api/getlatest/category/" + mRandomFavorite.getTopic();
                if (mArticleList != null) {
                    mArticleList.clear();
                }
                Log.d(TAG, "onPostExecute: ");

                        StringRequest request = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d(TAG, "onResponse: Getting items.");
                                        GsonArticle gsonArticle = new Gson().fromJson(response, GsonArticle.class);
                                        mArticleList = gsonArticle.getData().getItems();
                                        Log.d(TAG, "onResponse: Got the items. DOING WELL SO FAR!");
                                        imageUrl = mArticleList.get(0).getThumb();

                                        ImageRequest request2 = new ImageRequest(imageUrl,
                                                new Response.Listener<Bitmap>() {
                                                    @Override
                                                    public void onResponse(Bitmap response) {
                                                        RemoteViews customNotificationView = new RemoteViews(PACKAGE_NAME, R.layout.notification_bar_remoteview);
                                                        Log.d(TAG, "onResponse: URI! AH!" + mArticleList.get(0).getThumb());
                                                        customNotificationView.setImageViewBitmap(R.id.remote_image_background, response);
                                                        customNotificationView.setImageViewResource(R.id.remote_edgefade, R.drawable.edge_fades);
                                                        customNotificationView.setTextViewText(R.id.remote_textshadow, SEE_NEW_STORIES + mRandomFavorite.getTopic());
                                                        customNotificationView.setTextViewText(R.id.remote_text, SEE_NEW_STORIES + mRandomFavorite.getTopic());

                                                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());

                                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);

                                                        Notification notification = mBuilder.setSmallIcon(R.drawable.ic_vooz)
                                                                .setContentTitle(SEE_NEW_STORIES)
                                                                .setContent(customNotificationView)
                                                                .setAutoCancel(true)
                                                                .setContentIntent(pendingIntent)
                                                                .build();

                                                        NotificationManager notificationMan = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                                        notificationMan.notify(NOTIFICATION_ID, notification);
                                                        Log.d(TAG, "onResponse: Made it to after notifications");
                                                    }
                                                }, 0, 0, null,
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        error.printStackTrace();
                                                    }
                                                });
                                        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request2);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                    }
                                });
                        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
                    }
                }.execute();

        Log.d(TAG, "onStartJob: FINISHED?");
        jobFinished(jobParameters, true);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
