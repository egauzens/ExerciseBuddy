package com.bignerdranch.android.exercisebuddy.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.helpers.StorageHelper;
import com.bignerdranch.android.exercisebuddy.viewmodels.UserProfileActivityViewModel;

public abstract class ProfileActivity extends AppCompatActivity {
    protected UserProfileActivityViewModel mViewModel;
    private TextView mAgeTextView;
    private TextView mGenderTextView;
    private TextView mExperienceLevelTextView;
    private TextView mDescriptionTextView;
    private ImageView mProfileImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = getViewModel();
        setContentView();
        setupToolbar();

        mAgeTextView = (TextView) findViewById(R.id.age_text_view);
        mGenderTextView = (TextView) findViewById(R.id.gender_text_view);
        mExperienceLevelTextView = (TextView) findViewById(R.id.experience_level_text_view);
        mDescriptionTextView = (TextView) findViewById(R.id.description_text_view);
        mProfileImageView = (ImageView) findViewById(R.id.profile_image_view);

        if (!mViewModel.getAreAllUsersLoaded().getValue()){
            InitializeViewModel();
            mViewModel.getAreAllUsersLoaded().observe(this, areAllUsersLoadedObserver);
        }
        else {
            InitializeUI();
        }
    }

    final Observer<Boolean> areAllUsersLoadedObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable final Boolean isLoaded) {
            if (isLoaded){
                InitializeUI();
                mViewModel.getAreAllUsersLoaded().removeObserver(areAllUsersLoadedObserver);
            }
        }
    };

    protected abstract UserProfileActivityViewModel getViewModel();

    protected abstract void setupToolbar();

    protected abstract void setContentView();

    protected void InitializeViewModel(){
        Intent intent = getIntent();
        String profileUserId = (String)intent.getSerializableExtra("profileUserId");
        mViewModel.setProfileUserId(profileUserId);
    }

    protected void InitializeUI() {
        if (!mViewModel.getAreAllUsersLoaded().getValue()){
            return;
        }
        mAgeTextView.setText(getString(R.string.age) + " " + mViewModel.getProfileUserAge());
        mGenderTextView.setText(getString(R.string.gender) + " " + mViewModel.getProfileUserGender());
        mExperienceLevelTextView.setText(getString(R.string.experience_level) + " " + mViewModel.getProfileUserExperienceLevel());
        mDescriptionTextView.setText(getString(R.string.bio) + " " + mViewModel.getProfileUserDescription());
        StorageHelper.loadProfileImageFromStorageIntoImageView(this, mViewModel.getProfileUserId(), mProfileImageView);
    }
}
