package com.bignerdranch.android.exercisebuddy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class GenderPreferenceSelectionActivity extends AppCompatActivity {
    private GenderPreferenceSelectionActivityViewModel mViewModel;
    private NumberPicker mGenderPreferencePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(GenderPreferenceSelectionActivityViewModel.class);
        setContentView(R.layout.activity_gender_preference_selection);

        mGenderPreferencePicker = (NumberPicker) findViewById(R.id.gender_preference_picker);
        setupGenderPreferencePicker();
    }

    private void setupGenderPreferencePicker(){
        String[] genderPreferences = getGenderPreferences(getApplicationContext());
        mGenderPreferencePicker.setMinValue(0);
        mGenderPreferencePicker.setMaxValue(genderPreferences.length-1);
        mGenderPreferencePicker.setDisplayedValues(genderPreferences);
        mGenderPreferencePicker.setValue(mViewModel.getGenderPreferenceIndex());
        mGenderPreferencePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.setGenderPreferenceIndex(newVal);
            }
        });
    }

    public void goToAgeRangePreferenceSelection(View v){
        Intent currentIntent = getIntent();
        Intent newIntent = new Intent(GenderPreferenceSelectionActivity.this, AgeRangePreferenceSelectionActivity.class);
        Bundle extras = currentIntent.getExtras();
        extras.putInt("genderPreferenceIndex", mViewModel.getGenderPreferenceIndex());
        newIntent.putExtras(extras);
        startActivity(newIntent);
        return;
    }

    public static String[] getGenderPreferences(Context context){
        return new String[] {context.getString(R.string.no_preference),
                context.getString(R.string.male),
                context.getString(R.string.female)};
    }
}
