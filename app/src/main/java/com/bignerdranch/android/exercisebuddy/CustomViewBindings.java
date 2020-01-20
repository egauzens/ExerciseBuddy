package com.bignerdranch.android.exercisebuddy;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CustomViewBindings {

    @BindingAdapter("data")
    public static void bindRecyclerViewAdapter(RecyclerView view, List<UserGridItem> items) {
        view.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
        GridItemAdapter adapter = new GridItemAdapter(view.getContext(), items);
        view.setAdapter(adapter);
    }
}