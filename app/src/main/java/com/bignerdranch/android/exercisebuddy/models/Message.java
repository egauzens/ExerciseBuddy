package com.bignerdranch.android.exercisebuddy.models;

import java.io.Serializable;

public class Message implements Serializable {
    private long mTime;
    private String mText;
    private String mSenderId;
    private String mReceiverId;
    private String mMessageId;

    public Message(String text, String senderId, String receiverId, long time, String messageId){
        mText = text;
        mSenderId = senderId;
        mReceiverId = receiverId;
        mTime = time;
        mMessageId = messageId;
    }

    public long getTime() {
        return mTime;
    }

    public String getText() {
        return mText;
    }

    public String getSenderId() {
        return mSenderId;
    }

    public String getReceiverId() { return mReceiverId; }

    public String getMessageId() {
        return mMessageId;
    }
}
