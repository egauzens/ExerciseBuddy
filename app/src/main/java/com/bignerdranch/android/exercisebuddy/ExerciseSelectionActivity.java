package com.bignerdranch.android.exercisebuddy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class ExerciseSelectionActivity extends AppCompatActivity {
    private ExerciseSelectionActivityViewModel mViewModel;
    private NumberPicker mUserGenderPicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ExerciseSelectionActivityViewModel.class);
        setContentView(R.layout.activity_exercise_selection);

        mUserGenderPicker = (NumberPicker) findViewById(R.id.exercise_picker);
        setupExercisePicker();
    }

    private void setupExercisePicker(){
        String[] exercises = getExercises(getApplicationContext());
        mUserGenderPicker.setMinValue(0);
        mUserGenderPicker.setMaxValue(exercises.length-1);
        mUserGenderPicker.setDisplayedValues(exercises);
        mUserGenderPicker.setValue(mViewModel.getExerciseIndex());
        mUserGenderPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.setExerciseSelection(newVal);
            }
        });
    }

    public void goToGenderSelection(View v){
        Intent intent = new Intent(ExerciseSelectionActivity.this, UserGenderSelectionActivity.class);
        intent.putExtra("exerciseIndex", mViewModel.getExerciseIndex());
        startActivity(intent);
        return;
    }

    public static String[] getExercises(Context context){
        return new String[] {context.getString(R.string.biking),
                context.getString(R.string.running),
                context.getString(R.string.rock_climbing),
                context.getString(R.string.swimming),
                context.getString(R.string.weight_lifting),
                context.getString(R.string.hiking)};
    }
}
