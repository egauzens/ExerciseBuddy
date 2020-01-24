package com.bignerdranch.android.exercisebuddy;

import android.content.Context;

public class UserSelections {
    public static class UserInformation{
        public static String[] exercises(Context context){
            return new String[] {context.getString(R.string.biking),
                    context.getString(R.string.running),
                    context.getString(R.string.rock_climbing),
                    context.getString(R.string.swimming),
                    context.getString(R.string.weight_lifting),
                    context.getString(R.string.hiking)};
        }
        public static String[] userGenders(Context context){
            return new String[] {context.getString(R.string.male),
                    context.getString(R.string.female)};
        }
        public static String[] getUserExperienceLevels(Context context){
            return new String[] {context.getString(R.string.beginner),
                    context.getString(R.string.intermediate),
                    context.getString(R.string.advanced)};
        }
    }

    public static class UserPreferences{
        public static String[] genderPreferences(Context context){
            return new String[] {context.getString(R.string.no_preference),
                    context.getString(R.string.male),
                    context.getString(R.string.female)};
        }
        public static String[] getExperienceLevelPreferences(Context context){
            return new String[] {context.getString(R.string.no_preference),
                    context.getString(R.string.beginner),
                    context.getString(R.string.intermediate),
                    context.getString(R.string.advanced)};
        }
    }
}
