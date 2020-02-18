package com.bignerdranch.android.exercisebuddy.viewmodels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModel;

import com.bignerdranch.android.exercisebuddy.models.Message;
import com.bignerdranch.android.exercisebuddy.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MessagingActivityViewModel extends ViewModel {
    /*public final ObservableArrayList<Message> mMessages;
    private User mCurrentUser;
    private User mMatchedUser;
    private Query mMessagesDbQuery;
    private ChildEventListener mUsersListener;

    public MessagingActivityViewModel(){
        mMessages = new ObservableArrayList<>();

        updateMessages();
    }

    public ObservableArrayList<Message> getMessages() {
        return mMessages;
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.mCurrentUser = currentUser;
    }

    public User getMatchedUser() {
        return mMatchedUser;
    }

    public void setMatchedUser(User matchedUser) {
        this.mMatchedUser = matchedUser;
    }

    private void updateMessages(String convoId){
        DatabaseReference messagesDb = FirebaseDatabase.getInstance().getReference().child("messages").child(convoId);

        messagesDb.addChildEventListener(new ValueEventListener() {}
    }

    private void updateQuery(){
        resetMatches();
        mMessagesDbQuery = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("exercise").equalTo(mCurrentUser.getExercise());
        updateDisplayedUsers();
    }

    private void resetMatches(){
        if (!mMessages.isEmpty()){
            mMessages.clear();
        }
        if (mMessagesDbQuery != null && mUsersListener != null){
            mMessagesDbQuery.removeEventListener(mUsersListener);
        }
    }

    private void updateDisplayedUsers(){
        mUsersListener = mMessagesDbQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (dataSnapshot.exists()) {
                    if (!dataSnapshot.getKey().equals(currentUser.getUid())) {
                        User addedUser = createUser(dataSnapshot);
                        if (mCurrentUser.doesMatchWith(addedUser) && addedUser.doesMatchWith(mCurrentUser)){
                            mMessages.add(addedUser);
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (dataSnapshot.exists()) {
                    if (!dataSnapshot.getKey().equals(currentUser.getUid())) {
                        User changedUser = createUser(dataSnapshot);
                        for (User user : mMessages) {
                            if (user.getUid().equals(changedUser.getUid())){
                                if (!mCurrentUser.doesMatchWith(changedUser) || !changedUser.doesMatchWith(mCurrentUser))
                                    mMessages.remove(user);
                                return;
                            }
                        }
                        if (mCurrentUser.doesMatchWith(changedUser) && changedUser.doesMatchWith(mCurrentUser))
                            mMessages.add(changedUser);
                    }
                    else {
                        resetMatches();
                        updateDisplayedUsers();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (dataSnapshot.exists()) {
                    if (!dataSnapshot.getKey().equals(currentUser.getUid())) {
                        User removedUser = createUser(dataSnapshot);
                        for (User user : mMessages) {
                            if (user.getUid().equals(removedUser.getUid())){
                                mMessages.remove(user);
                                break;
                            }
                        }
                    }
                    else{
                        updateQuery();
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private User createUser(DataSnapshot dataSnapshot){
        String userName = dataSnapshot.child("name").getValue().toString();
        String exercisePreference = dataSnapshot.child("exercise").getValue().toString();
        String userGender = dataSnapshot.child("userGender").getValue().toString();
        String userDob = dataSnapshot.child("dateOfBirth").getValue().toString();
        String userExperienceLevel = dataSnapshot.child("userExperienceLevel").getValue().toString();
        String userProfileImageUri = dataSnapshot.child("profileImageUri").getValue().toString();
        String userDescription = dataSnapshot.child("userDescription").getValue().toString();
        String genderPreference = dataSnapshot.child("genderPreference").getValue().toString();
        int lowerAgePreference = Integer.parseInt(dataSnapshot.child("lowerAgePreference").getValue().toString());
        int upperAgePreference = Integer.parseInt(dataSnapshot.child("upperAgePreference").getValue().toString());
        String experienceLevelPreference = dataSnapshot.child("experienceLevelPreference").getValue().toString();
        return new User(userName,
                userGender,
                userDob,
                userExperienceLevel,
                exercisePreference,
                userProfileImageUri,
                userDescription,
                genderPreference,
                lowerAgePreference,
                upperAgePreference,
                experienceLevelPreference,
                dataSnapshot.getKey());
    }*/
}
