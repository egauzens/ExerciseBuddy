package com.bignerdranch.android.exercisebuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.exercisebuddy.interfaces.IMatchItemClickListener;
import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.models.User;
import com.bignerdranch.android.exercisebuddy.helpers.StorageHelper;

import java.util.List;

public class MatchItemAdapter extends RecyclerView.Adapter<MatchItemAdapter.MatchItemHolder> {

    private List<User> mUserMatches;
    private IMatchItemClickListener mMatchItemClickListener;

    public MatchItemAdapter(List<User> userMatches, IMatchItemClickListener listener){
        mUserMatches = userMatches;
        mMatchItemClickListener = listener;
    }

    @NonNull
    @Override
    public MatchItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        return new MatchItemHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchItemHolder holder, int position) {
        User userMatch = mUserMatches.get(position);
        holder.bind(userMatch, mMatchItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mUserMatches.size();
    }

    class MatchItemHolder extends RecyclerView.ViewHolder{
        private TextView mUsernameTextView;
        private ImageView mUserImage;
        private Context mContext;

        public MatchItemHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.item_match, parent, false));

            mContext = parent.getContext();
            mUsernameTextView = (TextView) itemView.findViewById(R.id.username);
            mUserImage = (ImageView) itemView.findViewById(R.id.user_image);
        }

        public void bind(final User userMatch, final IMatchItemClickListener listener){
            mUsernameTextView.setText(userMatch.getName());
            mUserImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMatchItemClicked(userMatch.getUid());
                }
            });
            StorageHelper.loadProfileImageFromStorageIntoImageView(mContext, userMatch.getUid(), mUserImage);
        }
    }
}