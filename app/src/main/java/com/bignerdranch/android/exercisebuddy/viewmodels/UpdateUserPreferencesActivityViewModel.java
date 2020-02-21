package com.bignerdranch.android.exercisebuddy.viewmodels;

import com.bignerdranch.android.exercisebuddy.staticHelpers.UserPreferencesSettings;

import androidx.lifecycle.ViewModel;

public class UpdateUserPreferencesActivityViewModel extends ViewModel {
    public UserPreferencesSettings mUserPreferencesSettings;

    public UpdateUserPreferencesActivityViewModel(){
        mUserPreferencesSettings = null;
    }

    public UserPreferencesSettings getUserPreferencesSettings() {
        return mUserPreferencesSettings;
    }

    public void setUserPreferencesSettings(UserPreferencesSettings userPreferencesSettings) {
        this.mUserPreferencesSettings = userPreferencesSettings;
    }
}
