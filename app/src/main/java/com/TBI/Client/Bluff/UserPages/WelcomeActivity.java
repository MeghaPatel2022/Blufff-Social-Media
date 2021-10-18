package com.TBI.Client.Bluff.UserPages;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Mains.BottombarActivity;
import com.TBI.Client.Bluff.Activity.Profile.EditProfile;
import com.TBI.Client.Bluff.Adapter.Post.NameSuggestAdapter;
import com.TBI.Client.Bluff.Adapter.UserPage.CustomAdapter;
import com.TBI.Client.Bluff.Database.DBHelper;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.Model.GetProfession.DatumProfession;
import com.TBI.Client.Bluff.Model.GetProfession.GetProfession;
import com.TBI.Client.Bluff.Model.Register.Regdata;
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
import com.applozic.audiovideo.activity.AudioCallActivityV2;
import com.applozic.audiovideo.activity.VideoActivity;
import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.User;
import com.applozic.mobicomkit.listners.AlLoginHandler;
import com.applozic.mobicomkit.listners.AlPushNotificationHandler;
import com.applozic.mobicomkit.uiwidgets.ApplozicSetting;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.ycuwq.datepicker.date.DatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.TBI.Client.Bluff.Activity.BusinessForm.PERMISSION_ALL;
import static com.TBI.Client.Bluff.Utils.Public_Function.hasPermissions;

