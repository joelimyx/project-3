package com.joelimyx.flipvicefeed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.joelimyx.flipvicefeed.Notifications.AlarmService;
import com.joelimyx.flipvicefeed.Notifications.AlarmSettingsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, AlarmSettingsActivity.class);
        startActivity(intent);

    }
}
