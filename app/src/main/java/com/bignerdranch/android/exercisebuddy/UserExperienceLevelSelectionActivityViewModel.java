package com.bignerdranch.android.exercisebuddy;

import androidx.lifecycle.ViewModel;

public class UserExperienceLevelSelectionActivityViewModel extends ViewModel {
    private int mUserExperienceLevelIndex;

    public UserExperienceLevelSelectionActivityViewModel(){
        mUserExperienceLevelIndex = 0;
    }

    public int getUserExperienceLevelIndex(){
        return mUserExperienceLevelIndex;
    }

    public void setUserExperienceLevelIndex(int index){
        mUserExperienceLevelIndex = index;
    }
}
