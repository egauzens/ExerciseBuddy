package com.bignerdranch.android.exercisebuddy;

public class Preferences {
    private Gender gender;
    private int minimumAge;
    private int maximumAge;
    private int minimumExperienceLevel;
    private int maximumExperienceLevel;
    private int distance;

    public Preferences(){

    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(int minimumAge) {
        this.minimumAge = minimumAge;
    }

    public int getMaximumAge() {
        return maximumAge;
    }

    public void setMaximumAge(int maximumAge) {
        this.maximumAge = maximumAge;
    }

    public int getMinimumExperienceLevel() {
        return minimumExperienceLevel;
    }

    public void setMinimumExperienceLevel(int minimumExperienceLevel) {
        this.minimumExperienceLevel = minimumExperienceLevel;
    }

    public int getMaximumExperienceLevel() {
        return maximumExperienceLevel;
    }

    public void setMaximumExperienceLevel(int maximumExperienceLevel) {
        this.maximumExperienceLevel = maximumExperienceLevel;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
