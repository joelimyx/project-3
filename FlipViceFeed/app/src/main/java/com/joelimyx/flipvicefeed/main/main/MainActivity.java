package com.joelimyx.flipvicefeed.main.main;

import android.content.Intent;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.joelimyx.flipvicefeed.classes.Data;
import com.joelimyx.flipvicefeed.classes.ItemsWithTags;
import com.joelimyx.flipvicefeed.classes.Tags;
import com.joelimyx.flipvicefeed.classes.DataRootWithTags;
import com.joelimyx.flipvicefeed.detailview.DetailActivity;
import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.database.DBAssetHelper;
import com.joelimyx.flipvicefeed.classes.GsonArticle;
import com.joelimyx.flipvicefeed.classes.Item;
import com.joelimyx.flipvicefeed.classes.VolleySingleton;
import com.joelimyx.flipvicefeed.setting.NotificationFragment;
import com.joelimyx.flipvicefeed.setting.SettingActivity;
import com.joelimyx.flipvicefeed.setting.TopicFilterFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MainAdapter.OnItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener {

    private ActionBarDrawerToggle mToggle;
    private RecyclerView mMainRecyclerView;
    private MainAdapter mAdapter;
    private DrawerLayout mDrawerLayout;
    private boolean mTwoPane;
    private VolleySingleton mVolleySingleton;
    private static Snackbar mSnackbar;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private EndlessRecyclerViewScrollListener mScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Reference
        mTwoPane = findViewById(R.id.fragment_container) != null;
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mVolleySingleton = VolleySingleton.getInstance(this);

        //Setup database
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                DBAssetHelper dbSetup = new DBAssetHelper(MainActivity.this);
                dbSetup.getReadableDatabase();
                return null;
            }
        }.execute();

        mSnackbar = Snackbar.make(findViewById(R.id.drawer_layout), "No Network Available", Snackbar.LENGTH_INDEFINITE);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Latest");

        //RecyclerView
        mMainRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMainRecyclerView.setLayoutManager(layoutManager);
        mMainRecyclerView.setAdapter(new MainAdapter(new ArrayList<Item>(), this, this));

        //Endless Scroll for Main RecyclerView
        mScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                addArticleToRecyclerView(page);
            }
        };
        mMainRecyclerView.addOnScrollListener(mScrollListener);

        //Enable swipe down refresh
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setRefreshing(true);

        /*---------------------------------------------------------------------------------
        // Drawer SETUP
        ---------------------------------------------------------------------------------*/
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.open_nav_bar,
                R.string.close_nav_bar);
        mToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*---------------------------------------------------------------------------------
        // Network Connection
        ---------------------------------------------------------------------------------*/
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            getLatestNews(0);
        } else {
            mSnackbar.show();
        }
    }

    @Override
    public void onItemSelected(int id) {
        //// TODO: 11/30/16 start detail activity if not in tablet else start detail fragment
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    /*---------------------------------------------------------------------------------
    // Navigation Drawer
    ---------------------------------------------------------------------------------*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mSwipeRefreshLayout.setRefreshing(true);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_current_filter), MODE_PRIVATE);
        switch (item.getItemId()) {
            //Grab the latest news
            case R.id.latest:
                getLatestNews(0);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                getSupportActionBar().setTitle("Latest");
                sharedPreferences.edit().putString(getString(R.string.current_filter), "latest").commit();
                mSwipeRefreshLayout.setRefreshing(false);
                return true;

            //Else grab the news according to the topic selected
            default:
                mAdapter.swapdata((String) item.getTitle());
                mScrollListener.resetState();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                mMainRecyclerView.scrollToPosition(0);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(item.getTitle());
                }
                sharedPreferences.edit().putString(getString(R.string.current_filter), (String) item.getTitle()).commit();
                mSwipeRefreshLayout.setRefreshing(false);
                return true;
        }
    }

    /*---------------------------------------------------------------------------------
    // Toolbar AREA
    ---------------------------------------------------------------------------------*/

    //Inflate And Setup Search View
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();

        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        final SearchView.OnQueryTextListener queryTextListener =
                new SearchView.OnQueryTextListener() {

                    public boolean onQueryTextChange(String newText) {
                        // Adapter that will be filtered
                        return true;
                    }

                    public boolean onQueryTextSubmit(String query) {
                        returnSearchTypes(query);
                        return true;
                    }
                };

        assert searchView != null;
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);

    }

    private void returnSearchTypes(final String query) {

        // Instantiate the RequestQueue
        String url = "http://vice.com/api/getlatest/1";

        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        DataRootWithTags dataRootWithTags = new Gson().fromJson(response, DataRootWithTags.class);
                        List<Tags> tagList = dataRootWithTags.getData().getItems();
                        for (Tags article : tagList) {
                            List<String> tags = article.getTags();
                            for (String tag : tags) {
                                if (tag.equals(query)) {


                                }

                            }
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error getting articles", Toast.LENGTH_SHORT).show();
                    }
                });

        mVolleySingleton.addToRequestQueue(stringRequest);
    }

