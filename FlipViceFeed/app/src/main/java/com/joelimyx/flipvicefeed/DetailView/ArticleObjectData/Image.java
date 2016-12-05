package com.joelimyx.flipvicefeed.detailview.articleobjectdata;

import com.squareup.picasso.Picasso;

/**
 * Created by colinbradley on 11/30/16.
 */

public class Image extends ArticleObject{

    private String mURL;

    public Image(String url){
        mURL = url;
    }

    public String getURL() {
        return mURL;
    }

    public void setURL(String mURL) {
        this.mURL = mURL;
    }

    @Override
    public String getName() {
        return null;
    }
}
