package com.bignerdranch.android.exercisebuddy;

import androidx.lifecycle.ViewModel;

public class UserProfileActivityViewModel extends ViewModel {
    private User mUser;

    public UserProfileActivityViewModel(){
        mUser = null;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }
}
