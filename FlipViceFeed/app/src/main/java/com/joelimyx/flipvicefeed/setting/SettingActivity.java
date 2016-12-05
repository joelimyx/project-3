package com.joelimyx.flipvicefeed.setting;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.joelimyx.flipvicefeed.R;

public class SettingActivity extends AppCompatActivity {
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Notification");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().getStringExtra("setting").equals(getString(R.string.topic_filter))){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.setting_container,new TopicFilterFragment())
                    .commit();
        }else{
            getSupportFragmentManager()
                    .beginTransaction().
                    replace(R.id.setting_container,new NotificationFragment())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.no_animation, R.anim.checkout_scale_out);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.checkout_scale_out);
    }
}
