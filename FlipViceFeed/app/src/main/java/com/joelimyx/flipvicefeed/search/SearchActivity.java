package com.joelimyx.flipvicefeed.search;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.classes.Item;
import com.joelimyx.flipvicefeed.detailview.DetailActivity;
import com.joelimyx.flipvicefeed.main.main.MainActivity;
import com.joelimyx.flipvicefeed.main.main.MainAdapter;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements MainAdapter.OnItemSelectedListener {

    public static final String SEARCH_KEY = "search";
    private ArrayList<Item> mSearchResults;
    private RecyclerView mRecyclerView;
    private ActivityOptions mActivityOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchResults = getIntent().getParcelableArrayListExtra(SEARCH_KEY);

        mRecyclerView = (RecyclerView) findViewById(R.id.searchRecyclerView);

        MainAdapter adapter = new MainAdapter(mSearchResults, this, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemSelected(int id, View view) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("id", id);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            android.util.Pair<View, String> mainPair = android.util.Pair.create(findViewById(R.id.article_item_image), getString(R.string.main_to_detail));
            mActivityOptions = ActivityOptions.makeSceneTransitionAnimation(SearchActivity.this, mainPair);
            startActivity(intent, mActivityOptions.toBundle());
        } else {
            startActivity(intent);
        }
    }
}
