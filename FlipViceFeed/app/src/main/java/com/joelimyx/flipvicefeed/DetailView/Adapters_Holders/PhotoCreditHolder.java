package com.joelimyx.flipvicefeed.detailview.adapters_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.joelimyx.flipvicefeed.detailview.articleobjectdata.PhotoCredit;
import com.joelimyx.flipvicefeed.R;

/**
 * Created by colinbradley on 12/5/16.
 */

public class PhotoCreditHolder extends RecyclerView.ViewHolder {

    private TextView mPhotoCredit;

    public PhotoCreditHolder(View itemView) {
        super(itemView);

        mPhotoCredit = (TextView)itemView.findViewById(R.id.photocredit);
    }

    public void bindDataToView(PhotoCredit photoCredit){
        mPhotoCredit.setText(photoCredit.getPhotoCredit());
    }
}
