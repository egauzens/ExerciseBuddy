package com.bignerdranch.android.exercisebuddy.models;

import java.io.Serializable;

public class Message implements Serializable {
    private long mTime;
    private String mText;
    private String mSenderId;
    private String mSenderName;
    private String mReceiverId;
    private String mReceiverName;
    private String mMessageId;

    public Message(String text, String senderId, String senderName, String receiverId, String receiverName, long time, String messageId){
        mText = text;
        mSenderId = senderId;
        mSenderName = senderName;
        mReceiverId = receiverId;
        mReceiverName = receiverName;
        mTime = time;
        mMessageId = messageId;
    }

    public long getTime() {
        return mTime;
    }

    public String getText() {
        return mText;
    }

    public String getSenderId() { return mSenderId; }

    public String getSenderName(){ return mSenderName; }

    public String getReceiverId() { return mReceiverId; }

    public String getReceiverName() { return mReceiverName; }

    public String getMessageId() {
        return mMessageId;
    }
}
