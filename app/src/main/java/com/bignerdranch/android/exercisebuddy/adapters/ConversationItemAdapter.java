package com.bignerdranch.android.exercisebuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.helpers.DateTimeHelpers;
import com.bignerdranch.android.exercisebuddy.interfaces.IConversationItemClickListener;
import com.bignerdranch.android.exercisebuddy.models.Conversation;
import com.bignerdranch.android.exercisebuddy.models.Message;
import com.bignerdranch.android.exercisebuddy.helpers.ConversationSettings;
import com.bignerdranch.android.exercisebuddy.helpers.StorageHelper;

import java.util.List;

public class ConversationItemAdapter extends RecyclerView.Adapter<ConversationItemAdapter.ConversationItemHolder> {
    private List<Conversation> mUserConversations;
    private String mUserId;
    private IConversationItemClickListener mConversationItemClickListener;

    public ConversationItemAdapter(List<Conversation> userConversations, String userId, IConversationItemClickListener listener){
        mUserConversations = userConversations;
        mUserId = userId;
        mConversationItemClickListener = listener;
    }

    @NonNull
    @Override
    public ConversationItemAdapter.ConversationItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        return new ConversationItemAdapter.ConversationItemHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationItemAdapter.ConversationItemHolder holder, int position) {
        Conversation conversation = mUserConversations.get(position);
        holder.bind(conversation, mUserId, mConversationItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mUserConversations.size();
    }

    class ConversationItemHolder extends RecyclerView.ViewHolder{
        private LinearLayout mConversationItemTextArea;
        private TextView mMatchNameTextView;
        private TextView mLastMessageTextView;
        private TextView mLastMessageTimeTextView;
        private ImageView mMatchImage;
        private Context mContext;

        public ConversationItemHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.item_conversation, parent, false));
            mContext = parent.getContext();
            mConversationItemTextArea = (LinearLayout) itemView.findViewById(R.id.conversation_text_linear_layout);
            mMatchNameTextView = (TextView) itemView.findViewById(R.id.match_name_text_view);
            mLastMessageTextView = (TextView) itemView.findViewById(R.id.last_message_text_view);
            mLastMessageTimeTextView = (TextView) itemView.findViewById(R.id.last_message_time_text_view);
            mMatchImage = (ImageView) itemView.findViewById(R.id.conversation_profile_image);
        }

        public void bind(final Conversation conversation, final String userId, final IConversationItemClickListener listener){
            final ConversationSettings conversationSettings = createConversationSettings(userId, conversation);
            Message lastMessage = conversation.getLastMessage();
            mMatchNameTextView.setText(conversationSettings.getMatchName());
            mLastMessageTextView.setText(lastMessage.getText());
            mLastMessageTimeTextView.setText(DateTimeHelpers.getTimeFromMilliseconds(lastMessage.getTime()));
            mConversationItemTextArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onConversationItemTextAreaClicked(conversationSettings);
                }
            });
            mMatchImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onConversationItemMatchImageClicked(conversationSettings.getMatchId());
                }
            });

            StorageHelper.loadProfileImageFromStorageIntoImageView(mContext, conversationSettings.getMatchId(), mMatchImage);
        }

        private String getMatchUserId(Conversation conversation, String userId){
            return !conversation.getReceiverUserId().equals(userId) ?
                conversation.getReceiverUserId() :
                conversation.getSenderUserId();
        }

        private String getMatchName(Conversation conversation, String userId){
            return !conversation.getReceiverUserId().equals(userId) ?
                    conversation.getReceiverName() :
                    conversation.getSenderName();
        }

        private String getUserName(Conversation conversation, String userId){
            return conversation.getReceiverUserId().equals(userId) ?
                    conversation.getReceiverName() :
                    conversation.getSenderName();
        }

        private ConversationSettings createConversationSettings(String userId, Conversation conversation){
            String matchUserId = getMatchUserId(conversation, userId);
            String matchName = getMatchName(conversation, userId);
            String userName = getUserName(conversation, userId);

            return new ConversationSettings(
                    conversation.getConversationId(),
                    userId,
                    userName,
                    matchUserId,
                    matchName
            );
        }
    }
}
