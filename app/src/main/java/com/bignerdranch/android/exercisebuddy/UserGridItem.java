package com.bignerdranch.android.exercisebuddy;

public class UserGridItem {
    private String mGender;
    private String mUserImageUrl;
    private String mName;
    private int mAge;

    public UserGridItem(String gender, String userImageUrl, String name, int age){
        mGender = gender;
        mUserImageUrl = userImageUrl;
        mName = name;
        mAge = age;
    }

    public String getUserImageUrl() {
        return mUserImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.mUserImageUrl = userImageUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int age) {
        this.mAge = age;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        this.mGender = gender;
    }
}
