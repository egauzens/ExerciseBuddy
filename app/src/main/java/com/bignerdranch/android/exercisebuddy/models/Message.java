package com.bignerdranch.android.exercisebuddy.models;

public class Message {
    private long mDate;
    private String mText;
    private String mSenderId;
    private String mMessageId;

    public Message(String text, String senderId, long date, String messageId){
        mText = text;
        mSenderId = senderId;
        mDate = date;
        mMessageId = messageId;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        this.mDate = date;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public String getSenderId() {
        return mSenderId;
    }

    public void setSenderId(String senderId) {
        this.mSenderId = senderId;
    }

    public String getMessageId() {
        return mMessageId;
    }

    public void setMessageId(String messageId) {
        this.mMessageId = messageId;
    }
}
