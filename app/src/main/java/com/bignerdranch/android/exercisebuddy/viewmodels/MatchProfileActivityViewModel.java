package com.bignerdranch.android.exercisebuddy.viewmodels;

import com.bignerdranch.android.exercisebuddy.models.User;

public class MatchProfileActivityViewModel extends UserProfileActivityViewModel {
    private User mMatch;

    public MatchProfileActivityViewModel(){
        super();
        mMatch = null;
    }

    public User getMatch() {
        return mMatch;
    }

    public void setMatch(User user) {
        this.mMatch = user;
    }
}
