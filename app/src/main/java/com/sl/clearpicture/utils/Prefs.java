package com.sl.clearpicture.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    public static final String PREFS_NAME = "JudiAuthPrefsFile";

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;

    public Prefs(Context context) {
        this.mPrefs = context.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        this.prefsEditor = mPrefs.edit();
    }

    public String getAuth() {
        return mPrefs.getString("auth", null);
    }

    public void setAuth(String object) {
        prefsEditor.putString("auth", object);
        prefsEditor.commit();
    }
}