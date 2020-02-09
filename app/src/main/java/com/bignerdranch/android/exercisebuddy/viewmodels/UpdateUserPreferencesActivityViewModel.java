package com.bignerdranch.android.exercisebuddy.viewmodels;

import com.bignerdranch.android.exercisebuddy.models.User;
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
