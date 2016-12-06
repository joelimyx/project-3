package com.joelimyx.flipvicefeed.detailview.articleobjectdata;

/**
 * Created by colinbradley on 12/6/16.
 */

public class TextStrong extends ArticleObject {

    private String mStrongText;

    public TextStrong(String mStrongText) {
        this.mStrongText = mStrongText;
    }

    public String getmStrongText() {
        return mStrongText;
    }

    public void setmStrongText(String mStrongText) {
        this.mStrongText = mStrongText;
    }

    @Override
    public String getName() {
        return null;
    }
}
