package com.joelimyx.flipvicefeed.detailview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class DetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARTICLE_ID = "param1";
    public static final String TAG = "Detail Fragment";

    // TODO: Rename and change types of parameters
    private int mArticleID;
    RecyclerView mRecyclerView;
    VolleySingleton mVolleySingleton;
    List<ArticleObject> mListOfObjectsInArticle;
    ArticleInfoAdapter mAdapter;
    TextView mTitle;
    CallbackManager mCallbackManager;
    ShareDialog mShareDialog;
    ShareButton mShareButton;
    List<ArticleObject> mMediaList;




    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(int param1) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARTICLE_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArticleID = getArguments().getInt(ARTICLE_ID);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitle = (TextView)view.findViewById(R.id.fragment_title);
        mListOfObjectsInArticle = new ArrayList<>();
        mMediaList = new ArrayList<>();
        mRecyclerView = (RecyclerView)view.findViewById(R.id.fragment_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mAdapter = new ArticleInfoAdapter(mListOfObjectsInArticle, getContext());
        mRecyclerView.setAdapter(mAdapter);

        mVolleySingleton = VolleySingleton.getInstance(getContext());

        mShareButton = (ShareButton)view.findViewById(R.id.fragment_fb_share_button);

        StringBuilder builder = new StringBuilder();
        String articleID = builder.append(mArticleID).toString();

        getArticleByID(articleID);
        getDataForShare(mArticleID);

        mCallbackManager = CallbackManager.Factory.create();
        mShareDialog = new ShareDialog(getActivity());
        mShareDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(getContext(), "Article Shared!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getContext(), "Canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getContext(), "Error Sharing", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private String getArticleByID(String articleID){
        final String baseURL = "http://www.vice.com/api/article/";
        String fullURL = baseURL + articleID;

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


                mTitle.setText(title);
                getDataFromHTML(body);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error in loading page", Toast.LENGTH_SHORT).show();
            }
        });
        mVolleySingleton.addToRequestQueue(stringRequest);
        return null;
    }

    private String getDataFromHTML(String html){
        org.jsoup.nodes.Document doc = Jsoup.parse(html);

        Elements textFromHTML = doc.select("p");

        List<ArticleObject> fullList = new ArrayList<>();
        for (org.jsoup.nodes.Element e: textFromHTML){
            if (!e.text().equals("")) {
                if (!e.html().contains("<img src=")) {
                    if (e.hasClass("photo-credit")){
                        String photoCredit = e.text();
                        PhotoCredit credit = new PhotoCredit(photoCredit);
                        fullList.add(credit);
                        Log.d(TAG, "getDataFromHTML: ADDED PHOTO CREDIT---- " + fullList.size());
                    }else if (e.html().startsWith("<strong>") && e.html().endsWith("</strong>")) {
                        TextStrong text = new TextStrong(e.text());
                        fullList.add(text);
                        Log.d(TAG, "getDataFromHTML: STRONG TEXT ADDED---- " +fullList.size());
                    }else{
                        Text text = new Text(e.text());
                        fullList.add(text);
                        Log.d(TAG, "getDataFromHTML: ADDED TEXT---- " +fullList.size());
                    }
                }else if (e.html().contains("<img src")) {
                    Log.d(TAG, "getDataFromHTML: GALLERY IMAGE FOUND --- " + e.html());

                }else {
                    String imgSrc = e.html();
                    int indexStart = imgSrc.indexOf("http");
                    int indexEnd = imgSrc.indexOf("\" ");
                    String imgLink = imgSrc.substring(indexStart,indexEnd);

                    Image image = new Image(imgLink);
                    fullList.add(image);
                    Log.d(TAG, "getDataFromHTML: ADDED IMAGE---- " +fullList.size());

                    String photoCredit = e.text();
                    PhotoCredit credit = new PhotoCredit(photoCredit);
                    fullList.add(credit);
                    Log.d(TAG, "getDataFromHTML: ADDED PHOTO CREDIT---- " + fullList.size());
                }
            }else if (e.text().equals("") && !e.html().equals("")) {

                if (!e.html().contains("<iframe") && !e.html().contains("<br>")) {

                    if (e.html().contains("<a href")){

                            String ahref = e.html();
                            int indexStart = ahref.indexOf("http");
                            int indexEnd = ahref.indexOf("\">");
                            String ahrefLink = ahref.substring(indexStart, indexEnd);
                            Log.d(TAG, "getDataFromHTML: A HREF HTML--- " + ahref);

                            Image image = new Image(ahrefLink);
                            fullList.add(image);
                            Log.d(TAG, "getDataFromHTML: ADDED IMAGE---- " + fullList.size());

                    }else if (!e.html().contains("<p href") && !e.html().contains("<o:p>") && !e.html().contains("<i>")){

                        String photoHTML = null;
                        String photoLink = e.html();
                        int indexStart = photoLink.indexOf("http");
                        int indexEnd1 = photoLink.indexOf("\" ");
                        photoHTML = photoLink.substring(indexStart, indexEnd1);

                        Image image = new Image(photoHTML);

                        fullList.add(image);

                        Log.d(TAG, "getDataFromHTML: IMAGE HTML: " + photoHTML);
                        Log.d(TAG, "getDataFromHTML: INDEX OF http: " + indexStart);
                        Log.d(TAG, "getDataFromHTML: INDEX OF \" : " );
                        Log.d(TAG, "getDataFromHTML: ADDED IMAGE---- " +fullList.size());

                    }
                }
            }
            Log.d(TAG, "getDataFromHTML: TEXT: " + e.text());
            Log.d(TAG, "getDataFromHTML: HTML: " + e.html());
        }

        Elements iframeList = doc.select("div");
        for (Element e: iframeList) {
            Log.d(TAG, "getDataFromHTML: ****iframeHTML***  " + e.html());
            String iframeHTML = e.html();

            if (!iframeHTML.contains("http")) {
                int indexStart = iframeHTML.indexOf("www");
                int indexEnd = iframeHTML.indexOf("\" ");
                String httpLessLink = iframeHTML.substring(indexStart, indexEnd);
                Log.d(TAG, "getDataFromHTML: gathered link that has no https-- " + httpLessLink);

                String link = "https://" + httpLessLink;
                Log.d(TAG, "getDataFromHTML: adds https:// ---- " + link);

                Video video = new Video(iframeHTML);

                fullList.add(video);
            }else {

                int indexStart = iframeHTML.indexOf("http");
                int indexEnd = iframeHTML.indexOf("\" ");
                String iframeLink = iframeHTML.substring(indexStart, indexEnd);

                Log.d(TAG, "getDataFromHTML: ***iframeStartIndex***  " + indexStart);
                Log.d(TAG, "getDataFromHTML: ***iframeEndIndex***  " + indexEnd);
                Log.d(TAG, "getDataFromHTML: ***iframeLINK***  " + iframeLink);

                Video video = new Video(iframeHTML);

                fullList.add(video);
                Log.d(TAG, "getDataFromHTML: ADDED VIDEO---- " + fullList.size());
            }
        }

        //TESTING THIS BLOCK OF TO GET OTHER HYPERLINKS IF WE DECIDE TO USE THEM
        Elements links = doc.select("a[href]");
        for (org.jsoup.nodes.Element l: links){
            Log.d(TAG, "getDataFromHTML: LINK: " + l.attr("abs:href"));
        }

        populateList(fullList);
        return null;
    }

    public void populateList(List<ArticleObject> list){
        mListOfObjectsInArticle.addAll(list);
        mListOfObjectsInArticle.addAll(mMediaList);
        mAdapter.notifyDataSetChanged();
    }



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
                        Toast.makeText(getContext(), "Error when attempting to share.", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    public void shareThisToFacebook(String title, Uri imageUrl, Uri linkUrl){


        final ShareLinkContent fbShare = new ShareLinkContent.Builder()
                .setContentTitle(title)
                .setImageUrl(imageUrl)
                .setContentUrl(linkUrl)
                .build();

        mShareButton.setShareContent(fbShare);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
    }













}
