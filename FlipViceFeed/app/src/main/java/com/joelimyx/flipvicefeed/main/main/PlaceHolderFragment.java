package com.joelimyx.flipvicefeed.main.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joelimyx.flipvicefeed.R;

public class PlaceHolderFragment extends Fragment {


    public PlaceHolderFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PlaceHolderFragment newInstance() {
        PlaceHolderFragment fragment = new PlaceHolderFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_place_holder, container, false);
    }

}
