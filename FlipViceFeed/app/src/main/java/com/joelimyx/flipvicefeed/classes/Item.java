
package com.joelimyx.flipvicefeed.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Item implements Parcelable{

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

    public Item() {
    }

    protected Item(Parcel in) {
        title = in.readString();
        thumb = in.readString();
        body = in.readString();
        tags = in.createStringArrayList();
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(thumb);
        dest.writeString(body);
        dest.writeStringList(tags);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

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

    /**
     * @return The tags
     */
    public List<String> getTags()

    {
        return tags;
    }



}
