
package com.joelimyx.flipvicefeed.main.data;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("items")
    @Expose
    private List<Item> items = new ArrayList<>();

    /**
     * 
     * @return
     *     The items
     */
    public List<Item> getItems() {
        return items;
    }


}
