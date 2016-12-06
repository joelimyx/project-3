package com.joelimyx.flipvicefeed.classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ds on 12/5/16.
 */

public class Tags {


    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("tags")
    @Expose
    private List<String> tags = new ArrayList<>();

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }


    /**
     * @return The thumb
     */
    public String getThumb() {
        return thumb;
    }

    /**
     * @return The body
     */
    public String getBody() {
        return body;
    }

    /**
     * @return The tags
     */
    public List<String> getTags()

    {
        return tags;
    }

}
