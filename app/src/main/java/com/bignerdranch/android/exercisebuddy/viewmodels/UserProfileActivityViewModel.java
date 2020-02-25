package com.bignerdranch.android.exercisebuddy.viewmodels;

import android.app.Application;
import android.content.ContentResolver;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bignerdranch.android.exercisebuddy.models.User;
import com.bignerdranch.android.exercisebuddy.helpers.ModelCreationHelpers;
import com.bignerdranch.android.exercisebuddy.helpers.UserProfileSettings;
import com.bignerdranch.android.exercisebuddy.helpers.StorageHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserProfileActivityViewModel extends AndroidViewModel {
    private String mProfileUserId;
    private ContentResolver mApplicationContentResolver;
    protected User mProfileUser;
    protected MutableLiveData<Boolean> mAreAllUsersLoaded;
    protected boolean mIsProfileUserLoaded;

    public UserProfileActivityViewModel(Application application){
        super(application);
        mApplicationContentResolver = application.getApplicationContext().getContentResolver();
        mProfileUserId = "";
        mProfileUser = null;
        mAreAllUsersLoaded = new MutableLiveData<>(false);
        mIsProfileUserLoaded = false;
    }

    public String getProfileUserId() {
        return mProfileUserId;
    }

    public LiveData<Boolean> getAreAllUsersLoaded(){
        return mAreAllUsersLoaded;
    }

    protected void setAreAllUsersLoaded(){
        mAreAllUsersLoaded.setValue(mIsProfileUserLoaded);
    }

    public void setProfileUserId(String profileUserId) {
        mProfileUserId = profileUserId;
        setProfileUser();
    }

    public String getProfileUserName(){
        if (mAreAllUsersLoaded.getValue()){
            return mProfileUser.getName();
        }
        return null;
    }

    public int getProfileUserAge(){
        if (mAreAllUsersLoaded.getValue()){
            return mProfileUser.getAge();
        }
        return -1;
    }

    public String getProfileUserGender(){
        if (mAreAllUsersLoaded.getValue()){
            return mProfileUser.getGender();
        }
        return null;
    }

    public String getProfileUserExperienceLevel(){
        if (mAreAllUsersLoaded.getValue()){
            return mProfileUser.getExperienceLevel();
        }
        return null;
    }

    public String getProfileUserDescription(){
        if (mAreAllUsersLoaded.getValue()){
            return mProfileUser.getDescription();
        }
        return null;
    }

    public String getProfileUserDob(){
        if (mAreAllUsersLoaded.getValue()){
            return mProfileUser.getDob();
        }
        return null;
    }

    public String getProfileUserImageUri(){
        if (mAreAllUsersLoaded.getValue()){
            return mProfileUser.getProfileImageUri();
        }
        return null;
    }

    private void setProfileUser(){
        DatabaseReference profileUserDb = FirebaseDatabase.getInstance().getReference().child("users").child(getProfileUserId());
        profileUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mProfileUser = ModelCreationHelpers.createUser(dataSnapshot);
                mIsProfileUserLoaded = true;
                setAreAllUsersLoaded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateDatabase(UserProfileSettings newUserProfileSettings){
        if (!mAreAllUsersLoaded.getValue()){
            return;
        }
        if(!newUserProfileSettings.getProfileImageUri().equals(mProfileUser.getProfileImageUri()))
        {
            StorageHelper.loadProfileImageIntoStorage(newUserProfileSettings.getProfileImageUri(), newUserProfileSettings.getUid(), mApplicationContentResolver);
        }
        DatabaseReference currentUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(getProfileUserId());
        Map<String, Object> newChildrenSettings = new HashMap<>();
        newChildrenSettings.put("profileImageUri", newUserProfileSettings.getProfileImageUri());
        newChildrenSettings.put("dateOfBirth", newUserProfileSettings.getDob());
        newChildrenSettings.put("userGender", newUserProfileSettings.getGender());
        newChildrenSettings.put("userDescription", newUserProfileSettings.getDescription());
        newChildrenSettings.put("userExperienceLevel", newUserProfileSettings.getExperienceLevel());
        newChildrenSettings.put("name", newUserProfileSettings.getName());
        currentUserDatabase.updateChildren(newChildrenSettings);
    }
}
