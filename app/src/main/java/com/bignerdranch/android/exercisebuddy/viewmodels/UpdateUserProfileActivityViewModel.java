package com.bignerdranch.android.exercisebuddy.viewmodels;

import androidx.lifecycle.ViewModel;

import com.bignerdranch.android.exercisebuddy.models.User;

public class UpdateUserProfileActivityViewModel extends ViewModel {
    public User mUser;

    public UpdateUserProfileActivityViewModel(){
        mUser = null;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }
}
