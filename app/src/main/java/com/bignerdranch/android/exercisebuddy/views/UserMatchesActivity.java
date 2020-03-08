package com.bignerdranch.android.exercisebuddy.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.exercisebuddy.adapters.MatchItemAdapter;
import com.bignerdranch.android.exercisebuddy.interfaces.IMatchItemClickListener;
import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.viewmodels.UserMatchesActivityViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class UserMatchesActivity extends AppCompatActivity implements IMatchItemClickListener {
    private UserMatchesActivityViewModel mViewModel;
    private RecyclerView mMatchItemsRecyclerView;
    private MatchItemAdapter mMatchItemAdapter;
    final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserMatchesActivityViewModel.class);
        setContentView(R.layout.activity_user_matches);
        setupToolbar();
        setupRecyclerView();
        addUserMatchesListener();
        if (mViewModel.getCurrentUserId().isEmpty()){
            initializeViewModel();
        }
    }

    private void initializeViewModel(){
        mViewModel.setCurrentUserId(userId);
    }

    @Override
    protected void onDestroy() {
        removeUserMatchesListener();
        super.onDestroy();
    }

    private void addUserMatchesListener(){
        mViewModel.getUserMatches().addOnListChangedCallback(userMatchesListener);
    }

    private void removeUserMatchesListener(){
        mViewModel.getUserMatches().removeOnListChangedCallback(userMatchesListener);
    }

    private final ObservableList.OnListChangedCallback userMatchesListener =
        new ObservableList.OnListChangedCallback() {
            @Override
            public void onChanged(ObservableList sender) {
            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
                mMatchItemAdapter.notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
                mMatchItemAdapter.notifyItemRangeRemoved(positionStart, itemCount);
            }
        };

    private void setupRecyclerView(){
        mMatchItemAdapter = new MatchItemAdapter(mViewModel.getUserMatches(), this);

        mMatchItemsRecyclerView = (RecyclerView) findViewById(R.id.matches_recycler_view);
        mMatchItemsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mMatchItemsRecyclerView.setAdapter(mMatchItemAdapter);
    }

    public void onMatchItemClicked(String profileUserId){
        String currentUserId = mViewModel.getCurrentUserId();
        Intent intent = new Intent(UserMatchesActivity.this, MatchProfileActivity.class);
        Bundle extras = new Bundle();
        // set the user to the gridItem (which is the match) because the actual profile we are viewing is that of the match.
        // the current logged in user is now considered the match.
        extras.putSerializable("profileUserId", profileUserId);
        extras.putSerializable("matchUserId", currentUserId);

        intent.putExtras(extras);
        startActivity(intent);
        return;
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
            case R.id.matches_menu_item:
                return true;
            case R.id.profile_menu_item:
                goToUserProfile();
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

    private void goToUserProfile(){
        Intent intent = new Intent(UserMatchesActivity.this, UserProfileActivity.class);
        Bundle extras = new Bundle();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        extras.putSerializable("profileUserId", mViewModel.getCurrentUserId());
        intent.putExtras(extras);
        startActivity(intent);
        return;
    }

    private void goToUserPreferences(){
        Intent intent = new Intent(UserMatchesActivity.this, UserPreferencesActivity.class);
        Bundle extras = new Bundle();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        extras.putSerializable("userId", mViewModel.getCurrentUserId());
        intent.putExtras(extras);
        startActivity(intent);
        return;
    }

    private void goToUserConversations(){
        Intent intent = new Intent(UserMatchesActivity.this, UserConversationsActivity.class);
        Bundle extras = new Bundle();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        extras.putSerializable("userId", mViewModel.getCurrentUserId());
        intent.putExtras(extras);
        startActivity(intent);
        return;
    }

    private void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UserMatchesActivity.this, LoginOrRegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return;
    }
}
