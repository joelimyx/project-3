package com.joelimyx.flipvicefeed.main.main;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.joelimyx.flipvicefeed.DetailView.DetailActivity;
import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.main.data.GsonArticle;
import com.joelimyx.flipvicefeed.main.data.Item;
import com.joelimyx.flipvicefeed.main.data.VolleySingleton;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Joe on 11/29/16.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private List<Item> mArticleList;
    private OnItemSelectedListener mListener;
    private Context mContext;

    interface OnItemSelectedListener{
        void onItemSelected(int id);
    }

    public MainAdapter(List<Item> articleList, OnItemSelectedListener listener, Context context) {
        mArticleList = articleList;
        mListener = listener;
        mContext = context;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_article,parent,false));
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, final int position) {

        holder.mTitleText.setText(mArticleList.get(position).getTitle());
        Picasso.with(mContext).setLoggingEnabled(true);
        Picasso.with(mContext)
                .load(mArticleList.get(position).getThumb())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .fit()
                .into(holder.mArticleImage);
        holder.mArticleItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemSelected(mArticleList.get(position).getId());
                String id = mArticleList.get(position).getId().toString();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }

    public void swapdata(String path){
        String url = "http://vice.com/api/getlatest/category/"+path;
        mArticleList.clear();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Extracting data
                        GsonArticle gsonArticle = new Gson().fromJson(response,GsonArticle.class);
                        List<Item> items = gsonArticle.getData().getItems();

                        //Setup up adapter to recycler view
                        mArticleList.addAll(items);
                        notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, "Error getting articles", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleySingleton.getInstance(mContext).addToRequestQueue(request);
    }

    class MainViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitleText;
        private ImageView mArticleImage;
        private FrameLayout mArticleItemLayout;
        public MainViewHolder(View itemView) {
            super(itemView);
            mTitleText = (TextView) itemView.findViewById(R.id.article_item_title);
            mArticleImage = (ImageView) itemView.findViewById(R.id.article_item_image);
            mArticleItemLayout = (FrameLayout) itemView.findViewById(R.id.article_item_layout);
        }
    }
}
