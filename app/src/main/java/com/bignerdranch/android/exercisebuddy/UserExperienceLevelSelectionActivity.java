package com.bignerdranch.android.exercisebuddy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class UserExperienceLevelSelectionActivity extends AppCompatActivity {
    private UserExperienceLevelSelectionActivityViewModel mViewModel;
    private NumberPicker mUserExperienceLevelPicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserExperienceLevelSelectionActivityViewModel.class);
        setContentView(R.layout.activity_user_experience_level_selection);

        mUserExperienceLevelPicker = (NumberPicker) findViewById(R.id.user_experience_level_picker);
        setupUserGenderPicker();
    }

    private void setupUserGenderPicker(){
        String[] experienceLevels = getUserExperienceLevels(getApplicationContext());
        mUserExperienceLevelPicker.setMinValue(0);
        mUserExperienceLevelPicker.setMaxValue(experienceLevels.length-1);
        mUserExperienceLevelPicker.setDisplayedValues(experienceLevels);
        mUserExperienceLevelPicker.setValue(mViewModel.getUserExperienceLevelIndex());
        mUserExperienceLevelPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.setUserExperienceLevelIndex(newVal);
            }
        });
    }

    public void goToGenderPreferenceSelection(View v){
        Intent currentIntent = getIntent();
        Intent newIntent = new Intent(UserExperienceLevelSelectionActivity.this, GenderPreferenceSelectionActivity.class);
        Bundle extras = currentIntent.getExtras();
        extras.putInt("userExperienceLevelIndex", mViewModel.getUserExperienceLevelIndex());
        newIntent.putExtras(extras);
        startActivity(newIntent);
        return;
    }

    public static String[] getUserExperienceLevels(Context context){
        return new String[] {context.getString(R.string.beginner),
                context.getString(R.string.intermediate),
                context.getString(R.string.advanced)};
    }
}
