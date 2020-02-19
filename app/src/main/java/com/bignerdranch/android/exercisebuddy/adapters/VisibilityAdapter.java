package com.bignerdranch.android.exercisebuddy.adapters;

import android.view.View;

import androidx.databinding.BindingAdapter;

public class VisibilityAdapter {
    @BindingAdapter("goneUnless")
    public static void goneUnless(View view, boolean visible) {
        int visibility = View.VISIBLE;
        if (!visible){
            visibility = view.GONE;
        }
        view.setVisibility(visibility);
    }
}
