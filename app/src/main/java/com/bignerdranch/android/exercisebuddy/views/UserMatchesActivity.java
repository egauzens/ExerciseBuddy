package com.bignerdranch.android.exercisebuddy.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
        mMatchItemAdapter = new MatchItemAdapter(mViewModel.getUserMatches(), this);
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

    public void goToUserProfile(View v){
        Intent intent = new Intent(UserMatchesActivity.this, UserProfileActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("profileUserId", mViewModel.getCurrentUserId());
        intent.putExtras(extras);
        startActivityForResult(intent, 3);
        return;
    }

    public void goToUserPreferences(View v){
        Intent intent = new Intent(UserMatchesActivity.this, UserPreferencesActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("userId", mViewModel.getCurrentUserId());
        intent.putExtras(extras);
        startActivityForResult(intent, 2);
        return;
    }

    public void goToUserConversations(View v){
        Intent intent = new Intent(UserMatchesActivity.this, UserConversationsActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("userId", mViewModel.getCurrentUserId());
        intent.putExtras(extras);
        startActivity(intent);
        return;
    }

    public void logoutUser(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UserMatchesActivity.this, LoginOrRegisterActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}
