package com.applozic.mobicomkit.uiwidgets.Pref;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    public static final String MyPREFERENCES = "Blufff_Mes";
    public static String BACKGROUND = "background";
    public static String BACKGROUND_COLOR = "background_color";

    public static String getWallpaper(Context c1) {
        if (c1!=null) {
            SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            String ans = sharedpreferences.getString(BACKGROUND, "");
            return ans;
        } return "";
    }

    public static void  setWallpaper(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(BACKGROUND, value);
        editor.apply();
    }
    public static int getWallpaperColor(Context c1) {
        if (c1!=null) {
            SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            int ans = sharedpreferences.getInt(BACKGROUND_COLOR, 0);
            return ans;
        } return 0;
    }

    public static void  setWallpaperColor(Context c1, int value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(BACKGROUND_COLOR, value);
        editor.apply();
    }

}
