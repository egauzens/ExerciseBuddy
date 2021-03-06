package com.bignerdranch.android.exercisebuddy.viewmodels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModel;

import com.bignerdranch.android.exercisebuddy.models.Conversation;
import com.bignerdranch.android.exercisebuddy.models.Message;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserConversationsActivityViewModel extends ViewModel {
    public final ObservableArrayList<Conversation> mUserConversations;
    public String mUserId;

    public UserConversationsActivityViewModel(){
        mUserConversations = new ObservableArrayList<>();
        mUserId = "";
    }

    public void setUserId(String userId){
        mUserId = userId;
        addListenerForUserConversations();
    }

    public ObservableArrayList<Conversation> getUserConversations() {
        return mUserConversations;
    }

    public String getUserId(){
        return mUserId;
    }

    private void addListenerForUserConversations(){
        final DatabaseReference conversationIdsDb = FirebaseDatabase.getInstance().getReference().child("users").child(mUserId).child("conversationIds");
        conversationIdsDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                createConversation(dataSnapshot.getKey());
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

    private void createConversation(final String conversationId) {
        // we only want to show the latest message sent which is the one with the largest time
        Query conversationDbQuery = FirebaseDatabase.getInstance().getReference().child("conversations").child(conversationId).orderByChild("time").limitToLast(1);
        conversationDbQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Whenever a child is added check if the convo is in mUserConversations. If not then add it. If it is already then update its position to the beginning.
                String conversationId = dataSnapshot.getRef().getParent().getKey();
                Conversation cachedConversation = getConversationWithConversationId(mUserConversations, conversationId);
                Message latestMessage = createMessage(dataSnapshot);

                if (cachedConversation == null){
                    Conversation conversation = new Conversation(latestMessage, conversationId);
                    if (mUserConversations.isEmpty())
                    {
                        mUserConversations.add(conversation);
                    }
                    else
                    {
                        // make sure that the conversations are in order by time
                        for (int i = 0; i < mUserConversations.size(); i++) {
                            long conversationTime = mUserConversations.get(i).getLastMessage().getTime();
                            if (latestMessage.getTime() > conversationTime) {
                                mUserConversations.add(i, conversation);
                                break;
                            }
                            if (i == mUserConversations.size()-1){
                                // it is the oldest conversation in the list so it goes at the end
                                mUserConversations.add(conversation);
                                break;
                            }
                        }
                    }
                }
                else
                {
                    cachedConversation.setLastMessage(latestMessage);
                    int sourceIndex = mUserConversations.indexOf(cachedConversation);
                    if (sourceIndex != 0) {
                        // if it's not already the first conversation in the list then move it to be the first one
                        CopyOnWriteArrayList<Conversation> conversationsAboveCachedConversation = new CopyOnWriteArrayList<Conversation>(mUserConversations.subList(0, sourceIndex));
                        mUserConversations.subList(0, sourceIndex).clear();
                        mUserConversations.addAll(1, conversationsAboveCachedConversation);

                    }
                    // update the first conversation to have the latest message
                    mUserConversations.set(0, cachedConversation);
                }
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

    private Conversation getConversationWithConversationId(ArrayList<Conversation> conversations, String conversationId){
        for (Conversation conversation : conversations){
            if (conversation.getConversationId().equals(conversationId)){
                return conversation;
            }
        }
        return null;
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
