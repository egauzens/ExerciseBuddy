package com.bignerdranch.android.exercisebuddy.viewmodels;

import androidx.lifecycle.ViewModel;

public class ExperienceLevelPreferenceSelectionActivityViewModel extends ViewModel {
    private String mExperienceLevelPreference;

    public ExperienceLevelPreferenceSelectionActivityViewModel(){
        mExperienceLevelPreference = "";
    }

    public String getExperienceLevelPreference(){
        return mExperienceLevelPreference;
    }

    public void setExperienceLevelPreference(String level){
        mExperienceLevelPreference = level;
    }
}
