package com.bignerdranch.android.exercisebuddy;

import androidx.lifecycle.ViewModel;

import java.util.Calendar;

public class DateOfBirthSelectionActivityViewModel extends ViewModel {
    private Calendar mDOB;

    public DateOfBirthSelectionActivityViewModel(){
        mDOB = null;
    }

    public void setDOB(int year, int month, int day){
        mDOB = Calendar.getInstance();
        mDOB.set(year, month, day);
    }

    public Calendar getDOB(){
        return mDOB;
    }
}
