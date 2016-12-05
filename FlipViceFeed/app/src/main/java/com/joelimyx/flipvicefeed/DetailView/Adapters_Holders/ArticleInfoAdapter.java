package com.joelimyx.flipvicefeed.detailview.adapters_holders;

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

import com.joelimyx.flipvicefeed.detailview.articleobjectdata.ArticleObject;
import com.joelimyx.flipvicefeed.detailview.articleobjectdata.Image;
import com.joelimyx.flipvicefeed.detailview.articleobjectdata.Text;
import com.joelimyx.flipvicefeed.detailview.DetailActivity;
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
        }
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

    @Override
    public int getItemCount() {
        return mListOfObjects.size();
    }
}
