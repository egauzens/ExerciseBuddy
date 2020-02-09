package com.bignerdranch.android.exercisebuddy.models;

import java.io.Serializable;
import java.util.Calendar;

public class User implements Serializable {
    private String mName;
    private String mGender;
    private String mDob;
    private String mExperienceLevel;
    private String mExercise;
    private String mProfileImageUri;
    private String mDescription;
    private String mGenderPreference;
    private int mMinimumAgePreference;
    private int mMaximumAgePreference;
    private String mExperienceLevelPreference;
    private int mDistancePreference;
    private int mAge;
    private String mUid;

    public User(String name,
                String gender,
                String dob,
                String experienceLevel,
                String exercise,
                String profileImageUri,
                String description,
                String genderPreference,
                int minAgePreference,
                int maxAgePreference,
                String experienceLevelPreference,
                String uid) {
        mName = name;
        mGender = gender;
        mDob = dob;
        mExperienceLevel = experienceLevel;
        mExercise = exercise;
        mProfileImageUri = profileImageUri;
        mDescription = description;
        mGenderPreference = genderPreference;
        mMinimumAgePreference = minAgePreference;
        mMaximumAgePreference = maxAgePreference;
        mExperienceLevelPreference = experienceLevelPreference;
        mUid = uid;
        mAge = getAgeFromDob(dob);
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
        mAge = getAgeFromDob(dob);
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

    public String getExercise() {
        return mExercise;
    }

    public void setExercise(String exercise) {
        this.mExercise = exercise;
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

    public String getGenderPreference() {
        return mGenderPreference;
    }

    public void setGenderPreference(String gender) {
        this.mGenderPreference = gender;
    }

    public int getMinimumAgePreference() {
        return mMinimumAgePreference;
    }

    public void setMinimumAgePreference(int minimumAgePreference) {
        this.mMinimumAgePreference = minimumAgePreference;
    }

    public int getMaximumAgePreference() {
        return mMaximumAgePreference;
    }

    public void setMaximumAgePreference(int maximumAgePreference) {
        this.mMaximumAgePreference = maximumAgePreference;
    }

    public String getExperienceLevelPreference() {
        return mExperienceLevelPreference;
    }

    public void setExperienceLevelPreference(String experienceLevelPreference) {
        this.mExperienceLevelPreference = experienceLevelPreference;
    }

    public int getDistancePreference() {
        return mDistancePreference;
    }

    public void setDistancePreference(int distancePreference) {
        this.mDistancePreference = distancePreference;
    }

    public String getUid(){
        return mUid;
    }

    public int getAge() {
        return mAge;
    }

    private int getAgeFromDob(String dob){
        Calendar currentDate = Calendar.getInstance();
        String[] birthDate = dob.split("-");
        int birthMonth = Integer.parseInt(birthDate[0]);
        int birthDay = Integer.parseInt(birthDate[1]);
        int birthYear = Integer.parseInt(birthDate[2]);

        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - birthYear;
        if (currentMonth < birthMonth) {
            age--;
        } else if (currentMonth == birthMonth) {
            if (currentDay > birthDay) {
                age--;
            }
        }

        return age;
    }

    public boolean doesMatchWith(User user){
        if (!user.getExercise().equals(mExercise)){
            return false;
        }
        String genderPreference = user.getGenderPreference();
        if (!genderPreference.equals("No preference") && !genderPreference.equals(mGender)){
            return false;
        }
        int minAge = user.getMinimumAgePreference();
        int maxAge = user.getMaximumAgePreference();
        if (minAge > mAge || maxAge < mAge){
            return false;
        }
        String experienceLevelPreference = user.getExperienceLevelPreference();
        if (!experienceLevelPreference.equals("No preference") && !experienceLevelPreference.equals(mExperienceLevel)){
            return false;
        }
        return true;
    }

    public boolean arePreferencesEqual(User user){
        if (!user.getExercise().equals(mExercise)){
            return false;
        }
        if (!user.getGenderPreference().equals(mGenderPreference)){
            return false;
        }
        if (!user.getExperienceLevelPreference().equals(mExperienceLevelPreference)){
            return false;
        }
        if (user.getMinimumAgePreference() != mMinimumAgePreference){
            return false;
        }
        if (user.getMaximumAgePreference() != mMaximumAgePreference){
            return false;
        }

        return true;
    }

    public boolean areProfilesEqual(User user){
        if (!user.getDob().equals(mDob)){
            return false;
        }
        if (!user.getGender().equals(mGender)){
            return false;
        }
        if (user.getExperienceLevel() != mExperienceLevel){
            return false;
        }
        if (!user.getName().equals(mName)){
            return false;
        }
        if (!user.getDescription().equals(mDescription)){
            return false;
        }
        if (!user.getProfileImageUri().equals(mProfileImageUri)){
            return false;
        }

        return true;
    }
}
