package com.bignerdranch.android.exercisebuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.exercisebuddy.databinding.ActivityUserGridBinding;
import com.google.firebase.auth.FirebaseAuth;

public class UserGridActivity extends AppCompatActivity {
    private UserGridActivityViewModel mViewModel;
    private RecyclerView mUserGrid;

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
        startActivity(intent);
        return;
    }

    public void logoutUser(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UserGridActivity.this, LoginOrRegisterActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}
