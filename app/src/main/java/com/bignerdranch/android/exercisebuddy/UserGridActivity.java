package com.bignerdranch.android.exercisebuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserGridActivity extends AppCompatActivity {
    private RecyclerView mUserGrid;
    private GridItemAdapter mAdpater;
    private List<String> mUsernames = new ArrayList<>();
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
        mAdpater = new GridItemAdapter(mUsernames);
        mUserGrid.setAdapter(mAdpater);
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

        public GridItemHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.grid_item_user, parent, false));

            mUsernameTextView = (TextView) itemView.findViewById(R.id.username);
        }

        public void bind(String username){
            mUsernameTextView.setText(username);
        }
    }

    private class GridItemAdapter extends RecyclerView.Adapter<GridItemHolder> {
        private List<String> mUsernames;

        public GridItemAdapter(List<String> usernames){
            mUsernames = usernames;
        }

        @NonNull
        @Override
        public GridItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(UserGridActivity.this);

            return new GridItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull GridItemHolder holder, int position) {
            String username = mUsernames.get(position);
            holder.bind(username);
        }

        @Override
        public int getItemCount() {
            return mUsernames.size();
        }
    }

    private String userGender;
    private String oppositeUserGender;
    public void checkUserGender(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference maleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Male");

        maleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(user.getUid())){
                    userGender = "Male";
                    oppositeUserGender = "Female";
                    updateOppositeGenderUsers();
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

        DatabaseReference femaleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Female");

        femaleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(user.getUid())){
                    userGender = "Female";
                    oppositeUserGender = "Male";
                    updateOppositeGenderUsers();
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

    public void updateOppositeGenderUsers(){
        DatabaseReference oppositeGenderDb = FirebaseDatabase.getInstance().getReference().child("Users").child(oppositeUserGender);
        oppositeGenderDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    mUsernames.add(dataSnapshot.child("Name").getValue().toString());
                    updateUI();
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
