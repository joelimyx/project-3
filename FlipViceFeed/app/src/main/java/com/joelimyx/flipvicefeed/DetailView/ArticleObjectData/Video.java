package com.joelimyx.flipvicefeed.DetailView.ArticleObjectData;

import android.net.Uri;

/**
 * Created by colinbradley on 12/3/16.
 */

public class Video extends ArticleObject {

    String mVideoLink;

    public Video(String videoLink) {
        mVideoLink = videoLink;
    }

    public String getVideoLink() {
        return mVideoLink;
    }

    public void setVideoLink(String videoLink) {
        this.mVideoLink = videoLink;
    }

    @Override
    public String getName() {
        return null;
    }
}
