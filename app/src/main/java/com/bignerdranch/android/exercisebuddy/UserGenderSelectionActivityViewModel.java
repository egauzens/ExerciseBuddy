package com.bignerdranch.android.exercisebuddy;

import androidx.lifecycle.ViewModel;

public class UserGenderSelectionActivityViewModel extends ViewModel {
    private int mUserGenderIndex;

    public UserGenderSelectionActivityViewModel(){
        mUserGenderIndex = 0;
    }

    public int getUserGenderIndex(){
        return mUserGenderIndex;
    }

    public void setUserGenderIndex(int index){
        mUserGenderIndex = index;
    }
}
