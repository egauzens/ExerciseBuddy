package com.bignerdranch.android.exercisebuddy;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

public class ProfileImageActivityViewModel extends ViewModel {
    private Uri mProfileImageUri;
    private String mUserDescription;

    public ProfileImageActivityViewModel(){
        mProfileImageUri = Uri.EMPTY;
        mUserDescription = "";
    }

    public Uri getProfileImageUri(){
        return mProfileImageUri;
    }

    public void setProfileImageUri(Uri uri){
        mProfileImageUri = uri;
    }

    public String getUserDescription() {
        return mUserDescription;
    }

    public void setUserDescription(String userDescription) {
        mUserDescription = userDescription;
    }
}
