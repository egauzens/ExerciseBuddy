package com.bignerdranch.android.exercisebuddy;

import androidx.lifecycle.ViewModel;

public class UserExperienceLevelSelectionActivityViewModel extends ViewModel {
    private String mUserExperienceLevel;

    public UserExperienceLevelSelectionActivityViewModel(){
        mUserExperienceLevel = "";
    }

    public String getUserExperienceLevel(){
        return mUserExperienceLevel;
    }

    public void setUserExperienceLevel(String level){
        mUserExperienceLevel = level;
    }
}
