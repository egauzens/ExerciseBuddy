package com.bignerdranch.android.exercisebuddy.adapters;

import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.models.Conversation;
import com.bignerdranch.android.exercisebuddy.models.Message;

import java.util.List;

public class MessageItemAdapter extends RecyclerView.Adapter<MessageItemAdapter.MessageItemHolder> {
    private List<Message> mUserMessages;
    private String mUserId;

    public MessageItemAdapter(List<Message> userMessages, String userId){
        mUserMessages = userMessages;
        mUserId = userId;
    }

    @NonNull
    @Override
    public MessageItemAdapter.MessageItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        return new MessageItemAdapter.MessageItemHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageItemAdapter.MessageItemHolder holder, int position) {
        Message message = mUserMessages.get(position);
        holder.bind(message, mUserId);
    }

    @Override
    public int getItemCount() {
        return mUserMessages.size();
    }

    class MessageItemHolder extends RecyclerView.ViewHolder{
        private LinearLayout mMessageLinearLayout;
        private TextView mMessageContentTextView;
        private TextView mMessageTimeTextView;
        private DisplayMetrics mDisplayMetrics;

        public MessageItemHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.item_message, parent, false));
            mDisplayMetrics = parent.getResources().getDisplayMetrics();
            mMessageLinearLayout = (LinearLayout) itemView.findViewById(R.id.message_linear_layout);
            mMessageContentTextView = (TextView) itemView.findViewById(R.id.message_content_text_view);
            mMessageTimeTextView = (TextView) itemView.findViewById(R.id.message_time_text_view);
        }

        public void bind(final Message message, final String userId){
            setMessageAppearance(message, userId);

            mMessageContentTextView.setText(message.getText());
            mMessageTimeTextView.setText(String.valueOf(message.getTime()));
        }

        private void setMessageAppearance(Message message, String userId){
            if (message.getSenderId().equals(userId)){
                RecyclerView.LayoutParams layoutParams =(RecyclerView.LayoutParams) mMessageLinearLayout.getLayoutParams();
                int desiredMarginStart = getPxInDp(50, mDisplayMetrics);
                layoutParams.setMarginStart(desiredMarginStart);
                mMessageLinearLayout.setLayoutParams(layoutParams);
                int green = Color.parseColor("#b5cf5e");
                mMessageLinearLayout.setBackgroundColor(green);
            }
            else{
                RecyclerView.LayoutParams layoutParams =(RecyclerView.LayoutParams) mMessageLinearLayout.getLayoutParams();
                int desiredMarginStart = getPxInDp(50, mDisplayMetrics);
                layoutParams.setMarginEnd(desiredMarginStart);
                mMessageLinearLayout.setLayoutParams(layoutParams);
                int grey = Color.parseColor("#dcdcdc");
                mMessageLinearLayout.setBackgroundColor(grey);
            }
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

        private int getPxInDp(int desiredDpValue, DisplayMetrics displayMetrics){
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, desiredDpValue, displayMetrics);
        }
    }
}
