package com.bignerdranch.android.exercisebuddy.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.viewmodels.UserGenderSelectionActivityViewModel;
import com.bignerdranch.android.exercisebuddy.UserSelections;

import java.util.Arrays;

public class UserGenderSelectionActivity extends AppCompatActivity {
    private UserGenderSelectionActivityViewModel mViewModel;
    private NumberPicker mUserGenderPicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserGenderSelectionActivityViewModel.class);
        if (mViewModel.getUserGender().isEmpty()){
            initializeViewModel();
        }
        setContentView(R.layout.activity_user_gender_selection);

        mUserGenderPicker = (NumberPicker) findViewById(R.id.user_gender_picker);
        setupUserGenderPicker();
    }

    private void setupUserGenderPicker(){
        final String[] genders = getUserGenders();
        mUserGenderPicker.setMinValue(0);
        mUserGenderPicker.setMaxValue(genders.length-1);
        mUserGenderPicker.setDisplayedValues(genders);
        mUserGenderPicker.setValue(Arrays.asList(genders).indexOf(mViewModel.getUserGender()));
        mUserGenderPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.setUserGender(genders[newVal]);
            }
        });
    }

    private void initializeViewModel(){
        final String[] genders = getUserGenders();
        mViewModel.setUserGender(genders[0]);
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
