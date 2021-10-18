package com.TBI.Client.Bluff.Activity.Mains;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.TBI.Client.Bluff.Activity.Home.OpenCamera1;
import com.TBI.Client.Bluff.Activity.Profile.ProfilePage;
import com.TBI.Client.Bluff.Adapter.WallPage.Feed_Adapter;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.Fragment.Catch_Fragment;
import com.TBI.Client.Bluff.Fragment.Look_Fragment;
import com.TBI.Client.Bluff.Fragment.Map_Fragment;
import com.TBI.Client.Bluff.Fragment.Talk_Fragment;
import com.TBI.Client.Bluff.Fragment.Wall_Fragment;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.UserPages.WelcomeActivity;
import com.TBI.Client.Bluff.Utils.GpsTracker;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottombarActivity extends AppCompatActivity implements Talk_Fragment.OnFragmentInteractionListener {

    public static boolean showcamera;
    public static String camera_bottom = "";
    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    /* @BindView(RM.id.expandable_bottom_bar)
     ExpandableBottomBar expandableBottomBar;*/
//    @BindView(RM.id.imgmap)
//    ImageView imgmap;
    @BindView(R.id.imgwall)
    ImageView imgwall;
    @BindView(R.id.imgcamera)
    ImageView imgcamera;
    @BindView(R.id.imgchat)
    ImageView imgchat;
    @BindView(R.id.imgMyLocation)
    ImageView imgMyLocation;
    @BindView(R.id.imgProfile)
    ImageView imgProfile;
    Fragment fragment1, fragment4;
    FragmentManager fm;
    Fragment active;
    Wall_Fragment fragment2;
    Feed_Adapter demo_adapter;
    List<String> abc = new ArrayList<>();
    String type = "";
    boolean click = false;
    String theme1 = "";
    GpsTracker gpsTracker;
    FragmentTransaction transaction;
    String latitude = "", longitude = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        theme1 = sharedpreference.getTheme(BottombarActivity.this);

        Log.d("mn13theme", sharedpreference.getTheme(BottombarActivity.this) + "11");
        if (sharedpreference.getTheme(BottombarActivity.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        new Handler().postDelayed(() -> {
            if (MobiComUserPreference.getInstance(BottombarActivity.this).isLoggedIn()) {
                if (sharedpreference.getUserId(BottombarActivity.this).equals("")) {
                    Intent i = new Intent(BottombarActivity.this, WelcomeActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.stay);
                } else {
                    ApplozicClient.getInstance(BottombarActivity.this).setContextBasedChat(true).setHandleDial(true).setIPCallEnabled(true);
                }
            } else {
                Intent i = new Intent(BottombarActivity.this, WelcomeActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }
        }, 1000);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottombar);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(BottombarActivity.this));
        Look_Fragment.postarray = new ArrayList<>();

        fragment1 = new Map_Fragment();
        fragment2 = new Wall_Fragment();
        fragment4 = new Catch_Fragment();

        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();

//        fm = getSupportFragmentManager();
        if (getIntent().getExtras() != null) {
            type = getIntent().getExtras().getString("type");
            if (!type.equals("map"))
                fm.beginTransaction().add(R.id.content_frame, fragment1, "1").commit();
        }
        if (type != null) {
            if (type.equalsIgnoreCase("home")) {
                click = false;
                active = fragment2;
                fm.beginTransaction().add(R.id.content_frame, fragment4, "4").hide(fragment4).commit();
                fm.beginTransaction().add(R.id.content_frame, fragment2, "2").commit();
                lookclick();
            } else if (type.equalsIgnoreCase("catch")) {
                click = false;
                active = fragment4;
                chatclick();
                fm.beginTransaction().add(R.id.content_frame, fragment2, "2").hide(fragment2).commit();
                fm.beginTransaction().add(R.id.content_frame, fragment4, "4").commit();
            } else if (type.equalsIgnoreCase("map")) {
                click = false;
                active = fragment1;
                fm.beginTransaction().add(R.id.content_frame, fragment2, "2").hide(fragment2).commit();
                fm.beginTransaction().add(R.id.content_frame, fragment4, "2").hide(fragment4).commit();
                fm.beginTransaction().add(R.id.content_frame, fragment1, "1").commit();
                mapclick();
            } else {
                click = false;
                active = fragment2;
                fm.beginTransaction().add(R.id.content_frame, fragment4, "4").hide(fragment4).commit();
                fm.beginTransaction().add(R.id.content_frame, fragment2, "2").commit();
                lookclick();
            }
        } else {
            click = false;
            active = fragment2;
            fm.beginTransaction().add(R.id.content_frame, fragment4, "4").hide(fragment4).commit();
            fm.beginTransaction().add(R.id.content_frame, fragment2, "2").commit();
            lookclick();
        }

        imgMyLocation.setOnClickListener(v -> {

            click = false;
            fm.beginTransaction().hide(active).show(fragment1).commit();
            active = fragment1;
            mapclick();
//                chatclick();

        });

//        imgmap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                click = false;
//                fm.beginTransaction().hide(active).show(fragment1).commit();
//                active = fragment1;
//                mapclick();
//
//            }
//        });

        imgwall.setOnClickListener(view -> {

            click = false;
            if (!active.equals(fragment2)) {
                //  fragment2.apicalling();
            }
            fm.beginTransaction().hide(active).show(fragment2).commit();
//                fm.beginTransaction().hide(active).commit();
//                fm.beginTransaction().replace(RM.id.content_frame, fragment2, "2").commit();
            active = fragment2;
            lookclick();
        });

        imgcamera.setOnClickListener(view -> {
            click = true;
            Intent intent = new Intent(BottombarActivity.this, OpenCamera1.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
        });

        Log.d("mn13date & timezone", Public_Function.getcurrentdatetime() + "\n" + Public_Function.gettimezone());
        imgchat.setOnClickListener(view -> {
            click = false;
            fm.beginTransaction().hide(active).show(fragment4).commit();
//                fm.beginTransaction().replace(RM.id.content_frame, fragment4, "4").commit();
//                fm.beginTransaction().hide(active).show(fragment4).commit();
            active = fragment4;
            chatclick();

        });

        imgProfile.setOnClickListener(view -> profileclick());

        imgProfile.setOnClickListener(v -> {
            Intent intent = new Intent(BottombarActivity.this, ProfilePage.class);
            intent.putExtra("type", "left");
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });

//        imgsaved.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (active.equals(fragment2)) {
//                    Intent intent = new Intent(BottombarActivity.this, Saved_Post.class);
//                    startActivity(intent);
//                    overridePendingTransition(RM.anim.slide_in_up, RM.anim.stay);
//                } else {
//                    Intent intent = new Intent(BottombarActivity.this, CreateChat.class);
//                    startActivity(intent);
//                    overridePendingTransition(RM.anim.slide_in_up, RM.anim.stay);
//
//                }
//
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (click) {
            click = false;
            if (active.equals(fragment1)) {
                mapclick();
            } else if (active.equals(fragment2)) {
                lookclick();
            } else if (active.equals(fragment4)) {
                chatclick();
            }
        }

        if (theme1.equalsIgnoreCase(sharedpreference.getTheme(BottombarActivity.this))) {

        } else {
            Intent i = getIntent();
            i.putExtra("type", "Profile");
            BottombarActivity.this.overridePendingTransition(0, 0);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            BottombarActivity.this.finish();
            BottombarActivity.this.overridePendingTransition(0, 0);
            BottombarActivity.this.startActivity(i);
        }

        if (camera_bottom.equalsIgnoreCase("catch")) {
            fm.beginTransaction().hide(active).show(fragment4).commit();
            active = fragment4;
            camera_bottom = "";
            chatclick();
        } else if (camera_bottom.equalsIgnoreCase("map")) {
            fm.beginTransaction().hide(active).show(fragment1).commit();
            active = fragment1;
            camera_bottom = "";
            mapclick();
        } else if (camera_bottom.equalsIgnoreCase("feed")) {
            fm.beginTransaction().hide(active).show(fragment2).commit();
            active = fragment2;
            camera_bottom = "";
            lookclick();
        }


        if (ContextCompat.checkSelfPermission(BottombarActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(BottombarActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {
            gpsTracker = new GpsTracker(BottombarActivity.this, BottombarActivity.this);
            if (gpsTracker.canGetLocation()) {
                latitude = String.valueOf(gpsTracker.getLatitude());
                longitude = String.valueOf(gpsTracker.getLongitude());
                Log.d("mn13getloacation:", latitude + "   " + longitude);
                Set_Location();
            } else {
                gpsTracker.showSettingsAlert();
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    gpsTracker = new GpsTracker(BottombarActivity.this, BottombarActivity.this);
                    if (gpsTracker.canGetLocation()) {
                        latitude = String.valueOf(gpsTracker.getLatitude());
                        longitude = String.valueOf(gpsTracker.getLongitude());
                        Log.d("mn13getloacation:", latitude + "   " + longitude);
                        Set_Location();
                    } else {
                        gpsTracker.showSettingsAlert();
                    }
                    // Toast.makeText(EditLocation.this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(Boto.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

//    @Override
//    public void onBackPressed() {
//        int count = getSupportFragmentManager().getBackStackEntryCount();
//
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//        } else {
//            getSupportFragmentManager().popBackStack();
//        }
//    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public void mapclick() {
        imgMyLocation.setImageDrawable(getResources().getDrawable(R.drawable.map1_selected));
        imgwall.setImageDrawable(getResources().getDrawable(R.drawable.look1_unselected));
        imgchat.setImageDrawable(getResources().getDrawable(R.drawable.catch_unselected));
        imgProfile.setImageDrawable(getResources().getDrawable(R.drawable.me_unselected));
    }

    public void lookclick() {
        imgMyLocation.setImageDrawable(getResources().getDrawable(R.drawable.map1_unselected));
        imgwall.setImageDrawable(getResources().getDrawable(R.drawable.look1_selected));
        imgchat.setImageDrawable(getResources().getDrawable(R.drawable.catch_unselected));
        imgProfile.setImageDrawable(getResources().getDrawable(R.drawable.me_unselected));
    }

    public void chatclick() {
        imgMyLocation.setImageDrawable(getResources().getDrawable(R.drawable.map1_unselected));
        imgwall.setImageDrawable(getResources().getDrawable(R.drawable.look1_unselected));
        imgchat.setImageDrawable(getResources().getDrawable(R.drawable.catch_selelcted));
        imgProfile.setImageDrawable(getResources().getDrawable(R.drawable.me_unselected));
    }

    public void profileclick() {
        imgMyLocation.setImageDrawable(getResources().getDrawable(R.drawable.map1_unselected));
        imgwall.setImageDrawable(getResources().getDrawable(R.drawable.look1_unselected));
        imgchat.setImageDrawable(getResources().getDrawable(R.drawable.catch_unselected));
        imgProfile.setImageDrawable(getResources().getDrawable(R.drawable.me_selected));
    }

    public void Set_Location() {


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", String.valueOf(sharedpreference.getUserId(BottombarActivity.this))));
        paramArrayList.add(new param("longt", longitude));
        paramArrayList.add(new param("lat", latitude + ""));//sharedpreference.getUserId(BottombarActivity.this)

        new geturl().getdata(BottombarActivity.this, data -> {

            try {
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("message");

                if (message) {
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, paramArrayList, "change_location");
    }

}
