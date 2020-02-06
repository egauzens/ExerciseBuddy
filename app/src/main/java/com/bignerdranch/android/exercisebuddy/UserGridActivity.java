package com.bignerdranch.android.exercisebuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.bignerdranch.android.exercisebuddy.databinding.ActivityUserGridBinding;
import com.google.firebase.auth.FirebaseAuth;

public class UserGridActivity extends AppCompatActivity {
    private UserGridActivityViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserGridActivityViewModel.class);
        ActivityUserGridBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user_grid);
        binding.setLifecycleOwner(this);
        binding.setViewModel(mViewModel);
    }

    public void goToUserProfile(View v){
        Intent intent = new Intent(UserGridActivity.this, UserProfileActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("user", mViewModel.getCurrentUser());
        intent.putExtras(extras);
        startActivityForResult(intent, 3);
        return;
    }

    public void editUserPreferences(View v){
        Intent intent = new Intent(UserGridActivity.this, UpdateUserPreferencesActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("user", mViewModel.getCurrentUser());
        intent.putExtras(extras);
        startActivityForResult(intent, 2);
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK){
            if (data.hasExtra("newUserData")){
                User newUserData = (User) data.getSerializableExtra("newUserData");
                if (!mViewModel.getCurrentUser().arePreferencesEqual(newUserData)){
                    mViewModel.updateDatabase(newUserData);
                }
            }
        }
        if (requestCode == 3 && resultCode == Activity.RESULT_OK){
            if (data.hasExtra("newUserData")){
                User newUserData = (User) data.getSerializableExtra("newUserData");
                if (!mViewModel.getCurrentUser().areProfilesEqual(newUserData)){
                    if (!mViewModel.getCurrentUser().getProfileImageUri().equals(newUserData.getProfileImageUri())){
                        mViewModel.loadProfileImageIntoStorage(newUserData, this.getContentResolver());
                    }
                    mViewModel.updateDatabase(newUserData);
                }
            }
        }
    }

    public void logoutUser(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UserGridActivity.this, LoginOrRegisterActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}
