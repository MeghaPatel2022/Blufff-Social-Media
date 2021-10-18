package com.TBI.Client.Bluff.Activity.Settings;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.TBI.Client.Bluff.Activity.PermissionActivity;
import com.TBI.Client.Bluff.Activity.Profile.EditProfile;
import com.TBI.Client.Bluff.Database.DBHelper;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.UserPages.WelcomeActivity;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.MyAsyncTaskCallback;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.listners.AlLogoutHandler;
import com.github.angads25.toggle.widget.LabeledSwitch;

import net.iquesoft.iquephoto.utils.SharedPrefrence;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class Setting extends AppCompatActivity {

    @BindView(R.id.tool_bar)
    Toolbar tool_bar;
    @BindView(R.id.relprofile)
    RelativeLayout relprofile;
    @BindView(R.id.relwifi)
    RelativeLayout relwifi;
    @BindView(R.id.relpushnoti)
    RelativeLayout relpushnoti;
    @BindView(R.id.relfaq)
    RelativeLayout relfaq;
    @BindView(R.id.relabout)
    RelativeLayout relabout;
    @BindView(R.id.rellogout)
    RelativeLayout rellogout;
    @BindView(R.id.rlnotification)
    RelativeLayout rlnotification;
    @BindView(R.id.relextras)
    RelativeLayout relextras;
    @BindView(R.id.switchnotification)
    LabeledSwitch switchnotification;
    @BindView(R.id.switchacctype)
    LabeledSwitch switchacctype;
    @BindView(R.id.switchtheme)
    LabeledSwitch switchtheme;
    @BindView(R.id.relaccount)
    RelativeLayout relaccount;
    @BindView(R.id.reladdservice)
    RelativeLayout reladdservice;
    @BindView(R.id.relpermission)
    RelativeLayout relpermission;
    @BindView(R.id.relfeedback)
    RelativeLayout relfeedback;
    @BindView(R.id.relmoreinfo)
    RelativeLayout relmoreinfo;
    @BindView(R.id.relaccaction)
    RelativeLayout relaccaction;
    @BindView(R.id.relsupport)
    RelativeLayout relsupport;
    @BindView(R.id.relaboutbluff)
    RelativeLayout relaboutbluff;
    @BindView(R.id.relsave)
    RelativeLayout relsave;
    @BindView(R.id.reltheme)
    RelativeLayout reltheme;

    Dialog dialogelogout;
    Button cancle, ok;


    ConnectionDetector cd;
    boolean isInternetPresent = false;
    DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Log.d("mn13theme", sharedpreference.getTheme(Setting.this) + "11");
        if (sharedpreference.getTheme(Setting.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(Setting.this));
        dbHelper = new DBHelper(Setting.this);
        setSupportActionBar(tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (sharedpreference.getTheme(Setting.this).equalsIgnoreCase("white")) {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        } else {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }


        switchnotification.setLabelOff(" ");
        switchnotification.setLabelOn(" ");
        switchtheme.setLabelOn(" ");
        switchtheme.setLabelOff(" ");

        if (sharedpreference.getTheme(Setting.this).equalsIgnoreCase("white")) {
            switchtheme.setOn(false);
        } else {
            switchtheme.setOn(true);
        }

        switchtheme.setOnToggledListener((toggleableView, isOn) -> {

            if (isOn) {
                sharedpreference.setTheme(Setting.this, "black");
                SharedPrefrence.setTheme(Setting.this, "black");
                Applozic logic = new Applozic(Setting.this);
                logic.sharedPreferences.edit().putString("theme", "black").commit();

            } else {
                sharedpreference.setTheme(Setting.this, "white");
                SharedPrefrence.setTheme(Setting.this, "white");
                Applozic logic = new Applozic(Setting.this);
                logic.sharedPreferences.edit().putString("theme", "white").commit();
            }

            Intent i = getIntent();
            Setting.this.overridePendingTransition(0, 0);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            Setting.this.finish();
            Setting.this.overridePendingTransition(0, 0);
            Setting.this.startActivity(i);


        });

        switchacctype.setOnToggledListener((toggleableView, isOn) -> {
            if (isOn) {
                sharedpreference.setAccountType(Setting.this, "1");
            } else {
                sharedpreference.setTheme(Setting.this, "0");
            }

        });

        relprofile.setOnClickListener(view -> {

            Intent intent = new Intent(Setting.this, EditProfile.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
            finish();
        });

        relsave.setOnClickListener(view -> {

            Intent intent = new Intent(Setting.this, Saved_Post.class);
            startActivity(intent);
            //overridePendingTransition(RM.anim.left_to_right, RM.anim.right_to_left);
            overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
        });

        rellogout.setOnClickListener(view -> logout());

        relaccount.setOnClickListener(view -> {

            Intent intent = new Intent(Setting.this, AccountPrivacy.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });

        reltheme.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, NotificationSettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });

        rlnotification.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, NotificationSettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });

        relextras.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, ExtrasActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });

        relpermission.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, PermissionActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });

        reladdservice.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, AdditionalServices.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });

        relfeedback.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, FeedBackActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });

        relmoreinfo.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, MoreInfo.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });

        relaccaction.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, AccountActions.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });

        relsupport.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, SupportActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });

        relaboutbluff.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, AboutBluff.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });

        /*switchnotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (compoundButton.isChecked()) {
                    sharedpreference.setTheme(Setting.this, "white");
                } else {
                    sharedpreference.setTheme(Setting.this, "black");
                }
            }
        });*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    public void logout() {
        dialogelogout = new Dialog(Setting.this);
        dialogelogout.setContentView(R.layout.dialoge_logout);
        dialogelogout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cancle = dialogelogout.findViewById(R.id.cancle);
        ok = dialogelogout.findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialogelogout.isShowing()) {
                    dialogelogout.dismiss();
                }

            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                cd = new ConnectionDetector(Setting.this);
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    Toasty.warning(Setting.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    Log_Out();
                }
            }
        });

        if (!dialogelogout.isShowing()) {
            dialogelogout.show();
        }

    }

    public void Log_Out() {

        Public_Function.Show_Progressdialog(Setting.this);


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(Setting.this)));
        paramArrayList.add(new param("token", sharedpreference.getfirebasetoken(Setting.this)));


        new geturl().getdata(Setting.this, new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {
                try {

                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("msg");
                    if (message) {

                        Applozic.logoutUser(Setting.this, new AlLogoutHandler() {
                            @Override
                            public void onSuccess(Context context) {
                                Public_Function.Hide_ProgressDialogue();

                                String token = "";
                                token = sharedpreference.getfirebasetoken(Setting.this);
                                sharedpreference.clearAll(Setting.this);
                                sharedpreference.setfirebasetoken(Setting.this, token);
                                dbHelper.delete_table();

                                Intent i = new Intent(Setting.this, WelcomeActivity.class);
                                startActivity(i);
                                finishAffinity();
                                overridePendingTransition(R.anim.fade_in, R.anim.stay);

                            }

                            @Override
                            public void onFailure(Exception exception) {
                                Public_Function.Hide_ProgressDialogue();
                                Log.d("mn13error", exception.getMessage() + "");
                                Toasty.error(Setting.this, exception.getMessage() + "", Toast.LENGTH_LONG, true).show();

                            }
                        });

                    } else {
                        Public_Function.Hide_ProgressDialogue();
                        Toasty.error(Setting.this, status + "", Toast.LENGTH_LONG, true).show();
                    }
                } catch (Exception e) {
                    Public_Function.Hide_ProgressDialogue();
                    Toasty.error(Setting.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                    e.printStackTrace();
                }
            }
        }, paramArrayList, "logout");
    }
}
