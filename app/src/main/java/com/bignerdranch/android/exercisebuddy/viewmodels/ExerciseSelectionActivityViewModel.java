package com.bignerdranch.android.exercisebuddy.viewmodels;

import androidx.lifecycle.ViewModel;

public class ExerciseSelectionActivityViewModel extends ViewModel {
    private String mExercise;

    public ExerciseSelectionActivityViewModel(){
        mExercise = "";
    }

    public String getExercise(){
        return mExercise;
    }

    public void setExercise(String exercise){
        mExercise = exercise;
    }
}