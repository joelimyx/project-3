package com.joelimyx.flipvicefeed.detailview.adapters_holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.joelimyx.flipvicefeed.detailview.articleobjectdata.Image;
import com.joelimyx.flipvicefeed.R;
import com.squareup.picasso.Picasso;

import java.net.URL;

/**
 * Created by colinbradley on 11/30/16.
 */

public class ImageHolder extends RecyclerView.ViewHolder {

    public ImageView mImage;

    public ImageHolder(View itemView) {
        super(itemView);

        mImage = (ImageView)itemView.findViewById(R.id.article_image);
    }

    public void bindDataToViews(String imageUrl, Context context){

        Picasso.with(context)
                .load(imageUrl)
                .fit()
                .into(mImage);

    }
}
