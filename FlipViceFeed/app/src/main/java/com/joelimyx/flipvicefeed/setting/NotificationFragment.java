package com.joelimyx.flipvicefeed.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.database.AlarmSQLHelper;
import com.joelimyx.flipvicefeed.database.DBAssetHelper;
import com.joelimyx.flipvicefeed.notifications.AlarmService;
import com.joelimyx.flipvicefeed.notifications.AlarmSettingsActivity;
import com.joelimyx.flipvicefeed.notifications.TimeObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class NotificationFragment extends Fragment {

    private List<CheckBox> mCheckList;
    private List<TimeObject> mTimeObjectList;
    private TimePicker mTimePicker;
    int ALARM_ID = 123456;

    public NotificationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadNotificationDatatoPage(view);

        final AlarmService alarmService = new AlarmService(getContext(),ALARM_ID);

        Button saveButton = (Button)view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int hour,minute;
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
            }
        });

        SwitchCompat switchCompat = (SwitchCompat) view.findViewById(R.id.notifiction_switch);

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("notification on or off", Context.MODE_PRIVATE);
        boolean savedChecked = sharedPreferences.getBoolean(getString(R.string.saved_switch_state),false);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        //If notification was previously not checked, hide all elements and set the switch to false
        if (!savedChecked){
            view.findViewById(R.id.time_picker).setVisibility(View.GONE);
            view.findViewById(R.id.day_list).setVisibility(View.GONE);
            view.findViewById(R.id.save_button).setVisibility(View.GONE);
            switchCompat.setChecked(false);
        }

        //Switch toggle to toggle between view visibility
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked){
                    alarmService.cancel(getContext());
                    view.findViewById(R.id.time_picker).setVisibility(View.GONE);
                    view.findViewById(R.id.day_list).setVisibility(View.GONE);
                    view.findViewById(R.id.save_button).setVisibility(View.GONE);
                    editor.putBoolean(getString(R.string.saved_switch_state),false);
                    editor.commit();
                }else {
                    view.findViewById(R.id.time_picker).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.day_list).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.save_button).setVisibility(View.VISIBLE);
                    editor.putBoolean(getString(R.string.saved_switch_state),true);
                    editor.commit();
                }
            }
        });
    }

    private void loadNotificationDatatoPage(View view){
        CheckBox cMon,cTue,cWed,cThu,cFri,cSat,cSun;
        AsyncTask<Void,Void,Void> mAsyncLoadData;


        mAsyncLoadData = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                DBAssetHelper dbSetup = new DBAssetHelper(getContext());
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
                mTimeObjectList = AlarmSQLHelper.getInstance(getContext()).getAllForCheckboxes();
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
    }
}
