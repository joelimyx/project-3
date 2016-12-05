package com.joelimyx.flipvicefeed.DetailView.Adapters_Holders;

import android.content.Context;
import android.graphics.Picture;
import android.graphics.Point;
import android.hardware.Camera;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;

import com.joelimyx.flipvicefeed.DetailView.ArticleObjectData.ArticleObject;
import com.joelimyx.flipvicefeed.DetailView.ArticleObjectData.Image;
import com.joelimyx.flipvicefeed.DetailView.ArticleObjectData.PhotoCredit;
import com.joelimyx.flipvicefeed.DetailView.ArticleObjectData.Text;
import com.joelimyx.flipvicefeed.DetailView.ArticleObjectData.Video;
import com.joelimyx.flipvicefeed.DetailView.DetailActivity;
import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.main.main.MainActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by colinbradley on 11/30/16.
 */

public class ArticleInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final String TAG = "ARTICLE INFO ADAPTER";

    private List<ArticleObject> mListOfObjects;
    private Context mContext;

    public static final int IMAGE = 0;
    public static final int TEXT = 1;
    public static final int VIDEO = 2;
    public static final int PHOTO_CREDIT = 3;

    public ArticleInfoAdapter(List<ArticleObject> mObjectList, Context context) {
        mListOfObjects = mObjectList;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        //DETERMINES VIEW TYPE
        if (mListOfObjects.get(position) instanceof Image){
            return IMAGE;
        }else if (mListOfObjects.get(position) instanceof Text){
            return TEXT;
        }else if (mListOfObjects.get(position) instanceof Video){
            return VIDEO;
        }else if (mListOfObjects.get(position) instanceof PhotoCredit)
            return PHOTO_CREDIT;
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //CHECKS FOR VIEW TYPE AND INFLATES THE CORRECT LAYOUT
        switch (viewType){
            case IMAGE:
                View imageView = inflater.inflate(R.layout.detail_item_image_layout, parent, false);
                viewHolder = new ImageHolder(imageView);
                Log.d(TAG, "onCreateViewHolder: image");
                return viewHolder;
            case TEXT:
                View textView = inflater.inflate(R.layout.detail_item_text_layout, parent, false);
                viewHolder = new TextHolder(textView);
                Log.d(TAG, "onCreateViewHolder: text");
                return viewHolder;
            case VIDEO:
                View videoView = inflater.inflate(R.layout.detail_item_video, parent, false);
                viewHolder = new VideoHolder(videoView);
                Log.d(TAG, "onCreateViewHolder: video");
                return viewHolder;
            case PHOTO_CREDIT:
                View photocreditHolder = inflater.inflate(R.layout.detail_item_photocredit_layout, parent, false);
                viewHolder = new PhotoCreditHolder(photocreditHolder);
                Log.d(TAG, "onCreateViewHolder: photo credit");
                return viewHolder;
            default:
                View view = inflater.inflate(R.layout.detail_item_image_layout, parent, false);
                viewHolder = new ImageHolder(view);
                Log.d(TAG, "onCreateViewHolder: default");
                return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //ASSIGNS HOLDER TO VIEW AND SETS CONFIGURATION
        switch (holder.getItemViewType()){
            case IMAGE:
                ImageHolder imageHolder = (ImageHolder) holder;
                configureImageViewHolder(imageHolder, position);
                break;
            case TEXT:
                TextHolder textHolder = (TextHolder) holder;
                configureTextViewHolder(textHolder, position);
                break;
            case VIDEO:
                VideoHolder videoHolder = (VideoHolder) holder;
                configureVideoViewHolder(videoHolder, position);
                break;
            case PHOTO_CREDIT:
                PhotoCreditHolder photocreditHolder = (PhotoCreditHolder)  holder;
                configurePhotoCreditViewHolder(photocreditHolder,position);
                break;
        }
    }

    private void configureImageViewHolder(ImageHolder holder, int position){
        Image image = (Image) mListOfObjects.get(position);
        if (image != null){
            Picasso.with(mContext).setLoggingEnabled(true);
            Picasso.with(mContext)
                    .load(image.getURL())
                    .resize(1500,1250)
                    .into(holder.mImage);
            Log.d(TAG, "configureImageViewHolder: "+((Image) mListOfObjects.get(position)).getURL());
            if (((Image) mListOfObjects.get(position)).getURL() == null){
                Log.d("ADAPTER", "configureImageViewHolder: no URL ");
            }else{
                Log.d("ADAPTER", "configureImageViewHolder: URL RECIEVED " + ((Image) mListOfObjects.get(position)).getURL());
            }
        }
    }

    private void configureTextViewHolder(TextHolder holder, int position){
        Text text = (Text) mListOfObjects.get(position);
        if (text != null){
            holder.bindDataToViews(text);
        }
    }

    private void configureVideoViewHolder(VideoHolder holder, int position){
        Video video = (Video) mListOfObjects.get(position);
        if (video != null){
            holder.bindDataToViews(video.getVideoLink(), mContext);
        }
    }

    private void configurePhotoCreditViewHolder(PhotoCreditHolder holder, int position){
        PhotoCredit credit = (PhotoCredit) mListOfObjects.get(position);
        if (credit != null){
            holder.bindDataToView(credit);
        }
    }

    @Override
    public int getItemCount() {
        return mListOfObjects.size();
    }
}
