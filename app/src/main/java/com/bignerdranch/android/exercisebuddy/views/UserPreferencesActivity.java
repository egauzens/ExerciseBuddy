package com.bignerdranch.android.exercisebuddy.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.helpers.UserPreferencesSettings;
import com.bignerdranch.android.exercisebuddy.viewmodels.UserPreferencesActivityViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class UserPreferencesActivity extends AppCompatActivity {
    protected UserPreferencesActivityViewModel mViewModel;
    private TextView mExercisePreferenceTextView;
    private TextView mGenderPreferenceTextView;
    private TextView mExperienceLevelPreferenceTextView;
    private TextView mLowerAgePreferenceTextView;
    private TextView mUpperAgePreferenceTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserPreferencesActivityViewModel.class);
        setContentView(R.layout.activity_user_preferences);
        setupToolbar();

        mExercisePreferenceTextView = (TextView) findViewById(R.id.exercise_preference_text_view);
        mGenderPreferenceTextView = (TextView) findViewById(R.id.gender_preference_text_view);
        mExperienceLevelPreferenceTextView = (TextView) findViewById(R.id.experience_level_preference_text_view);
        mLowerAgePreferenceTextView = (TextView) findViewById(R.id.lower_age_preference_text_view);
        mUpperAgePreferenceTextView = (TextView) findViewById(R.id.upper_age_preference_text_view);

        if (!mViewModel.getAreAllUsersLoaded().getValue()){
            InitializeViewModel();
            mViewModel.getAreAllUsersLoaded().observe(this, areAllUsersLoadedObserver);
        }
        else {
            InitializeUI();
        }
    }

    public void editUserPreferences(View v){
        if (!mViewModel.getAreAllUsersLoaded().getValue())
        {
            return;
        }
        Intent intent = new Intent(UserPreferencesActivity.this, UpdateUserPreferencesActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("userPreferencesSettings", createUserPreferencesSettings());
        intent.putExtras(extras);
        startActivityForResult(intent, 2);
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            if (data.hasExtra("newUserPreferencesSettings")) {
                UserPreferencesSettings newUserPreferencesSettings = (UserPreferencesSettings) data.getSerializableExtra("newUserPreferencesSettings");
                UserPreferencesSettings oldUserPreferencesSettings = createUserPreferencesSettings();
                if (!oldUserPreferencesSettings.equals(newUserPreferencesSettings)) {
                    mViewModel.updateDatabase(newUserPreferencesSettings);
                }
            }
            finish();
        }
    }

    final Observer<Boolean> areAllUsersLoadedObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable final Boolean isLoaded) {
            if (isLoaded){
                InitializeUI();
                mViewModel.getAreAllUsersLoaded().removeObserver(areAllUsersLoadedObserver);
            }
        }
    };

    private void InitializeViewModel(){
        Intent intent = getIntent();
        String profileUserId = (String)intent.getSerializableExtra("userId");
        mViewModel.setProfileUserId(profileUserId);
    }

    private void InitializeUI() {
        if (!mViewModel.getAreAllUsersLoaded().getValue()){
            return;
        }
        mExercisePreferenceTextView.setText(getString(R.string.exercise_preference) + " " + mViewModel.getExercisePreference());
        mGenderPreferenceTextView.setText(getString(R.string.gender_preference) + " " + mViewModel.getGenderPreference());
        mExperienceLevelPreferenceTextView.setText(getString(R.string.experience_level_preference) + " " + mViewModel.getExperienceLevelPreference());
        mLowerAgePreferenceTextView.setText(getString(R.string.lower_age_range) + " " + mViewModel.getLowerAgePreference());
        mUpperAgePreferenceTextView.setText(getString(R.string.upper_age_range) + " " + mViewModel.getUpperAgePreference());
    }

    public UserPreferencesSettings createUserPreferencesSettings(){
        return new UserPreferencesSettings(
                mViewModel.getExercisePreference(),
                mViewModel.getGenderPreference(),
                mViewModel.getLowerAgePreference(),
                mViewModel.getUpperAgePreference(),
                mViewModel.getExperienceLevelPreference()
        );
    }

    private void setupToolbar(){
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
            case R.id.profile_menu_item:
                goToUserProfile();
                return true;
            case R.id.preferences_menu_item:
                return true;
            case R.id.matches_menu_item:
                goToUserMatches();
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

    public void goToUserProfile(){
        Intent intent = new Intent(UserPreferencesActivity.this, UserProfileActivity.class);
        Bundle extras = new Bundle();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        extras.putSerializable("profileUserId", mViewModel.getUserId());
        intent.putExtras(extras);
        startActivity(intent);
        return;
    }

    public void goToUserMatches(){
        Intent intent = new Intent(UserPreferencesActivity.this, UserMatchesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        return;
    }

    public void goToUserConversations(){
        Intent intent = new Intent(UserPreferencesActivity.this, UserConversationsActivity.class);
        Bundle extras = new Bundle();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        extras.putSerializable("userId", mViewModel.getUserId());
        intent.putExtras(extras);
        startActivity(intent);
        return;
    }

    public void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UserPreferencesActivity.this, LoginOrRegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return;
    }
}
