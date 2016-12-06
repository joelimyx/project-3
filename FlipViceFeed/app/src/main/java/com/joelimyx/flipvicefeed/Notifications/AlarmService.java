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

        //The intent with the alarm that we send to the AlarmReceiver.
        mSender = PendingIntent.getBroadcast(context, ALARM_ID,
                new Intent(context, AlarmReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void cancel(Context context){
        this.context = context;

        //If there's alarms running, this cancels them all.
        //Called every time new alarms are set.
        if(alarmMan!=null) {
            alarmMan.cancel(mSender);
        }
    }

    public void startAlarm(int hour, int minute, int day){

        //Got the day and time from the notifications settings.
        //Now use those to set the day and time an alarm will be set for
        //To shoot a notification at that time.

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,day);
        c.set(Calendar.HOUR_OF_DAY,hour);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);
        long thisTime = c.getTimeInMillis();

        //Get the alarm manager to set the incoming alarm.
        alarmMan = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        //An alarm is set for any day checked off at the given time,
        //to be repeated every week.
        alarmMan.setRepeating(
                AlarmManager.RTC_WAKEUP,
                thisTime,
                alarmMan.INTERVAL_DAY*7,
                mSender);
    }


}
