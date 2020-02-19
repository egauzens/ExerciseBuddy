package com.bignerdranch.android.exercisebuddy.views;

import android.app.Activity;
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
import com.bignerdranch.android.exercisebuddy.models.User;
import com.bignerdranch.android.exercisebuddy.viewmodels.UserMatchesActivityViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class UserMatchesActivity extends AppCompatActivity implements IMatchItemClickListener {
    private UserMatchesActivityViewModel mViewModel;
    private RecyclerView mMatchItemsRecyclerView;
    private ObservableList.OnListChangedCallback mUserMatchesListener;
    private MatchItemAdapter mMatchItemAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserMatchesActivityViewModel.class);
        setContentView(R.layout.activity_user_matches);
        mMatchItemAdapter = new MatchItemAdapter(mViewModel.getUserMatches(), this);
        setupRecyclerView();
        mUserMatchesListener = createUserMatchesListener();
        addUserMatchesListener();
    }

    @Override
    protected void onDestroy() {
        removeUserMatchesListener();
        super.onDestroy();
    }

    private void addUserMatchesListener(){
        mViewModel.getUserMatches().addOnListChangedCallback(mUserMatchesListener);
    }

    private void removeUserMatchesListener(){
        mViewModel.getUserMatches().removeOnListChangedCallback(mUserMatchesListener);
    }

    private ObservableList.OnListChangedCallback createUserMatchesListener(){
        return new ObservableList.OnListChangedCallback() {
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
    }

    private void setupRecyclerView(){
        mMatchItemsRecyclerView = (RecyclerView) findViewById(R.id.matches_recycler_view);
        mMatchItemsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mMatchItemsRecyclerView.setAdapter(mMatchItemAdapter);
    }

    public void onMatchItemClicked(User gridItem){
        User currentUser = mViewModel.getCurrentUser();
        Intent intent = new Intent(UserMatchesActivity.this, MatchProfileActivity.class);
        Bundle extras = new Bundle();
        // set the user to the gridItem (which is the match) because the actual profile we are viewing is that of the match.
        // the current logged in user is now considered the match.
        extras.putSerializable("user", gridItem);
        extras.putSerializable("match", currentUser);

        intent.putExtras(extras);
        startActivity(intent);
        return;
    }

    public void goToUserProfile(View v){
        Intent intent = new Intent(UserMatchesActivity.this, UserProfileActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("user", mViewModel.getCurrentUser());
        intent.putExtras(extras);
        startActivityForResult(intent, 3);
        return;
    }

    public void editUserPreferences(View v){
        Intent intent = new Intent(UserMatchesActivity.this, UpdateUserPreferencesActivity.class);
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
        Intent intent = new Intent(UserMatchesActivity.this, LoginOrRegisterActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public void goToUserConversations(View v){
        Intent intent = new Intent(UserMatchesActivity.this, UserConversationsActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("userId", mViewModel.getCurrentUser().getUid());
        intent.putExtras(extras);
        startActivity(intent);
        return;
    }
}
