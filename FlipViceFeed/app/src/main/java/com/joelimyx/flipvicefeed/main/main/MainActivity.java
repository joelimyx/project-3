package com.joelimyx.flipvicefeed.main.main;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import com.google.gson.Gson;
import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.main.data.GsonArticle;
import com.joelimyx.flipvicefeed.main.data.Item;
import com.joelimyx.flipvicefeed.main.data.ShareGsonRootObject;
import com.joelimyx.flipvicefeed.main.data.ShareItem;
import com.joelimyx.flipvicefeed.main.data.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MainAdapter.OnItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "ok";
    private RecyclerView mMainRecyclerView;
    private MainAdapter mAdapter;
    private DrawerLayout mDrawerLayout;
    private boolean mTwoPane;
    private VolleySingleton mVolleySingleton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVolleySingleton = VolleySingleton.getInstance(this);


        getDataForShare(254510);





        //RecyclerView
        mMainRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        mMainRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        mTwoPane = findViewById(R.id.fragment_container)!=null;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open_nav_bar,R.string.close_nav_bar);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Network Connection
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            String url = "http://www.vice.com/api/getlatest/";
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            findViewById(R.id.progressbar).setVisibility(View.GONE);

                            //Extracting data
                            GsonArticle gsonArticle = new Gson().fromJson(response,GsonArticle.class);
                            List<Item> items = gsonArticle.getData().getItems();

                            //Setup up adapter to recycler view
                            mAdapter = new MainAdapter(items,MainActivity.this,MainActivity.this);
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
        }else{
            Snackbar.make(findViewById(R.id.drawer_layout),"No Network Connection",Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    @Override
    public void onItemSelected(int id) {
        //// TODO: 11/30/16 start detail activity if not in tablet else start detail fragment
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            // TODO: 11/30/16 Populate switch case
            case R.id.stuff:
                findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
                mAdapter.swapdata("stuff");
                findViewById(R.id.progressbar).setVisibility(View.GONE);
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    //------------------------------------- ----  --     -              -
    //  SHARING TO FACEBOOK BUTTON
    //------------------------------------- ----  --     -              -

    public void getDataForShare(Integer id){
        String url = "http://www.vice.com/api/article/"+id;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ShareGsonRootObject shareGsonRoot = new Gson().fromJson(response,ShareGsonRootObject.class);
                        ShareItem item = shareGsonRoot.getData().getArticle();
                        shareThisToFacebook(item.getTitle(),
                                Uri.parse(item.getThumb()),
                                Uri.parse(item.getUrl()));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error when attempting to share.", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
    }

    public void shareThisToFacebook(String title, Uri imageUrl, Uri linkUrl){
        final ShareButton shareButton = (ShareButton)findViewById(R.id.fb_share_button);

        final ShareLinkContent fbShare = new ShareLinkContent.Builder()
                .setContentTitle(title)
                .setImageUrl(imageUrl)
                .setContentUrl(linkUrl)
                .build();

        shareButton.setShareContent(fbShare);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareDialog.show(MainActivity.this,fbShare);
            }
        });
    }
}
