package com.joelimyx.flipvicefeed.splashscreen;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.joelimyx.flipvicefeed.R;

public class WelcomeActivity extends AppCompatActivity
    {

    ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        vp = (ViewPager) findViewById(R.id.welcome_viewpager);
        SplashPagerAdapter sp = new SplashPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(sp);

    }

    public void secondPageJumper(View view) {
        vp.setCurrentItem(1);
    }

    public void thirdPageJumper(View view) {
        vp.setCurrentItem(2);
    }

//    @Override
//    public void finishWelcomeActivity() {
//        finish();
//    }


}
