package com.bignerdranch.android.exercisebuddy.views;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.viewmodels.UpdateUserProfileActivityViewModel;
import com.bignerdranch.android.exercisebuddy.models.User;
import com.bignerdranch.android.exercisebuddy.UserSelections;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class UpdateUserProfileActivity extends AppCompatActivity {
    private static final long EIGHTEEN_YEARS_IN_MILLISECONDS = 568024668000L;

    private ImageView mProfileImage;
    private EditText mName;
    private DatePicker mDOBPicker;
    private NumberPicker mGenderPicker;
    private NumberPicker mExperienceLevelPicker;
    private EditText mDescription;
    private UpdateUserProfileActivityViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UpdateUserProfileActivityViewModel.class);
        setContentView(R.layout.activity_update_user_profile);

        mProfileImage = (ImageView) findViewById(R.id.update_profile_image);
        mName = (EditText) findViewById(R.id.update_name);
        mDOBPicker = (DatePicker) findViewById(R.id.update_date_of_birth_picker);
        mGenderPicker = (NumberPicker) findViewById(R.id.update_gender_picker);
        mExperienceLevelPicker = (NumberPicker) findViewById(R.id.update_experience_level_picker);
        mDescription = (EditText) findViewById(R.id.update_user_description);

        if (mViewModel.getUser() == null){
            InitializeViewModel();
        }
        InitializeUI();
    }

    private void InitializeViewModel(){
        Intent intent = getIntent();
        User user = (User)intent.getSerializableExtra("user");
        mViewModel.setUser(user);
    }

    private void InitializeUI() {
        loadProfileImage();
        mName.setText(mViewModel.getUser().getName());
        setupGenderPicker();
        setupExperienceLevelPicker();
        setupDOBPicker();
        mDescription.setText(mViewModel.getUser().getDescription());
    }

    private void setupExperienceLevelPicker(){
        final String[] experienceLevels = getUserExperienceLevels();
        mExperienceLevelPicker.setMinValue(0);
        mExperienceLevelPicker.setMaxValue(experienceLevels.length-1);
        mExperienceLevelPicker.setDisplayedValues(experienceLevels);
        mExperienceLevelPicker.setValue(Arrays.asList(experienceLevels).indexOf(mViewModel.getUser().getExperienceLevel()));
        mExperienceLevelPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.getUser().setExperienceLevel(experienceLevels[newVal]);
            }
        });
    }

    private void setupGenderPicker(){
        final String[] genders = getUserGenders();
        mGenderPicker.setMinValue(0);
        mGenderPicker.setMaxValue(genders.length-1);
        mGenderPicker.setDisplayedValues(genders);
        mGenderPicker.setValue(Arrays.asList(genders).indexOf(mViewModel.getUser().getGender()));
        mGenderPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mViewModel.getUser().setGender(genders[newVal]);
            }
        });
    }
    private void loadProfileImage(){
        if (mViewModel.getUser().getProfileImageUri().isEmpty()){
            return;
        }
        final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profileImages").child(mViewModel.getUser().getUid());
        Task task = filePath.getDownloadUrl();
        task.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Uri imageUri = (Uri) task.getResult();
                Glide.with(getApplication()).load(imageUri).into(mProfileImage);
            }
        });
    }

    private void setupDOBPicker(){
        Date currentDate = Calendar.getInstance().getTime();
        long currentTime = currentDate.getTime();
        long maxTime = currentTime - EIGHTEEN_YEARS_IN_MILLISECONDS;
        mDOBPicker.setMaxDate(maxTime);

        int year = mViewModel.getUser().getBirthYear();
        int month = mViewModel.getUser().getBirthMonth();
        int day = mViewModel.getUser().getBirthDay();
        mDOBPicker.updateDate(year, month, day);

        // For older versions there is no listener implementation so we just look at what is shown on the calendar when the user clicks the update button
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mDOBPicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mViewModel.getUser().setDob(year, monthOfYear, dayOfMonth);
                }
            });
        }
    }

    public void updateUserProfile(View v){
        String name = mName.getText().toString();
        String description = mDescription.getText().toString();
        mViewModel.getUser().setName(name);
        mViewModel.getUser().setDescription(description);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mViewModel.getUser().setDob(mDOBPicker.getYear(), mDOBPicker.getMonth(), mDOBPicker.getDayOfMonth());
        }

        Intent data = new Intent();
        data.putExtra("newUserData", mViewModel.getUser());
        setResult(RESULT_OK, data);
        finish();
    }

    public void changeProfilePicture(View v){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            mViewModel.getUser().setProfileImageUri(imageUri.toString());
            Glide.with(getApplication()).load(imageUri).into(mProfileImage);
        }
    }

    private String[] getUserGenders(){
        return UserSelections.UserInformation.userGenders(getApplicationContext());
    }

    public String[] getUserExperienceLevels(){
        return UserSelections.UserInformation.getUserExperienceLevels(getApplicationContext());
    }
}