public class WelcomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public final static int PERMISSION_REQUEST_CODE = 16;
    private static final String IMAGE_DIRECTORY = "/Blufff";
    private static final int RC_SIGN_IN = 420;

    File selectedimage;
    boolean flag = false;
    ConnectionDetector cd;
    boolean isInternetPresent = false;

    Dialog dialog;
    Dialog dialog1;
    Dialog dialog2;
    Dialog dialog3;
    Dialog dialog4;
    Dialog dialog5;
    Dialog dialog6;
    Dialog dialog7;

    @BindView(R.id.tv_sign_up)
    TextView tv_sign_up;
    @BindView(R.id.tv_login)
    TextView tv_login;

    // Sign Up 1
    TextView tv_email, tv_number;
    View v_email, v_number;
    TextInputLayout input_email, input_number;
    EditText et_email, et_number;
    Spinner spinner;
    LinearLayout ll_number;
    TextView tv_next;
    boolean is_email = true;
    String[] country_code;
    ArrayList<Regdata> regdataArrayList = new ArrayList<>();

    // Verify OTP
    ImageView img_back_otp;
    TextView header_title1, header_title2, header_title3;
    TextInputLayout input_otp;
    EditText et_otp;
    TextView tv_ot_next;
    String mobile_num = "";
    String mobile_cod1 = "";

    // Suggest Username
    ImageView img_back_uname;
    LinearLayout ll_username;
    TextInputLayout input_username;
    EditText et_username;
    ImageView img_cancel;
    TextView error_username;
    RecyclerView rv_suggest_username;
    TextView tv_username_next;
    ArrayList<String> suggest_name = new ArrayList<>();
    NameSuggestAdapter nameSuggestAdapter;

    // Create Password
    ImageView img_back_passs;
    TextInputLayout input_password;
    EditText et_password;
    TextView tv_pass_next;
    ImageView img_pass_cancel;

    // Profile
    ImageView img_back_prof;
    CircleImageView img_profile;
    TextInputLayout input_full_name, input_dec;
    EditText et_full_name, et_desc;
    Spinner spinner_prof;
    TextView tv_profile_next;
    LabeledSwitch switch_profession;
    String[] profession;
    List<DatumProfession> getlist = new ArrayList<>();
    int position = 0;
    int privacy = 1;

    // B'day
    ImageView img_back_bday;
    TextInputLayout input_bday;
    EditText et_bday;
    TextView tv_age;
    TextView tv_bday_next;
    DatePicker datepicker;

    // Welcome
    TextView tv_welcome_next;
    TextView tv_change_name;

    // Google Login
    LinearLayout ll_google;
    TextView tv_signup;
    EditText et_username1;
    EditText et_password1;
    TextView tv_login_next;
    ImageView img_show_pass;
    int start1 = 0, end = 0;
    boolean is_password_shown = false;

    String token;
    String s_photo;
    String username;
    String fullname;
    String email;
    String android_id;
    DBHelper dbHelper;
    private GoogleSignInClient googleSignInClient;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(WelcomeActivity.this));

        AndroidNetworking.initialize(WelcomeActivity.this);
        dbHelper = new DBHelper(WelcomeActivity.this);
        ButterKnife.bind(WelcomeActivity.this);

        checkPermissions();
        getProfession();
        Applozic.init(this, getString(R.string.application_key));

        dialog = new Dialog(WelcomeActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog1 = new Dialog(WelcomeActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog2 = new Dialog(WelcomeActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog3 = new Dialog(WelcomeActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog4 = new Dialog(WelcomeActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog5 = new Dialog(WelcomeActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog6 = new Dialog(WelcomeActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog7 = new Dialog(WelcomeActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);

        tv_sign_up.setOnClickListener(v -> {
            is_email = true;
            signUp_1();
        });


        tv_login.setOnClickListener(v -> googleLogin());
    }

    // Dialoge Design
    @SuppressLint("ClickableViewAccessibility")
    private void signUp_1() {
        // Generate OTP

        dialog.setContentView(R.layout.dialoge_sign_up_1);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.black);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.show();

        tv_email = dialog.findViewById(R.id.tv_email);
        tv_number = dialog.findViewById(R.id.tv_number);
        tv_next = dialog.findViewById(R.id.tv_next);
        v_email = dialog.findViewById(R.id.v_email);
        v_number = dialog.findViewById(R.id.v_number);
        input_email = dialog.findViewById(R.id.input_email);
        input_number = dialog.findViewById(R.id.input_number);
        et_email = dialog.findViewById(R.id.et_email);
        et_number = dialog.findViewById(R.id.et_number);
        spinner = dialog.findViewById(R.id.spinner);
        ll_number = dialog.findViewById(R.id.ll_number);

        country_code = getResources().getStringArray(R.array.countryCodes);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countryCodes, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(80);

        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input_email.setHint("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_email.setOnClickListener(v -> {
            tv_email.setTextColor(getResources().getColor(R.color.text_select_color));
            v_email.setBackgroundColor(getResources().getColor(R.color.text_select_color));
            tv_number.setTextColor(getResources().getColor(R.color.text_unselect_color));
            v_number.setBackgroundColor(getResources().getColor(R.color.black));
            input_email.setVisibility(View.VISIBLE);
            if (ll_number.getVisibility() == View.VISIBLE)
                ll_number.setVisibility(View.GONE);
        });

        tv_number.setOnClickListener(v -> {
            if (!is_email) {
                tv_email.setTextColor(getResources().getColor(R.color.text_unselect_color));
                v_email.setBackgroundColor(getResources().getColor(R.color.black));
                tv_number.setTextColor(getResources().getColor(R.color.text_select_color));
                v_number.setBackgroundColor(getResources().getColor(R.color.text_select_color));
                ll_number.setVisibility(View.VISIBLE);
                if (input_email.getVisibility() == View.VISIBLE)
                    input_email.setVisibility(View.GONE);
            }
        });

        if (!is_email) {
            tv_email.setTextColor(getResources().getColor(R.color.text_unselect_color));
            v_email.setBackgroundColor(getResources().getColor(R.color.black));
            tv_number.setTextColor(getResources().getColor(R.color.text_select_color));
            v_number.setBackgroundColor(getResources().getColor(R.color.text_select_color));

            if (input_email.getVisibility() == View.VISIBLE) {
                input_email.setVisibility(View.GONE);
                ll_number.setVisibility(View.VISIBLE);
            }

        } else {
            tv_email.setTextColor(getResources().getColor(R.color.text_select_color));
            v_email.setBackgroundColor(getResources().getColor(R.color.text_select_color));
            tv_number.setTextColor(getResources().getColor(R.color.text_unselect_color));
            v_number.setBackgroundColor(getResources().getColor(R.color.black));

            input_email.setVisibility(View.VISIBLE);
            if (ll_number.getVisibility() == View.VISIBLE) {
                ll_number.setVisibility(View.GONE);
            }
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        tv_next.setOnClickListener(v -> {

            if (is_email) {
                String email = et_email.getText().toString().trim();
                if (email.matches(emailPattern) && email.length() > 0) {
                    emailSet("1", et_email.getText().toString().trim());
                } else {
                    hideKeyboard(WelcomeActivity.this);
                    Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                }
            } else {
                String str = country_code[spinner.getSelectedItemPosition()];
                Log.e("LLL_Code: ", str.split(" ")[1]);
                String number = et_number.getText().toString().trim();
                if (number.length() > 0) {
                    if (dialog.isShowing() && dialog != null)
                        dialog.dismiss();
                    sendOTP("2", str.split(" ")[1], number);
                } else {
                    et_number.setError("Please enter valid number..");
                }
            }

        });


    }

    private void verifyOTP() {
        // Generate OTP

        dialog1.setContentView(R.layout.dialoge_verify_otp);
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.black);
        dialog1.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog1.show();

        img_back_otp = dialog1.findViewById(R.id.img_back_otp);
        header_title1 = dialog1.findViewById(R.id.header_title1);
        header_title2 = dialog1.findViewById(R.id.header_title2);
        header_title3 = dialog1.findViewById(R.id.header_title3);
        input_otp = dialog1.findViewById(R.id.input_otp);
        et_otp = dialog1.findViewById(R.id.et_otp);
        tv_ot_next = dialog1.findViewById(R.id.tv_ot_next);

        if (mobile_num.length() > 0)
            header_title2.setText(mobile_cod1 + " XXXXXXX" + mobile_num.substring(mobile_num.length() - 3));

        header_title3.setOnClickListener(v -> requestNewOTP());

        et_otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input_otp.setHint("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        img_back_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_ot_next.setOnClickListener(v -> verifyOtp(et_otp.getText().toString().trim()));


    }

    private void suggestUsername() {

        dialog2.setContentView(R.layout.dialoge_select_username);
        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.black);
        dialog2.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog2.show();

        img_back_uname = dialog2.findViewById(R.id.img_back_uname);
        ll_username = dialog2.findViewById(R.id.ll_username);
        input_username = dialog2.findViewById(R.id.input_username);
        et_username = dialog2.findViewById(R.id.et_username);
        img_cancel = dialog2.findViewById(R.id.img_cancel);
        error_username = dialog2.findViewById(R.id.error_username);
        rv_suggest_username = dialog2.findViewById(R.id.rv_suggest_username);
        tv_username_next = dialog2.findViewById(R.id.tv_username_next);

        img_back_uname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rv_suggest_username.setVisibility(View.VISIBLE);
                suggestName(s.toString().trim());
                nameSuggestAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rv_suggest_username.setLayoutManager(new LinearLayoutManager(WelcomeActivity.this, RecyclerView.VERTICAL, false));
        nameSuggestAdapter = new NameSuggestAdapter(WelcomeActivity.this, suggest_name);
        rv_suggest_username.setAdapter(nameSuggestAdapter);


        tv_username_next.setOnClickListener(v -> setUserName(et_username.getText().toString().trim()));
    }

    private void setPassword() {
        dialog3.setContentView(R.layout.dialoge_password);
        dialog3.getWindow().setBackgroundDrawableResource(android.R.color.black);
        dialog3.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog3.show();

        img_back_passs = dialog3.findViewById(R.id.img_back_passs);
        input_password = dialog3.findViewById(R.id.input_password);
        et_password = dialog3.findViewById(R.id.et_password);
        tv_pass_next = dialog3.findViewById(R.id.tv_pass_next);
        img_pass_cancel = dialog3.findViewById(R.id.img_pass_cancel);

        img_back_passs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        img_pass_cancel.setOnClickListener(v -> et_password.setText(""));

        tv_pass_next.setOnClickListener(v -> {
            String pass = et_password.getText().toString().trim();
            if (pass.length() > 0)
                setUserPassword(et_password.getText().toString().trim());
            else
                et_password.setError("Please enter password..");
        });


    }

    private void setProfile() {

        dialog4.setContentView(R.layout.dialoge_profile);
        dialog4.getWindow().setBackgroundDrawableResource(android.R.color.black);
        dialog4.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog4.show();

        img_back_prof = dialog4.findViewById(R.id.img_back_prof);
        img_profile = dialog4.findViewById(R.id.img_profile);
        input_full_name = dialog4.findViewById(R.id.input_full_name);
        input_dec = dialog4.findViewById(R.id.input_dec);
        et_full_name = dialog4.findViewById(R.id.et_full_name);
        et_desc = dialog4.findViewById(R.id.et_desc);
        spinner_prof = dialog4.findViewById(R.id.spinner_prof);
        switch_profession = dialog4.findViewById(R.id.switch_profession);
        tv_profile_next = dialog4.findViewById(R.id.tv_profile_next);

        img_back_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        et_full_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input_full_name.setHint("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input_dec.setHint("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        img_profile.setOnClickListener(v -> {

            String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CAMERA};

            if (!hasPermissions(WelcomeActivity.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(WelcomeActivity.this, PERMISSIONS, PERMISSION_ALL);
            } else {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(WelcomeActivity.this);
            }

        });

        switch_profession.setOnToggledListener((toggleableView, isOn) -> {

            if (isOn) {
                privacy = 2;
                sharedpreference.setAccountType(WelcomeActivity.this, "2");
                Log.d("mn13", "true");
            } else {
                privacy = 1;
                sharedpreference.setAccountType(WelcomeActivity.this, "1");
                Log.d("mn13", "false");
            }

            cd = new ConnectionDetector(WelcomeActivity.this);
            isInternetPresent = cd.isConnectingToInternet();

            if (!isInternetPresent) {
                Toasty.warning(WelcomeActivity.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
            } else {

            }
        });

        CustomAdapter customAdapter = new CustomAdapter(WelcomeActivity.this, profession);
        spinner_prof.setAdapter(customAdapter);
        spinner_prof.setSelection(0);

        tv_profile_next.setOnClickListener(v -> setUserProfile(et_full_name.getText().toString().trim(),
                String.valueOf(getlist.get(position).getId()),
                String.valueOf(privacy),
                et_desc.getText().toString().trim()));

        dialog4.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });


    }

    private void setBday() {

        dialog5.setContentView(R.layout.dialoge_birth_day);
        dialog5.getWindow().setBackgroundDrawableResource(android.R.color.black);
        dialog5.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog5.show();

        img_back_bday = dialog5.findViewById(R.id.img_back_bday);
        input_bday = dialog5.findViewById(R.id.input_bday);
        et_bday = dialog5.findViewById(R.id.et_bday);
        tv_age = dialog5.findViewById(R.id.tv_age);
        tv_bday_next = dialog5.findViewById(R.id.tv_bday_next);
        datepicker = dialog5.findViewById(R.id.datepicker);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        img_back_bday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        et_bday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                input_bday.setHint("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_bday.setText(datepicker.getDay() + "-" + datepicker.getMonth() + "-" + datepicker.getYear());
        datepicker.setOnDateSelectedListener((year, month, day) -> {
            Log.d("LLLL_Date", "Year=" + year + " Month=" + (month + 1) + " day=" + day);
            String bday = day + "-" + month + "-" + year;
            et_bday.setText(bday.trim());
            String age = getAge(year, (month + 1), day);
            tv_age.setText(age.trim() + " years old");
        });

        tv_bday_next.setOnClickListener(v -> setUserBday(et_bday.getText().toString().trim()));


    }

    private void setWelcome(String username, String password) {

        dialog6.setContentView(R.layout.dialoge_welcome);
        dialog6.getWindow().setBackgroundDrawableResource(android.R.color.black);
        dialog6.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog6.show();

        tv_welcome_next = dialog6.findViewById(R.id.tv_welcome_next);
        tv_change_name = dialog6.findViewById(R.id.tv_change_name);

        tv_welcome_next.setOnClickListener(v -> {
            Public_Function.Show_Progressdialog(WelcomeActivity.this);
            loginWithou(username.trim(),
                    password.trim());
        });

        tv_change_name.setOnClickListener(v -> suggestUsername());


    }

    private void googleLogin() {

        dialog7.setContentView(R.layout.dialoge_login);
        dialog7.getWindow().setBackgroundDrawableResource(android.R.color.black);
        dialog7.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog7.show();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        tv_signup = dialog7.findViewById(R.id.tv_signup);
        googleSignInClient = GoogleSignIn.getClient(WelcomeActivity.this, gso);
        ll_google = dialog7.findViewById(R.id.ll_google);
        et_username1 = dialog7.findViewById(R.id.et_username1);
        et_password1 = dialog7.findViewById(R.id.et_password1);
        tv_login_next = dialog7.findViewById(R.id.tv_login_next);
        img_show_pass = dialog7.findViewById(R.id.img_show_pass);

        et_password1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                start1 = et_password1.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                end = et_password1.getSelectionEnd();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        img_show_pass.setOnClickListener(v -> {
            if (is_password_shown) {
                is_password_shown = false;
                et_password1.setTransformationMethod(new PasswordTransformationMethod());
                et_password1.setSelection(et_password1.getText().length());
            } else {
                is_password_shown = true;
                et_password1.setTransformationMethod(null);
                et_password1.setSelection(et_password1.getText().length());
            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp_1();
            }
        });

        ll_google.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        tv_login_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Public_Function.Show_Progressdialog(WelcomeActivity.this);
                loginWithou(et_username1.getText().toString().trim(),
                        et_password1.getText().toString().trim());
            }
        });


    }

    // Spinner Method
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinner.setSelection(position);
        spinner_prof.setSelection(position);
        this.position = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Api Calling
    private void emailSet(String step, String email) {
        AndroidNetworking.post(geturl.BASE_URL + "register")
                .addBodyParameter("step", step)
                .addBodyParameter("email", email)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getString("success").equalsIgnoreCase("false")) {
                                if (response.getString("message").equalsIgnoreCase("Email already exist!")) {
                                    if (dialog.isShowing() && dialog != null)
                                        dialog.dismiss();
                                    Toast.makeText(WelcomeActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                                }
                                if (response.getString("message").equalsIgnoreCase("Mobile No already exist!")) {
                                    if (dialog.isShowing() && dialog != null)
                                        dialog.dismiss();
                                    Toast.makeText(WelcomeActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                regdataArrayList.clear();
                                JSONArray jsonArray = response.getJSONArray("data");
                                Log.e("LLLLL_data1: ", jsonArray.toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Regdata regdata = new Regdata();
                                    regdata.setId(jsonObject.getInt("id"));
                                    regdata.setCurrentProfileStep(jsonObject.getInt("current_profile_step"));
                                    regdataArrayList.add(regdata);

                                    sharedpreference.setVeryStep(WelcomeActivity.this, jsonObject.getInt("current_profile_step"));
                                    sharedpreference.setUserId(WelcomeActivity.this, String.valueOf(jsonObject.getInt("id")));


                                    if (jsonObject.getInt("current_profile_step") == 1) {
                                        is_email = false;
                                        signUp_1();
                                    }

                                    if (jsonObject.getInt("current_profile_step") == 2) {
                                        signUp_1();
                                    }

                                    if (jsonObject.getInt("current_profile_step") == 3) {
                                        suggestUsername();
                                    }

                                    if (jsonObject.getInt("current_profile_step") == 4) {
                                        setPassword();
                                    }

                                    if (jsonObject.getInt("current_profile_step") == 5) {
                                        setProfile();
                                    }

                                    if (jsonObject.getInt("current_profile_step") == 6) {
                                        setBday();
                                    }

                                    if (jsonObject.getInt("current_profile_step") == 7) {
                                        setWelcome(jsonObject.getString("username"),
                                                jsonObject.getString("password"));
                                    }

                                }
                            }
                        } catch (JSONException e) {
                            Log.e("LLLL_Error1: ", e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLL_Email_Error1: ", anError.getMessage());
                    }
                });
    }

    private void sendOTP(String step, String mobile_code, String mobile_no) {
        String id = (sharedpreference.getUserId(WelcomeActivity.this));
        Log.e("LLL_Pass_Data:", id + " \nstep: " + step + " \nmobile_code: " + mobile_code + "  \nmobile_no: " + mobile_no);
        AndroidNetworking.post(geturl.BASE_URL + "register")
                .addBodyParameter("user_id", id)
                .addBodyParameter("step", step)
                .addBodyParameter("mobile_code", mobile_code)
                .addBodyParameter("mobile_no", mobile_no)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("message").equalsIgnoreCase("Mobile No already exist!")) {
                                if (dialog.isShowing() && dialog != null)
                                    dialog.dismiss();
                                Toast.makeText(WelcomeActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                            } else {

                                mobile_num = mobile_no;
                                mobile_cod1 = mobile_code;
                                regdataArrayList.clear();
                                JSONArray jsonArray = response.getJSONArray("data");
                                Log.e("LLLLL_data2: ", jsonArray.toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Regdata regdata = new Regdata();
                                    regdata.setId(jsonObject.getInt("id"));
                                    regdata.setCurrentProfileStep(jsonObject.getInt("current_profile_step"));
                                    regdataArrayList.add(regdata);

                                    sharedpreference.setVeryStep(WelcomeActivity.this, jsonObject.getInt("current_profile_step"));
                                    sharedpreference.setUserId(WelcomeActivity.this, String.valueOf(jsonObject.getInt("id")));
                                }
                                verifyOTP();
                            }

                        } catch (JSONException e) {
                            Log.e("LLLL_Error2: ", e.getMessage());
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLL_Email_Error2: ", anError.getMessage());
                    }
                });

    }

    private void verifyOtp(String otp) {
        String id = String.valueOf((sharedpreference.getUserId(WelcomeActivity.this)));
        AndroidNetworking.post(geturl.BASE_URL + "verify_otp")
                .addBodyParameter("otp", otp)
                .addBodyParameter("user_id", id)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("LLL_Verify: ", response.getString("message"));
                            suggestUsername();
                            Toasty.info(WelcomeActivity.this, "Verify Successfuly..");
                        } catch (JSONException e) {
                            Log.e("LLLL_Error3: ", e.getMessage());
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLL_Email_Error3: ", anError.getMessage());
                    }
                });

    }

    private void requestNewOTP() {
        String id = String.valueOf((sharedpreference.getUserId(WelcomeActivity.this)));
        AndroidNetworking.post(geturl.BASE_URL + "resend_otp")
                .addBodyParameter("user_id", id)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("LLL_Verify: ", response.getString("message"));
                            Toasty.normal(WelcomeActivity.this, response.getString("message"));
                        } catch (JSONException e) {
                            Log.e("LLLL_Error3: ", e.getMessage());
                            Toasty.error(WelcomeActivity.this, e.getMessage());
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLL_Email_Error3: ", anError.getMessage());
                    }
                });
    }

    private void suggestName(String keyword) {
        String id = String.valueOf((sharedpreference.getUserId(WelcomeActivity.this)));
        AndroidNetworking.post(geturl.BASE_URL + "username_suggest")
                .addBodyParameter("user_id", id)
                .addBodyParameter("keyword", keyword)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            suggest_name.clear();
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                suggest_name.add(jsonArray.getString(i));
                                Log.e("LLL_name1: ", jsonArray.getString(i));
                            }
                            if (response.getString("message").equalsIgnoreCase("Username already exists.")) {
                                ll_username.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_box_error));
                                error_username.setVisibility(View.VISIBLE);
                                error_username.setText(response.getString("message"));
                            }
                            if (response.getString("message").equalsIgnoreCase("Username available!")) {
                                ll_username.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_box3));
                                error_username.setVisibility(View.GONE);
                                error_username.setText(response.getString("message"));
                            }
                            Log.e("LLL_name: ", response.getString("message"));
                        } catch (JSONException e) {
                            Log.e("LLLL_Error4: ", e.getMessage());
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLL_Email_Error4: ", anError.getMessage());
                    }
                });
    }

    private void setUserName(String userName) {
        String id = String.valueOf((sharedpreference.getUserId(WelcomeActivity.this)));
        AndroidNetworking.post(geturl.BASE_URL + "register")
                .addBodyParameter("user_id", id)
                .addBodyParameter("step", "4")
                .addBodyParameter("username", userName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (rv_suggest_username.getVisibility() == View.VISIBLE)
                                rv_suggest_username.setVisibility(View.GONE);

                            regdataArrayList.clear();
                            JSONArray jsonArray = response.getJSONArray("data");
                            Log.e("LLLLL_data1: ", jsonArray.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Regdata regdata = new Regdata();
                                regdata.setId(jsonObject.getInt("id"));
                                regdata.setCurrentProfileStep(jsonObject.getInt("current_profile_step"));
                                regdataArrayList.add(regdata);

                                sharedpreference.setVeryStep(WelcomeActivity.this, jsonObject.getInt("current_profile_step"));
                                sharedpreference.setUserId(WelcomeActivity.this, String.valueOf(jsonObject.getInt("id")));

                                if (jsonObject.getInt("profile_completed") == 1) {
                                    if (sharedpreference.getInro(WelcomeActivity.this)) {
                                        Intent intent = new Intent(WelcomeActivity.this, BottombarActivity.class);
                                        intent.putExtra("type", "home");
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.fade_in, R.anim.stay);
                                    }else {
                                        Intent intent = new Intent(WelcomeActivity.this, IntroActivity.class);
                                        intent.putExtra("type", "home");
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.fade_in, R.anim.stay);
                                    }
                                } else {
                                    setPassword();
                                }
                            }
                            Log.e("LLL_Response_username: ", response.toString());

                        } catch (Exception e) {
                            Log.e("LLLL_Error1: ", e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLL_Email_Error1: ", anError.getMessage());
                    }
                });
    }

    private void setUserPassword(String password) {
        String id = String.valueOf((sharedpreference.getUserId(WelcomeActivity.this)));
        AndroidNetworking.post(geturl.BASE_URL + "register")
                .addBodyParameter("user_id", id)
                .addBodyParameter("step", "5")
                .addBodyParameter("password", password)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("LLLL_response_5: ", response.toString());
                            setProfile();
                        } catch (Exception e) {
                            Log.e("LLLL_Error5: ", e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLL_Email_Error5: ", anError.getMessage());
                    }
                });
    }

    private void getProfession() {
        AndroidNetworking.post(geturl.BASE_URL + "get_profession")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        GetProfession getProfession = new Gson().fromJson(response.toString(), GetProfession.class);
                        getlist = getProfession.getData();
                        profession = new String[getlist.size()];
                        for (int i = 0; i < getlist.size(); i++) {
                            profession[i] = getlist.get(i).getName().trim();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLLL_Error_Profession:", anError.getMessage());
                    }
                });
    }

    private void setUserProfile(String full_name, String profession_id, String acc_privacy, String bio) {
        if (selectedimage == null) {
            selectedimage = new File("");
        }
        Log.e("LLLL_File: ", String.valueOf(selectedimage));
        String id = String.valueOf((sharedpreference.getUserId(WelcomeActivity.this)));
        AndroidNetworking.upload(geturl.BASE_URL + "register")
                .addMultipartParameter("user_id", id)
                .addMultipartParameter("step", "6")
                .addMultipartParameter("full_name", full_name)
                .addMultipartParameter("profession_id", profession_id)
                .addMultipartParameter("account_privacy", acc_privacy)
                .addMultipartFile("photo", selectedimage)
                .addMultipartParameter("bio", bio)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            regdataArrayList.clear();
                            JSONArray jsonArray = response.getJSONArray("data");
                            Log.e("LLLLL_data2: ", jsonArray.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Regdata regdata = new Regdata();
                                regdata.setId(jsonObject.getInt("id"));
                                regdata.setCurrentProfileStep(jsonObject.getInt("current_profile_step"));
                                regdataArrayList.add(regdata);

                                sharedpreference.setVeryStep(WelcomeActivity.this, jsonObject.getInt("current_profile_step"));
                                sharedpreference.setUserId(WelcomeActivity.this, String.valueOf(jsonObject.getInt("id")));
                            }

                            setBday();
                            Log.e("LLL_Res_6: ", response.toString());
                        } catch (Exception e) {
                            Log.e("LLLL_Error6: ", e.getMessage());
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLL_Email_Error6: ", anError.getMessage());
                    }
                });
    }

    private void setUserBday(String bday) {
        String id = String.valueOf((sharedpreference.getUserId(WelcomeActivity.this)));
        AndroidNetworking.post(geturl.BASE_URL + "register")
                .addBodyParameter("user_id", id)
                .addBodyParameter("step", "7")
                .addBodyParameter("birth_date", bday)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            regdataArrayList.clear();
                            JSONArray jsonArray = response.getJSONArray("data");
                            Log.e("LLLLL_data1: ", jsonArray.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Regdata regdata = new Regdata();
                                regdata.setId(jsonObject.getInt("id"));
                                regdata.setCurrentProfileStep(jsonObject.getInt("current_profile_step"));
                                regdataArrayList.add(regdata);

                                Log.e("LLL_Res: ", jsonObject.getString("_token"));

                                sharedpreference.setVeryStep(WelcomeActivity.this, jsonObject.getInt("current_profile_step"));
                                sharedpreference.setUserId(WelcomeActivity.this, String.valueOf(jsonObject.getInt("id")));


                                User user = new User();
                                user.setUserId(jsonObject.getString("id"));
                                user.setEmail(jsonObject.getString("email"));
                                user.setPassword("Pinal@123");
                                user.setDisplayName(jsonObject.getString("full_name"));
//                                user.setImageLink(jsonObject.getString("photo") + "");
                                user.setContactNumber(jsonObject.getString("mobile_code") + "" + jsonObject.getString("mobile_no"));
                                user.setAuthenticationTypeId(User.AuthenticationType.APPLOZIC.getValue());

                                Map<String, String> metadata = new HashMap<>();
                                metadata.put("Professionid", jsonObject.getString("profession_id") + "");
                                metadata.put("Username", jsonObject.getString("username"));
                                user.setMetadata(metadata);

                                Applozic.connectUser(WelcomeActivity.this, user, new AlLoginHandler() {
                                    @Override
                                    public void onSuccess(RegistrationResponse registrationResponse, Context context) {
                                        Public_Function.Hide_ProgressDialogue();
                                        Log.e("LLLLL_successfull", registrationResponse.toString());


                                        if (TextUtils.isEmpty(registrationResponse.getUserKey())) {
                                            return; // or break, continue, throw
                                        }
                                        try {
                                            AddChattoken(Integer.valueOf(jsonObject.getString("id")), registrationResponse.getUserKey());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        try {
                                            setWelcome(jsonObject.getString("username"),
                                                    jsonObject.getString("password"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                                        Log.d("mn13error", registrationResponse.toString());
                                        Public_Function.Hide_ProgressDialogue();
                                        Toasty.error(WelcomeActivity.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                                    }
                                });

                            }

                            Log.e("LLL_Response_7: ", response.toString());

                        } catch (Exception e) {
                            Toasty.error(WelcomeActivity.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                            Log.e("LLLL_Error7: ", e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toasty.error(WelcomeActivity.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                        Log.e("LLL_Email_Error7: ", anError.getMessage());
                    }
                });
    }

    private void login(String email, String token, String s_photo, String username, String fullname) {
        AndroidNetworking.post(geturl.BASE_URL + "login")
                .addBodyParameter("email", email)
                .addBodyParameter("social_token", token)
                .addBodyParameter("social_platform", "google")
                .addBodyParameter("social_photo", s_photo)
                .addBodyParameter("username", username)
                .addBodyParameter("full_name", fullname)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("LLLL_Login: ", response.toString());
                            if (response.getString("success") == "true") {

                                JSONObject jsonObject = response.getJSONArray("data").getJSONObject(0);
                                sharedpreference.setUserToken(WelcomeActivity.this, "Bearer " + jsonObject.getString("_token"));

                                if (jsonObject.getString("profile_completed").equalsIgnoreCase("1")) {
                                    User user = new User();
                                    user.setUserId(String.valueOf(jsonObject.get("id")));
                                    user.setPassword("Pinal@123");
                                    user.setDisplayName(jsonObject.getString("full_name"));
                                    user.setContactNumber(jsonObject.getString("mobile_code") + "" + jsonObject.getString("mobile_no"));
                                    user.setAuthenticationTypeId(User.AuthenticationType.APPLOZIC.getValue());

                                    Applozic.connectUser(WelcomeActivity.this, user, new AlLoginHandler() {
                                        @Override
                                        public void onSuccess(RegistrationResponse registrationResponse, final Context context) {
                                            Public_Function.Hide_ProgressDialogue();

                                            ApplozicClient.getInstance(context).setContextBasedChat(true);

                                            Map<ApplozicSetting.RequestCode, String> activityCallbacks = new HashMap<ApplozicSetting.RequestCode, String>();
                                            activityCallbacks.put(ApplozicSetting.RequestCode.USER_LOOUT, WelcomeActivity.class.getName());
                                            activityCallbacks.put(ApplozicSetting.RequestCode.AUDIO_CALL, AudioCallActivityV2.class.getName());
                                            activityCallbacks.put(ApplozicSetting.RequestCode.VIDEO_CALL, VideoActivity.class.getName());
                                            ApplozicSetting.getInstance(context).setActivityCallbacks(activityCallbacks);

                                            Applozic.registerForPushNotification(context, Applozic.getInstance(context).getDeviceRegistrationId(), new AlPushNotificationHandler() {
                                                @Override
                                                public void onSuccess(RegistrationResponse registrationResponse) {

                                                }

                                                @Override
                                                public void onFailure(RegistrationResponse registrationResponse, Exception exception) {

                                                }
                                            });

                                            try {
                                                sharedpreference.setusername(WelcomeActivity.this, jsonObject.getString("username") + "");
                                                sharedpreference.setUserId(WelcomeActivity.this, jsonObject.getString("id") + "");
                                                sharedpreference.setnumber(WelcomeActivity.this, jsonObject.getString("mobile_no"));
                                                sharedpreference.setDailcode(WelcomeActivity.this, jsonObject.getString("mobile_code"));
                                                sharedpreference.setfirstname(WelcomeActivity.this, jsonObject.getString("full_name"));
                                                sharedpreference.setEmail(WelcomeActivity.this, jsonObject.getString("email"));
                                                sharedpreference.setphoto(WelcomeActivity.this, jsonObject.getString("photo"));
                                                sharedpreference.setCoverPhoto(WelcomeActivity.this, jsonObject.getString("cover_photo"));
                                                sharedpreference.setBio(WelcomeActivity.this, jsonObject.getString("bio") + "");
                                                sharedpreference.setlogin(WelcomeActivity.this, true);
                                                //starting main MainActivity
                                                Public_Function.Hide_ProgressDialogue();
                                                sharedpreference.setUserId(WelcomeActivity.this, String.valueOf(jsonObject.getInt("id")));
                                                if (sharedpreference.getInro(WelcomeActivity.this)) {
                                                    Intent intent = new Intent(WelcomeActivity.this, BottombarActivity.class);
                                                    intent.putExtra("type", "home");
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.fade_in, R.anim.stay);
                                                    finish();
                                                }else {
                                                    Intent intent = new Intent(WelcomeActivity.this, IntroActivity.class);
                                                    intent.putExtra("type", "home");
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.fade_in, R.anim.stay);
                                                    finish();
                                                }
                                                overridePendingTransition(R.anim.fade_in, R.anim.stay);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        @Override
                                        public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                                            // If any failure in registration the callback  will come here
                                            Public_Function.Hide_ProgressDialogue();
                                            Toasty.error(WelcomeActivity.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();

                                        }
                                    });

                                } else {
                                    if (jsonObject.getInt("current_profile_step") == 1) {
                                        is_email = false;
                                        signUp_1();
                                    }

                                    if (jsonObject.getInt("current_profile_step") == 2) {
                                        signUp_1();
                                    }

                                    if (jsonObject.getInt("current_profile_step") == 3) {
                                        suggestUsername();
                                    }

                                    if (jsonObject.getInt("current_profile_step") == 4) {
                                        setPassword();
                                    }

                                    if (jsonObject.getInt("current_profile_step") == 5) {
                                        setProfile();
                                    }

                                    if (jsonObject.getInt("current_profile_step") == 6) {
                                        setBday();
                                    }

                                    if (jsonObject.getInt("current_profile_step") == 7) {
                                        setWelcome(jsonObject.getString("username"),
                                                jsonObject.getString("password"));
                                    }
                                }


                            }else {
                                Toasty.error(WelcomeActivity.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toasty.error(WelcomeActivity.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toasty.error(WelcomeActivity.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                    }
                });
    }

    @SuppressLint("HardwareIds")
    private void loginWithou(String login_key, String password) {
        android_id = Settings.Secure.getString(WelcomeActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Log.e("LLLLL_login_key: ",login_key+"   "+"\n password: "+password+"\n token: "+android_id);

        AndroidNetworking.post(geturl.BASE_URL + "login")
                .addBodyParameter("login_key", login_key)
                .addBodyParameter("password", password)
                .addBodyParameter("token", android_id)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("LLLL_Data: ", response.toString());
                            if (response.getString("success").equalsIgnoreCase("true")) {

                                JSONObject jsonObject = response.getJSONArray("data").getJSONObject(0);
                                sharedpreference.setUserToken(WelcomeActivity.this, "Bearer " + jsonObject.getString("_token"));

                                User user = new User();
                                user.setUserId(String.valueOf(jsonObject.get("id")));
                                user.setDisplayName(jsonObject.getString("full_name"));
                                user.setPassword("Pinal@123");
                                user.setAuthenticationTypeId(User.AuthenticationType.APPLOZIC.getValue());

                                Log.e("LLLLL_Send: ",String.valueOf(jsonObject.get("id"))+"     "+
                                        User.AuthenticationType.APPLOZIC.getValue());

                                Applozic.connectUser(WelcomeActivity.this, user, new AlLoginHandler() {
                                    @Override
                                    public void onSuccess(RegistrationResponse registrationResponse, final Context context) {
                                        Public_Function.Hide_ProgressDialogue();

                                        ApplozicClient.getInstance(context).setContextBasedChat(true);

                                        Map<ApplozicSetting.RequestCode, String> activityCallbacks = new HashMap<ApplozicSetting.RequestCode, String>();
                                        activityCallbacks.put(ApplozicSetting.RequestCode.USER_LOOUT, WelcomeActivity.class.getName());
                                        activityCallbacks.put(ApplozicSetting.RequestCode.AUDIO_CALL, AudioCallActivityV2.class.getName());
                                        activityCallbacks.put(ApplozicSetting.RequestCode.VIDEO_CALL, VideoActivity.class.getName());
                                        ApplozicSetting.getInstance(context).setActivityCallbacks(activityCallbacks);

                                        Applozic.registerForPushNotification(context, Applozic.getInstance(context).getDeviceRegistrationId(), new AlPushNotificationHandler() {
                                            @Override
                                            public void onSuccess(RegistrationResponse registrationResponse) {

                                            }

                                            @Override
                                            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {

                                            }
                                        });

                                        try {
                                            sharedpreference.setusername(WelcomeActivity.this, jsonObject.getString("username") + "");
                                            sharedpreference.setUserId(WelcomeActivity.this, jsonObject.getString("id") + "");
                                            sharedpreference.setnumber(WelcomeActivity.this, jsonObject.getString("mobile_no"));
                                            sharedpreference.setDailcode(WelcomeActivity.this, jsonObject.getString("mobile_code"));
                                            sharedpreference.setfirstname(WelcomeActivity.this, jsonObject.getString("full_name"));
                                            sharedpreference.setEmail(WelcomeActivity.this, jsonObject.getString("email"));
                                            sharedpreference.setphoto(WelcomeActivity.this, jsonObject.getString("photo"));
                                            sharedpreference.setCoverPhoto(WelcomeActivity.this, jsonObject.getString("cover_photo"));
                                            sharedpreference.setBio(WelcomeActivity.this, jsonObject.getString("bio") + "");
                                            sharedpreference.setlogin(WelcomeActivity.this, true);
                                            //starting main MainActivity
                                            Public_Function.Hide_ProgressDialogue();
                                            sharedpreference.setUserId(WelcomeActivity.this, String.valueOf(jsonObject.getInt("id")));
                                            if (sharedpreference.getInro(WelcomeActivity.this)) {
                                                Intent intent = new Intent(WelcomeActivity.this, BottombarActivity.class);
                                                intent.putExtra("type", "home");
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.fade_in, R.anim.stay);
                                                finish();
                                            } else {
                                                Intent intent = new Intent(WelcomeActivity.this, IntroActivity.class);
                                                intent.putExtra("type", "home");
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.fade_in, R.anim.stay);
                                                finish();
                                            }
                                            overridePendingTransition(R.anim.fade_in, R.anim.stay);

                                        } catch (JSONException e) {
                                            Toasty.error(WelcomeActivity.this,"1: ",Toast.LENGTH_LONG,true).show();
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                                        // If any failure in registration the callback  will come here
                                        Public_Function.Hide_ProgressDialogue();
                                        Log.e("LLLLL_Mes: ",registrationResponse.toString());
                                        Toasty.error(WelcomeActivity.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
//                                        Toasty.error(WelcomeActivity.this, "5: "+registrationResponse.toString(), Toast.LENGTH_LONG, true).show();
                                    }
                                });

                            } else {
                                Toasty.error(WelcomeActivity.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toasty.error(WelcomeActivity.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toasty.error(WelcomeActivity.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult res = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = res.getUri();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    img_profile.setImageBitmap(bitmap);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
                    if (!wallpaperDirectory.exists()) {
                        wallpaperDirectory.mkdirs();
                    }
                    try {
                        File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
                        f.createNewFile();
                        f.getAbsoluteFile();
                        selectedimage = f;
                        flag = true;
                        //   uploadpic();
                        Log.e("selectedfile", f + "");
                        FileOutputStream fo = new FileOutputStream(f);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = res.getError();
                Log.e("LLL_File_Error: ", error.getMessage());
            }
        }

        if (requestCode == RC_SIGN_IN) {
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                emailSet("1", account.getEmail());
                Log.e("LLL_Display_Name: ", account.getDisplayName());
                Log.e("LLL_Email: ", account.getEmail());
                email = account.getEmail();
                token = account.getIdToken();
                username = account.getGivenName().toLowerCase();
                fullname = account.getFamilyName();
                s_photo = String.valueOf(account.getPhotoUrl());

                login(email, token, s_photo, username, fullname);
                Log.e("LLL_Server_Photo: ", username);
                Log.e("LLL_Server_Photo: ", account.getFamilyName());
            } catch (ApiException e) {
                Log.e("LLL_SIgn_failed: ", e.getMessage());
                e.printStackTrace();
            }

        }

    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    private void requestPermissions(boolean requestCamera) {
        Activity activity = null;
        Context context = WelcomeActivity.this;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                activity = (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }

        List<String> permissions = new ArrayList<>();
        if (requestCamera) permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.RECORD_AUDIO);
        if (activity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(permissions.toArray(new String[0]),
                        PERMISSION_REQUEST_CODE);
            }
        }
    }

    @SuppressLint("NewApi")
    protected boolean checkPermissions() {
        // Manifest is OK at this point. Let's check runtime permissions.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;

        Context c = WelcomeActivity.this;
        boolean needsCamera = true;

        needsCamera = needsCamera && c.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED;

        if (needsCamera) {
            requestPermissions(needsCamera);
            return false;
        }
        return true;
    }

    public void AddChattoken(Integer id, String userKey) {

        Public_Function.Show_Progressdialog(WelcomeActivity.this);

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", id + ""));
        paramArrayList.add(new param("chat_key", userKey));

        new geturl().getdata(WelcomeActivity.this, new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {
                try {
                    Public_Function.Hide_ProgressDialogue();

                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("message");


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, paramArrayList, "update_chatkey");
    }

    @Override
    public void onBackPressed() {

        if (dialog7.isShowing() && dialog7 != null) {
            dialog7.dismiss();
            signUp_1();
        }
        if (dialog6.isShowing() && dialog6 != null) {
            dialog6.dismiss();
            setBday();
        }
        if (dialog5.isShowing() && dialog5 != null) {
            dialog5.dismiss();
            setProfile();
        }
        if (dialog4.isShowing() && dialog4 != null) {
            dialog4.dismiss();
            setPassword();
        }
        if (dialog3.isShowing() && dialog3 != null) {
            dialog3.dismiss();
            suggestUsername();
        }
        if (dialog2.isShowing() && dialog2 != null) {
            dialog2.dismiss();
            verifyOTP();
        }
        if (dialog1.isShowing() && dialog1 != null) {
            dialog1.dismiss();
            signUp_1();
        }
        if (dialog.isShowing() && dialog != null) {
            dialog.dismiss();
            finish();
        } else {
            finish();
        }
    }
}
