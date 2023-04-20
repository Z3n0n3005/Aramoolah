package com.example.aramoolah.data.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.example.aramoolah.R;

public class Session {
    private final SharedPreferences prefs;

    public Session(Context context) {
        prefs = context.getSharedPreferences("com.example.Aramoolah.LOGIN_SESSION", Context.MODE_PRIVATE);
    }

    public void setUserId(int userId) {
        prefs.edit().putInt("userId", userId).apply();
    }

    public int getUserId() {
        return prefs.getInt("userId",-1);
    }
}
