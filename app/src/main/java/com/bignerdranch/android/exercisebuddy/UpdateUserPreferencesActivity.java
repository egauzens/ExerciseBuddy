package com.bignerdranch.android.exercisebuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.util.Arrays;

public class UpdateUserPreferencesActivity extends AppCompatActivity {
    private static final int YOUNGEST_AGE = 18;
    private static final int OLDEST_AGE = 102;

    private NumberPicker mExercisePicker;
    private NumberPicker mGenderPicker;
    private NumberPicker mExperienceLevelPicker;
    private NumberPicker mMinAgePicker;
    private NumberPicker mMaxAgePicker;
    private UpdateUserPreferencesActivityViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UpdateUserPreferencesActivityViewModel.class);
        setContentView(R.layout.activity_update_user_preferences);

        mExercisePicker = (NumberPicker) findViewById(R.id.update_exercise_picker);
        mGenderPicker = (NumberPicker) findViewById(R.id.update_gender_preference_picker);
        mExperienceLevelPicker = (NumberPicker) findViewById(R.id.update_experience_level_preference_picker);
        mMinAgePicker = (NumberPicker) findViewById(R.id.lower_age_preference_picker);
        mMaxAgePicker = (NumberPicker) findViewById(R.id.upper_age_preference_picker);

        if (mViewModel.getUser() == null){
            InitializeViewModel();
        }
        InitializeUI();
    }

    private void InitializeViewModel(){
        Intent intent = getIntent();
        User user = (User)intent.getSerializableExtra("user");
        mViewModel.setUser(user);
    }

    private void InitializeUI() {
        setupExercisePicker();
        setupGenderPicker();
        setupExperienceLevelPicker();
        setupAgeRangePickers();
    }

    private void setupExercisePicker(){
        final String[] exercises = getExercises();
        mExercisePicker.setWrapSelectorWheel(false);
        mExercisePicker.setMinValue(0);
        mExercisePicker.setMaxValue(exercises.length-1);
        mExercisePicker.setDisplayedValues(exercises);
        mExercisePicker.setValue(Arrays.asList(exercises).indexOf(mViewModel.getUser().getExercise()));
        mExercisePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.getUser().setExercise(exercises[newVal]);
            }
        });
    }

    private void setupGenderPicker(){
        final String[] genderPreferences = getGenderPreferences();
        mGenderPicker.setWrapSelectorWheel(false);
        mGenderPicker.setMinValue(0);
        mGenderPicker.setMaxValue(genderPreferences.length-1);
        mGenderPicker.setDisplayedValues(genderPreferences);
        mGenderPicker.setValue(Arrays.asList(genderPreferences).indexOf(mViewModel.getUser().getGenderPreference()));
        mGenderPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.getUser().setGenderPreference(genderPreferences[newVal]);
            }
        });
    }

    private void setupExperienceLevelPicker(){
        final String[] experienceLevelPreferences = getExperienceLevelPreferences();
        mExperienceLevelPicker.setWrapSelectorWheel(false);
        mExperienceLevelPicker.setMinValue(0);
        mExperienceLevelPicker.setMaxValue(experienceLevelPreferences.length-1);
        mExperienceLevelPicker.setDisplayedValues(experienceLevelPreferences);
        mExperienceLevelPicker.setValue(Arrays.asList(experienceLevelPreferences).indexOf(mViewModel.getUser().getExperienceLevelPreference()));
        mExperienceLevelPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.getUser().setExperienceLevelPreference(experienceLevelPreferences[newVal]);
            }
        });
    }

    private void setupAgeRangePickers(){
        mMinAgePicker.setWrapSelectorWheel(false);
        mMinAgePicker.setMinValue(YOUNGEST_AGE);
        mMinAgePicker.setMaxValue(OLDEST_AGE);
        mMinAgePicker.setValue(mViewModel.getUser().getMinimumAgePreference());
        mMinAgePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.getUser().setMinimumAgePreference(newVal);
                if (newVal > mViewModel.getUser().getMaximumAgePreference()){
                    mMaxAgePicker.setValue(newVal);
                    mViewModel.getUser().setMaximumAgePreference(newVal);
                }
            }
        });
        mMaxAgePicker.setWrapSelectorWheel(false);
        mMaxAgePicker.setMinValue(YOUNGEST_AGE);
        mMaxAgePicker.setMaxValue(OLDEST_AGE);
        mMaxAgePicker.setValue(mViewModel.getUser().getMaximumAgePreference());
        mMaxAgePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.getUser().setMaximumAgePreference(newVal);
                if (newVal < mViewModel.getUser().getMinimumAgePreference()){
                    mMinAgePicker.setValue(newVal);
                    mViewModel.getUser().setMinimumAgePreference(newVal);
                }
            }
        });
    }

    public void updateUserPreferences(View v){
        Intent data = new Intent();
        data.putExtra("newUserData", mViewModel.getUser());
        setResult(RESULT_OK, data);
        finish();
    }

    public String[] getExercises(){
        return UserSelections.UserInformation.exercises(getApplicationContext());
    }

    public String[] getExperienceLevelPreferences(){
        return UserSelections.UserPreferences.getExperienceLevelPreferences(getApplicationContext());
    }

    public String[] getGenderPreferences(){
        return UserSelections.UserPreferences.genderPreferences(getApplicationContext());
    }
}
