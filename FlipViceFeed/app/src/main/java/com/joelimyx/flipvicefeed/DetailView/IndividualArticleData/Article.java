package com.joelimyx.flipvicefeed.DetailView.IndividualArticleData;

/**
 * Created by colinbradley on 12/1/16.
 */

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("tags")
    @Expose
    private List<String> tags = new ArrayList<String>();

    @SerializedName("body")
    @Expose
    private String body;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("pubDate")
    @Expose
    private String pubDate;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("thumb")
    @Expose
    private String thumb;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("thumb_10_4")
    @Expose
    private String thumb104;

    @SerializedName("thumb_10_3")
    @Expose
    private String thumb103;

    @SerializedName("thumb_16_9")
    @Expose
    private String thumb169;

    @SerializedName("thumb_7_10")
    @Expose
    private String thumb710;

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return
     * The tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     *
     * @return
     * The body
     */
    public String getBody() {
        return body;
    }

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @return
     * The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @return
     * The author
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @return
     * The pubDate
     */
    public String getPubDate() {
        return pubDate;
    }

    /**
     *
     * @return
     * The category
     */
    public String getCategory() {
        return category;
    }

    /**
     *
     * @return
     * The thumb
     */
    public String getThumb() {
        return thumb;
    }

    /**
     *
     * @return
     * The image
     */
    public String getImage() {
        return image;
    }

    /**
     *
     * @return
     * The thumb104
     */
    public String getThumb104() {
        return thumb104;
    }

    /**
     *
     * @return
     * The thumb103
     */
    public String getThumb103() {
        return thumb103;
    }

    /**
     *
     * @return
     * The thumb169
     */
    public String getThumb169() {
        return thumb169;
    }

    /**
     *
     * @return
     * The thumb710
     */
    public String getThumb710() {
        return thumb710;
    }
}
