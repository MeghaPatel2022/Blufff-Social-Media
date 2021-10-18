package com.TBI.Client.Bluff.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class sharedpreference {

    public static final String MyPREFERENCES = "Blufff";
    public static final String MyPREFERENCES1 = "Blufff_fix";
    public static String loction = "location";
    public static String longitude = "longitude";
    public static String latitude = "latitude";
    public static String firebasetoken = "firebasetoken";
    public static String customer_id = "c_id";
    public static String username = "username";
    public static String email = "Email";
    public static String logintype = "logintype";
    public static String bio = "bio";
    public static String rate = "rate";
    public static String firstname = "firstname";
    public static String dailcode = "dailcode";
    public static String phonenumber = "number";
    public static String photo = "photo";
    public static String coverPhoto = "coverPhoto";
    public static String pincode = "pincode";
    public static String socialtype = "socialtype";
    public static String searcharraylist = "searcharraylist";
    public static String searchproductarraylist = "searchproductarraylist";
    public static String preofession = "profession";
    public static String theme = "theme";
    public static String Agreement = "agreement";
    public static String INTRO = "intro";
    public static String UserID = "userID";
    public static String Flash = "Flash.ON";
    public static String verification_step = "VerifyStep";
    public static String USER_ID = "USER_ID";
    public static String TOKEN = "TOKEN";
    public static String login = "login";
    public static String IMAGEURL = "imgUrl";

    public static String getUserId(Context c1) {
        if (c1 != null) {
            SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            String ans = sharedpreferences.getString(USER_ID, "0");
            return ans;
        }
        return "";
    }

    public static void setUserId(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USER_ID, value);
        editor.apply();
    }

    public static String getImagUrl(Context c1) {
        if (c1 != null) {
            SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            String ans = sharedpreferences.getString(IMAGEURL, "");
            return ans;
        }
        return "";
    }

    public static void setImagUrl(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(IMAGEURL, value);
        editor.apply();
    }

    public static String getUserToken(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(TOKEN, "Bearer " + "");
        return ans;
    }

    public static void setUserToken(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(TOKEN, value);
        editor.apply();
    }

    public static int getVeryStep(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int ans = sharedpreferences.getInt(verification_step, 0);
        return ans;
    }

    public static void setVeryStep(Context c1, int value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(verification_step, value);
        editor.apply();
    }

    public static String getTheme(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(theme, "");
        return ans;
    }

    public static void setTheme(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(theme, value);
        editor.apply();
    }

    public static String getPincode(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(pincode, "");
        return ans;
    }

    public static void setPincode(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(pincode, value);
        editor.apply();
    }

    public static String getPreofession(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(preofession, "");
        return ans;
    }

    public static void setPreofession(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(preofession, value);
        editor.apply();
    }

    public static String getEmail(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(email, "");
        return ans;
    }

    public static void setEmail(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(email, value);
        editor.apply();
    }

    public static String getDailcode(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(dailcode, "");
        return ans;
    }

    public static void setDailcode(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(dailcode, value);
        editor.apply();
    }

    public static String getLongitude(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(longitude, "");
        return ans;
    }

    public static void setLongitude(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(longitude, value);
        editor.apply();
    }

    public static String getLatitude(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(latitude, "");
        return ans;
    }

    public static void setLatitude(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(latitude, value);
        editor.apply();
    }


    public static String getSocialtype(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(socialtype, "");
        return ans;
    }

    public static void setSocialtype(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(socialtype, value);
        editor.apply();
    }

    public static String getAccountType(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(socialtype, "");
        return ans;
    }

    public static void setAccountType(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(socialtype, value);
        editor.apply();
    }

    public static String getNumber(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(phonenumber, "");
        return ans;
    }

    public static void setnumber(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(phonenumber, value);
        editor.apply();
    }

    public static String getphoto(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(photo, "");
        return ans;
    }

    public static void setphoto(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(photo, value);
        editor.apply();
    }

    public static String getCoverPhoto(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(coverPhoto, "");
        return ans;
    }

    public static void setCoverPhoto(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(coverPhoto, value);
        editor.apply();
    }


    public static String getfirebasetoken(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(firebasetoken, "");
        return ans;
    }

    public static void setfirebasetoken(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(firebasetoken, value);
        editor.apply();
    }

    public static String getLoction(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(loction, "");
        return ans;
    }

    public static void setLoction(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(loction, value);
        editor.apply();
    }

    public static String getfirstname(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(firstname, "");
        return ans;
    }

    public static void setfirstname(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(firstname, value);
        editor.apply();
    }

    public static String getBio(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(bio, "");
        return ans;
    }

    public static void setBio(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(bio, value);
        editor.apply();
    }

    public static String getRate(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(rate, "");
        return ans;
    }

    public static void setRate(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(rate, value);
        editor.apply();
    }

    public static String getFlash(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(Flash, "Flash.ON");
        return ans;
    }

    public static void setFlash(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Flash, value);
        editor.apply();
    }

    public static void clearAll(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void clearValue(Context c1, String name) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(name);
        editor.apply();
    }

    public static String getusername(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(username, "");
        return ans;
    }

    public static void setusername(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(username, value);
        editor.apply();
    }

    public static String getlogintype(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(logintype, "");
        return ans;
    }

    public static void setlogintype(Context c1, String value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(logintype, value);
        editor.apply();
    }


    public static void clearall(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void clear(String name, Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(name);
        editor.commit();
    }

    public static Boolean getInro(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
        Boolean ans = sharedpreferences.getBoolean(INTRO, false);
        return ans;
    }

    public static void setInro(Context c1, Boolean value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(INTRO, value);
        editor.apply();
    }

    public static boolean getlogin(Context c1) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        boolean ans = sharedpreferences.getBoolean(login, false);
        return ans;
    }

    public static void setlogin(Context c1, boolean value) {
        SharedPreferences sharedpreferences = c1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(login, value);
        editor.apply();
    }

}


