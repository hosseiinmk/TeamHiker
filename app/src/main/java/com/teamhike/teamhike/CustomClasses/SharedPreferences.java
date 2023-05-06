package com.teamhike.teamhike.CustomClasses;

import android.app.Activity;
import android.content.Context;

public class SharedPreferences {
    public void setSharedPreferences(Activity activity, String tagName, String value) {
        android.content.SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString(tagName, value).apply();
    }

    public String getSharedPreferences(Activity activity, String tagName) {
        android.content.SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getString(tagName, "");
    }
}
