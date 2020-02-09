package com.bignerdranch.android.exercisebuddy.viewmodels;

import androidx.lifecycle.ViewModel;

public class UserGenderSelectionActivityViewModel extends ViewModel {
    private String mUserGender;

    public UserGenderSelectionActivityViewModel(){ mUserGender = ""; }

    public String getUserGender(){
        return mUserGender;
    }

    public void setUserGender(String gender){
        mUserGender = gender;
    }
}
