package com.bignerdranch.android.exercisebuddy;

import androidx.lifecycle.ViewModel;

public class AgeRangePreferenceSelectionActivityViewModel extends ViewModel {
    private static final int YOUNGEST_AGE = 18;
    private static final int OLDEST_AGE = 102;

    private int mLowerAgePreference;
    private int mUpperAgePreference;

    public AgeRangePreferenceSelectionActivityViewModel() {
        mLowerAgePreference = YOUNGEST_AGE;
        mUpperAgePreference = OLDEST_AGE;
    }

    public int getLowerAgePreference(){
        return mLowerAgePreference;
    }

    public void setLowerAgePreference(int age){
        mLowerAgePreference = age;
    }

    public int getUpperAgePreference(){
        return mUpperAgePreference;
    }

    public void setUpperAgePreference(int age){
        mUpperAgePreference = age;
    }
}
