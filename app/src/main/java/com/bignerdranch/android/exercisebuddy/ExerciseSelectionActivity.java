package com.bignerdranch.android.exercisebuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.util.Arrays;

public class ExerciseSelectionActivity extends AppCompatActivity {
    private ExerciseSelectionActivityViewModel mViewModel;
    private NumberPicker mExercisePicker;
    private String[] mExercises;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ExerciseSelectionActivityViewModel.class);
        mExercises = getExercises();
        if (mViewModel.getExercise().isEmpty()){
            initializeViewModel();
        }
        setContentView(R.layout.activity_exercise_selection);

        mExercisePicker = (NumberPicker) findViewById(R.id.exercise_picker);
        setupExercisePicker();
    }

    private void initializeViewModel(){
        mViewModel.setExercise(mExercises[0]);
    }

    private void setupExercisePicker(){
        String[] exercises = getExercises();
        mExercisePicker.setWrapSelectorWheel(false);
        mExercisePicker.setMinValue(0);
        mExercisePicker.setMaxValue(exercises.length-1);
        mExercisePicker.setDisplayedValues(exercises);
        mExercisePicker.setValue(Arrays.asList(mExercises).indexOf(mViewModel.getExercise()));
        mExercisePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.setExercise(mExercises[newVal]);
            }
        });
    }

    public void goToGenderSelection(View v){
        Intent intent = new Intent(ExerciseSelectionActivity.this, UserGenderSelectionActivity.class);
        Bundle extras = new Bundle();
        extras.putString("exercise", mViewModel.getExercise());
        intent.putExtras(extras);
        startActivity(intent);
        return;
    }

    public String[] getExercises(){
        return UserSelections.UserInformation.exercises(getApplicationContext());
    }
}
