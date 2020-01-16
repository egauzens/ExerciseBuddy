package com.bignerdranch.android.exercisebuddy;

public class UserGridItem {
    private String mUserImageUrl;
    private String mName;
    private int mAge;

    public UserGridItem(String userImageUrl, String name, int age){
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
}
