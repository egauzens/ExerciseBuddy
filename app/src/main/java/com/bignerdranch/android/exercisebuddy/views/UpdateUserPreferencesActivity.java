package com.bignerdranch.android.exercisebuddy.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.helpers.UserPreferencesSettings;
import com.bignerdranch.android.exercisebuddy.viewmodels.UpdateUserPreferencesActivityViewModel;
import com.bignerdranch.android.exercisebuddy.helpers.UserSelectionsHelpers;

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

        if (mViewModel.getUserPreferencesSettings() == null){
            InitializeViewModel();
        }
        InitializeUI();
    }

    private void InitializeViewModel(){
        Intent intent = getIntent();
        UserPreferencesSettings userPreferencesSettings = (UserPreferencesSettings) intent.getSerializableExtra("userPreferencesSettings");
        mViewModel.setUserPreferencesSettings(userPreferencesSettings);
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
        mExercisePicker.setValue(Arrays.asList(exercises).indexOf(mViewModel.getUserPreferencesSettings().getExercisePreference()));
        mExercisePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.getUserPreferencesSettings().setExercise(exercises[newVal]);
            }
        });
    }

    private void setupGenderPicker(){
        final String[] genderPreferences = getGenderPreferences();
        mGenderPicker.setWrapSelectorWheel(false);
        mGenderPicker.setMinValue(0);
        mGenderPicker.setMaxValue(genderPreferences.length-1);
        mGenderPicker.setDisplayedValues(genderPreferences);
        mGenderPicker.setValue(Arrays.asList(genderPreferences).indexOf(mViewModel.getUserPreferencesSettings().getGenderPreference()));
        mGenderPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.getUserPreferencesSettings().setGenderPreference(genderPreferences[newVal]);
            }
        });
    }

    private void setupExperienceLevelPicker(){
        final String[] experienceLevelPreferences = getExperienceLevelPreferences();
        mExperienceLevelPicker.setWrapSelectorWheel(false);
        mExperienceLevelPicker.setMinValue(0);
        mExperienceLevelPicker.setMaxValue(experienceLevelPreferences.length-1);
        mExperienceLevelPicker.setDisplayedValues(experienceLevelPreferences);
        mExperienceLevelPicker.setValue(Arrays.asList(experienceLevelPreferences).indexOf(mViewModel.getUserPreferencesSettings().getExperienceLevelPreference()));
        mExperienceLevelPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.getUserPreferencesSettings().setExperienceLevelPreference(experienceLevelPreferences[newVal]);
            }
        });
    }

    private void setupAgeRangePickers(){
        mMinAgePicker.setWrapSelectorWheel(false);
        mMinAgePicker.setMinValue(YOUNGEST_AGE);
        mMinAgePicker.setMaxValue(OLDEST_AGE);
        mMinAgePicker.setValue(mViewModel.getUserPreferencesSettings().getMinimumAgePreference());
        mMinAgePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.getUserPreferencesSettings().setMinimumAgePreference(newVal);
                if (newVal > mViewModel.getUserPreferencesSettings().getMaximumAgePreference()){
                    mMaxAgePicker.setValue(newVal);
                    mViewModel.getUserPreferencesSettings().setMaximumAgePreference(newVal);
                }
            }
        });
        mMaxAgePicker.setWrapSelectorWheel(false);
        mMaxAgePicker.setMinValue(YOUNGEST_AGE);
        mMaxAgePicker.setMaxValue(OLDEST_AGE);
        mMaxAgePicker.setValue(mViewModel.getUserPreferencesSettings().getMaximumAgePreference());
        mMaxAgePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.getUserPreferencesSettings().setMaximumAgePreference(newVal);
                if (newVal < mViewModel.getUserPreferencesSettings().getMinimumAgePreference()){
                    mMinAgePicker.setValue(newVal);
                    mViewModel.getUserPreferencesSettings().setMinimumAgePreference(newVal);
                }
            }
        });
    }

    public void updateUserPreferences(View v){
        Intent data = new Intent();
        data.putExtra("newUserPreferencesSettings", mViewModel.getUserPreferencesSettings());
        setResult(RESULT_OK, data);
        finish();
    }

    public String[] getExercises(){
        return UserSelectionsHelpers.UserInformation.exercises(getApplicationContext());
    }

    public String[] getExperienceLevelPreferences(){
        return UserSelectionsHelpers.UserPreferences.getExperienceLevelPreferences(getApplicationContext());
    }

    public String[] getGenderPreferences(){
        return UserSelectionsHelpers.UserPreferences.genderPreferences(getApplicationContext());
    }
}
