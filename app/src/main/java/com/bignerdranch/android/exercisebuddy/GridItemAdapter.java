package com.bignerdranch.android.exercisebuddy;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.exercisebuddy.models.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class GridItemAdapter extends RecyclerView.Adapter<GridItemAdapter.GridItemHolder> {

    private List<User> mUserMatches;
    private GridItemClickListener mGridItemClickListener;

    public GridItemAdapter(List<User> userMatches, GridItemClickListener listener){
        mUserMatches = userMatches;
        mGridItemClickListener = listener;
    }

    @NonNull
    @Override
    public GridItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        return new GridItemHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull GridItemHolder holder, int position) {
        User userMatch = mUserMatches.get(position);
        holder.bind(userMatch, mGridItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mUserMatches.size();
    }

    class GridItemHolder extends RecyclerView.ViewHolder{
        private TextView mUsernameTextView;
        private ImageView mUserImage;
        private Context mContext;

        public GridItemHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.grid_item_user, parent, false));

            mContext = parent.getContext();
            mUsernameTextView = (TextView) itemView.findViewById(R.id.username);
            mUserImage = (ImageView) itemView.findViewById(R.id.user_image);
        }

        public void bind(final User userMatch, final GridItemClickListener listener){
            mUsernameTextView.setText(userMatch.getName());
            mUserImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onGridItemClicked(userMatch);
                }
            });
            if (!userMatch.getProfileImageUri().isEmpty()) {
                loadProfileImage(userMatch.getUid());
            }
        }

        private void loadProfileImage(String userId){
            final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Task task = filePath.getDownloadUrl();
            task.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    Uri imageUri = (Uri) task.getResult();
                    Glide.with(mContext).load(imageUri).into(mUserImage);
                }
            });
        }
    }
}
