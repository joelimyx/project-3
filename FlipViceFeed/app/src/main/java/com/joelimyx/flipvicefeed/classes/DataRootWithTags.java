package com.joelimyx.flipvicefeed.classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ds on 12/5/16.
 */

public class DataRootWithTags {

    @SerializedName("data")
    @Expose
    private ItemsWithTags data;

    /**
     *
     * @return
     *     The data
     */
    public ItemsWithTags getData() {
        return data;
    }

    /**
     *
     * @param data
     *     The data
     */
    public void setData(ItemsWithTags data) {
        this.data = data;
    }

}
