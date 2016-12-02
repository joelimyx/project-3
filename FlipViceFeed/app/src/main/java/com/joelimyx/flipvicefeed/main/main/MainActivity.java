package com.joelimyx.flipvicefeed.main.main;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.main.data.GsonArticle;
import com.joelimyx.flipvicefeed.main.data.Item;
import com.joelimyx.flipvicefeed.main.data.VolleySingleton;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MainAdapter.OnItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mMainRecyclerView;
    private MainAdapter mAdapter;
    private DrawerLayout mDrawerLayout;
    private boolean mTwoPane;
    private VolleySingleton mVolleySingleton;
    private NetworkStateReceiver mNetworkStateReceiver;
    private Snackbar mSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVolleySingleton = VolleySingleton.getInstance(this);

        mSnackbar = Snackbar.make(findViewById(R.id.drawer_layout), "No Network Available", Snackbar.LENGTH_INDEFINITE);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        //Registers Broadcast Receiver to Track Network Change
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mNetworkStateReceiver = new NetworkStateReceiver();
        this.registerReceiver(mNetworkStateReceiver, filter);

        //RecyclerView
        mMainRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        mMainRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mTwoPane = findViewById(R.id.fragment_container) != null;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open_nav_bar, R.string.close_nav_bar);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Network Connection
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            String url = "http://www.vice.com/api/getlatest/";

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            findViewById(R.id.progressbar).setVisibility(View.GONE);

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

        } else {

            Snackbar.make(findViewById(R.id.drawer_layout), "No Network Connection", Snackbar.LENGTH_INDEFINITE).show();




        }
    }

    @Override
    public void onItemSelected(int id) {
        //// TODO: 11/30/16 start detail activity if not in tablet else start detail fragment
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        mAdapter.swapdata((String) item.getTitle());
        mMainRecyclerView.scrollToPosition(0);
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

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
            case R.id.settings:
                return true;
            case R.id.search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregisters BroadcastReceiver when app is destroyed.
        if (mNetworkStateReceiver != null) {
            this.unregisterReceiver(mNetworkStateReceiver);
        }
    }


    public class NetworkStateReceiver extends BroadcastReceiver {

        private static final String TAG = "NetworkStateReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: ");
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


