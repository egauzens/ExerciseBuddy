package com.bignerdranch.android.exercisebuddy;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
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
    private Context mContext;

    public GridItemAdapter(Context context, List<User> userMatches){
        mContext = context;
        mUserMatches = userMatches;
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
        holder.bind(userMatch);
    }

    @Override
    public int getItemCount() {
        return mUserMatches.size();
    }

    class GridItemHolder extends RecyclerView.ViewHolder{
        private TextView mUsernameTextView;
        private ImageView mUserImage;

        public GridItemHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.grid_item_user, parent, false));

            mUsernameTextView = (TextView) itemView.findViewById(R.id.username);
            mUserImage = (ImageView) itemView.findViewById(R.id.user_image);
        }

        public void bind(User userMatch){
            mUsernameTextView.setText(userMatch.getName());
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
