package com.joelimyx.flipvicefeed;

import android.graphics.drawable.Drawable;

import com.joelimyx.flipvicefeed.detailview.DetailActivity;
import com.joelimyx.flipvicefeed.detailview.articleobjectdata.Image;
import com.joelimyx.flipvicefeed.detailview.articleobjectdata.PhotoCredit;
import com.joelimyx.flipvicefeed.detailview.articleobjectdata.Text;
import com.joelimyx.flipvicefeed.detailview.articleobjectdata.Video;

import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by colinbradley on 12/5/16.
 */

public class IndividualArticleUnitTests {

    @Test
    public void getImageURL() throws Exception{
        Image image = new Image("http://www.jqueryscript.net/images/Simplest-Responsive-jQuery-Image-Lightbox-Plugin-simple-lightbox.jpg");

        String actualURL = image.getURL();

        String expectedURL = "http://www.jqueryscript.net/images/Simplest-Responsive-jQuery-Image-Lightbox-Plugin-simple-lightbox.jpg";

        assertEquals(expectedURL,actualURL);
    }

    @Test
    public void getBodyText() throws Exception{
        Text text = new Text("This is the body of the article");

        String actualBody = text.getBodyText();

        String expectedBody = "This is the body of the article";

        assertEquals(expectedBody,actualBody);
    }

    @Test
    public void getVideoURL() throws Exception{
        Video video = new Video("https://www.youtube.com/watch?v=AS4QmL39Y-g");

        String actualURL = video.getVideoLink();

        String expectedURL = "https://www.youtube.com/watch?v=AS4QmL39Y-g";

        assertEquals(expectedURL,actualURL);
    }

    @Test
    public void getPhotoCredit() throws Exception{
        PhotoCredit credit = new PhotoCredit("photo by Colin");

        String actualCredit = credit.getPhotoCredit();

        String expectedCredit = "photo by Colin";

        assertEquals(expectedCredit,actualCredit);
    }

}
