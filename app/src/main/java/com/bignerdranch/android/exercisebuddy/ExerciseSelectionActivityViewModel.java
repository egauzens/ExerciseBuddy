package com.bignerdranch.android.exercisebuddy;

import androidx.lifecycle.ViewModel;

public class ExerciseSelectionActivityViewModel extends ViewModel {
    private int mExerciseIndex;

    public ExerciseSelectionActivityViewModel(){
        mExerciseIndex = 0;
    }

    public int getExerciseIndex(){
        return mExerciseIndex;
    }

    public void setExerciseSelection(int index){
        mExerciseIndex = index;
    }
}