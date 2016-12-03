package com.joelimyx.flipvicefeed.detailview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.joelimyx.flipvicefeed.detailview.adapters_holders.ArticleInfoAdapter;
import com.joelimyx.flipvicefeed.detailview.articleobjectdata.ArticleObject;
import com.joelimyx.flipvicefeed.detailview.articleobjectdata.Image;
import com.joelimyx.flipvicefeed.detailview.articleobjectdata.Text;
import com.joelimyx.flipvicefeed.detailview.individualarticledata.Article;
import com.joelimyx.flipvicefeed.detailview.individualarticledata.ArticleData;
import com.joelimyx.flipvicefeed.detailview.individualarticledata.Example;
import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.main.data.ShareGsonRootObject;
import com.joelimyx.flipvicefeed.main.data.ShareItem;
import com.joelimyx.flipvicefeed.classes.VolleySingleton;

import org.jsoup.Jsoup;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    RecyclerView mRV;
    ArticleInfoAdapter mAdapter;
    VolleySingleton mVolleySingleton;
    List<ArticleObject> mListOfObjectsInArticle;

    public static final String TAG = "DETAIL ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mVolleySingleton = VolleySingleton.getInstance(this);

        //RECIEVE ID FROM MainActivity
        Intent intent = getIntent();
        int id = intent.getIntExtra("id",-1);
        String idAsString = Integer.toString(id);

        //FACEBOOK SHARING
        getDataForShare(id);

        mListOfObjectsInArticle = new ArrayList<>();
        //STARTS VOLLEY API SEARCH FOR INDIVIDUAL ARTICLE
        getArticleByID(idAsString);

        //SETTING LAYOUT AND ADAPTER
        mRV = (RecyclerView)findViewById(R.id.detailview_recyclerview);
        mRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ArticleInfoAdapter(mListOfObjectsInArticle, this);
        mRV.setAdapter(mAdapter);
    }

    private String getArticleByID(String articleID){
        //CREATE URL FOR API CALL
        final String baseURL = "http://www.vice.com/api/article/";
        String fullURL = baseURL + articleID;

        //SET API CALL RESPONSE ACTIOP...
        //CHECKING EACH STEP IN THE RESPONSE TO ENSURE THE JSON->GSON WORKS CORRECTLY
        StringRequest stringRequest = new StringRequest(Request.Method.GET, fullURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: recieved response" + response);

                Example gsonArticle = new Gson().fromJson(response, Example.class);
                Log.d(TAG, "onResponse: GSON created" + gsonArticle.toString());

                ArticleData articleData = gsonArticle.getData();
                Log.d(TAG, "onResponse: ARTICLE DATA pulled from GSON" + articleData);

                Article article = articleData.getArticle();
                Log.d(TAG, "onResponse: ARTICLE from ARTICLE DATA" + article);

                String body = article.getBody();
                Log.d(TAG, "onResponse: BODY pulled from ARTICLE" + body);

                //SEND TO getDataFromHTML() TO PARSE HTML
                getDataFromHTML(body);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailActivity.this, "Error in loading page", Toast.LENGTH_SHORT).show();
            }
        });
        //START API CALL
        mVolleySingleton.addToRequestQueue(stringRequest);
        return null;
    }

    private String getDataFromHTML(String html){
        //CREATE JSOUP DOCUMENT FROM THE HTML
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        //GETS LIST OF  <p> ELEMENTS FROM DOCUMENT
        Elements textFromHTML = doc.select("p");

        List<ArticleObject> fullList = new ArrayList<>(); //LIST TO BE USED IN ADAPTER
        //***VERY ANNOYING PLEASE DON'T TOUCH THIS BLOCK :) *******
        //for() LOOP GOES THROUGH THE LIST OF ELEMENTS AND CHECKS EACH ELEMENT
        for (org.jsoup.nodes.Element e: textFromHTML){
            if (!e.text().equals("")) {  //FINDS ELEMENTS WITH READABLE TEXT ON SCREEN

                Text text = new Text(e.text());  //CREATES NEW Text OBJECT

                fullList.add(text);   //ADDS TO LIST

                //CHECKS FOR EMPTY .text(), AND A POPULATED .html() WHICH SHOWS OTHER CONTENT(IMGS,VIDEOS) ON SCREEN
            }else if (e.text().equals("") && !e.html().equals("")){

                //CHECKS TO SEE IF THERE ARE NO <iframe> OBJECTS IN THE HTML
                if (!e.html().contains("<iframe") && !e.html().contains("<br>")) {

                    String photoLink = e.html();
                    int indexStart = photoLink.indexOf("http");//GETS THE START AND END INDEX OF THE IMAGE LINK
                    int indexEnd = photoLink.indexOf("\" ");

                    //PARSES FULL .html() TO JUST THE LINK NEEDED
                    String photoHTML = photoLink.substring(indexStart, indexEnd);

                    Image image = new Image(photoHTML);//CREATES NEW Image OBJECT

                    fullList.add(image);//ADDS TO LIST

                    Log.d(TAG, "getDataFromHTML: IMAGE HTML: " + photoHTML);
                    Log.d(TAG, "getDataFromHTML: INDEX OF http: " + indexStart);
                    Log.d(TAG, "getDataFromHTML: INDEX OF \" : " + indexEnd);
                }       //SERIES OF CHECKS TO ENSURE ACCURACY OF PARSER
            }
            Log.d(TAG, "getDataFromHTML: TEXT: " + e.text());
            Log.d(TAG, "getDataFromHTML: HTML: " + e.html());
        }

        //TESTING THIS BLOCK OF TO GET OTHER HYPERLINKS IF WE DECIDE TO USE THEM
        Elements links = doc.select("a[href]");
        for (org.jsoup.nodes.Element l: links){
            Log.d(TAG, "getDataFromHTML: LINK: " + l.attr("abs:href"));
        }

        populateList(fullList); //CALLS populateList()
        return null;
    }

    public void populateList(List<ArticleObject> list){
        mListOfObjectsInArticle.addAll(list); //ADDS LIST TO THE MAIN LIST AND
        mAdapter.notifyDataSetChanged();      //AND UPDATES ADAPTER
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
                        Toast.makeText(DetailActivity.this, "Error when attempting to share.", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleySingleton.getInstance(DetailActivity.this).addToRequestQueue(request);
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
                ShareDialog.show(DetailActivity.this,fbShare);
            }
        });
    }
}
