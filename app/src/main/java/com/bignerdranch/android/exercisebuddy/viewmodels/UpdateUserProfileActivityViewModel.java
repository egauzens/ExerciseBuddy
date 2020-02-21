package com.bignerdranch.android.exercisebuddy.viewmodels;

import androidx.lifecycle.ViewModel;

import com.bignerdranch.android.exercisebuddy.staticHelpers.UserProfileSettings;

public class UpdateUserProfileActivityViewModel extends ViewModel {
    private UserProfileSettings mUserProfileSettings;

    public UpdateUserProfileActivityViewModel(){
        mUserProfileSettings = null;
    }

    public UserProfileSettings getProfileUserSettings() {
        return mUserProfileSettings;
    }

    public void setProfileUserSettings(UserProfileSettings settings) {
        this.mUserProfileSettings = settings;
    }
}
