package com.bignerdranch.android.exercisebuddy.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.helpers.UserProfileSettings;
import com.bignerdranch.android.exercisebuddy.viewmodels.UserProfileActivityViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileActivity extends ProfileActivity {
    private TextView mNameTextView;

    protected UserProfileActivityViewModel getViewModel(){
        return ViewModelProviders.of(this).get(UserProfileActivityViewModel.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNameTextView = (TextView) findViewById(R.id.name_text_view);
    }

    @Override
    protected void setContentView(){
        setContentView(R.layout.activity_user_profile);
    }

    public void editProfile(View v){
        if (!mViewModel.getAreAllUsersLoaded().getValue())
        {
            return;
        }
        Intent intent = new Intent(UserProfileActivity.this, UpdateUserProfileActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("userProfileSettings", createProfileUserSettings());
        intent.putExtras(extras);
        startActivityForResult(intent, 3);
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            if (data.hasExtra("newUserProfileSettings")) {
                UserProfileSettings newUserProfileSettings = (UserProfileSettings) data.getSerializableExtra("newUserProfileSettings");
                UserProfileSettings oldUserProfileSettings = createProfileUserSettings();
                if (!oldUserProfileSettings.equals(newUserProfileSettings)) {
                    mViewModel.updateDatabase(newUserProfileSettings);
                }
            }
            finish();
        }
    }

    @Override
    protected void InitializeUI() {
        super.InitializeUI();
        mNameTextView.setText(getString(R.string.name) + " " + mViewModel.getProfileUserName().getValue());
    }

    public UserProfileSettings createProfileUserSettings(){
        return new UserProfileSettings(
             mViewModel.getProfileUserName().getValue(),
             mViewModel.getProfileUserGender(),
             mViewModel.getProfileUserDob(),
             mViewModel.getProfileUserExperienceLevel(),
             mViewModel.getProfileUserImageUri(),
             mViewModel.getProfileUserDescription(),
             mViewModel.getProfileUserId()
        );
    }

    protected void setupToolbar(){
        Toolbar appToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(appToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tool_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.matches_menu_item:
                goToUserMatches();
                return true;
            case R.id.profile_menu_item:
                return true;
            case R.id.preferences_menu_item:
                goToUserPreferences();
                return true;
            case R.id.conversations_menu_item:
                goToUserConversations();
                return true;
            case R.id.logout_menu_item:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToUserMatches(){
        Intent intent = new Intent(UserProfileActivity.this, UserMatchesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        return;
    }

    private void goToUserPreferences(){
        Intent intent = new Intent(UserProfileActivity.this, UserPreferencesActivity.class);
        Bundle extras = new Bundle();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        extras.putSerializable("userId", mViewModel.getProfileUserId());
        intent.putExtras(extras);
        startActivity(intent);
        return;
    }

    private void goToUserConversations(){
        Intent intent = new Intent(UserProfileActivity.this, UserConversationsActivity.class);
        Bundle extras = new Bundle();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        extras.putSerializable("userId", mViewModel.getProfileUserId());
        intent.putExtras(extras);
        startActivity(intent);
        return;
    }

    private void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UserProfileActivity.this, LoginOrRegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return;
    }
}