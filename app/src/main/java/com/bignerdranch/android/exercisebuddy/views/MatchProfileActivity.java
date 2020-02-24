package com.bignerdranch.android.exercisebuddy.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.databinding.ActivityMatchProfileBinding;
import com.bignerdranch.android.exercisebuddy.viewmodels.MatchProfileActivityViewModel;
import com.bignerdranch.android.exercisebuddy.viewmodels.UserProfileActivityViewModel;

public class MatchProfileActivity extends UserProfileActivity {

    private EditText mFirstMessageEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirstMessageEditText = (EditText) findViewById(R.id.first_message_edit_text);
        if (!mViewModel.getAreAllUsersLoaded().getValue()){
            InitializeViewModel();
            mViewModel.getAreAllUsersLoaded().observe(this, areAllUsersLoadedObserver);
        }
    }

    @Override
    public void setContentView(){
        ActivityMatchProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_match_profile);
        binding.setLifecycleOwner(this);
        binding.setViewModel((MatchProfileActivityViewModel) mViewModel);
    }

    public UserProfileActivityViewModel getViewModel(){
        return ViewModelProviders.of(this).get(MatchProfileActivityViewModel.class);
    }

    @Override
    protected void InitializeViewModel(){
        super.InitializeViewModel();
        Intent intent = getIntent();
        String matchUserId = (String)intent.getSerializableExtra("matchUserId");
        ((MatchProfileActivityViewModel)mViewModel).setMatchUserId(matchUserId);
    }

    final Observer<Boolean> areAllUsersLoadedObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable final Boolean isLoaded) {
            if (isLoaded){
                ((MatchProfileActivityViewModel)mViewModel).setConversationId();
                mViewModel.getAreAllUsersLoaded().removeObserver(areAllUsersLoadedObserver);
            }
        }
    };

    public void goToMessagingActivity(View v){
        Intent intent = new Intent(MatchProfileActivity.this, MessagingActivity.class);
        Bundle extras = new Bundle();
        // The logged in user is the match user id bc we are currently viewing the profile of a match (so the profile user id is set to that of the match id)
        extras.putSerializable("matchUserId", mViewModel.getProfileUserId());
        extras.putSerializable("loggedInUserId", ((MatchProfileActivityViewModel)mViewModel).getMatchUserId());
        intent.putExtras(extras);
        startActivity(intent);
        finish();
        return;
    }

    public void sendFirstMessage(View v){
        if(!mViewModel.getAreAllUsersLoaded().getValue())
        {
            return;
        }
        // no need to check that the other user hasn't sent a message right before creating a new convoId since the
        // listener on the view model takes care of this. If the other user does send a message first than the button
        // which calls this onClickListener will not be shown
        String firstMessageText = mFirstMessageEditText.getText().toString();
        ((MatchProfileActivityViewModel)mViewModel).addConversationToDb(firstMessageText);
    }
}
