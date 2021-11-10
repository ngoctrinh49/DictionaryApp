package com.example.dictionary;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Global {

    //Hàm lưu trạng thái của từ điển
    public static void saveState(Activity activity, String key, String value) {
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //Hàm lấy ra trạng thái của từ điển
    public static String getState(Activity activity, String key) {
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }
}
