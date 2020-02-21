package com.bignerdranch.android.exercisebuddy.staticHelpers;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Calendar;

public class UserProfileSettings implements Serializable {
    private String mUid;
    private String mName;
    private String mGender;
    private String mDob;
    private String mExperienceLevel;
    private String mProfileImageUri;
    private String mDescription;

    public UserProfileSettings(
            String name,
            String gender,
            String dob,
            String experienceLevel,
            String profileImageUri,
            String description,
            String uid) {
        mName = name;
        mGender = gender;
        mDob = dob;
        mExperienceLevel = experienceLevel;
        mProfileImageUri = profileImageUri;
        mDescription = description;
        mUid = uid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDob() {
        return mDob;
    }

    public void setDob(String dob) {
        this.mDob = dob;
    }

    public void setDob(int year, int month, int day){
        Calendar birthdayCalendar = Calendar.getInstance();
        birthdayCalendar.set(year, month, day);
        setDob(convertCalendarToString(birthdayCalendar));
    }

    private String convertCalendarToString(Calendar calendar){

        return String.valueOf(calendar.get(Calendar.MONTH)) +
                "-" + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) +
                "-" + String.valueOf(calendar.get(Calendar.YEAR));
    }

    public int getBirthDay(){
        String[] date = mDob.split("-");
        return Integer.parseInt(date[1]);
    }

    public int getBirthMonth(){
        String[] date = mDob.split("-");
        return Integer.parseInt(date[0]);
    }

    public int getBirthYear(){
        String[] date = mDob.split("-");
        return Integer.parseInt(date[2]);
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        this.mGender = gender;
    }

    public String getExperienceLevel() {
        return mExperienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.mExperienceLevel = experienceLevel;
    }

    public String getProfileImageUri() {
        return mProfileImageUri;
    }

    public void setProfileImageUri(String profileImageUri) {
        this.mProfileImageUri = profileImageUri;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getUid(){
        return mUid;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!obj.getClass().equals(UserProfileSettings.class)){
            return false;
        }
        else{
            UserProfileSettings userProfileSettings = (UserProfileSettings) obj;
            return areSettingsEqual(userProfileSettings);
        }
    }

    private boolean areSettingsEqual(UserProfileSettings settings){
        if (!settings.getDob().equals(mDob)){
            return false;
        }
        if (!settings.getGender().equals(mGender)){
            return false;
        }
        if (settings.getExperienceLevel() != mExperienceLevel){
            return false;
        }
        if (!settings.getName().equals(mName)){
            return false;
        }
        if (!settings.getDescription().equals(mDescription)){
            return false;
        }
        if (!settings.getProfileImageUri().equals(mProfileImageUri)){
            return false;
        }

        return true;
    }
}
