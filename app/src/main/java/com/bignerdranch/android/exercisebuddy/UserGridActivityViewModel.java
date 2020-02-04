package com.bignerdranch.android.exercisebuddy;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserGridActivityViewModel extends ViewModel {
    public final ObservableArrayList<User> mUserMatches;
    private User mCurrentUser;
    private Query mUsersDBQuery;

    public UserGridActivityViewModel(){
        mUserMatches = new ObservableArrayList<>();

        updateUserPreferences();
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentUser(User newUser) {
        updateDatabase(newUser);
        this.mCurrentUser = newUser;
    }

    public ObservableArrayList<User> getUserMatches() {
        return mUserMatches;
    }

    private ChildEventListener mUsersListener;

    private void updateDatabase(User newUser){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference currentUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        if (!newUser.getExercise().equals(mCurrentUser.getExercise()))
        {
            currentUserDatabase.child("exercise").setValue(newUser.getExercise());
        }
        if (!newUser.getGenderPreference().equals(mCurrentUser.getGenderPreference()))
        {
            currentUserDatabase.child("genderPreference").setValue(newUser.getGenderPreference());
        }
        if(!newUser.getExperienceLevelPreference().equals(mCurrentUser.getExperienceLevelPreference()))
        {
            currentUserDatabase.child("experienceLevelPreference").setValue(newUser.getExperienceLevelPreference());
        }
        if(newUser.getMaximumAgePreference() != mCurrentUser.getMaximumAgePreference())
        {
            currentUserDatabase.child("upperAgePreference").setValue(newUser.getMaximumAgePreference());
        }
        if(newUser.getMinimumAgePreference() != mCurrentUser.getMinimumAgePreference())
        {
            currentUserDatabase.child("lowerAgePreference").setValue(newUser.getMinimumAgePreference());
        }
    }

    private void updateUserPreferences(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    mCurrentUser = createUser(dataSnapshot);

                    updateDisplayedUsers();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateDisplayedUsers(){
        if (mUsersDBQuery != null && mUsersListener != null){
            mUsersDBQuery.removeEventListener(mUsersListener);
        }
        if (!mUserMatches.isEmpty()){
            mUserMatches.clear();
        }
        mUsersDBQuery = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("exercise").equalTo(mCurrentUser.getExercise());
        mUsersListener = mUsersDBQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (dataSnapshot.exists()) {
                    if (!dataSnapshot.getKey().equals(currentUser.getUid())) {
                        User addedUser = createUser(dataSnapshot);
                        if (mCurrentUser.doesMatchWith(addedUser) && addedUser.doesMatchWith(mCurrentUser)){
                            mUserMatches.add(addedUser);
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (dataSnapshot.exists()) {
                    if (!dataSnapshot.getKey().equals(currentUser.getUid())) {
                        User changedUser = createUser(dataSnapshot);
                        for (User user : mUserMatches) {
                            if (user.getUid().equals(changedUser.getUid())){
                                if (!mCurrentUser.doesMatchWith(changedUser) || !changedUser.doesMatchWith(mCurrentUser))
                                mUserMatches.remove(user);
                                return;
                            }
                        }
                    }
                    else{
                        updateDisplayedUsers();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (dataSnapshot.exists()) {
                    if (!dataSnapshot.getKey().equals(currentUser.getUid())) {
                        User removedUser = createUser(dataSnapshot);
                        for (User user : mUserMatches) {
                            if (user.getUid().equals(removedUser.getUid())){
                                mUserMatches.remove(user);
                                break;
                            }
                        }
                    }
                    else{
                        updateDisplayedUsers();
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private User createUser(DataSnapshot dataSnapshot){
        String userName = dataSnapshot.child("name").getValue().toString();
        String exercisePreference = dataSnapshot.child("exercise").getValue().toString();
        String userGender = dataSnapshot.child("userGender").getValue().toString();
        String userDob = dataSnapshot.child("dateOfBirth").getValue().toString();
        String userExperienceLevel = dataSnapshot.child("userExperienceLevel").getValue().toString();
        String userProfileImageUri = dataSnapshot.child("profileImageUri").getValue().toString();
        String userDescription = dataSnapshot.child("userDescription").getValue().toString();
        String genderPreference = dataSnapshot.child("genderPreference").getValue().toString();
        int lowerAgePreference = Integer.parseInt(dataSnapshot.child("lowerAgePreference").getValue().toString());
        int upperAgePreference = Integer.parseInt(dataSnapshot.child("upperAgePreference").getValue().toString());
        String experienceLevelPreference = dataSnapshot.child("experienceLevelPreference").getValue().toString();
        return new User(userName,
                userGender,
                userDob,
                userExperienceLevel,
                exercisePreference,
                userProfileImageUri,
                userDescription,
                genderPreference,
                lowerAgePreference,
                upperAgePreference,
                experienceLevelPreference,
                dataSnapshot.getKey());
    }
}
