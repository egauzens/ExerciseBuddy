package com.bignerdranch.android.exercisebuddy.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Conversation implements Serializable {
    private ArrayList<Message> mMessages;
    private String mConversationId;

    public Conversation(Message message, String conversationId){
        mMessages = new ArrayList<>();
        // A conversation must have at least one message
        mMessages.add(message);
        mConversationId = conversationId;
    }

    public void addMessage(Message message){
        mMessages.add(message);
    }

    public long getTimeOfMostRecentMessage(){
        int indexOfMostRecentMessage = mMessages.size()-1;
        return mMessages.get(indexOfMostRecentMessage).getTime();
    }

    public String getConversationId(){
        return mConversationId;
    }

    public String getReceiverUserId(){ return mMessages.get(0).getReceiverId(); }

    public String getSenderUserId() { return mMessages.get(0).getSenderId(); }
}
