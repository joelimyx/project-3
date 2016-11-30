package com.joelimyx.flipvicefeed.DetailView;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.joelimyx.flipvicefeed.DetailView.Adapters_Holders.ArticleInfoAdapter;
import com.joelimyx.flipvicefeed.DetailView.ArticleObjectData.ArticleObject;
import com.joelimyx.flipvicefeed.DetailView.ArticleObjectData.Image;
import com.joelimyx.flipvicefeed.DetailView.ArticleObjectData.Text;
import com.joelimyx.flipvicefeed.R;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    RecyclerView mRV;
    ArticleInfoAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<ArticleObject> list = new ArrayList<>();
        list.add(new Text("skjalrueffb gwetfewtf 9ewgf gofygefy gaifygafgyuieg feiuyagf ydugf gyf yewgf wagf af "));
        list.add(new Image("https://vice-images.vice.com/images/articles/meta/2016/11/30/you-can-finally-download-netflix-shows-to-watch-offline-vgtrn-1480523317.jpg?resize=*:*&output-quality=75"));


        mRV = (RecyclerView)findViewById(R.id.detailview_recyclerview);
        mRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ArticleInfoAdapter(list, this);
        mRV.setAdapter(mAdapter);



    }

    private void getArticleByID(String articleID){

        RequestQueue queue = Volley.newRequestQueue(this);
        String baseURL = "http://www.vice.com/api/article/";
        String fullURL = baseURL + articleID;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, fullURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailActivity.this, "Error in loading page", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }

    private void jsonExtraction(){

    }
}
