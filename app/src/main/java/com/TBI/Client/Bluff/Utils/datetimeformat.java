package com.TBI.Client.Bluff.Utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class datetimeformat {

    public static String getTwitterDate(String datetxt) {
        Date date;


        final String TWITTER = "dd/MM/yyyy hh:mm aa";
        Log.d("date", datetxt);
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER, Locale.ENGLISH);
        sf.setLenient(true);

        try {
            date = sf.parse(datetxt);

            long milliseconds = new Date().getTime() - date.getTime();
            Log.d("now", new Date().toString());
            Log.d("check date", date.toString());
            int minutes = (int) (int) Math.ceil((milliseconds / (1000 * 60)));
            int hours = (int) (int) Math.ceil((milliseconds / (1000 * 60 * 60)));
            Log.d("hours", hours + "");
            if (hours > 0) {
                if (hours == 1)
                    return "1 hour ago";
                else if (hours < 24)
                    return hours + " hours ago";
                else {
                    int days = (int) Math.ceil(hours / 24);
                    Log.d("days", days + "");
                    if (days == 1)
                        return "1 day ago";
                    else {
                        if (days >= 7) {
                            int weeks = (int) Math.ceil(days / 7);
                            if (weeks == 1) {
                                return weeks + " week ago";
                            }
                            return weeks + " weeks ago";
                        }
                        return days + " days ago";
                    }
                }
            } else {
                if (minutes <= 0)
                    return "Just Now";
                else if (minutes == 1)
                    return "1 minute ago";
                else
                    return minutes + " minutes ago";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static String gettime(String time) {
        String tym = time;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date dateObj = sdf.parse(time);

            tym = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(dateObj);
            tym = tym.replace("a.m.", "am");
            tym = tym.replace("p.m.", "pm");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tym;
    }

    public static String getdate(String date) {
        String dt = date;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateObj = sdf.parse(date);

            dt = new SimpleDateFormat("yyyy-MM-dd").format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    public static String getdate1(String date) {
        String dt = date;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date dateObj = sdf.parse(date);

            dt = new SimpleDateFormat("dd MMM yyyy").format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    public static String gettimehhmm(String time) {
        String tym = time;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            Date dateObj = sdf.parse(time);

            tym = new SimpleDateFormat("HH:mm").format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tym;
    }
}

