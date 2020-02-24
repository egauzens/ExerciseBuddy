package com.bignerdranch.android.exercisebuddy.interfaces;

import com.bignerdranch.android.exercisebuddy.staticHelpers.ConversationSettings;

public interface IConversationItemClickListener {
    void onConversationItemTextAreaClicked(ConversationSettings conversationSettings);

    void onConversationItemMatchImageClicked(String matchUserId);
}
