package com.bignerdranch.android.exercisebuddy.models;

import java.io.Serializable;

public class Conversation implements Serializable {
    private Message mFirstMessage;
    private Message mLastMessage;
    private String mConversationId;

    public Conversation(Message firstMessage, String conversationId){
        // A conversation must have at least one message
        mFirstMessage = firstMessage;
        mLastMessage = firstMessage;
        mConversationId = conversationId;
    }

    public void setLastMessage(Message lastMessage) {
        mLastMessage = lastMessage;
    }

    public Message getLastMessage(){
        return mLastMessage;
    }

    public String getConversationId(){
        return mConversationId;
    }

    public String getReceiverUserId(){ return mFirstMessage.getReceiverId(); }

    public String getReceiverName() { return mFirstMessage.getReceiverName(); }

    public String getSenderUserId() { return mFirstMessage.getSenderId(); }

    public String getSenderName() { return mFirstMessage.getSenderName(); }
}
