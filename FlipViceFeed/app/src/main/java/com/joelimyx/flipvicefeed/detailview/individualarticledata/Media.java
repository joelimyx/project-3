package com.joelimyx.flipvicefeed.detailview.individualarticledata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by colinbradley on 12/6/16.
 */

public class Media {
    @SerializedName("gallery")
    @Expose
    private List<Gallery> gallery = null;

    /**
     *
     * @return
     * The gallery
     */
    public List<Gallery> getGallery() {
        return gallery;
    }

    /**
     *
     * @param gallery
     * The gallery
     */
    public void setGallery(List<Gallery> gallery) {
        this.gallery = gallery;
    }

}
