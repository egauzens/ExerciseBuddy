package com.bignerdranch.android.exercisebuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.util.Arrays;

public class GenderPreferenceSelectionActivity extends AppCompatActivity {
    private GenderPreferenceSelectionActivityViewModel mViewModel;
    private NumberPicker mGenderPreferencePicker;
    private String[] mGenderPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(GenderPreferenceSelectionActivityViewModel.class);
        mGenderPreferences = getGenderPreferences();
        setContentView(R.layout.activity_gender_preference_selection);
        if (mViewModel.getGenderPreference() == ""){
            initializeViewModel();
        }

        mGenderPreferencePicker = (NumberPicker) findViewById(R.id.gender_preference_picker);
        setupGenderPreferencePicker();
    }

    private void initializeViewModel(){
        mViewModel.setGenderPreference(mGenderPreferences[0]);
    }

    private void setupGenderPreferencePicker(){
        mGenderPreferencePicker.setMinValue(0);
        mGenderPreferencePicker.setMaxValue(mGenderPreferences.length-1);
        mGenderPreferencePicker.setDisplayedValues(mGenderPreferences);
        mGenderPreferencePicker.setValue(Arrays.asList(mGenderPreferences).indexOf(mViewModel.getGenderPreference()));
        mGenderPreferencePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.setGenderPreference(mGenderPreferences[newVal]);
            }
        });
    }

    public void goToAgeRangePreferenceSelection(View v){
        Intent currentIntent = getIntent();
        Intent newIntent = new Intent(GenderPreferenceSelectionActivity.this, AgeRangePreferenceSelectionActivity.class);
        Bundle extras = currentIntent.getExtras();
        extras.putString("genderPreference", mViewModel.getGenderPreference());
        newIntent.putExtras(extras);
        startActivity(newIntent);
        return;
    }

    public String[] getGenderPreferences(){
        return UserSelections.UserPreferences.genderPreferences(getApplicationContext());
    }
}