//
//        // Instantiate the RequestQueue
//        String url = "http://vice.com/api/getlatest/" + queryText;
//
//        // Request a string response from the provided URL
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {

//                        GsonArticle gsonType = new Gson().fromJson(response, GsonArticle.class);
////                      List<Item> items = gsonType.getData().getItems();

//                        mAdapter.addData(items);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), "Error getting articles", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//        mVolleySingleton.addToRequestQueue(stringRequest);
//    }

    //Select Option on Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.alarm_settings:
            case R.id.topic_setting:
                if (mTwoPane) {
                    if (item.getTitle().equals(getString(R.string.topic_filter))) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new TopicFilterFragment())
                                .commit();
                    } else {

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new NotificationFragment())
                                .commit();
                    }
                } else {
                    Intent intent = new Intent(this, SettingActivity.class);
                    intent.putExtra("setting", item.getTitle());
                    startActivity(intent);
                }
                return true;
            case R.id.search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*---------------------------------------------------------------------------------
    // SwipeRefreshLayout AREA
    ---------------------------------------------------------------------------------*/
    //When refresh is initiated by swiping down at the very top
    @Override
    public void onRefresh() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_current_filter), MODE_PRIVATE);
        String currentFilter = sharedPreferences.getString(getString(R.string.current_filter), null);
        if (currentFilter != null) {
            if (currentFilter.equals("latest")) {
                getLatestNews(0);
            } else {
                mAdapter.swapdata(currentFilter);
                mScrollListener.resetState();
            }
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /*---------------------------------------------------------------------------------
    // Helper Method AREA
    ---------------------------------------------------------------------------------*/

    /**
     * Grab the latest news with selected page
     *
     * @param page current page to grab
     */
    private void getLatestNews(int page) {
        String url = "http://www.vice.com/api/getlatest/" + page;
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_current_filter), MODE_PRIVATE);
        sharedPreferences.edit().putString(getString(R.string.current_filter), "latest").commit();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        mSwipeRefreshLayout.setRefreshing(false);

                        //Extracting data
                        GsonArticle gsonArticle = new Gson().fromJson(response, GsonArticle.class);
                        List<Item> items = gsonArticle.getData().getItems();

                        //Setup up adapter to recycler view
                        mAdapter = new MainAdapter(items, MainActivity.this, MainActivity.this);
                        mMainRecyclerView.setAdapter(mAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(MainActivity.this, "Error getting articles", Toast.LENGTH_SHORT).show();
                    }
                });

        mVolleySingleton.addToRequestQueue(request);
    }

    /**
     * Helper method for adding article to recyclerview while scrolling
     *
     * @param page
     */
    private void addArticleToRecyclerView(int page) {

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_current_filter), MODE_PRIVATE);
        String currentFilter = sharedPreferences.getString(getString(R.string.current_filter), null);
        String url = "http://www.vice.com/api/getlatest/";
        if (currentFilter != null) {
            if (currentFilter.equals("latest")) {
                url += page;
            } else {
                url += "category/" + currentFilter + "/" + page;
            }
        }
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Extracting data
                        GsonArticle gsonArticle = new Gson().fromJson(response, GsonArticle.class);
                        List<Item> items = gsonArticle.getData().getItems();

                        //Setup up adapter to recycler view
                        mAdapter.addData(items);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error getting articles", Toast.LENGTH_SHORT).show();
                    }
                });
        mVolleySingleton.addToRequestQueue(request);

    }

    /*---------------------------------------------------------------------------------
    // Network State AREA
    ---------------------------------------------------------------------------------*/
    //Network State Listener to show or dismiss snackbar
    public static class NetworkStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager conn = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = conn.getActiveNetworkInfo();

            if (networkInfo == null) {
                mSnackbar.show();
            } else {
                mSnackbar.dismiss();
            }
        }
    }
}
