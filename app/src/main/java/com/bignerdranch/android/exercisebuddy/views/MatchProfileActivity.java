package com.bignerdranch.android.exercisebuddy.views;

import android.content.Intent;

import androidx.lifecycle.ViewModelProviders;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.models.User;
import com.bignerdranch.android.exercisebuddy.viewmodels.MatchProfileActivityViewModel;
import com.bignerdranch.android.exercisebuddy.viewmodels.UserProfileActivityViewModel;

public class MatchProfileActivity extends UserProfileActivity {

    public UserProfileActivityViewModel getViewModel(){
        return ViewModelProviders.of(this).get(MatchProfileActivityViewModel.class);
    }

    public int getLayoutId(){
        return R.layout.activity_match_profile;
    }

    @Override
    protected void InitializeViewModel(){
        super.InitializeViewModel();
        Intent intent = getIntent();
        User match = (User)intent.getSerializableExtra("match");
        ((MatchProfileActivityViewModel)mViewModel).setMatch(match);
    }
}
