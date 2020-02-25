package com.bignerdranch.android.exercisebuddy.interfaces;

import com.bignerdranch.android.exercisebuddy.helpers.ConversationSettings;

public interface IConversationItemClickListener {
    void onConversationItemTextAreaClicked(ConversationSettings conversationSettings);

    void onConversationItemMatchImageClicked(String matchUserId);
}
