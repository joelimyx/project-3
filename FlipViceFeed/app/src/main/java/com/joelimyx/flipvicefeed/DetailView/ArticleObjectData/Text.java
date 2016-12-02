package com.joelimyx.flipvicefeed.DetailView.ArticleObjectData;

/**
 * Created by colinbradley on 11/30/16.
 */

public class Text extends ArticleObject{

    private String mBodyText;

    public Text (String body){
        mBodyText = body;
    }

    public String getBodyText() {
        return mBodyText;
    }

    public void setBodyText(String mBodyText) {
        this.mBodyText = mBodyText;
    }

    @Override
    public String getName() {
        return null;
    }
}
