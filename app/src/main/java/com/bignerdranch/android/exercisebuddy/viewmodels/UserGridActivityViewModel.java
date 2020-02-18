package com.bignerdranch.android.exercisebuddy.viewmodels;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UserGridActivityViewModel extends ViewModel {
    public final ObservableArrayList<User> mUserMatches;
    private User mCurrentUser;
    private Query mUsersDBQuery;
    private ChildEventListener mUsersListener;

    public UserGridActivityViewModel(){
        mUserMatches = new ObservableArrayList<>();

        addListenerForUserSettings();
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public ObservableArrayList<User> getUserMatches() {
        return mUserMatches;
    }

    public void updateDatabase(User newUserSettings){
        DatabaseReference currentUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(newUserSettings.getUid());
        if (!newUserSettings.getExercise().equals(mCurrentUser.getExercise()))
        {
            currentUserDatabase.child("exercise").setValue(newUserSettings.getExercise());
        }
        if (!newUserSettings.getGenderPreference().equals(mCurrentUser.getGenderPreference()))
        {
            currentUserDatabase.child("genderPreference").setValue(newUserSettings.getGenderPreference());
        }
        if(!newUserSettings.getExperienceLevelPreference().equals(mCurrentUser.getExperienceLevelPreference()))
        {
            currentUserDatabase.child("experienceLevelPreference").setValue(newUserSettings.getExperienceLevelPreference());
        }
        if(newUserSettings.getMaximumAgePreference() != mCurrentUser.getMaximumAgePreference())
        {
            currentUserDatabase.child("upperAgePreference").setValue(newUserSettings.getMaximumAgePreference());
        }
        if(newUserSettings.getMinimumAgePreference() != mCurrentUser.getMinimumAgePreference())
        {
            currentUserDatabase.child("lowerAgePreference").setValue(newUserSettings.getMinimumAgePreference());
        }
        if(newUserSettings.getProfileImageUri() != mCurrentUser.getProfileImageUri())
        {
            currentUserDatabase.child("profileImageUri").setValue(newUserSettings.getProfileImageUri());
        }
        if(newUserSettings.getDob() != mCurrentUser.getDob())
        {
            currentUserDatabase.child("dateOfBirth").setValue(newUserSettings.getDob());
        }
        if(newUserSettings.getGender() != mCurrentUser.getGender())
        {
            currentUserDatabase.child("userGender").setValue(newUserSettings.getGender());
        }
        if(newUserSettings.getDescription() != mCurrentUser.getDescription())
        {
            currentUserDatabase.child("userDescription").setValue(newUserSettings.getDescription());
        }
        if(newUserSettings.getExperienceLevel() != mCurrentUser.getExperienceLevel())
        {
            currentUserDatabase.child("userExperienceLevel").setValue(newUserSettings.getExperienceLevel());
        }
        if(newUserSettings.getName() != mCurrentUser.getName())
        {
            currentUserDatabase.child("name").setValue(newUserSettings.getName());
        }
    }

    public boolean loadProfileImageIntoStorage(User user, ContentResolver resolver){
        String imageUrl = user.getProfileImageUri();
        if (imageUrl.isEmpty()) {
            return false;
        }
        else {
            Uri imageUri = Uri.parse(imageUrl);
            Bitmap bitmap = null;
            String userId = user.getUid();
            final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.Source source = ImageDecoder.createSource(resolver, imageUri);
                try {
                    bitmap = ImageDecoder.decodeBitmap(source);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(resolver, imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);
            byte[] data = outputStream.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);
            return uploadTask.isSuccessful();
        }
    }

    private void addListenerForUserSettings(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        userDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    User oldUser = mCurrentUser;
                    mCurrentUser = createUser(dataSnapshot);
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference conversationIdsDb = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("conversationIds");
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

    /*private void populateUserWithConversations(User user, DataSnapshot dataSnapshot){
        for (DataSnapshot child : dataSnapshot.getChildren())
        {
            final String conversationId = child.getKey();
            DatabaseReference conversationDb = FirebaseDatabase.getInstance().getReference().child("conversations");
            conversationDb.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String conversationId = dataSnapshot.getKey();
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        Message firstMessage = createMessage(child);
                        Conversation conversation = new Conversation(firstMessage, conversationId);
                        mCurrentUser.addConversation(conversation);
                        break;
                    }
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
    }*/

    private Message createMessage(DataSnapshot dataSnapshot){
        String messageId = dataSnapshot.getKey();
        String senderUserId = dataSnapshot.child("senderUserId").getValue(String.class);
        String receiverUserId = dataSnapshot.child("receiverUserId").getValue(String.class);
        String content = dataSnapshot.child("content").getValue(String.class);
        long time = (long)dataSnapshot.child("time").getValue(Long.class);

        return new Message(content, senderUserId, receiverUserId, time, messageId);
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
                        User addedUser = createUser(dataSnapshot);
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
                        User changedUser = createUser(dataSnapshot);
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
                        User removedUser = createUser(dataSnapshot);
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
        User user = new User(userName,
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

        for (DataSnapshot child : dataSnapshot.child("conversationIds").getChildren()){
            user.addConversationId(child.getKey());
        }

        return user;
    }
}
