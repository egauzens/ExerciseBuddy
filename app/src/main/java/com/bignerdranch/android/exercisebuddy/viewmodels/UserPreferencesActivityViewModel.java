package com.bignerdranch.android.exercisebuddy.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bignerdranch.android.exercisebuddy.models.User;
import com.bignerdranch.android.exercisebuddy.helpers.ModelCreationHelpers;
import com.bignerdranch.android.exercisebuddy.helpers.UserPreferencesSettings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserPreferencesActivityViewModel extends ViewModel {
    private String mUserId;
    protected User mUser;
    protected MutableLiveData<Boolean> mAreAllUsersLoaded;

    public UserPreferencesActivityViewModel(){
        mUserId = "";
        mUser = null;
        mAreAllUsersLoaded = new MutableLiveData<>(false);
    }

    public String getUserId() {
        return mUserId;
    }

    public LiveData<Boolean> getAreAllUsersLoaded(){
        return mAreAllUsersLoaded;
    }

    public void setProfileUserId(String profileUserId) {
        mUserId = profileUserId;
        setUser();
    }

    public String getExercisePreference(){
        if (mAreAllUsersLoaded.getValue()){
            return mUser.getExercise();
        }
        return null;
    }

    public String getGenderPreference(){
        if (mAreAllUsersLoaded.getValue()){
            return mUser.getGenderPreference();
        }
        return null;
    }

    public String getExperienceLevelPreference(){
        if (mAreAllUsersLoaded.getValue()){
            return mUser.getExperienceLevelPreference();
        }
        return null;
    }

    public int getLowerAgePreference(){
        if (mAreAllUsersLoaded.getValue()){
            return mUser.getMinimumAgePreference();
        }
        return -1;
    }

    public int getUpperAgePreference(){
        if (mAreAllUsersLoaded.getValue()){
            return mUser.getMaximumAgePreference();
        }
        return -1;
    }

    private void setUser(){
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("users").child(getUserId());
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = ModelCreationHelpers.createUser(dataSnapshot);
                mAreAllUsersLoaded.setValue(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateDatabase(UserPreferencesSettings newUserPreferencesSettings){
        if (!mAreAllUsersLoaded.getValue()){
            return;
        }
        DatabaseReference currentUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(getUserId());
        Map<String, Object> newChildrenSettings = new HashMap<>();
        newChildrenSettings.put("exercise", newUserPreferencesSettings.getExercisePreference());
        newChildrenSettings.put("experienceLevelPreference", newUserPreferencesSettings.getExperienceLevelPreference());
        newChildrenSettings.put("genderPreference", newUserPreferencesSettings.getGenderPreference());
        newChildrenSettings.put("lowerAgePreference", newUserPreferencesSettings.getMinimumAgePreference());
        newChildrenSettings.put("upperAgePreference", newUserPreferencesSettings.getMaximumAgePreference());
        currentUserDatabase.updateChildren(newChildrenSettings);
    }
}
