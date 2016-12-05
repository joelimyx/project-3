package com.joelimyx.flipvicefeed.detailview.articleobjectdata;

/**
 * Created by colinbradley on 12/5/16.
 */

public class PhotoCredit extends ArticleObject {

    private String mPhotoCredit;

    public PhotoCredit(String photoCredit) {
        mPhotoCredit = photoCredit;
    }

    public String getPhotoCredit() {
        return mPhotoCredit;
    }

    public void setPhotoCredit(String photoCredit) {
        mPhotoCredit = photoCredit;
    }

    @Override
    public String getName() {
        return null;
    }
}
