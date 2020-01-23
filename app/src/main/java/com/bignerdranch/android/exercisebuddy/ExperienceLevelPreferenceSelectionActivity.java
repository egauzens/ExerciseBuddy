package com.bignerdranch.android.exercisebuddy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class ExperienceLevelPreferenceSelectionActivity extends AppCompatActivity {
    private ExperienceLevelPreferenceSelectionActivityViewModel mViewModel;
    private NumberPicker mExperienceLevelPreferencePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ExperienceLevelPreferenceSelectionActivityViewModel.class);
        setContentView(R.layout.activity_experience_level_preference_selection);

        mExperienceLevelPreferencePicker = (NumberPicker) findViewById(R.id.experience_level_preference_picker);
        setupExperienceLevelPreferencePicker();
    }

    private void setupExperienceLevelPreferencePicker(){
        String[] experienceLevelPreferences = getExperienceLevelPreferences(getApplicationContext());
        mExperienceLevelPreferencePicker.setMinValue(0);
        mExperienceLevelPreferencePicker.setMaxValue(experienceLevelPreferences.length-1);
        mExperienceLevelPreferencePicker.setDisplayedValues(experienceLevelPreferences);
        mExperienceLevelPreferencePicker.setValue(mViewModel.getExperienceLevelPreferenceIndex());
        mExperienceLevelPreferencePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.setExperienceLevelPreferenceIndex(newVal);
            }
        });
    }

    public void goToProfileImageActivity(View v){
        Intent currentIntent = getIntent();
        Intent newIntent = new Intent(ExperienceLevelPreferenceSelectionActivity.this, ProfileImageActivity.class);
        Bundle extras = currentIntent.getExtras();
        extras.putInt("experienceLevelPreferenceIndex", mViewModel.getExperienceLevelPreferenceIndex());
        newIntent.putExtras(extras);
        startActivity(newIntent);
        return;
    }

    public static String[] getExperienceLevelPreferences(Context context){
        return new String[] {context.getString(R.string.no_preference),
                context.getString(R.string.beginner),
                context.getString(R.string.intermediate),
                context.getString(R.string.advanced)};
    }
}
