package com.bignerdranch.android.exercisebuddy.viewmodels;

import androidx.lifecycle.ViewModel;

import com.bignerdranch.android.exercisebuddy.models.User;

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
