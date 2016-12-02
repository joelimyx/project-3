
package com.joelimyx.flipvicefeed.main.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

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

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }


    /**
     * 
     * @return
     *     The thumb
     */
    public String getThumb() {
        return thumb;
    }

    /**
     *
     * @return
     *      The body
     */
    public String getBody(){
        return body;
    }

}
