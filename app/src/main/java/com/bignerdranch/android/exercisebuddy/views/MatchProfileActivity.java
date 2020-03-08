package com.bignerdranch.android.exercisebuddy.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.databinding.ActivityMatchProfileBinding;
import com.bignerdranch.android.exercisebuddy.helpers.ConversationSettings;
import com.bignerdranch.android.exercisebuddy.viewmodels.MatchProfileActivityViewModel;
import com.bignerdranch.android.exercisebuddy.viewmodels.UserProfileActivityViewModel;

public class MatchProfileActivity extends ProfileActivity {

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
    protected void setContentView(){
        ActivityMatchProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_match_profile);
        binding.setLifecycleOwner(this);
        binding.setViewModel((MatchProfileActivityViewModel) mViewModel);
    }

    @Override
    protected UserProfileActivityViewModel getViewModel(){
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
        ConversationSettings conversationSettings = new ConversationSettings(
                ((MatchProfileActivityViewModel)mViewModel).getConversationId(),
                ((MatchProfileActivityViewModel)mViewModel).getMatchUserId(),
                ((MatchProfileActivityViewModel)mViewModel).getMatchUserName(),
                mViewModel.getProfileUserId(),
                mViewModel.getProfileUserName().getValue());
        extras.putSerializable("conversationSettings", conversationSettings);
        intent.putExtras(extras);
        startActivity(intent);
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

    @Override
    protected void setupToolbar() {
        Toolbar appToolbar = (Toolbar) findViewById(R.id.match_profile_tool_bar);
        // must set a title before calling setSupportActionBar so that binding works
        // https://stackoverflow.com/questions/26486730/in-android-app-toolbar-settitle-method-has-no-effect-application-name-is-shown/32582780
        appToolbar.setTitle("");
        setSupportActionBar(appToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.match_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                return true;
            case R.id.block_user_menu_item:
                blockUser();
                return true;
            case R.id.unblock_user_menu_item:
                unblockUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goBack(){
        finish();
    }

    private void blockUser(){

    }

    private void unblockUser(){

    }
}
