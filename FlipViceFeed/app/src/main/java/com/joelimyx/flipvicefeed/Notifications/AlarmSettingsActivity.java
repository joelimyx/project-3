package com.joelimyx.flipvicefeed.notifications;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import com.joelimyx.flipvicefeed.classes.TimeObject;
import com.joelimyx.flipvicefeed.database.AlarmSQLHelper;
import com.joelimyx.flipvicefeed.database.DBAssetHelper;
import com.joelimyx.flipvicefeed.R;

import java.util.ArrayList;
import java.util.List;

public class AlarmSettingsActivity extends AppCompatActivity {
    private static final String TAG = "Okay";
    CheckBox cMon,cTue,cWed,cThu,cFri,cSat,cSun;
    TimePicker mTimePicker;
    int hour,minute;
    int ALARM_ID = 123456;
    List<CheckBox> mCheckList;
    List<TimeObject> mTimeObjectList;
    AsyncTask<Void,Void,Void> mAsyncLoadData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_settings);

        mAsyncLoadData = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                DBAssetHelper dbSetup = new DBAssetHelper(AlarmSettingsActivity.this);
                dbSetup.getReadableDatabase();
                return null;
            }
        };
        mAsyncLoadData.execute();

        mTimePicker = (TimePicker)findViewById(R.id.time_picker);

        mCheckList = new ArrayList<>();
        cSun = (CheckBox)findViewById(R.id.sun_checkbox);
        cMon = (CheckBox)findViewById(R.id.mon_checkbox);
        cTue = (CheckBox)findViewById(R.id.tue_checkbox);
        cWed = (CheckBox)findViewById(R.id.wed_checkbox);
        cThu = (CheckBox)findViewById(R.id.thu_checkbox);
        cFri = (CheckBox)findViewById(R.id.fri_checkbox);
        cSat = (CheckBox)findViewById(R.id.sat_checkbox);


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
                mTimeObjectList = AlarmSQLHelper.getInstance(AlarmSettingsActivity.this).getAllDaysAndTime();
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

        final AlarmService alarmService = new AlarmService(AlarmSettingsActivity.this,ALARM_ID);

        Button button = (Button)findViewById(R.id.testList);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour = mTimePicker.getCurrentHour();
                minute = mTimePicker.getCurrentMinute();

                alarmService.cancel(AlarmSettingsActivity.this);

                AlarmSQLHelper helper = AlarmSQLHelper.getInstance(AlarmSettingsActivity.this);

                for (int i = 0; i < 7; i++) {
                    if (mCheckList.get(i).isChecked()){
                        helper.updateDaysAndTime(hour,minute,i);
                        alarmService.startAlarm(hour,minute,i);
                    } else {
                        helper.updateDaysAndTime(-1,-1,i);
                    }
                }
                Toast.makeText(AlarmSettingsActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
