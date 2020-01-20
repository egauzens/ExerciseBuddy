package com.bignerdranch.android.exercisebuddy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GridItemAdapter extends RecyclerView.Adapter<GridItemAdapter.GridItemHolder> {

    private List<UserGridItem> mUserGridItems;
    private Context mContext;

    public GridItemAdapter(Context context, List<UserGridItem> userGridItems){
        mContext = context;
        mUserGridItems = userGridItems;
    }

    @NonNull
    @Override
    public GridItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        return new GridItemHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull GridItemHolder holder, int position) {
        UserGridItem userGridItem = mUserGridItems.get(position);
        holder.bind(userGridItem);
    }

    @Override
    public int getItemCount() {
        return mUserGridItems.size();
    }

    class GridItemHolder extends RecyclerView.ViewHolder{
        private TextView mUsernameTextView;
        private ImageView mUserImage;

        public GridItemHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.grid_item_user, parent, false));

            mUsernameTextView = (TextView) itemView.findViewById(R.id.username);
            mUserImage = (ImageView) itemView.findViewById(R.id.user_image);
        }

        public void bind(UserGridItem userGridItem){
            mUsernameTextView.setText(userGridItem.getName());
            if (!userGridItem.getUserImageUrl().isEmpty()) {
                Glide.with(mContext).load(userGridItem.getUserImageUrl()).into(mUserImage);
            }
        }
    }
}
