package com.joelimyx.flipvicefeed.detailview.adapters_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.joelimyx.flipvicefeed.R;
import com.joelimyx.flipvicefeed.detailview.articleobjectdata.TextStrong;

/**
 * Created by colinbradley on 12/6/16.
 */

public class StrongTextHolder extends RecyclerView.ViewHolder {

    private TextView mStrongText;

    public StrongTextHolder(View itemView) {
        super(itemView);

        mStrongText = (TextView) itemView.findViewById(R.id.strong_text);
    }

    public void bindDataToViews(TextStrong strongText){
        mStrongText.setText(strongText.getmStrongText());
    }
}
