package com.joelimyx.flipvicefeed.main.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.main.data.Article;
import com.joelimyx.flipvicefeed.main.data.Item;
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
    public void onBindViewHolder(MainViewHolder holder, final int position) {

        holder.mTitleText.setText(mArticleList.get(position).getTitle());
        Picasso.with(mContext)
                .load(mArticleList.get(position).getThumb())
                .fit()
                .into(holder.mArticleImage);
        holder.mArticleItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemSelected(mArticleList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
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
