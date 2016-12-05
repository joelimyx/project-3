package com.joelimyx.flipvicefeed.detailview.individualarticledata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by colinbradley on 12/1/16.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArticleData {

    @SerializedName("article")
    @Expose
    private Article article;

    /**
     *
     * @return
     * The article
     */
    public Article getArticle() {
        return article;
    }

    /**
     *
     * @param article
     * The article
     */
    public void setArticle(Article article) {
        this.article = article;
    }

}