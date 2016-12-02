package com.joelimyx.flipvicefeed.classes;

/**
 * Created by KorbBookProReturns on 11/30/16.
 */

public class TimeObject {
    Integer mHour,mMinute,mDay;

    public TimeObject(Integer hour, Integer minute, Integer day) {
        mHour = hour;
        mMinute = minute;
        mDay = day;
    }

    public Integer getHour() {
        return mHour;
    }

    public void setHour(Integer hour) {
        mHour = hour;
    }

    public Integer getMinute() {
        return mMinute;
    }

    public void setMinute(Integer minute) {
        mMinute = minute;
    }

    public Integer getDay() {
        return mDay;
    }

    public void setDay(Integer day) {
        mDay = day;
    }
}
