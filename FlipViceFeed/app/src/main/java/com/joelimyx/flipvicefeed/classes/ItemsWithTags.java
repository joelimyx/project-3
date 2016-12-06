package com.joelimyx.flipvicefeed.classes;

import android.nfc.Tag;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ds on 12/5/16.
 */

public class ItemsWithTags {

    @SerializedName("items")
    @Expose
    private List<Tags> items = new ArrayList<>();


    /**
     * @return The items
     */

    public List<Tags> getItems() {
        return items;
    }


}

