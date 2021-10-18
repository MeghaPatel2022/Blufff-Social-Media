package com.TBI.Client.Bluff.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.core.app.ActivityCompat;

import com.TBI.Client.Bluff.Model.GetFeed.NearByUser;
import com.TBI.Client.Bluff.Model.GetFeed.PersonYouKnow;
import com.TBI.Client.Bluff.Model.GetFeed.RequestedUser;
import com.TBI.Client.Bluff.Model.GetFriends.GetCloseFrend;
import com.TBI.Client.Bluff.Model.GetFriends.GetOtherFriends;
import com.TBI.Client.Bluff.Model.Like.LikeData;
import com.TBI.Client.Bluff.Model.View_Connection.Following;
import com.TBI.Client.Bluff.Model.getNewNotification.So;
import com.TBI.Client.Bluff.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import dmax.dialog.SpotsDialog;

public class Public_Function {

    public static ArrayList<GetCloseFrend> getCloseFrends = new ArrayList<>();
    public static ArrayList<GetCloseFrend> getCloseFrends1 = new ArrayList<>();
    public static ArrayList<GetOtherFriends> getOtherFriends = new ArrayList<>();
    public static List<Following> followingarray1 = new ArrayList<>();
    public static List<LikeData> likeDataArrayList = new ArrayList<>();

    public static ArrayList<Integer> shareUserList = new ArrayList<>();

    public static List<So> sosList = new ArrayList<>();

    public static List<PersonYouKnow> personYouKnows = new ArrayList<>();
    public static List<RequestedUser> requestedUsers = new ArrayList<>();
    public static List<NearByUser> nearByUsers = new ArrayList<>();

    public static String current_add = "";
    public static Double current_lat;
    public static Double current_long;
    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    static AlertDialog SpotsDialog;

    public static boolean isInternetConnected(Context mContext) {
        try {
            ConnectivityManager connect = (ConnectivityManager) mContext.getSystemService("connectivity");
            if (connect != null) {
                NetworkInfo resultMobile = connect.getNetworkInfo(0);
                NetworkInfo resultWifi = connect.getNetworkInfo(1);
                return (resultMobile != null && resultMobile.isConnectedOrConnecting()) || (resultWifi != null && resultWifi.isConnectedOrConnecting());
            }
        } catch (Exception e) {
            Log.e("LLLL_Debug: ", Objects.requireNonNull(e.getMessage()));
        }
        return false;
    }

    public static void Show_Progressdialog(Context context) {

        SpotsDialog = new SpotsDialog.Builder()
                .setContext(context)
                .setTheme(R.style.Custom)
                .setCancelable(true)
                .build();
        SpotsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        SpotsDialog.show();

    }

    public static void Hide_ProgressDialogue() {

        if (SpotsDialog.isShowing() && SpotsDialog!=null) {
            SpotsDialog.dismiss();
        }
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(View view, Activity activity) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static String getcurrentdatetime() {

        String formattedDate = "";
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formattedDate = df.format(c);
        return formattedDate;

    }

    public static String gettimezone() {
        String time_zone = "";
        time_zone = TimeZone.getDefault().getID();
        return time_zone;
    }

    public static int setdiplaywidth(Context activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) activity).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //if you need three fix imageview in width
        int devicewidth = displaymetrics.widthPixels / 2;

        return devicewidth;
    }

    public static Float convertheight(float oldheight, float newheight, Float x) {

        Float newx;
        newx = (newheight * x) / oldheight;
        return newx;

    }

    void setFirebaseToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
            }
        });
    }
}
