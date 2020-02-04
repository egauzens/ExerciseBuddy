package com.bignerdranch.android.exercisebuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.util.Arrays;

public class UserExperienceLevelSelectionActivity extends AppCompatActivity {
    private UserExperienceLevelSelectionActivityViewModel mViewModel;
    private NumberPicker mUserExperienceLevelPicker;
    private String[] mUserExperienceLevels;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserExperienceLevelSelectionActivityViewModel.class);
        mUserExperienceLevels = getUserExperienceLevels();
        setContentView(R.layout.activity_user_experience_level_selection);
        if (mViewModel.getUserExperienceLevel() == ""){
            initializeViewModel();
        }

        mUserExperienceLevelPicker = (NumberPicker) findViewById(R.id.user_experience_level_picker);
        setupUserGenderPicker();
    }

    private void initializeViewModel(){
        mViewModel.setUserExperienceLevel(mUserExperienceLevels[0]);
    }

    private void setupUserGenderPicker(){
        mUserExperienceLevelPicker.setMinValue(0);
        mUserExperienceLevelPicker.setMaxValue(mUserExperienceLevels.length-1);
        mUserExperienceLevelPicker.setDisplayedValues(mUserExperienceLevels);
        mUserExperienceLevelPicker.setValue(Arrays.asList(mUserExperienceLevels).indexOf(mViewModel.getUserExperienceLevel()));
        mUserExperienceLevelPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            mViewModel.setUserExperienceLevel(mUserExperienceLevels[newVal]);
            }
        });
    }

    public void goToGenderPreferenceSelection(View v){
        Intent currentIntent = getIntent();
        Intent newIntent = new Intent(UserExperienceLevelSelectionActivity.this, GenderPreferenceSelectionActivity.class);
        Bundle extras = currentIntent.getExtras();
        extras.putString("userExperienceLevel", mViewModel.getUserExperienceLevel());
        newIntent.putExtras(extras);
        startActivity(newIntent);
        return;
    }

    public String[] getUserExperienceLevels(){
        return UserSelections.UserInformation.getUserExperienceLevels(getApplicationContext());
    }
}
