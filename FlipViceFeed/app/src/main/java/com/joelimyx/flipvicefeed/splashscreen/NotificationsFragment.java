package com.joelimyx.flipvicefeed.splashscreen;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.classes.TimeObject;
import com.joelimyx.flipvicefeed.database.AlarmSQLHelper;
import com.joelimyx.flipvicefeed.database.DBAssetHelper;
import com.joelimyx.flipvicefeed.notifications.AlarmService;
import com.joelimyx.flipvicefeed.notifications.AlarmSettingsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KorbBookProReturns on 12/4/16.
 */

public class NotificationsFragment extends Fragment {
    private static final String TAG = "Okay";
    CheckBox cMon,cTue,cWed,cThu,cFri,cSat,cSun;
    TimePicker mTimePicker;
    int hour,minute;
    int ALARM_ID = 123456;
    List<CheckBox> mCheckList;
    List<TimeObject> mTimeObjectList;
    AsyncTask<Void,Void,Void> mAsyncLoadData;
//    FinishWelcomingListener mListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.splash_fragment_notifications,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAsyncLoadData = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                DBAssetHelper dbSetup = new DBAssetHelper(NotificationsFragment.this.getContext());
                dbSetup.getReadableDatabase();
                return null;
            }
        };
        mAsyncLoadData.execute();

        mTimePicker = (TimePicker)view.findViewById(R.id.time_picker);

        mCheckList = new ArrayList<>();
        cSun = (CheckBox)view.findViewById(R.id.sun_checkbox);
        cMon = (CheckBox)view.findViewById(R.id.mon_checkbox);
        cTue = (CheckBox)view.findViewById(R.id.tue_checkbox);
        cWed = (CheckBox)view.findViewById(R.id.wed_checkbox);
        cThu = (CheckBox)view.findViewById(R.id.thu_checkbox);
        cFri = (CheckBox)view.findViewById(R.id.fri_checkbox);
        cSat = (CheckBox)view.findViewById(R.id.sat_checkbox);


        mCheckList.add(cSun);
        mCheckList.add(cMon);
        mCheckList.add(cTue);
        mCheckList.add(cWed);
        mCheckList.add(cThu);
        mCheckList.add(cFri);
        mCheckList.add(cSat);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mTimeObjectList = AlarmSQLHelper.getInstance(getContext()).getAllDaysAndTime();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                int timePickerInitializer = 0;
                for (int i = 0; i < 7; i++) {
                    if(mTimeObjectList.get(i).getHour() != -1){
                        mCheckList.get(i).setChecked(true);
                        if(timePickerInitializer == 0) {
                            mTimePicker.setCurrentHour(mTimeObjectList.get(i).getHour());
                            mTimePicker.setCurrentMinute(mTimeObjectList.get(i).getMinute());
                            timePickerInitializer++;
                        }
                    }else{
                        mCheckList.get(i).setChecked(false);
                    }
                }
            }
        }.execute();

        final AlarmService alarmService = new AlarmService(getContext(),ALARM_ID);

        Button button = (Button)view.findViewById(R.id.testList);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour = mTimePicker.getCurrentHour();
                minute = mTimePicker.getCurrentMinute();

                alarmService.cancel(getContext());

                AlarmSQLHelper helper = AlarmSQLHelper.getInstance(getContext());

                for (int i = 0; i < 7; i++) {
                    if (mCheckList.get(i).isChecked()){
                        helper.updateDaysAndTime(hour,minute,i);
                        alarmService.startAlarm(hour,minute,i);
                    } else {
                        helper.updateDaysAndTime(-1,-1,i);
                    }
                }
                Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
//                mListener.finishWelcomeActivity();
                ((Activity)getContext()).finish();
            }
        });
    }

//    interface FinishWelcomingListener{
//        void finishWelcomeActivity();
//    }

}

