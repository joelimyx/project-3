package com.joelimyx.flipvicefeed.main.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.classes.GsonArticle;
import com.joelimyx.flipvicefeed.classes.Item;
import com.joelimyx.flipvicefeed.classes.VolleySingleton;
import com.joelimyx.flipvicefeed.search.SearchActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe on 11/29/16.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private List<Item> mArticleList;
    private OnItemSelectedListener mListener;
    private Context mContext;
    private int lastPosition = -1;

    public interface OnItemSelectedListener{
        void onItemSelected(int id, View view);
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
                mListener.onItemSelected(mArticleList.get(position).getId(),holder.mArticleImage);
            }
        });
        animateRecyclerView(holder.mArticleItemLayout,position);
    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }

    /*---------------------------------------------------------------------------------
    // Helper Method
    ---------------------------------------------------------------------------------*/
    public void swapData(String path){
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

    public void addData(List<Item> addedItems){
        int start = getItemCount();
        mArticleList.addAll(addedItems);
        notifyItemRangeInserted(start,addedItems.size());
    }

    public void addQueriedData(List<Item> addedTags) {
        mArticleList.clear();
        mArticleList.addAll(addedTags);
        notifyDataSetChanged();
    }

    private void animateRecyclerView(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.list_item_main_animation);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void showSearchResults(String query) {
        ArrayList<Item> matchingList = new ArrayList<>();
        for (int i = 0; i < mArticleList.size(); i++) {
            for (String tag:mArticleList.get(i).getTags()) {
                if (tag.toLowerCase().equals(query.toLowerCase())){
                    matchingList.add(mArticleList.get(i));
                }

            }
        }
        if (!matchingList.isEmpty()) {
            Intent intent = new Intent(mContext, SearchActivity.class);
            intent.putParcelableArrayListExtra(SearchActivity.SEARCH_KEY, matchingList);
            mContext.startActivity(intent);
        }
    }

    /*---------------------------------------------------------------------------------
    // Main View Holder
    ---------------------------------------------------------------------------------*/
    class MainViewHolder extends RecyclerView.ViewHolder {
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
