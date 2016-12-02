package com.joelimyx.flipvicefeed.main.main;

import android.content.Intent;
import android.app.SearchManager;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import com.google.gson.Gson;
import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.main.network.NetworkStateReceiver;
import com.joelimyx.flipvicefeed.classes.GsonArticle;
import com.joelimyx.flipvicefeed.classes.Item;
import com.joelimyx.flipvicefeed.classes.VolleySingleton;
import com.joelimyx.flipvicefeed.database.DBAssetHelper;
import com.joelimyx.flipvicefeed.setting.SettingActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MainAdapter.OnItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener {
    ActionBarDrawerToggle mToggle;
    private RecyclerView mMainRecyclerView;
    private MainAdapter mAdapter;
    private DrawerLayout mDrawerLayout;
    private boolean mTwoPane;
    private VolleySingleton mVolleySingleton;
    private NetworkStateReceiver mNetworkStateReceiver;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mVolleySingleton = VolleySingleton.getInstance(this);
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

        mTwoPane = findViewById(R.id.fragment_container) != null;

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

        //Network Connection
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
    }

    /*---------------------------------------------------------------------------------
    // Navigation Drawer
    ---------------------------------------------------------------------------------*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mProgressBar.setVisibility(View.VISIBLE);
        switch (item.getItemId()){
            case R.id.latest:
                getLatestNews();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                getSupportActionBar().setTitle("Latest");
                mProgressBar.setVisibility(View.GONE);
                return true;
            default:
                mAdapter.swapdata((String) item.getTitle());
                mDrawerLayout.closeDrawer(GravityCompat.START);
                mMainRecyclerView.scrollToPosition(0);
                if (getSupportActionBar()!=null) {
                    getSupportActionBar().setTitle(item.getTitle());
                }
                mProgressBar.setVisibility(View.GONE);
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

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        mProgressBar.setVisibility(View.GONE);

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
                        Toast.makeText(MainActivity.this, "Error getting articles", Toast.LENGTH_SHORT).show();
                    }
                });

        mVolleySingleton.addToRequestQueue(request);
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
