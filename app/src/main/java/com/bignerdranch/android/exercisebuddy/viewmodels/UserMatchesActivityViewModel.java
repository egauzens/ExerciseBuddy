package com.bignerdranch.android.exercisebuddy.viewmodels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModel;

import com.bignerdranch.android.exercisebuddy.models.User;
import com.bignerdranch.android.exercisebuddy.staticHelpers.ModelCreationHelpers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserMatchesActivityViewModel extends ViewModel {
    public final ObservableArrayList<User> mUserMatches;
    private String mCurrentUserId;
    private User mCurrentUser;
    private Query mUsersDBQuery;
    private ChildEventListener mUsersListener;

    public UserMatchesActivityViewModel(){
        mUserMatches = new ObservableArrayList<>();
        mCurrentUserId = "";
    }

    public void setCurrentUserId(String userId){
        if (!mCurrentUserId.isEmpty()) {
            return;
        }
        mCurrentUserId = userId;
        addListenerForUserSettings();
    }

    public String getCurrentUserId() {
        return mCurrentUserId;
    }

    public ObservableArrayList<User> getUserMatches() {
        return mUserMatches;
    }

    private void addListenerForUserSettings(){
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("users").child(getCurrentUserId());

        userDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    User oldUser = mCurrentUser;
                    mCurrentUser = ModelCreationHelpers.createUser(dataSnapshot);
                    if (oldUser == null || !oldUser.getExercise().equals(mCurrentUser.getExercise())) {
                        if (oldUser == null){
                            // we know that this is the first time the user info is being retrieved
                            addListenerForUserConversations();
                        }
                        updateQuery();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void addListenerForUserConversations(){
        final DatabaseReference conversationIdsDb = FirebaseDatabase.getInstance().getReference().child("users").child(getCurrentUserId()).child("conversationIds");
        conversationIdsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    // set a dummy value in case the user has no conversations yet (Firebase does not allow empty directories)
                    conversationIdsDb.setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        conversationIdsDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mCurrentUser.addConversationId(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateQuery(){
        resetMatches();
        mUsersDBQuery = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("exercise").equalTo(mCurrentUser.getExercise());
        updateDisplayedUsers();
    }

    private void resetMatches(){
        if (!mUserMatches.isEmpty()){
            mUserMatches.clear();
        }
        if (mUsersDBQuery != null && mUsersListener != null){
            mUsersDBQuery.removeEventListener(mUsersListener);
        }
    }

    private void updateDisplayedUsers(){
        mUsersListener = mUsersDBQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (dataSnapshot.exists()) {
                    if (!dataSnapshot.getKey().equals(currentUser.getUid())) {
                        User addedUser = ModelCreationHelpers.createUser(dataSnapshot);
                        if (mCurrentUser.doesMatchWith(addedUser) && addedUser.doesMatchWith(mCurrentUser)){
                            mUserMatches.add(addedUser);
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (dataSnapshot.exists()) {
                    if (!dataSnapshot.getKey().equals(currentUser.getUid())) {
                        User changedUser = ModelCreationHelpers.createUser(dataSnapshot);
                        for (User user : mUserMatches) {
                            if (user.getUid().equals(changedUser.getUid())){
                                if (!mCurrentUser.doesMatchWith(changedUser) || !changedUser.doesMatchWith(mCurrentUser))
                                    mUserMatches.remove(user);
                                return;
                            }
                        }
                        if (mCurrentUser.doesMatchWith(changedUser) && changedUser.doesMatchWith(mCurrentUser))
                            mUserMatches.add(changedUser);
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
                        User removedUser = ModelCreationHelpers.createUser(dataSnapshot);
                        for (User user : mUserMatches) {
                            if (user.getUid().equals(removedUser.getUid())){
                                mUserMatches.remove(user);
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
}
