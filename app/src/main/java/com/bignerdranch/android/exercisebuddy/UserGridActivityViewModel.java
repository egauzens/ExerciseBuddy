package com.bignerdranch.android.exercisebuddy;

import android.renderscript.Sampler;

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
    private User mCurrentUserSettings;
    private Query mUsersDBQuery;

    public UserGridActivityViewModel(){
        mUserMatches = new ObservableArrayList<>();

        updateUserSettings();
    }

    public User getCurrentUser() {
        return mCurrentUserSettings;
    }

    public ObservableArrayList<User> getUserMatches() {
        return mUserMatches;
    }

    private ChildEventListener mUsersListener;

    public void updateDatabase(User newUserSettings){
        DatabaseReference currentUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(newUserSettings.getUid());
        if (!newUserSettings.getExercise().equals(mCurrentUserSettings.getExercise()))
        {
            currentUserDatabase.child("exercise").setValue(newUserSettings.getExercise());
        }
        if (!newUserSettings.getGenderPreference().equals(mCurrentUserSettings.getGenderPreference()))
        {
            currentUserDatabase.child("genderPreference").setValue(newUserSettings.getGenderPreference());
        }
        if(!newUserSettings.getExperienceLevelPreference().equals(mCurrentUserSettings.getExperienceLevelPreference()))
        {
            currentUserDatabase.child("experienceLevelPreference").setValue(newUserSettings.getExperienceLevelPreference());
        }
        if(newUserSettings.getMaximumAgePreference() != mCurrentUserSettings.getMaximumAgePreference())
        {
            currentUserDatabase.child("upperAgePreference").setValue(newUserSettings.getMaximumAgePreference());
        }
        if(newUserSettings.getMinimumAgePreference() != mCurrentUserSettings.getMinimumAgePreference())
        {
            currentUserDatabase.child("lowerAgePreference").setValue(newUserSettings.getMinimumAgePreference());
        }
    }

    private void updateUserSettings(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        userDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    User oldUserSettings = mCurrentUserSettings;
                    mCurrentUserSettings = createUser(dataSnapshot);
                    if (oldUserSettings == null || !oldUserSettings.getExercise().equals(mCurrentUserSettings.getExercise())) {
                        updateQuery();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateQuery(){
        resetMatches();
        mUsersDBQuery = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("exercise").equalTo(mCurrentUserSettings.getExercise());
        updateDisplayedUsers();
    }

    private void resetMatches(){
        if (mUsersDBQuery != null && mUsersListener != null){
            mUsersDBQuery.removeEventListener(mUsersListener);
        }
        if (!mUserMatches.isEmpty()){
            mUserMatches.clear();
        }
    }

    private void updateDisplayedUsers(){
        mUsersListener = mUsersDBQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (dataSnapshot.exists()) {
                    if (!dataSnapshot.getKey().equals(currentUser.getUid())) {
                        User addedUser = createUser(dataSnapshot);
                        if (mCurrentUserSettings.doesMatchWith(addedUser) && addedUser.doesMatchWith(mCurrentUserSettings)){
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
                                if (!mCurrentUserSettings.doesMatchWith(changedUser) || !changedUser.doesMatchWith(mCurrentUserSettings))
                                mUserMatches.remove(user);
                                return;
                            }
                        }
                    }
                    else {
                        resetMatches();
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
                        updateQuery();
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
