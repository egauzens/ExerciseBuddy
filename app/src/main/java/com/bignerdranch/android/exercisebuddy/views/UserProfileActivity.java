package com.bignerdranch.android.exercisebuddy.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.viewmodels.UserProfileActivityViewModel;

public class UserProfileActivity extends ProfileActivity {

    public UserProfileActivityViewModel getViewModel(){
        return ViewModelProviders.of(this).get(UserProfileActivityViewModel.class);
    }

    public int getLayoutId(){
        return R.layout.activity_user_profile;
    }

    public void editProfile(View v){
        Intent intent = new Intent(UserProfileActivity.this, UpdateUserProfileActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("user", mViewModel.getUserProfile());
        intent.putExtras(extras);
        startActivityForResult(intent, 3);
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == Activity.RESULT_OK){
            setResult(resultCode, data);
            finish();
        }
    }
}