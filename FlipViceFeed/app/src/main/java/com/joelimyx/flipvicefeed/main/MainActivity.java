package com.joelimyx.flipvicefeed.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.joelimyx.flipvicefeed.R;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mMainRecyclerView;
    private MainAdapter mAdapter;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Reference
        mMainRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);

        mTwoPane = findViewById(R.id.fragment_container)!=null;
        mMainRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

    }
}
