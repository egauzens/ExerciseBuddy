package com.bignerdranch.android.exercisebuddy.viewmodels;

import androidx.lifecycle.ViewModel;

import com.bignerdranch.android.exercisebuddy.models.User;

public class UserProfileActivityViewModel extends ViewModel {
    private User mUserProfile;

    public UserProfileActivityViewModel(){
        mUserProfile = null;
    }

    public User getUserProfile() {
        return mUserProfile;
    }

    public void setUserProfile(User user) {
        this.mUserProfile = user;
    }
}
