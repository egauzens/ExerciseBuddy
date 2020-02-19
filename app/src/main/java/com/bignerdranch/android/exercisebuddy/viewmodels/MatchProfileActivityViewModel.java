package com.bignerdranch.android.exercisebuddy.viewmodels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableBoolean;

import com.bignerdranch.android.exercisebuddy.models.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MatchProfileActivityViewModel extends UserProfileActivityViewModel {

    private User mMatch;
    private String mConversationId;
    private final ObservableBoolean mHasConversation;
    private ChildEventListener mUserConversationsDbListener;
    private DatabaseReference mUserConversationsDb;

    public MatchProfileActivityViewModel(){
        super();
        mHasConversation = new ObservableBoolean(false);
        mUserConversationsDbListener = null;
        mUserConversationsDb = null;
        mConversationId = "";
        mMatch = null;
    }

    public User getMatch() {
        return mMatch;
    }

    public void setMatch(User user) {
        this.mMatch = user;
        String sharedConversationId = getSharedConversationId(getMatch(), getUserProfile());
        addConversationIdsListener();
        setConversationId(sharedConversationId);
    }

    public ObservableBoolean getHasConversation() {
        return mHasConversation;
    }

    public String getConversationId(){ return mConversationId; }

    public void setConversationId(String conversationId) {
        if (mConversationId.isEmpty()){
            this.mConversationId = conversationId;
        }
        if (!conversationId.isEmpty()){
            mHasConversation.set(true);

        }
        if (mUserConversationsDbListener != null){
            mUserConversationsDb.removeEventListener(mUserConversationsDbListener);
        }
    }

    private void addConversationIdsListener(){
        if (mUserConversationsDbListener != null){
            return;
        }
        mUserConversationsDb = FirebaseDatabase.getInstance().getReference().child("users").child(getMatch().getUid()).child("conversationIds");
        mUserConversationsDbListener = mUserConversationsDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String conversationId = dataSnapshot.getKey();
                DatabaseReference conversationsIdDb = FirebaseDatabase.getInstance().getReference().child("conversations").child(conversationId);
                // This is in case the person who you are about to send a message to sends one to you first while you are looking at their profile
                conversationsIdDb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()){
                            String receiverUserId = child.child("receiverUserId").getValue(String.class);
                            String senderUserId = child.child("senderUserId").getValue(String.class);
                            if (receiverUserId.equals(getMatch().getUid()) && senderUserId.equals(getUserProfile().getUid())){
                                setConversationId(dataSnapshot.getKey());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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

    public void addConversationToDb(String firstMessage){
        // must populate the conversations DB first so that if the match's user conversations listener fires than the DB is up to date
        String conversationId = addConversationToConversationsDb(firstMessage);
        addConversationIdToUsersDb(conversationId);
        setConversationId(conversationId);
    }

    private String getSharedConversationId(User user1, User user2){
        ArrayList<String> user1ConversationIds = user1.getConversationIds();
        ArrayList<String> user2ConversationIds = user2.getConversationIds();
        for (String conversationId : user1ConversationIds){
            if (user2ConversationIds.contains(conversationId)){
                return conversationId;
            }
        }
        return "";
    }

    private String addConversationToConversationsDb(String firstMessage){
        // generates a unique id for the conversation and adds a child to the conversations node
        DatabaseReference newConversationDb = FirebaseDatabase.getInstance().getReference().child("conversations").push();
        String conversationId = newConversationDb.getKey();
        addMessageToDb(newConversationDb, firstMessage);

        return conversationId;
    }

    private void addConversationIdToUsersDb(String conversationId){
        // Firebase cannot have empty directory paths. Since we only need the key of the child (the convoId itself) then we add a dummy value of true
        FirebaseDatabase.getInstance().getReference().child("users").child(getMatch().getUid()).child("conversationIds").child(conversationId).setValue(true);
        FirebaseDatabase.getInstance().getReference().child("users").child(getUserProfile().getUid()).child("conversationIds").child(conversationId).setValue(true);
    }

    private void addMessageToDb(DatabaseReference conversationDb, String firstMessage){
        // generates a unique id for the message and adds a child to the conversations node
        DatabaseReference newMessageDb = conversationDb.push();

        newMessageDb.child("time").setValue(System.currentTimeMillis());
        newMessageDb.child("content").setValue(firstMessage);
        newMessageDb.child("senderUserId").setValue(getMatch().getUid());
        newMessageDb.child("senderName").setValue(getMatch().getName());
        newMessageDb.child("receiverUserId").setValue(getUserProfile().getUid());
        newMessageDb.child("receiverName").setValue(getUserProfile().getName());
    }
}
