package com.bignerdranch.android.exercisebuddy.interfaces;

public interface IConversationItemClickListener {
    public void onConversationItemTextAreaClicked(String currentUserId, String conversationId);

    public void onConversationItemMatchImageClicked(String matchUserId);
}
