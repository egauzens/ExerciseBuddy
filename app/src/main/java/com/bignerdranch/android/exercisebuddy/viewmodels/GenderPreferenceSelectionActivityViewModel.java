package com.bignerdranch.android.exercisebuddy.viewmodels;

import androidx.lifecycle.ViewModel;

public class GenderPreferenceSelectionActivityViewModel extends ViewModel {
    private String mGenderPreference;

    public GenderPreferenceSelectionActivityViewModel(){
        mGenderPreference = "";
    }

    public String getGenderPreference(){
        return mGenderPreference;
    }

    public void setGenderPreference(String gender){
        mGenderPreference = gender;
    }
}
