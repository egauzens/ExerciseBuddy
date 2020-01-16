package com.bignerdranch.android.exercisebuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserGridActivity extends AppCompatActivity {
    private RecyclerView mUserGrid;
    private GridItemAdapter mAdpater;
    private List<UserGridItem> mUserGridItems = new ArrayList<>();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_grid);

        mUserGrid = findViewById(R.id.recycler_view);
        mUserGrid.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        mAuth = FirebaseAuth.getInstance();
        checkUserGender();
    }

    private void updateUI(){
        mAdpater = new GridItemAdapter(mUserGridItems);
        mUserGrid.setAdapter(mAdpater);
    }

    public void goToUserProfile(View v){
        Intent intent = new Intent(UserGridActivity.this, UserProfileActivity.class);
        startActivity(intent);
        return;
    }

    public void logoutUser(View v){
        mAuth.signOut();
        Intent intent = new Intent(UserGridActivity.this, LoginOrRegisterActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    private class GridItemHolder extends RecyclerView.ViewHolder{
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
                Glide.with(getApplicationContext()).load(userGridItem.getUserImageUrl()).into(mUserImage);
            }
        }
    }

    private class GridItemAdapter extends RecyclerView.Adapter<GridItemHolder> {
        private List<UserGridItem> mUserGridItems;

        public GridItemAdapter(List<UserGridItem> userGridItems){
            mUserGridItems = userGridItems;
        }

        @NonNull
        @Override
        public GridItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(UserGridActivity.this);

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
    }

    private String userGender;
    private String oppositeUserGender;
    public void checkUserGender(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    DataSnapshot genderDataSnapshot = dataSnapshot.child("Gender");
                    if (genderDataSnapshot == null || genderDataSnapshot.getValue() == null) {
                        return;
                    }
                    userGender = genderDataSnapshot.getValue().toString();
                    switch (userGender){
                        case "Male":
                            oppositeUserGender = "Female";
                            break;
                        case "Female":
                            oppositeUserGender = "Male";
                            break;
                    }
                    updateDisplayedUsers();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateDisplayedUsers(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    if (!dataSnapshot.getKey().equals(user.getUid())) {
                        String imageUrl = "";
                        if (dataSnapshot.hasChild("profileImageUrl")) {
                            imageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                        }
                        String gender = dataSnapshot.child("Gender").getValue().toString();
                        String name = dataSnapshot.child("Name").getValue().toString();
                        UserGridItem item = new UserGridItem(gender, imageUrl, name, 0);
                        mUserGridItems.add(item);
                        updateUI();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
