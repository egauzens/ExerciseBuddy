package com.bignerdranch.android.exercisebuddy.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bignerdranch.android.exercisebuddy.viewmodels.DateOfBirthSelectionActivityViewModel;
import com.bignerdranch.android.exercisebuddy.R;

import java.util.Calendar;
import java.util.Date;

public class DateOfBirthSelectionActivity extends AppCompatActivity {
    private DateOfBirthSelectionActivityViewModel mViewModel;
    private DatePicker mDOBPicker;
    private static final long EIGHTEEN_YEARS_IN_MILLISECONDS = 568024668000L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DateOfBirthSelectionActivityViewModel.class);
        setContentView(R.layout.activity_date_of_birth_selection);

        mDOBPicker = (DatePicker) findViewById(R.id.date_of_birth_picker);
        setupDOBPicker();
        if (mViewModel.getDOB() != null){
            Calendar DOB = mViewModel.getDOB();
            int year = DOB.get(Calendar.YEAR);
            int month = DOB.get(Calendar.MONTH);
            int day = DOB.get(Calendar.DAY_OF_MONTH);
            mDOBPicker.updateDate(year, month, day);
        }
        else{
            setInitialDOB();
        }
    }

    private void setupDOBPicker(){
        Date currentDate = Calendar.getInstance().getTime();
        long currentTime = currentDate.getTime();
        long maxTime = currentTime - EIGHTEEN_YEARS_IN_MILLISECONDS;
        mDOBPicker.setMaxDate(maxTime);
        // For older versions there is no listener implementation so we just look at what is shown on the calendar when the user clicks the continue button
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mDOBPicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mViewModel.setDOB(year, monthOfYear, dayOfMonth);
                }
            });
        }
    }

    public void setInitialDOB(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mDOBPicker.getMaxDate());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mDOBPicker.updateDate(year, month, day);
        mViewModel.setDOB(year, month, day);
    }

    public void goToExperienceLevelSelection(View v){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mViewModel.setDOB(mDOBPicker.getYear(), mDOBPicker.getMonth(), mDOBPicker.getDayOfMonth());
        }
        Intent currentIntent = getIntent();
        Intent newIntent = new Intent(DateOfBirthSelectionActivity.this, UserExperienceLevelSelectionActivity.class);
        Bundle extras = currentIntent.getExtras();
        extras.putSerializable("dateOfBirth", mViewModel.getDOB());
        newIntent.putExtras(extras);
        startActivity(newIntent);
        return;
    }
}
