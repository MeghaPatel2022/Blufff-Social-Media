package com.TBI.Client.Bluff.ExceptionHandler;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.TBI.Client.Bluff.Activity.Home.OpenCamera1;

import java.util.Arrays;

public class DefaultExceptionHandler implements Thread.UncaughtExceptionHandler {

    Activity activity;

    public DefaultExceptionHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {

        Intent intent = new Intent(activity, OpenCamera1.class);
        Log.d("ERROR", "---------" + ex.getMessage());
        Log.d("ERROR", "--------" + ex.getCause());
        Log.d("ERROR", "--------" + Arrays.toString(ex.getStackTrace()));
        activity.startActivity(intent);
        activity.finish();

        System.exit(0);
    }

}