package com.joelimyx.flipvicefeed.detailview.adapters_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.joelimyx.flipvicefeed.detailview.articleobjectdata.Text;
import com.joelimyx.flipvicefeed.R;

/**
 * Created by colinbradley on 11/30/16.
 */

public class TextHolder extends RecyclerView.ViewHolder {

    private TextView mText;


    public TextHolder(View itemView) {
        super(itemView);

        mText = (TextView)itemView.findViewById(R.id.article_text_body);
    }

    public void bindDataToViews(Text text){
        mText.setText(text.getBodyText());
    }
}
