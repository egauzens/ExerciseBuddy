package com.bignerdranch.android.exercisebuddy.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.databinding.ActivityMatchProfileBinding;
import com.bignerdranch.android.exercisebuddy.models.User;
import com.bignerdranch.android.exercisebuddy.viewmodels.MatchProfileActivityViewModel;
import com.bignerdranch.android.exercisebuddy.viewmodels.UserProfileActivityViewModel;

public class MatchProfileActivity extends UserProfileActivity {

    private EditText mFirstMessageEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirstMessageEditText = (EditText) findViewById(R.id.first_message_edit_text);
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
        User match = (User)intent.getSerializableExtra("match");
        ((MatchProfileActivityViewModel)mViewModel).setMatch(match);
        ((MatchProfileActivityViewModel)mViewModel).addConversationIdsListener();
    }

    public void goToMessagingActivity(View v){
        Intent intent = new Intent(MatchProfileActivity.this, MessagingActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("user", mViewModel.getUserProfile());
        extras.putSerializable("match", ((MatchProfileActivityViewModel)mViewModel).getMatch());
        intent.putExtras(extras);
        startActivity(intent);
        finish();
        return;
    }

    public void sendMessage(View v){
        // no need to check that the other user hasn't sent a message right before creating a new convoId since the
        // listener on the view model takes care of this. If the other user does send a message first than the button
        // which calls this onClickListener will not be shown
        String firstMessageText = mFirstMessageEditText.getText().toString();
        ((MatchProfileActivityViewModel)mViewModel).addConversationToDb(firstMessageText);
    }
}
