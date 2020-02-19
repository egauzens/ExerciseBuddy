package com.bignerdranch.android.exercisebuddy.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.interfaces.IConversationItemClickListener;
import com.bignerdranch.android.exercisebuddy.models.Conversation;
import com.bignerdranch.android.exercisebuddy.models.Message;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
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
        private TextView mMatchNameTextView;
        private TextView mLastMessageTextView;
        private TextView mLastMessageTimeTextView;
        private ImageView mMatchImage;
        private Context mContext;

        public ConversationItemHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.item_conversation, parent, false));
            mContext = parent.getContext();
            mMatchNameTextView = (TextView) itemView.findViewById(R.id.match_name_text_view);
            mLastMessageTextView = (TextView) itemView.findViewById(R.id.last_message_text_view);
            mLastMessageTimeTextView = (TextView) itemView.findViewById(R.id.last_message_time_text_view);
            mMatchImage = (ImageView) itemView.findViewById(R.id.conversation_profile_image);
        }

        public void bind(final Conversation conversation, final String userId, final IConversationItemClickListener listener){
            Message lastMessage = conversation.getLastMessage();

            mMatchNameTextView.setText(getMatchName(conversation, userId));
            mLastMessageTextView.setText(lastMessage.getText());
            mLastMessageTimeTextView.setText(String.valueOf(lastMessage.getTime()));
            mMatchImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onConversationItemClicked(conversation);
                }
            });

            loadProfileImage(getMatchUserId(conversation, userId));
        }

        private void loadProfileImage(String userId){
            final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(mContext).load(uri).into(mMatchImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    return;
                }
            });
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
    }
}
