package com.bignerdranch.android.exercisebuddy.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.helpers.DateTimeHelpers;
import com.bignerdranch.android.exercisebuddy.models.Message;

import java.util.List;

public class MessageItemAdapter extends RecyclerView.Adapter {
    private static final int MESSAGE_SENT = 1;
    private static final int MESSAGE_RECEIVED = 2;
    private List<Message> mUserMessages;
    private String mUserId;

    public MessageItemAdapter(List<Message> userMessages, String userId){
        mUserMessages = userMessages;
        mUserId = userId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if (viewType == MESSAGE_SENT){
            return new MessageItemAdapter.SentMessageItemHolder(layoutInflater, parent);
        } else if (viewType == MESSAGE_RECEIVED){
            return new MessageItemAdapter.ReceivedMessageItemHolder(layoutInflater, parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = mUserMessages.get(position);
        switch (holder.getItemViewType()) {
            case MESSAGE_SENT:
                ((SentMessageItemHolder) holder).bind(message);
                break;
            case MESSAGE_RECEIVED:
                ((ReceivedMessageItemHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mUserMessages.get(position);
        if (message.getSenderId().equals(mUserId)){
            return MESSAGE_SENT;
        }
        else{
            return MESSAGE_RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        return mUserMessages.size();
    }

    class SentMessageItemHolder extends RecyclerView.ViewHolder{
        private TextView mMessageContentTextView;
        private TextView mMessageTimeTextView;

        public SentMessageItemHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.item_message_sent, parent, false));

            mMessageContentTextView = (TextView) itemView.findViewById(R.id.message_content_text_view);
            mMessageTimeTextView = (TextView) itemView.findViewById(R.id.message_time_text_view);
        }

        public void bind(final Message message){
            mMessageContentTextView.setText(message.getText());
            mMessageTimeTextView.setText(DateTimeHelpers.getTimeFromMilliseconds(message.getTime()));
        }
    }

    class ReceivedMessageItemHolder extends RecyclerView.ViewHolder{
        private TextView mMessageContentTextView;
        private TextView mMessageTimeTextView;

        public ReceivedMessageItemHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.item_message_received, parent, false));
            mMessageContentTextView = (TextView) itemView.findViewById(R.id.message_content_text_view);
            mMessageTimeTextView = (TextView) itemView.findViewById(R.id.message_time_text_view);
        }

        public void bind(final Message message){
            mMessageContentTextView.setText(message.getText());
            mMessageTimeTextView.setText(DateTimeHelpers.getTimeFromMilliseconds(message.getTime()));
        }
    }
}
