package com.bignerdranch.android.exercisebuddy;

import androidx.lifecycle.ViewModel;

public class ExperienceLevelPreferenceSelectionActivityViewModel extends ViewModel {
    private int mExperienceLevelPreferenceIndex;

    public ExperienceLevelPreferenceSelectionActivityViewModel(){
        mExperienceLevelPreferenceIndex = 0;
    }

    public int getExperienceLevelPreferenceIndex(){
        return mExperienceLevelPreferenceIndex;
    }

    public void setExperienceLevelPreferenceIndex(int index){
        mExperienceLevelPreferenceIndex = index;
    }
}
