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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.adapters.ConversationItemAdapter;
import com.bignerdranch.android.exercisebuddy.interfaces.IConversationItemClickListener;
import com.bignerdranch.android.exercisebuddy.helpers.ConversationSettings;
import com.bignerdranch.android.exercisebuddy.viewmodels.UserConversationsActivityViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class UserConversationsActivity extends AppCompatActivity implements IConversationItemClickListener {
    private UserConversationsActivityViewModel mViewModel;
    private RecyclerView mGridItemsRecyclerView;
    private ObservableList.OnListChangedCallback mUserConversationsListener;
    private ConversationItemAdapter mConversationItemAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserConversationsActivityViewModel.class);
        setContentView(R.layout.activity_user_conversations);
        setupToolbar();
        InitializeViewModel();
        setupRecyclerView();
    }

    @Override
    protected void onDestroy() {
        removeUserConversationsListener();
        super.onDestroy();
    }

    protected void InitializeViewModel(){
        Intent intent = getIntent();
        String userId = (String)intent.getSerializableExtra("userId");
        mViewModel.setUserId(userId);
        addUserConversationsListener();
    }

    private void addUserConversationsListener(){
        mUserConversationsListener = createUserConversationsListener();
        mViewModel.getUserConversations().addOnListChangedCallback(mUserConversationsListener);
    }

    private void removeUserConversationsListener(){
        mViewModel.getUserConversations().removeOnListChangedCallback(mUserConversationsListener);
    }

    private ObservableList.OnListChangedCallback createUserConversationsListener(){
        return new ObservableList.OnListChangedCallback() {
            @Override
            public void onChanged(ObservableList sender) {
                return;
            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
                mConversationItemAdapter.notifyItemChanged(positionStart);
            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
                mConversationItemAdapter.notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
                return;
            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
                mConversationItemAdapter.notifyItemRangeRemoved(positionStart, itemCount);
            }
        };
    }

    private void setupRecyclerView(){
        mConversationItemAdapter = new ConversationItemAdapter(mViewModel.getUserConversations(), mViewModel.getUserId(), this);
        mGridItemsRecyclerView = (RecyclerView) findViewById(R.id.conversations_recycler_view);
        mGridItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mGridItemsRecyclerView.setAdapter(mConversationItemAdapter);
    }

    public void onConversationItemTextAreaClicked(ConversationSettings conversationSettings){
        Intent intent = new Intent(UserConversationsActivity.this, MessagingActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("conversationSettings", conversationSettings);

        intent.putExtras(extras);
        startActivity(intent);
        return;
    }

    public void onConversationItemMatchImageClicked(String profileUserId){
        Intent intent = new Intent(UserConversationsActivity.this, MatchProfileActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("profileUserId", profileUserId);
        extras.putSerializable("matchUserId", mViewModel.getUserId());

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
                goToUserMatches();
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

    public void goToUserMatches(){
        Intent intent = new Intent(UserConversationsActivity.this, UserMatchesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        return;
    }

    private void goToUserProfile(){
        Intent intent = new Intent(UserConversationsActivity.this, UserProfileActivity.class);
        Bundle extras = new Bundle();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        extras.putSerializable("profileUserId", mViewModel.getUserId());
        intent.putExtras(extras);
        startActivity(intent);
        return;
    }

    private void goToUserPreferences(){
        Intent intent = new Intent(UserConversationsActivity.this, UserPreferencesActivity.class);
        Bundle extras = new Bundle();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        extras.putSerializable("userId", mViewModel.getUserId());
        intent.putExtras(extras);
        startActivity(intent);
        return;
    }

    private void goToUserConversations(){
        Intent intent = new Intent(UserConversationsActivity.this, UserConversationsActivity.class);
        Bundle extras = new Bundle();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        extras.putSerializable("userId", mViewModel.getUserId());
        intent.putExtras(extras);
        startActivity(intent);
        return;
    }

    private void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UserConversationsActivity.this, LoginOrRegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return;
    }
}
