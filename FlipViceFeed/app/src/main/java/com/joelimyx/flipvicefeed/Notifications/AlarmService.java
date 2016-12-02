package com.joelimyx.flipvicefeed.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by KorbBookProReturns on 11/30/16.
 */

public class AlarmService {
    private Context context;
    private PendingIntent mSender;
    private int ALARM_ID;
    private AlarmManager alarmMan;

    public AlarmService(Context context, int id) {
        this.context = context;
        ALARM_ID = id;
        mSender = PendingIntent.getBroadcast(context, ALARM_ID,
                new Intent(context, AlarmReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void cancel(Context context){
        this.context = context;

        if(alarmMan!=null) {
            alarmMan.cancel(mSender);
        }
    }

    public void startAlarm(int hour, int minute, int day){

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,day);
        c.set(Calendar.HOUR_OF_DAY,hour);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);
        long thisTime = c.getTimeInMillis();

        alarmMan = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        alarmMan.setRepeating(
                AlarmManager.RTC_WAKEUP,
                thisTime,
                alarmMan.INTERVAL_DAY*7,
                mSender);
    }


}
