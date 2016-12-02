package com.joelimyx.flipvicefeed.setting;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.classes.TopicObject;
import com.joelimyx.flipvicefeed.database.AlarmSQLHelper;

import java.util.List;

public class TopicFilterFragment extends Fragment {
    private static final String TAG = "TopicFilterFragment";
    public TopicFilterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_topic_filter, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.topic_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        List<TopicObject> mTopicList = AlarmSQLHelper.getInstance(getContext()).getAllTopic();
        Log.d(TAG, "onViewCreated: "+mTopicList.get(0).isSelected());
        recyclerView.setAdapter(
                new TopicFilterAdapter(
                        AlarmSQLHelper.getInstance(getContext())
                                .getAllTopic(),getContext()));
    }
}
