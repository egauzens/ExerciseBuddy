package com.bignerdranch.android.exercisebuddy.staticHelpers;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class UserPreferencesSettings implements Serializable {
    private String mExercisePreference;
    private String mGenderPreference;
    private int mMinimumAgePreference;
    private int mMaximumAgePreference;
    private String mExperienceLevelPreference;
    private int mDistancePreference;

    public UserPreferencesSettings(String exercise, String gender, int minAge, int maxAge, String experienceLevel){
        mExercisePreference = exercise;
        mGenderPreference = gender;
        mMinimumAgePreference = minAge;
        mMaximumAgePreference = maxAge;
        mExperienceLevelPreference = experienceLevel;
    }

    public String getExercisePreference() {
        return mExercisePreference;
    }

    public void setExercise(String exercise) {
        this.mExercisePreference = exercise;
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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!obj.getClass().equals(UserPreferencesSettings.class)){
            return false;
        }
        else{
            UserPreferencesSettings userPreferencesSettings = (UserPreferencesSettings) obj;
            return areSettingsEqual(userPreferencesSettings);
        }
    }

    private boolean areSettingsEqual(UserPreferencesSettings settings){
        if (!settings.getExercisePreference().equals(mExercisePreference)){
            return false;
        }
        if (!settings.getGenderPreference().equals(mGenderPreference)){
            return false;
        }
        if (settings.getExperienceLevelPreference() != mExperienceLevelPreference){
            return false;
        }
        if (settings.getMinimumAgePreference() != mMinimumAgePreference){
            return false;
        }
        if (settings.getMaximumAgePreference() != mMaximumAgePreference){
            return false;
        }

        return true;
    }
}
