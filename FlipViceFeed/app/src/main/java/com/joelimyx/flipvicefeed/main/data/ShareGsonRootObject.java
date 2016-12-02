
package com.joelimyx.flipvicefeed.main.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShareGsonRootObject {

    @SerializedName("data")
    @Expose
    private ShareData data;

    /**
     *
     * @return
     *     The data
     */
    public ShareData getData() {
        return data;
    }

    /**
     *
     * @param data
     *     The data
     */
    public void setData(ShareData data) {
        this.data = data;
    }

}
