package com.joelimyx.flipvicefeed.classes;

/**
 * Created by Joe on 12/1/16.
 */

public class TopicObject {
    String mTopic;
    boolean mIsSelected;

    public TopicObject(String topic, int isSelected) {
        mTopic = topic;
        mIsSelected = setIsSelectedWithInt(isSelected);
    }

    public String getTopic() {
        return mTopic;
    }

    public boolean setIsSelectedWithInt(int isSelected){
        return isSelected==1;
    }

    public void setSelected(boolean selected) {
        mIsSelected = selected;
    }

    public boolean isSelected() {
        return mIsSelected;
    }
}
