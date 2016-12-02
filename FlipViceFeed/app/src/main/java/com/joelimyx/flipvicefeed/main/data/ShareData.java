
package com.joelimyx.flipvicefeed.main.data;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShareData {

    @SerializedName("article")
    @Expose
    private ShareItem article;

    /**
     *
     * @return
     *     The article
     */
    public ShareItem getArticle() {
        return article;
    }



}
