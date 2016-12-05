package com.joelimyx.flipvicefeed.DetailView.Adapters_Holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.VideoView;

import com.joelimyx.flipvicefeed.R;

/**
 * Created by colinbradley on 12/3/16.
 */

public class VideoHolder extends RecyclerView.ViewHolder {

    WebView mVideo;

    public VideoHolder(View itemView) {
        super(itemView);

        mVideo = (WebView)itemView.findViewById(R.id.article_video);
    }

    public void bindDataToViews(String url, Context context){



        WebSettings webSettings = mVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mVideo.loadData(url, "text/html", null);



    }
}
