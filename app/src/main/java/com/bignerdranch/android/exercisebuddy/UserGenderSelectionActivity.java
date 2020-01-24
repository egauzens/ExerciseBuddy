package com.bignerdranch.android.exercisebuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.util.Arrays;

public class UserGenderSelectionActivity extends AppCompatActivity {
    private UserGenderSelectionActivityViewModel mViewModel;
    private NumberPicker mUserGenderPicker;
    private String[] mGenders;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserGenderSelectionActivityViewModel.class);
        mGenders = getUserGenders();
        if (mViewModel.getUserGender().isEmpty()){
            initializeViewModel();
        }
        setContentView(R.layout.activity_user_gender_selection);

        mUserGenderPicker = (NumberPicker) findViewById(R.id.user_gender_picker);
        setupUserGenderPicker();
    }

    private void setupUserGenderPicker(){
        mUserGenderPicker.setMinValue(0);
        mUserGenderPicker.setMaxValue(mGenders.length-1);
        mUserGenderPicker.setDisplayedValues(mGenders);
        mUserGenderPicker.setValue(Arrays.asList(mGenders).indexOf(mViewModel.getUserGender()));
        mUserGenderPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.setUserGender(mGenders[newVal]);
            }
        });
    }

    private void initializeViewModel(){
        mViewModel.setUserGender(mGenders[0]);
    }

    public void goToDateOfBirthSelection(View v){
        Intent currentIntent = getIntent();
        Intent newIntent = new Intent(UserGenderSelectionActivity.this, DateOfBirthSelectionActivity.class);
        Bundle extras = currentIntent.getExtras();
        extras.putString("userGender", mViewModel.getUserGender());
        newIntent.putExtras(extras);
        startActivity(newIntent);
        return;
    }

    private String[] getUserGenders(){
        return UserSelections.UserInformation.userGenders(getApplicationContext());
    }
}
