package com.bignerdranch.android.exercisebuddy.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bignerdranch.android.exercisebuddy.viewmodels.AgeRangePreferenceSelectionActivityViewModel;
import com.bignerdranch.android.exercisebuddy.R;

public class AgeRangePreferenceSelectionActivity extends AppCompatActivity {
    private static final int YOUNGEST_AGE = 18;
    private static final int OLDEST_AGE = 102;

    private AgeRangePreferenceSelectionActivityViewModel mViewModel;
    private NumberPicker mLowerAgePreferencePicker;
    private NumberPicker mUpperAgePreferencePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AgeRangePreferenceSelectionActivityViewModel.class);
        setContentView(R.layout.activity_age_range_preference_selection);

        mLowerAgePreferencePicker = (NumberPicker) findViewById(R.id.lower_age_preference_picker);
        mUpperAgePreferencePicker = (NumberPicker) findViewById(R.id.upper_age_preference_picker);
        setupAgeRangePreferencePickers();
    }

    private void setupAgeRangePreferencePickers(){
        mLowerAgePreferencePicker.setWrapSelectorWheel(false);
        mLowerAgePreferencePicker.setMinValue(YOUNGEST_AGE);
        mLowerAgePreferencePicker.setMaxValue(OLDEST_AGE);
        mLowerAgePreferencePicker.setValue(mViewModel.getLowerAgePreference());
        mLowerAgePreferencePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.setLowerAgePreference(newVal);
                if (newVal > mViewModel.getUpperAgePreference()){
                    mUpperAgePreferencePicker.setValue(newVal);
                    mViewModel.setUpperAgePreference(newVal);
                }
            }
        });
        mUpperAgePreferencePicker.setWrapSelectorWheel(false);
        mUpperAgePreferencePicker.setMinValue(YOUNGEST_AGE);
        mUpperAgePreferencePicker.setMaxValue(OLDEST_AGE);
        mUpperAgePreferencePicker.setValue(mViewModel.getUpperAgePreference());
        mUpperAgePreferencePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.setUpperAgePreference(newVal);
                if (newVal < mViewModel.getLowerAgePreference()){
                    mLowerAgePreferencePicker.setValue(newVal);
                    mViewModel.setLowerAgePreference(newVal);
                }
            }
        });
    }

    public void goToExperienceLevelPreferenceSelection(View v){
        Intent currentIntent = getIntent();
        Intent newIntent = new Intent(AgeRangePreferenceSelectionActivity.this, ExperienceLevelPreferenceSelectionActivity.class);
        Bundle extras = currentIntent.getExtras();
        extras.putInt("lowerAgePreference", mViewModel.getLowerAgePreference());
        extras.putInt("upperAgePreference", mViewModel.getUpperAgePreference());
        newIntent.putExtras(extras);
        startActivity(newIntent);
        return;
    }
}
