package com.bignerdranch.android.exercisebuddy.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.models.User;
import com.bignerdranch.android.exercisebuddy.viewmodels.UserProfileActivityViewModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public abstract class ProfileActivity extends AppCompatActivity {
    protected UserProfileActivityViewModel mViewModel;
    private TextView mNameTextView;
    private TextView mAgeTextView;
    private TextView mGenderTextView;
    private TextView mExperienceLevelTextView;
    private TextView mDescriptionTextView;
    private ImageView mProfileImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = getViewModel();
        setContentView(getLayoutId());

        mNameTextView = (TextView) findViewById(R.id.name_text_view);
        mAgeTextView = (TextView) findViewById(R.id.age_text_view);
        mGenderTextView = (TextView) findViewById(R.id.gender_text_view);
        mExperienceLevelTextView = (TextView) findViewById(R.id.experience_level_text_view);
        mDescriptionTextView = (TextView) findViewById(R.id.description_text_view);
        mProfileImageView = (ImageView) findViewById(R.id.profile_image_view);

        if (mViewModel.getUserProfile() == null){
            InitializeViewModel();
        }
        InitializeUI();
    }

    public abstract UserProfileActivityViewModel getViewModel();

    public abstract int getLayoutId();

    protected void InitializeViewModel(){
        Intent intent = getIntent();
        User user = (User)intent.getSerializableExtra("user");
        mViewModel.setUserProfile(user);
    }

    private void InitializeUI() {
        mNameTextView.setText(getString(R.string.name) + " " + mViewModel.getUserProfile().getName());
        mAgeTextView.setText(getString(R.string.age) + " " + mViewModel.getUserProfile().getAge());
        mGenderTextView.setText(getString(R.string.gender) + " " + mViewModel.getUserProfile().getGender());
        mExperienceLevelTextView.setText(getString(R.string.experience_level) + " " + mViewModel.getUserProfile().getExperienceLevel());
        mDescriptionTextView.setText(getString(R.string.bio) + " " + mViewModel.getUserProfile().getDescription());
        loadProfileImage();
    }

    private void loadProfileImage(){
        if (mViewModel.getUserProfile().getProfileImageUri().isEmpty()) {
            return;
        }
        final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profileImages").child(mViewModel.getUserProfile().getUid());
        Task task = filePath.getDownloadUrl();
        task.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Uri imageUri = (Uri) task.getResult();
                Glide.with(getApplication()).load(imageUri).into(mProfileImageView);
            }
        });
    }
}
