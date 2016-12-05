
package com.joelimyx.flipvicefeed.main.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShareGsonRootObject {

    @SerializedName("data")
    @Expose
    private FacebookShareData data;

    /**
     *
     * @return
     *     The data
     */
    public FacebookShareData getData() {
        return data;
    }

    /**
     *
     * @param data
     *     The data
     */
    public void setData(FacebookShareData data) {
        this.data = data;
    }

}
