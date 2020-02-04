package com.bignerdranch.android.exercisebuddy;

import androidx.lifecycle.ViewModel;

public class UpdateUserPreferencesActivityViewModel extends ViewModel {
    public User mUser;

    public UpdateUserPreferencesActivityViewModel(){
        mUser = null;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }
}
