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
    private User mCurrentUserSettings;
    private Query mUsersDBQuery;

    public UserGridActivityViewModel(){
        mUserMatches = new ObservableArrayList<>();

        updateUserSettings();
    }

    public User getCurrentUser() {
        return mCurrentUserSettings;
    }

    public ObservableArrayList<User> getUserMatches() {
        return mUserMatches;
    }

    private ChildEventListener mUsersListener;

    public void updateDatabase(User newUserSettings){
        DatabaseReference currentUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(newUserSettings.getUid());
        if (!newUserSettings.getExercise().equals(mCurrentUserSettings.getExercise()))
        {
            currentUserDatabase.child("exercise").setValue(newUserSettings.getExercise());
        }
        if (!newUserSettings.getGenderPreference().equals(mCurrentUserSettings.getGenderPreference()))
        {
            currentUserDatabase.child("genderPreference").setValue(newUserSettings.getGenderPreference());
        }
        if(!newUserSettings.getExperienceLevelPreference().equals(mCurrentUserSettings.getExperienceLevelPreference()))
        {
            currentUserDatabase.child("experienceLevelPreference").setValue(newUserSettings.getExperienceLevelPreference());
        }
        if(newUserSettings.getMaximumAgePreference() != mCurrentUserSettings.getMaximumAgePreference())
        {
            currentUserDatabase.child("upperAgePreference").setValue(newUserSettings.getMaximumAgePreference());
        }
        if(newUserSettings.getMinimumAgePreference() != mCurrentUserSettings.getMinimumAgePreference())
        {
            currentUserDatabase.child("lowerAgePreference").setValue(newUserSettings.getMinimumAgePreference());
        }
        if(newUserSettings.getProfileImageUri() != mCurrentUserSettings.getProfileImageUri())
        {
            currentUserDatabase.child("profileImageUri").setValue(newUserSettings.getProfileImageUri());
        }
        if(newUserSettings.getDob() != mCurrentUserSettings.getDob())
        {
            currentUserDatabase.child("dateOfBirth").setValue(newUserSettings.getDob());
        }
        if(newUserSettings.getGender() != mCurrentUserSettings.getGender())
        {
            currentUserDatabase.child("userGender").setValue(newUserSettings.getGender());
        }
        if(newUserSettings.getDescription() != mCurrentUserSettings.getDescription())
        {
            currentUserDatabase.child("userDescription").setValue(newUserSettings.getDescription());
        }
        if(newUserSettings.getExperienceLevel() != mCurrentUserSettings.getExperienceLevel())
        {
            currentUserDatabase.child("userExperienceLevel").setValue(newUserSettings.getExperienceLevel());
        }
        if(newUserSettings.getName() != mCurrentUserSettings.getName())
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

    private void updateUserSettings(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        userDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    User oldUserSettings = mCurrentUserSettings;
                    mCurrentUserSettings = createUser(dataSnapshot);
                    if (oldUserSettings == null || !oldUserSettings.getExercise().equals(mCurrentUserSettings.getExercise())) {
                        updateQuery();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateQuery(){
        resetMatches();
        mUsersDBQuery = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("exercise").equalTo(mCurrentUserSettings.getExercise());
        updateDisplayedUsers();
    }

    private void resetMatches(){
        if (mUsersDBQuery != null && mUsersListener != null){
            mUsersDBQuery.removeEventListener(mUsersListener);
        }
        if (!mUserMatches.isEmpty()){
            mUserMatches.clear();
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
                        if (mCurrentUserSettings.doesMatchWith(addedUser) && addedUser.doesMatchWith(mCurrentUserSettings)){
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
                                if (!mCurrentUserSettings.doesMatchWith(changedUser) || !changedUser.doesMatchWith(mCurrentUserSettings))
                                mUserMatches.remove(user);
                                return;
                            }
                        }
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
    }
}
