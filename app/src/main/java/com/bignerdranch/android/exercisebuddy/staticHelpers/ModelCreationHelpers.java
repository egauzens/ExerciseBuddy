package com.bignerdranch.android.exercisebuddy.staticHelpers;

import com.bignerdranch.android.exercisebuddy.models.User;
import com.google.firebase.database.DataSnapshot;

public class ModelCreationHelpers {
    public static User createUser(DataSnapshot dataSnapshot){
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
