package com.TBI.Client.Bluff.Activity.Settings;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.MyAsyncTaskCallback;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class AccountPrivacy extends AppCompatActivity {

    @BindView(R.id.tool_bar)
    Toolbar tool_bar;
    @BindView(R.id.switchaccount)
    LabeledSwitch switchaccount;
    @BindView(R.id.relbloack)
    RelativeLayout relbloack;
    @BindView(R.id.relpassword)
    RelativeLayout relpassword;

    // Change Password
    Dialog dialog1;
    TextInputLayout input_old_password;
    TextInputLayout input_new_password;
    TextInputLayout input_confiorm_password;
    EditText et_old_password;
    EditText et_new_password;
    EditText et_confiorm_password;
    TextView tv_pass_next;
    ImageView img_back_change_pass;


    ConnectionDetector cd;
    boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (sharedpreference.getTheme(AccountPrivacy.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountprivacy);
        ButterKnife.bind(this);

        AndroidNetworking.initialize(AccountPrivacy.this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(AccountPrivacy.this));
        setSupportActionBar(tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (sharedpreference.getTheme(AccountPrivacy.this).equalsIgnoreCase("white")) {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        } else {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }
        //   tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(RM.color.blacklight), PorterDuff.Mode.SRC_IN);

        switchaccount.setLabelOff(" ");
        switchaccount.setLabelOn(" ");
        dialog1 = new Dialog(AccountPrivacy.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        cd = new ConnectionDetector(AccountPrivacy.this);
        isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent) {
            Toasty.warning(AccountPrivacy.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
        } else {
            Get_Privacy();
        }

        switchaccount.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {

                int privacy = 2;

                if (isOn) {
                    privacy = 2;
                    Log.d("mn13", "true");
                } else {
                    privacy = 1;
                    Log.d("mn13", "false");
                }

                cd = new ConnectionDetector(AccountPrivacy.this);
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    Toasty.warning(AccountPrivacy.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    Change_Privacy(privacy);
                }


            }
        });

        relbloack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i1 = new Intent(AccountPrivacy.this, BlockList.class);
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }
        });

        relpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyChnagePass();
            }
        });

    }

    private void verifyChnagePass() {
        // Change Password

        dialog1.setContentView(R.layout.dialoge_change_password);
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.black);
        dialog1.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog1.show();

        input_old_password = dialog1.findViewById(R.id.input_old_password);
        input_new_password = dialog1.findViewById(R.id.input_new_password);
        input_confiorm_password = dialog1.findViewById(R.id.input_confiorm_password);
        et_old_password = dialog1.findViewById(R.id.et_old_password);
        et_new_password = dialog1.findViewById(R.id.et_new_password);
        et_confiorm_password = dialog1.findViewById(R.id.et_confiorm_password);
        tv_pass_next = dialog1.findViewById(R.id.tv_pass_next);
        img_back_change_pass = dialog1.findViewById(R.id.img_back_change_pass);

        et_old_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                input_old_password.setHint("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                input_new_password.setHint("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_confiorm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                input_confiorm_password.setHint("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!et_new_password.getText().toString().trim().equals(et_confiorm_password.getText().toString().trim())) {
                    et_confiorm_password.setError("Password doesn't match");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!et_new_password.getText().toString().trim().equals(et_confiorm_password.getText().toString().trim())) {
                    et_confiorm_password.setError("Password doesn't match");
                }
            }
        });

        tv_pass_next.setOnClickListener(v -> {
            if (!et_new_password.getText().toString().trim().equals(et_confiorm_password.getText().toString().trim())) {
                et_confiorm_password.setError("Password doesn't match");
            } else {
                changePassword(et_confiorm_password.getText().toString().trim(),
                        et_old_password.getText().toString().trim());
            }
        });

    }

    public void changePassword(String newPass, String oldPass) {
        AndroidNetworking.post(geturl.BASE_URL + "change_password")
                .addHeaders("Authorization", sharedpreference.getUserToken(AccountPrivacy.this))
                .addBodyParameter("user_id", sharedpreference.getUserId(AccountPrivacy.this))
                .addBodyParameter("old_password", oldPass)
                .addBodyParameter("new_password", newPass)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean sucsess = response.getBoolean("success");

                            if (sucsess) {
                                Log.e("LLLLL_Response: ", response.getString("message"));
                                Toasty.normal(AccountPrivacy.this, response.getString("message")).show();
                                if (dialog1.isShowing() && dialog1 != null) {
                                    dialog1.dismiss();
                                }
                            } else {
                                Toasty.error(AccountPrivacy.this, response.getString("message")).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toasty.error(AccountPrivacy.this, anError.getLocalizedMessage()).show();
                    }
                });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_out);
        return super.onOptionsItemSelected(item);
    }

    public void Change_Privacy(int privacy) {

        Public_Function.Show_Progressdialog(AccountPrivacy.this);


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(AccountPrivacy.this)));
        paramArrayList.add(new param("privacy", privacy + ""));


        new geturl().getdata(AccountPrivacy.this, new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {
                try {
                    Public_Function.Hide_ProgressDialogue();
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("msg");
                    if (message) {
                    } else {
                        if (privacy == 1) {
                            switchaccount.setOn(true);
                        } else {
                            switchaccount.setOn(false);
                        }
                        Toasty.error(AccountPrivacy.this, status + "", Toast.LENGTH_LONG, true).show();
                    }
                } catch (Exception e) {
                    Toasty.error(AccountPrivacy.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                    e.printStackTrace();
                    if (privacy == 1) {
                        switchaccount.setOn(true);
                    } else {
                        switchaccount.setOn(false);
                    }

                }
            }
        }, paramArrayList, "change_account_privacy");
    }


    public void Get_Privacy() {

        Public_Function.Show_Progressdialog(AccountPrivacy.this);


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(AccountPrivacy.this)));


        new geturl().getdata(AccountPrivacy.this, new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {
                try {
                    Public_Function.Hide_ProgressDialogue();
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("msg");
                    if (message) {
                        if (object.optInt("account_privacy") == 2) {
                            switchaccount.setOn(true);
                        } else {
                            switchaccount.setOn(false);
                        }
                    } else {
                        Toasty.error(AccountPrivacy.this, status + "", Toast.LENGTH_LONG, true).show();
                    }
                } catch (Exception e) {
                    Toasty.error(AccountPrivacy.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                    e.printStackTrace();

                }
            }
        }, paramArrayList, "get_user_privacy");
    }

    @Override
    public void onBackPressed() {
        if (dialog1.isShowing() && dialog1 != null) {
            dialog1.dismiss();
        } else {
            super.onBackPressed();
        }
    }
}
