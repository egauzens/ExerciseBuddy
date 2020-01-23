package com.bignerdranch.android.exercisebuddy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class UserGenderSelectionActivity extends AppCompatActivity {
    private UserGenderSelectionActivityViewModel mViewModel;
    private NumberPicker mUserGenderPicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserGenderSelectionActivityViewModel.class);
        setContentView(R.layout.activity_user_gender_selection);

        mUserGenderPicker = (NumberPicker) findViewById(R.id.user_gender_picker);
        setupUserGenderPicker();
    }

    private void setupUserGenderPicker(){
        String[] genders = getUserGenders(getApplicationContext());
        mUserGenderPicker.setMinValue(0);
        mUserGenderPicker.setMaxValue(genders.length-1);
        mUserGenderPicker.setDisplayedValues(genders);
        mUserGenderPicker.setValue(mViewModel.getUserGenderIndex());
        mUserGenderPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.setUserGenderIndex(newVal);
            }
        });
    }

    public void goToDateOfBirthSelection(View v){
        Intent currentIntent = getIntent();
        Intent newIntent = new Intent(UserGenderSelectionActivity.this, DateOfBirthSelectionActivity.class);
        Bundle extras = currentIntent.getExtras();
        extras.putInt("userGenderIndex", mViewModel.getUserGenderIndex());
        newIntent.putExtras(extras);
        startActivity(newIntent);
        return;
    }

    public static String[] getUserGenders(Context context){
        return new String[] {context.getString(R.string.male),
            context.getString(R.string.female)};
    }
}
