package com.joelimyx.flipvicefeed.main;

/**
 * Created by Joe on 11/29/16.
 */

public class Article {
    private String mImgUrl, mTitle;
    private int mID;

    public Article(String imgUrl, String title, int ID) {
        mImgUrl = imgUrl;
        mTitle = title;
        mID = ID;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getID() {
        return mID;
    }
}
