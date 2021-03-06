package com.joelimyx.flipvicefeed.detailview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.classes.VolleySingleton;
import com.joelimyx.flipvicefeed.detailview.adapters_holders.ArticleInfoAdapter;
import com.joelimyx.flipvicefeed.detailview.articleobjectdata.ArticleObject;
import com.joelimyx.flipvicefeed.detailview.articleobjectdata.Image;
import com.joelimyx.flipvicefeed.detailview.articleobjectdata.PhotoCredit;
import com.joelimyx.flipvicefeed.detailview.articleobjectdata.Text;
import com.joelimyx.flipvicefeed.detailview.articleobjectdata.TextStrong;
import com.joelimyx.flipvicefeed.detailview.articleobjectdata.Video;
import com.joelimyx.flipvicefeed.detailview.individualarticledata.Article;
import com.joelimyx.flipvicefeed.detailview.individualarticledata.ArticleData;
import com.joelimyx.flipvicefeed.detailview.individualarticledata.Example;
import com.joelimyx.flipvicefeed.detailview.individualarticledata.Gallery;
import com.joelimyx.flipvicefeed.detailview.individualarticledata.Media;
import com.joelimyx.flipvicefeed.main.data.ShareGsonRootObject;
import com.joelimyx.flipvicefeed.main.data.ShareItem;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    RecyclerView mRV;
    ArticleInfoAdapter mAdapter;
    VolleySingleton mVolleySingleton;
    List<ArticleObject> mListOfObjectsInArticle;
    CollapsingToolbarLayout mToolbarLayout;
    ImageView mToolbarBackground;
    String mToolbarBackgroundImage;
    Toolbar mToolbar;
    CallbackManager mCallbackManager;
    ShareDialog mShareDialog;
    List<ArticleObject> mMediaList;


    public static final String TAG = "DETAIL ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        /*---------------------------------------------------------------------------------
        // Facebook Callback manager to show Toast
        ---------------------------------------------------------------------------------*/
        mCallbackManager = CallbackManager.Factory.create();
        mShareDialog = new ShareDialog(this);
        mShareDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(DetailActivity.this, "Article Shared!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(DetailActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(DetailActivity.this, "Error Sharing", Toast.LENGTH_SHORT).show();
            }
        });




        mToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        mToolbarBackground = (ImageView)findViewById(R.id.toolbar_image);
        mVolleySingleton = VolleySingleton.getInstance(this);

        //RECIEVE ID FROM MainActivity
        Intent intent = getIntent();
        int id = intent.getIntExtra("id",-1);
        String idAsString = Integer.toString(id);

        //FACEBOOK SHARING
        getDataForShare(id);

        mMediaList = new ArrayList<>();
        mListOfObjectsInArticle = new ArrayList<>();
        //STARTS VOLLEY API SEARCH FOR INDIVIDUAL ARTICLE
        getArticleByID(idAsString);

        //SETTING LAYOUT AND ADAPTER
        mRV = (RecyclerView)findViewById(R.id.detailview_recyclerview);
        mRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ArticleInfoAdapter(mListOfObjectsInArticle, this);
        mRV.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return false;
    }

    public String getArticleByID(String articleID){
        //CREATE URL FOR API CALL
        final String baseURL = "http://www.vice.com/api/article/";
        String fullURL = baseURL + articleID;

        //SET API CALL RESPONSE ACTIOP...
        //CHECKING EACH STEP IN THE RESPONSE TO ENSURE THE JSON->GSON WORKS CORRECTLY
        StringRequest stringRequest = new StringRequest(Request.Method.GET, fullURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: recieved response: " + response);

                Example gsonArticle = new Gson().fromJson(response, Example.class);
                Log.d(TAG, "onResponse: GSON created: " + gsonArticle.toString());

                ArticleData articleData = gsonArticle.getData();
                Log.d(TAG, "onResponse: ARTICLE DATA pulled from GSON: " + articleData);

                Article article = articleData.getArticle();
                Log.d(TAG, "onResponse: ARTICLE from ARTICLE DATA: " + article);

                String body = article.getBody();
                Log.d(TAG, "onResponse: BODY pulled from ARTICLE: " + body);

                String title = article.getTitle();
                Log.d(TAG, "onResponse: TITLE pulled from ARTICLE: " + title);

                String imgURL = article.getThumb();
                Log.d(TAG, "onResponse: THUMBNAIL pulled from ARTICLE: " + imgURL);
                mToolbarBackgroundImage = imgURL;

                Media media = article.getMedia();
                if (media != null){
                    List<String> mediaList = new ArrayList<>();
                    List<Gallery> gallery = media.getGallery();
                    for (Gallery g:gallery){
                        String imageLink = g.getImage();
                        mediaList.add(imageLink);
                    }
                    for (String s:mediaList){
                        Image image = new Image(s);
                        mMediaList.add(image);
                    }
                }

                setTitle(title,mToolbarBackgroundImage); //SENDS TITLE AND IMAGE TO THE TOOLBAR

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

    public String getDataFromHTML(String html){
        //CREATE JSOUP DOCUMENT FROM THE HTML
        org.jsoup.nodes.Document doc = Jsoup.parse(html);

        //GETS LIST OF  <p> ELEMENTS FROM DOCUMENT
        Elements textFromHTML = doc.select("p");

        List<ArticleObject> fullList = new ArrayList<>(); //LIST TO BE USED IN ADAPTER
        //***VERY ANNOYING PLEASE DON'T TOUCH THIS BLOCK :) *******
        //for() LOOP GOES THROUGH THE LIST OF ELEMENTS AND CHECKS EACH ELEMENT
        for (org.jsoup.nodes.Element e: textFromHTML){
            if (!e.text().equals("")) {//FINDS ELEMENTS WITH READABLE TEXT ON SCREEN
                if (!e.html().contains("<img src=")) {
                    if (e.hasClass("photo-credit")){  //CHECKS TO SEE IF THE TEXT IS A PHOTO CREDIT CLASS AND SEPARATES
                        String photoCredit = e.text();
                        PhotoCredit credit = new PhotoCredit(photoCredit);
                        fullList.add(credit);
                    }else if (e.html().startsWith("<strong>") && e.html().endsWith("</strong>")) {
                        TextStrong text = new TextStrong(e.text());
                        fullList.add(text);
                        Log.d(TAG, "getDataFromHTML: STRONG TEXT ADDED---- " +fullList.size());
                    }else if (e.html().contains("<iframe")) {
                        Video video = new Video(e.html());
                        fullList.add(video);
                        Log.d(TAG, "getDataFromHTML: VIDEO ADDED----*" + fullList.size());
                    }else {
                        Text text = new Text(e.text());  //CREATES NEW Text OBJECT
                        fullList.add(text);   //ADDS TO LIST
                    }
                }else {  //IF HTML HAS BOTH IMAGE AND PHOTO CREDIT TEXT IT LOCATES AND SEPARATES
                    if (e.html().contains("<img src") && e.html().endsWith("alt=\"\">")){
                        String imgSrc = e.html();
                        int indexStart = imgSrc.indexOf("http");
                        int indexEnd = imgSrc.indexOf("\" alt=\"\">");

                        Text text = new Text(e.text());
                        fullList.add(text);
                        Log.d(TAG, "getDataFromHTML: ADDED TEXT----* " +fullList.size());

                        String imageHtml = imgSrc.substring(indexStart,indexEnd);
                        Image image = new Image(imageHtml);
                        fullList.add(image);
                        Log.d(TAG, "getDataFromHTML: ADDED IMAGE----* " + imageHtml);


                    }else {
                        String imgSrc = e.html();
                        int indexStart = imgSrc.indexOf("http");
                        int indexEnd = imgSrc.indexOf("\" ");
                        String imgLink = imgSrc.substring(indexStart, indexEnd);

                        Image image = new Image(imgLink);
                        fullList.add(image);

                        String photoCredit = e.text();
                        PhotoCredit credit = new PhotoCredit(photoCredit);
                        fullList.add(credit);
                    }
                }

                //CHECKS FOR EMPTY .text(), AND A POPULATED .html() WHICH SHOWS OTHER CONTENT(IMGS,VIDEOS) ON SCREEN
                //ALSO CHECKS IF THIS IMAGE WILL BE FIRST INTO fullList
                //IGNORES IF LIST IS EMPTY B/C THE TOP OF DETAIL PAGE GETS THIS IMAGE SO WE CAN AVOID DOUBLE IMAGES AT THE TOP OF THE PAGE
            }else if (e.text().equals("") && !e.html().equals("") && !e.html().equals(mToolbarBackgroundImage)) {

                //CHECKS TO SEE IF THERE ARE NO <iframe> OBJECTS IN THE HTML
                if (!e.html().contains("<iframe") && !e.html().contains("<br>")) {

                    if (e.html().contains("<a href")){
                        String ahref = e.html();
                        int indexStart = ahref.indexOf("http");
                        int indexEnd = ahref.indexOf("\"><");
                        String ahrefLink = ahref.substring(indexStart,indexEnd);
                        Log.d(TAG, "getDataFromHTML: A HREF HTML--- " + ahref);

                        Image image  = new Image(ahrefLink);
                        fullList.add(image);
                        Log.d(TAG, "getDataFromHTML: ADDED IMAGE---- " + fullList.size());

                    }else if (!e.html().contains("<p href") && !e.html().contains("<o:p>") && !e.html().contains("<i>")){

                        String photoHTML = null;
                        String photoLink = e.html();
                        int indexStart = photoLink.indexOf("http");//GETS THE START AND END INDEX OF THE IMAGE LINK
                        int indexEnd1 = photoLink.indexOf("\" ");
                        photoHTML = photoLink.substring(indexStart, indexEnd1);

                        //PARSES FULL .html() TO JUST THE LINK NEEDED

                        Image image = new Image(photoHTML);//CREATES NEW Image OBJECT

                        fullList.add(image);//ADDS TO LIST

                        Log.d(TAG, "getDataFromHTML: IMAGE HTML: " + photoHTML);
                        Log.d(TAG, "getDataFromHTML: INDEX OF http: " + indexStart);
                        Log.d(TAG, "getDataFromHTML: INDEX OF \" : " );
                        //SERIES OF CHECKS TO ENSURE ACCURACY OF PARSER
                    }
                }
            }
            Log.d(TAG, "getDataFromHTML: TEXT: " + e.text());
            Log.d(TAG, "getDataFromHTML: HTML: " + e.html());
        }

        Elements iframeList = doc.select("div");    //LOCATES AND EXTRACTS VIDEO LINKS AND OTHER IFRAME LINKS
        List<ArticleObject> iFrameList = new ArrayList<>();
        for (Element e: iframeList){
            Log.d(TAG, "getDataFromHTML: ****iframeHTML***  " + e.html());
            String iframeHTML = e.html();

            if (!iframeHTML.contains("http")) {
                int indexStart = iframeHTML.indexOf("www");
                int indexEnd = iframeHTML.indexOf("\" ");
                String httpLessLink = iframeHTML.substring(indexStart, indexEnd);
                Log.d(TAG, "getDataFromHTML: gathered link that has no https-- " +httpLessLink);

                String link = "https://" + httpLessLink;
                Log.d(TAG, "getDataFromHTML: adds https:// ---- " +link);

                Video video = new Video(iframeHTML);

                fullList.add(video);
            } else {
                int indexStart = iframeHTML.indexOf("http");
                int indexEnd = iframeHTML.indexOf("\" ");
                String iframeLink = iframeHTML.substring(indexStart, indexEnd);

                Log.d(TAG, "getDataFromHTML: ***iframeStartIndex***  " + indexStart);
                Log.d(TAG, "getDataFromHTML: ***iframeEndIndex***  " + indexEnd);
                Log.d(TAG, "getDataFromHTML: ***iframeLINK***  " + iframeLink);

                Video video = new Video(iframeHTML);

                fullList.add(video);
            }
        }



        //TESTING THIS BLOCK OF TO GET OTHER HYPERLINKS IF WE DECIDE TO USE THEM
        Elements links = doc.select("a[href]");
        for (org.jsoup.nodes.Element l: links){
            Log.d(TAG, "getDataFromHTML: LINK: " + l.attr("abs:href"));
        }


        if (fullList.get(0).getClass() == Image.class){
            fullList.remove(0);
        }

        populateList(fullList); //CALLS populateList()
        return null;
    }


    //SETS TITLE AND IMAGE TO THE TOOLBAR
    public void setTitle(String title, String imgURL){
        Picasso.with(this)
                .load(imgURL)
                .fit()
                .into(mToolbarBackground);
        mToolbarLayout.setTitle(title);
    }

    public void populateList(List<ArticleObject> list){
        mListOfObjectsInArticle.addAll(list); //ADDS LIST TO THE MAIN LIST AND
        mListOfObjectsInArticle.addAll(mMediaList);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
