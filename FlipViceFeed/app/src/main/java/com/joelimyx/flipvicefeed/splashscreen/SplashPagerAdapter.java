package com.joelimyx.flipvicefeed.splashscreen;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by KorbBookProReturns on 12/4/16.
 */

public class SplashPagerAdapter extends FragmentPagerAdapter {
    public SplashPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new WelcomeFragment();
            case 1:
                return new TopicsFragment();
            case 2:
                return new NotificationsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount(){
        return 3;
    }
}

