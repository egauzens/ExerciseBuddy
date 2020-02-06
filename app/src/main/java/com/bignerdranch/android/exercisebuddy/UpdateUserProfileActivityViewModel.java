package com.bignerdranch.android.exercisebuddy;

import androidx.lifecycle.ViewModel;

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
