package com.bignerdranch.android.exercisebuddy.models;

import java.util.ArrayList;

public class Conversation {
    private ArrayList<Message> mMessages;

    public Conversation(Message message){
        mMessages = new ArrayList<>();
        // A conversation must have at least one message
        mMessages.add(message);
    }

    public void addMessage(Message message){
        mMessages.add(message);
    }

    public long getTimeOfMostRecentMessage(){
        int indexOfMostRecentMessage = mMessages.size()-1;
        return mMessages.get(indexOfMostRecentMessage).getDate();
    }
}
