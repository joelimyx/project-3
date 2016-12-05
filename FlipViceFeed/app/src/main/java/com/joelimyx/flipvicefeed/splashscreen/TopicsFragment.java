package com.joelimyx.flipvicefeed.splashscreen;

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
import com.joelimyx.flipvicefeed.setting.TopicFilterAdapter;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by KorbBookProReturns on 12/4/16.
 */

public class TopicsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.splash_fragment_topics,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.splash_topics_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        List<TopicObject> mTopicList = AlarmSQLHelper.getInstance(getContext()).getAllTopic();
        Log.d(TAG, "onViewCreated: "+mTopicList.get(0).isSelected());
        recyclerView.setAdapter(
                new TopicFilterAdapter(
                        AlarmSQLHelper.getInstance(getContext())
                                .getAllTopic(),getContext()));
    }
}
