package com.bignerdranch.android.exercisebuddy.viewmodels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModel;

import com.bignerdranch.android.exercisebuddy.models.Message;
import com.bignerdranch.android.exercisebuddy.staticHelpers.ConversationSettings;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MessagingActivityViewModel extends ViewModel {
    public final ObservableArrayList<Message> mMessages;
    private ConversationSettings mConversationSettings;

    public MessagingActivityViewModel(){
        mMessages = new ObservableArrayList<>();
        mConversationSettings = null;
    }

    public ObservableArrayList<Message> getMessages() {
        return mMessages;
    }

    public void setConversationSettings(ConversationSettings conversationSettings) {
        this.mConversationSettings = conversationSettings;
        addListenerForMessages();
    }

    public String getConversationId(){
        if (mConversationSettings == null){
            return null;
        }
        return mConversationSettings.getConversationId();
    }

    public String getSenderId(){
        if (mConversationSettings == null){
            return null;
        }
        return mConversationSettings.getUserId();
    }

    public String getSenderName(){
        if (mConversationSettings == null){
            return null;
        }
        return mConversationSettings.getUserName();
    }

    public String getReceiverId(){
        if (mConversationSettings == null){
            return null;
        }
        return mConversationSettings.getMatchId();
    }

    public String getReceiverName(){
        if (mConversationSettings == null){
            return null;
        }
        return mConversationSettings.getMatchName();
    }

    private void addListenerForMessages(){
        final DatabaseReference conversationIdsDb = FirebaseDatabase.getInstance().getReference().child("conversations").child(getConversationId());
        conversationIdsDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = createMessage(dataSnapshot);
                mMessages.add(message);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public Task<Void> addMessageToDb(String message, String conversationId){
        // generates a unique id for the message and adds a child to the conversations node
        DatabaseReference newMessageDb = FirebaseDatabase.getInstance().getReference().child("conversations").child(conversationId).push();
        Map<String, Object> newMessageSettings = new HashMap<>();
        newMessageSettings.put("time", System.currentTimeMillis());
        newMessageSettings.put("content", message);
        newMessageSettings.put("senderUserId", getSenderId());
        newMessageSettings.put("senderName", getSenderName());
        newMessageSettings.put("receiverUserId", getReceiverId());
        newMessageSettings.put("receiverName", getReceiverName());
        return newMessageDb.updateChildren(newMessageSettings);
    }

    private Message createMessage(DataSnapshot dataSnapshot){
        String messageId = dataSnapshot.getKey();
        String senderUserId = dataSnapshot.child("senderUserId").getValue(String.class);
        String senderName = dataSnapshot.child("senderName").getValue(String.class);
        String receiverUserId = dataSnapshot.child("receiverUserId").getValue(String.class);
        String receiverName = dataSnapshot.child("receiverName").getValue(String.class);
        String content = dataSnapshot.child("content").getValue(String.class);
        long time = dataSnapshot.child("time").getValue(Long.class);

        return new Message(content, senderUserId, senderName, receiverUserId, receiverName, time, messageId);
    }
}
