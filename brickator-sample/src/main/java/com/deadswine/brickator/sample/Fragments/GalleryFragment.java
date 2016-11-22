package com.deadswine.brickator.sample.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deadswine.brickator.sample.R;

/**
 * Copyright 2016 Adam Fresko. All Rights Reserved.
 */
public class GalleryFragment extends Fragment {

    private static final String TAG = "GalleryFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: ");
        View v = inflater.inflate(R.layout.content_gallery, container, false);

        return v;
    }

}
