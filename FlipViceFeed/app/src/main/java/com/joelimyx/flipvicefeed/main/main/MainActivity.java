package com.joelimyx.flipvicefeed.main.main;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainAdapter.OnItemSelectedListener{
    private RecyclerView mMainRecyclerView;
    private MainAdapter mAdapter;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RecyclerView
        mMainRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        mMainRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        mTwoPane = findViewById(R.id.fragment_container)!=null;

        //Network Connection
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://www.vice.com/api/getlatest/";
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            //Extracting data
                            GsonArticle gsonArticle = new Gson().fromJson(response,GsonArticle.class);
                            List<Item> items = gsonArticle.getData().getItems();

                            //Setup up adapter to recycler view
                            mMainRecyclerView.setAdapter(new MainAdapter(items,MainActivity.this,MainActivity.this));
                        }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Error getting articles", Toast.LENGTH_SHORT).show();
                        }
            });
            queue.add(request);
        }else{
            Snackbar.make(findViewById(R.id.activity_main),"No Network Connection",Snackbar.LENGTH_INDEFINITE);
        }

    }

    @Override
    public void onItemSelected(int id) {
        //// TODO: 11/30/16 start activity if not in tablet else start fragment
    }
}
