package com.bignerdranch.android.exercisebuddy.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.adapters.ConversationItemAdapter;
import com.bignerdranch.android.exercisebuddy.interfaces.IConversationItemClickListener;
import com.bignerdranch.android.exercisebuddy.viewmodels.UserConversationsActivityViewModel;

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
            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
                mConversationItemAdapter.notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {

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

    public void onConversationItemTextAreaClicked(String currentUserId, String conversationId){
        Intent intent = new Intent(UserConversationsActivity.this, MessagingActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("conversationId", conversationId);
        extras.putString("userId", currentUserId);

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
}
