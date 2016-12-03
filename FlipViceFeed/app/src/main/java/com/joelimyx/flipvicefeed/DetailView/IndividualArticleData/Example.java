package com.joelimyx.flipvicefeed.detailview.individualarticledata;

/**
 * Created by colinbradley on 12/1/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {

    @SerializedName("data")
    @Expose
    private ArticleData data;

    /**
     * @return The data
     */
    public ArticleData getData() {
        return data;
    }
}
