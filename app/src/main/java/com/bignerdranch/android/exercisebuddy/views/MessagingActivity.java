package com.bignerdranch.android.exercisebuddy.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.models.User;
import com.bignerdranch.android.exercisebuddy.viewmodels.MatchProfileActivityViewModel;
import com.bignerdranch.android.exercisebuddy.viewmodels.MessagingActivityViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessagingActivity extends AppCompatActivity {
    /*private MessagingActivityViewModel mViewModel;
    private RecyclerView mMessageItemsRecyclerView;
    private ObservableList.OnListChangedCallback mMessagesListener;
    private MessageItemAdapter mMessageItemAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MessagingActivityViewModel.class);
        setContentView(R.layout.activity_messaging);
        mMessageItemAdapter = new MessageItemAdapter(mViewModel.getUserMatches(), this);
        setupRecyclerView();
        mMessagesListener = createMessagesListener();
        addUserMatchesListener();
    }

    private void InitializeViewModel(){
        Intent intent = getIntent();
        User user = (User)intent.getSerializableExtra("user");
        User match = (User)intent.getSerializableExtra("match");
        mViewModel.setMatch(match);
    }


    @Override
    protected void onDestroy() {
        removeUserMatchesListener();
        super.onDestroy();
    }

    private void addUserMatchesListener(){
        mViewModel.getUserMatches().addOnListChangedCallback(mMessagesListener);
    }

    private void removeUserMatchesListener(){
        mViewModel.getUserMatches().removeOnListChangedCallback(mMessagesListener);
    }

    private ObservableList.OnListChangedCallback createMessagesListener(){
        return new ObservableList.OnListChangedCallback() {
            @Override
            public void onChanged(ObservableList sender) {
            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
                mMessageItemAdapter.notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
                mMessageItemAdapter.notifyItemRangeRemoved(positionStart, itemCount);
            }
        };
    }

    private void setupRecyclerView(){
        mMessageItemsRecyclerView = (RecyclerView) findViewById(R.id.messages_recycler_view);
        mMessageItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMessageItemsRecyclerView.setAdapter(mMessageItemAdapter);
    }

    public void sendMessage(View v){

    }

    private String addMessageToConversationsDb(String message, String conversationId){
        // generates a unique id for the conversation and adds a child to the conversations node
        DatabaseReference conversationDb = FirebaseDatabase.getInstance().getReference().child("conversations").child(conversationId);
        String messageId = addMessageToDb(conversationDb, message);

        return messageId;
    }*/
}
