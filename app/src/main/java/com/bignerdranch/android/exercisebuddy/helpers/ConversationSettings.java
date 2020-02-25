package com.bignerdranch.android.exercisebuddy.helpers;

import java.io.Serializable;

public class ConversationSettings implements Serializable {
    private String mUserId;
    private String mUserName;
    private String mMatchId;
    private String mMatchName;
    private String mConversationId;

    public ConversationSettings(String conversationId, String userId, String userName, String matchId, String matchName){
        this.mConversationId = conversationId;
        this.mUserId = userId;
        this.mUserName = userName;
        this.mMatchId = matchId;
        this.mMatchName = matchName;
    }

    public String getConversationId() { return mConversationId; }

    public String getUserId() {
        return mUserId;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getMatchId() {
        return mMatchId;
    }

    public String getMatchName() {
        return mMatchName;
    }
}
