package com.joelimyx.flipvicefeed.main.main;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.joelimyx.flipvicefeed.DetailView.DetailActivity;
import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.database.DBAssetHelper;
import com.joelimyx.flipvicefeed.main.network.NetworkStateReceiver;
import com.joelimyx.flipvicefeed.classes.GsonArticle;
import com.joelimyx.flipvicefeed.classes.Item;
import com.joelimyx.flipvicefeed.classes.VolleySingleton;
import com.joelimyx.flipvicefeed.setting.SettingActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MainAdapter.OnItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener{
    ActionBarDrawerToggle mToggle;
    private RecyclerView mMainRecyclerView;
    private MainAdapter mAdapter;
    private DrawerLayout mDrawerLayout;
    private boolean mTwoPane;
    private VolleySingleton mVolleySingleton;
    private NetworkStateReceiver mNetworkStateReceiver;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Reference
        mTwoPane = findViewById(R.id.fragment_container)!=null;
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

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Latest");

        //Registers Broadcast Receiver to Track Network Change
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mNetworkStateReceiver = new NetworkStateReceiver();
        this.registerReceiver(mNetworkStateReceiver, filter);

        //RecyclerView
        mMainRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        mMainRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mMainRecyclerView.setAdapter(new MainAdapter(new ArrayList<Item>(),this,this));

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
            getLatestNews();
        } else {
            Snackbar.make(findViewById(R.id.drawer_layout), "No Network Connection", Snackbar.LENGTH_INDEFINITE).show();
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
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_current_filter),MODE_PRIVATE);
        switch (item.getItemId()){
            case R.id.latest:
                getLatestNews();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                getSupportActionBar().setTitle("Latest");
                sharedPreferences.edit().putString(getString(R.string.current_filter),"latest").commit();
                mSwipeRefreshLayout.setRefreshing(false);
                return true;
            default:
                mAdapter.swapdata((String) item.getTitle());
                mDrawerLayout.closeDrawer(GravityCompat.START);
                mMainRecyclerView.scrollToPosition(0);
                if (getSupportActionBar()!=null) {
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
    //Inflate Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Select Option on Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.alarm_settings:
            case R.id.topic_setting:
                Intent intent = new Intent(this,SettingActivity.class);
                intent.putExtra("setting",item.getTitle());
                startActivity(intent);
                return true;
            case R.id.search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*---------------------------------------------------------------------------------
    // Helper Method
    ---------------------------------------------------------------------------------*/
    public void getLatestNews(){
        String url = "http://www.vice.com/api/getlatest/";
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_current_filter),MODE_PRIVATE);
        sharedPreferences.edit().putString(getString(R.string.current_filter),"latest").commit();

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

    //When refresh is initiated
    @Override
    public void onRefresh() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_current_filter),MODE_PRIVATE);
        String currentFilter = sharedPreferences.getString(getString(R.string.current_filter),null);
        if (currentFilter!=null){
            if(currentFilter.equals("latest")) {
                getLatestNews();
            }else{
                mAdapter.swapdata(currentFilter);
            }
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregisters BroadcastReceiver when app is destroyed.
        if (mNetworkStateReceiver != null) {
            this.unregisterReceiver(mNetworkStateReceiver);
        }
    }
}
