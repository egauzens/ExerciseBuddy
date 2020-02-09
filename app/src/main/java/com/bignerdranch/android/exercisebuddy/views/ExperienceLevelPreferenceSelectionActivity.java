package com.bignerdranch.android.exercisebuddy.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bignerdranch.android.exercisebuddy.viewmodels.ExperienceLevelPreferenceSelectionActivityViewModel;
import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.UserSelections;

import java.util.Arrays;

public class ExperienceLevelPreferenceSelectionActivity extends AppCompatActivity {
    private ExperienceLevelPreferenceSelectionActivityViewModel mViewModel;
    private NumberPicker mExperienceLevelPreferencePicker;
    private String[] mExperienceLevelPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ExperienceLevelPreferenceSelectionActivityViewModel.class);
        mExperienceLevelPreferences = getExperienceLevelPreferences();
        setContentView(R.layout.activity_experience_level_preference_selection);
        if (mViewModel.getExperienceLevelPreference() == ""){
            initializeViewModel();
        }

        mExperienceLevelPreferencePicker = (NumberPicker) findViewById(R.id.experience_level_preference_picker);
        setupExperienceLevelPreferencePicker();
    }

    private void initializeViewModel(){
        mViewModel.setExperienceLevelPreference(mExperienceLevelPreferences[0]);
    }

    private void setupExperienceLevelPreferencePicker(){
        mExperienceLevelPreferencePicker.setWrapSelectorWheel(false);
        mExperienceLevelPreferencePicker.setMinValue(0);
        mExperienceLevelPreferencePicker.setMaxValue(mExperienceLevelPreferences.length-1);
        mExperienceLevelPreferencePicker.setDisplayedValues(mExperienceLevelPreferences);
        mExperienceLevelPreferencePicker.setValue(Arrays.asList(mExperienceLevelPreferences).indexOf(mViewModel.getExperienceLevelPreference()));
        mExperienceLevelPreferencePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.setExperienceLevelPreference(mExperienceLevelPreferences[newVal]);
            }
        });
    }

    public void goToProfileImageActivity(View v){
        Intent currentIntent = getIntent();
        Intent newIntent = new Intent(ExperienceLevelPreferenceSelectionActivity.this, ProfileImageActivity.class);
        Bundle extras = currentIntent.getExtras();
        extras.putString("experienceLevelPreference", mViewModel.getExperienceLevelPreference());
        newIntent.putExtras(extras);
        startActivity(newIntent);
        return;
    }

    public String[] getExperienceLevelPreferences(){
        return UserSelections.UserPreferences.getExperienceLevelPreferences(getApplicationContext());
    }
}
