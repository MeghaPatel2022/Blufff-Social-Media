package net.iquesoft.iquephoto.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefrence {

    public static String theme = "theme";
    public static final String MyPREFERENCES = "BlufffProject";

    public static String getTheme(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(theme, "white");
        return ans;
    }

    public static void setTheme(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(theme, value);
        editor.apply();
    }

}
