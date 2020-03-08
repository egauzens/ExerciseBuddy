package com.bignerdranch.android.exercisebuddy.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.adapters.MessageItemAdapter;
import com.bignerdranch.android.exercisebuddy.databinding.ActivityMessagingBinding;
import com.bignerdranch.android.exercisebuddy.helpers.ConversationSettings;
import com.bignerdranch.android.exercisebuddy.viewmodels.MessagingActivityViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MessagingActivity extends AppCompatActivity {
    private MessagingActivityViewModel mViewModel;
    private RecyclerView mMessageItemsRecyclerView;
    private ObservableList.OnListChangedCallback mMessagesListener;
    private MessageItemAdapter mMessageItemAdapter;
    private EditText mMessageEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMessagingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_messaging);
        mViewModel = ViewModelProviders.of(this).get(MessagingActivityViewModel.class);
        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(this);

        setupToolbar();
        InitializeViewModel();
        setupRecyclerView();

        mMessageEditText = (EditText) findViewById(R.id.messaging_edit_text);
    }

    private void InitializeViewModel(){
        Intent intent = getIntent();
        ConversationSettings conversationSettings = (ConversationSettings)intent.getSerializableExtra("conversationSettings");
        mViewModel.setConversationSettings(conversationSettings);
        addUserMessagesListener();
    }

    @Override
    protected void onDestroy() {
        removeUserMatchesListener();
        super.onDestroy();
    }

    private void addUserMessagesListener(){
        mMessagesListener = createMessagesListener();
        mViewModel.getMessages().addOnListChangedCallback(mMessagesListener);
    }

    private void removeUserMatchesListener(){
        mViewModel.getMessages().removeOnListChangedCallback(mMessagesListener);
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
                mMessageItemsRecyclerView.scrollToPosition(mViewModel.getMessagesCount()-1);
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
        mMessageItemAdapter = new MessageItemAdapter(mViewModel.getMessages(), mViewModel.getSenderId());
        mMessageItemsRecyclerView = (RecyclerView) findViewById(R.id.messages_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mMessageItemsRecyclerView.setLayoutManager(layoutManager);
        mMessageItemsRecyclerView.setAdapter(mMessageItemAdapter);
    }

    public void sendMessage(final View v){
        String messageText = mMessageEditText.getText().toString();
        mViewModel.addMessageToDb(messageText, mViewModel.getConversationId()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), R.string.message_send_failure, Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mMessageEditText.setText("");
            }
        });
    }

    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setupToolbar() {
        Toolbar appToolbar = (Toolbar) findViewById(R.id.messaging_tool_bar);
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
