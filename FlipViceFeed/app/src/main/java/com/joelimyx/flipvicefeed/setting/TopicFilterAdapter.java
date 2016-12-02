package com.joelimyx.flipvicefeed.setting;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.classes.TopicObject;
import com.joelimyx.flipvicefeed.database.AlarmSQLHelper;

import java.util.List;

/**
 * Created by Joe on 12/2/16.
 */

public class TopicFilterAdapter extends RecyclerView.Adapter<TopicFilterAdapter.TopicViewHolder> {
    List<TopicObject> mTopicList;
    Context mContext;
    private static final String TAG = "TopicFilterAdapter";

    public TopicFilterAdapter(List<TopicObject> topicList, Context context) {
        mTopicList = topicList;
        mContext = context;
    }

    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopicViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_topic_filter,parent,false));
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, int position) {
        final TopicObject current = mTopicList.get(position);
        holder.mTopicText.setText(current.getTopic());
        holder.mTopicSelectedCheckBox.setChecked(current.isSelected());
        holder.mTopicSelectedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AlarmSQLHelper.getInstance(mContext).updateSelectedTopic(current.getTopic(),!current.isSelected());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTopicList.size();
    }

    class TopicViewHolder extends RecyclerView.ViewHolder{
        TextView mTopicText;
        CheckBox mTopicSelectedCheckBox;
        public TopicViewHolder(View itemView) {
            super(itemView);
            mTopicText = (TextView) itemView.findViewById(R.id.topic);
            mTopicSelectedCheckBox = (CheckBox) itemView.findViewById(R.id.filter_checkbox);
        }
    }
}
