package com.bignerdranch.android.exercisebuddy;

import androidx.lifecycle.ViewModel;

public class GenderPreferenceSelectionActivityViewModel extends ViewModel {
    private int mGenderPreferenceIndex;

    public GenderPreferenceSelectionActivityViewModel(){
        mGenderPreferenceIndex = 0;
    }

    public int getGenderPreferenceIndex(){
        return mGenderPreferenceIndex;
    }

    public void setGenderPreferenceIndex(int index){
        mGenderPreferenceIndex = index;
    }
}
