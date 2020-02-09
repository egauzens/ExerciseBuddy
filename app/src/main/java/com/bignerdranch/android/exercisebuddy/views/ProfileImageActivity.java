package com.bignerdranch.android.exercisebuddy.views;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bignerdranch.android.exercisebuddy.viewmodels.ProfileImageActivityViewModel;
import com.bignerdranch.android.exercisebuddy.R;
import com.bumptech.glide.Glide;

public class ProfileImageActivity extends AppCompatActivity {
    private ProfileImageActivityViewModel mViewModel;
    private ImageView mProfileImage;
    private EditText mUserDescription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileImageActivityViewModel.class);
        setContentView(R.layout.activity_profile_image);

        mProfileImage = (ImageView) findViewById(R.id.profile_image_view);
        mUserDescription = (EditText) findViewById(R.id.user_description);
        setupProfileImage();
        setupUserDescription();
    }

    public void setupProfileImage(){
        Uri profileImageUri = mViewModel.getProfileImageUri();
        if (profileImageUri == Uri.EMPTY){
            return;
        }
        loadProfileImage(profileImageUri);
    }

    public void setupUserDescription(){
        String userDescriptionText = mViewModel.getUserDescription();
        if (!userDescriptionText.equals("")){
            mUserDescription.setText(userDescriptionText);
        }
        mUserDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewModel.setUserDescription(mUserDescription.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void uploadProfileImage(View v){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    public void goToRegisterActivity(View v){
        Intent currentIntent = getIntent();
        Intent newIntent = new Intent(ProfileImageActivity.this, RegisterActivity.class);
        Bundle extras = currentIntent.getExtras();
        extras.putString("profileImageUrl", mViewModel.getProfileImageUri().toString());
        extras.putString("userDescription", mViewModel.getUserDescription());
        newIntent.putExtras(extras);
        startActivity(newIntent);
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            Uri imageUri = data.getData();
            mViewModel.setProfileImageUri(imageUri);
            loadProfileImage(imageUri);
        }
    }

    private void loadProfileImage(Uri imageUri){
        Glide.with(getApplication()).load(imageUri).into(mProfileImage);
    }
}
