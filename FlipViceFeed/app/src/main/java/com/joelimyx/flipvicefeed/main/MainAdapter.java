package com.joelimyx.flipvicefeed.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joelimyx.flipvicefeed.R;

import java.util.List;

/**
 * Created by Joe on 11/29/16.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private List<Article> mArticleList;
    private OnItemSelectedListener mListener;

    interface OnItemSelectedListener{
        void onItemSelected(int id);
    }

    public MainAdapter(List<Article> articleList, OnItemSelectedListener listener) {
        mArticleList = articleList;
        mListener = listener;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_article,parent,false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position) {

        holder.mArticleItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemSelected(mArticleList.get(position).getID());
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
        private RelativeLayout mArticleItemLayout;
        public MainViewHolder(View itemView) {
            super(itemView);
            mTitleText = (TextView) itemView.findViewById(R.id.article_item_title);
            mArticleImage = (ImageView) itemView.findViewById(R.id.article_item_image);
            mArticleItemLayout = (RelativeLayout) itemView.findViewById(R.id.article_item_layout);
        }
    }
}
