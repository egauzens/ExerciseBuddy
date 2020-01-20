package com.bignerdranch.android.exercisebuddy;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserGridActivityViewModel extends ViewModel {
    public final ObservableArrayList<UserGridItem> mUserGridItems;
    private String userGender;
    private String oppositeUserGender;

    public UserGridActivityViewModel(){
        Log.d("UserGridActivityVM", "view model created");
        mUserGridItems = new ObservableArrayList<>();

        checkUserGender();
    }

    public ObservableArrayList<UserGridItem> getUserGridItems() {
        return mUserGridItems;
    }

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
                    Log.d("checkUserGender", "Gender updated");
                    updateDisplayedUsers();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void updateDisplayedUsers(){
        FirebaseDatabase.getInstance().getReference().child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (dataSnapshot.exists()) {
                    if (!dataSnapshot.getKey().equals(user.getUid())) {
                        String imageUrl = "";
                        if (dataSnapshot.hasChild("profileImageUrl")) {
                            imageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                        }
                        String gender = dataSnapshot.child("Gender").getValue().toString();
                        String name = dataSnapshot.child("Name").getValue().toString();
                        UserGridItem item = new UserGridItem(gender, imageUrl, name, 0);
                        Log.d("updateDisplayedUsers", "item added");
                        mUserGridItems.add(item);
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
