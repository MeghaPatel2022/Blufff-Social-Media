package com.TBI.Client.Bluff.Activity.Profile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.TBI.Client.Bluff.Activity.Home.OpenCamera1;
import com.TBI.Client.Bluff.Activity.Settings.Setting;
import com.TBI.Client.Bluff.Adapter.Profile.Profession_Adapter;
import com.TBI.Client.Bluff.Database.DBHelper;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.Model.GetFeed.Image;
import com.TBI.Client.Bluff.Model.GetProfession.DatumProfession;
import com.TBI.Client.Bluff.Model.GetProfile.Datum;
import com.TBI.Client.Bluff.Model.GetProfile.Friend;
import com.TBI.Client.Bluff.Model.GetProfile.GetProfile;
import com.TBI.Client.Bluff.Model.GetProfile.Other;
import com.TBI.Client.Bluff.Model.GetProfile.Post;
import com.TBI.Client.Bluff.Model.PostDetail.Comment;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.UserPages.WelcomeActivity;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.MyAsyncTaskCallback;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.applozic.audiovideo.activity.AudioCallActivityV2;
import com.applozic.audiovideo.activity.VideoActivity;
import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.User;
import com.applozic.mobicomkit.api.account.user.UserService;
import com.applozic.mobicomkit.listners.AlCallback;
import com.applozic.mobicomkit.listners.AlLoginHandler;
import com.applozic.mobicomkit.listners.AlPushNotificationHandler;
import com.applozic.mobicomkit.uiwidgets.ApplozicSetting;
import com.bumptech.glide.Glide;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.gson.Gson;
import com.rilixtech.CountryCodePicker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.TBI.Client.Bluff.Utils.Public_Function.hasPermissions;

public class EditProfile extends AppCompatActivity {

    public static final int PERMISSION_ALL = 200;
    private static final String IMAGE_DIRECTORY = "/Blufff";
    public static boolean update;
    public static List<Datum> updatearray = new ArrayList<Datum>();
    @BindView(R.id.imguser)
    CircleImageView imguser;
    @BindView(R.id.txtchange)
    TextView txtchange;
    @BindView(R.id.edtfnamae)
    EditText edtfnamae;
    @BindView(R.id.edtuname)
    EditText edtuname;
    @BindView(R.id.edtemail)
    EditText edtemail;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    @BindView(R.id.edtmobile)
    EditText edtmobile;
    @BindView(R.id.edtdescription)
    EditText edtdescription;
    @BindView(R.id.spinnerprofession)
    Spinner spinnerprofession;
    @BindView(R.id.empty)
    TextView empty;
    @BindView(R.id.tool_bar)
    Toolbar tool_bar;
    @BindView(R.id.txtdone)
    TextView txtdone;
    @BindView(R.id.switchaccount)
    LabeledSwitch switchaccount;
    @BindView(R.id.imgCover)
    ImageView imgCover;

    ConnectionDetector cd;
    boolean isInternetPresent = false;
    boolean flag = false;
    File selectedimage;
    File selectedCoverImg;
    List<Datum> profilemodel = new ArrayList<Datum>();
    List<DatumProfession> profesiionarray = new ArrayList<DatumProfession>();
    Profession_Adapter profession_adapter;
    String profession_id = "";
    private int mSelectedIndexmain = 0;
    private DBHelper dbHelper;
    static boolean isCover = false;
    static boolean isCoverSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (sharedpreference.getTheme(EditProfile.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_editprofile);
        ButterKnife.bind(this);
        dbHelper = new DBHelper(EditProfile.this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(EditProfile.this));
        if (sharedpreference.getTheme(EditProfile.this).equalsIgnoreCase("white")) {
            txtdone.setTextColor(getResources().getColor(R.color.black));
        } else {
            txtdone.setTextColor(getResources().getColor(R.color.white));
        }


        Typeface myFont = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            myFont = getResources().getFont(R.font.poppins_regular);
            ccp.setTypeFace(myFont);
        }

        setSupportActionBar(tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (sharedpreference.getTheme(EditProfile.this).equalsIgnoreCase("white")) {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        } else {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }

        imguser.setOnClickListener(v -> {

            String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CAMERA};

            if (!hasPermissions(EditProfile.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(EditProfile.this, PERMISSIONS, PERMISSION_ALL);
            } else {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(EditProfile.this);
            }

        });

        imgCover.setOnClickListener(v -> {

            String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CAMERA};

            if (!hasPermissions(EditProfile.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(EditProfile.this, PERMISSIONS, PERMISSION_ALL);
            } else {
                isCoverSelect = true;
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(EditProfile.this);
            }

        });

        txtchange.setOnClickListener(view -> {

            String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CAMERA};

            if (!hasPermissions(EditProfile.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(EditProfile.this, PERMISSIONS, PERMISSION_ALL);
            } else {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(EditProfile.this);
            }

        });

        cd = new ConnectionDetector(EditProfile.this);
        isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent) {
            Toasty.warning(EditProfile.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
        } else {
            Get_Privacy();
        }
        GetProfile();

        switchaccount.setOnToggledListener((toggleableView, isOn) -> {
            int privacy = 2;

            if (isOn) {
                privacy = 2;
                sharedpreference.setAccountType(EditProfile.this, "2");
                Log.d("mn13", "true");
            } else {
                privacy = 1;
                sharedpreference.setAccountType(EditProfile.this, "1");
                Log.d("mn13", "false");
            }

            cd = new ConnectionDetector(EditProfile.this);
            isInternetPresent = cd.isConnectingToInternet();

            if (!isInternetPresent) {
                Toasty.warning(EditProfile.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
            } else {
                Change_Privacy(privacy);
            }
        });

        txtdone.setOnClickListener(view -> {

            edtmobile.setError(null);
            edtemail.setError(null);
            edtuname.setError(null);
            edtfnamae.setError(null);
            edtdescription.setError(null);

            if (edtfnamae.getText().toString().trim().equalsIgnoreCase("")) {
                edtfnamae.setError("Required");
                edtfnamae.requestFocus();
            } else if (edtuname.getText().toString().trim().equalsIgnoreCase("")) {
                edtuname.setError("Required");
                edtuname.requestFocus();
            } else if (edtemail.getText().toString().trim().equalsIgnoreCase("")) {
                edtemail.setError("Required");
                edtemail.requestFocus();
            } else if (edtmobile.getText().toString().trim().equalsIgnoreCase("")) {
                edtmobile.setError("Required");
                edtmobile.requestFocus();
            } else if (profession_id.equalsIgnoreCase("")) {
                Toasty.warning(EditProfile.this, "Please Select Profession...", Toast.LENGTH_LONG, true).show();
            } else {

                cd = new ConnectionDetector(EditProfile.this);
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    Toasty.warning(EditProfile.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    Edit_Profile();
                }

            }
        });

    }

    public void Get_Privacy() {

        Public_Function.Show_Progressdialog(EditProfile.this);


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(EditProfile.this)));


        new geturl().getdata(EditProfile.this, new MyAsyncTaskCallback() {
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
                        Toasty.error(EditProfile.this, status + "", Toast.LENGTH_LONG, true).show();
                    }
                } catch (Exception e) {
                    Toasty.error(EditProfile.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                    e.printStackTrace();

                }
            }
        }, paramArrayList, "get_user_privacy");
    }


    public void Change_Privacy(int privacy) {

        Public_Function.Show_Progressdialog(EditProfile.this);


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(EditProfile.this)));
        paramArrayList.add(new param("privacy", privacy + ""));


        new geturl().getdata(EditProfile.this, new MyAsyncTaskCallback() {
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
                        Toasty.error(EditProfile.this, status + "", Toast.LENGTH_LONG, true).show();
                    }
                } catch (Exception e) {
                    Toasty.error(EditProfile.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_out);
        return super.onOptionsItemSelected(item);
    }

    public void Edit_Profile() {

        Public_Function.Show_Progressdialog(EditProfile.this);

        try {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            if (flag) {
                builder.addFormDataPart("photo", selectedimage.getName(), RequestBody.create(MediaType.parse("image/*"), selectedimage));
            } else {
                builder.addFormDataPart("photo", "");
            }

            if (isCover){
                builder.addFormDataPart("cover", selectedCoverImg.getName(), RequestBody.create(MediaType.parse("image/*"), selectedCoverImg));
            } else {
                builder.addFormDataPart("cover", "");
            }

            builder.addFormDataPart("user_id", String.valueOf(sharedpreference.getUserId(EditProfile.this)));
            builder.addFormDataPart("username", edtuname.getText().toString());
            builder.addFormDataPart("full_name", edtfnamae.getText().toString());
            builder.addFormDataPart("email", edtemail.getText().toString());
            builder.addFormDataPart("mobile_code", ccp.getSelectedCountryCodeWithPlus());
            builder.addFormDataPart("mobile_no", edtmobile.getText().toString());
            builder.addFormDataPart("profession_id", profession_id);
            builder.addFormDataPart("bio", edtdescription.getText().toString().trim());

            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
                    .addHeader("Authorization", sharedpreference.getUserToken(EditProfile.this))
                    .url(getString(R.string.url1) + "edit_profile")
                    .post(requestBody)
                    .build();


            OkHttpClient client = new OkHttpClient();
            OkHttpClient.Builder builder1 = new OkHttpClient.Builder();
            builder1.connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES);
            client = builder1.build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    EditProfile.this.runOnUiThread(() -> {
                        Public_Function.Hide_ProgressDialogue();
                        Toasty.error(EditProfile.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                    });

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    Public_Function.Hide_ProgressDialogue();
                    Log.e("LLLL_Res: ", response.body().toString());

                    EditProfile.this.runOnUiThread(() -> {
                        if (response.message().equalsIgnoreCase("OK")) {

                            try {
                                GetProfile login = new Gson().fromJson(response.body().string(), GetProfile.class);
                                if (login.getSuccess()) {

                                    if (!login.getData().isEmpty()) {

                                        User user = new User();
                                        user.setUserId(String.valueOf(login.getData().get(0).getId()));
                                        user.setEmail(login.getData().get(0).getEmail() + "");
                                        user.setPassword("Pinal@123");
                                        user.setDisplayName(login.getData().get(0).getFullName() + "");
                                        user.setImageLink(login.getData().get(0).getPhoto() + "");
                                        user.setContactNumber(ccp.getSelectedCountryCodeWithPlus() + "" + edtmobile.getText().toString());

                                        Map<String, String> metadata = new HashMap<>();
                                        metadata.put("Professionid", login.getData().get(0).getProfessionId() + "");
                                        metadata.put("Username", login.getData().get(0).getUsername());
                                        user.setMetadata(metadata);
                                        //user.setMetadata(metadata);

                                        user.setAuthenticationTypeId(User.AuthenticationType.APPLOZIC.getValue());

                                        Applozic.connectUser(EditProfile.this, user, new AlLoginHandler() {
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

                                            }

                                            @Override
                                            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                                                // If any failure in registration the callback  will come here
                                                Log.e("LLLL_Login_Failed: ", registrationResponse.getMessage() + "");
                                                Toasty.error(EditProfile.this, registrationResponse.getMessage() + "", Toast.LENGTH_LONG, true).show();

                                            }
                                        });

                                        UserService.getInstance(EditProfile.this).updateUser(user, new AlCallback() {
                                            @Override
                                            public void onSuccess(Object response1) {
                                                Public_Function.Hide_ProgressDialogue();

                                                Log.d("User", "Update success ");
                                                sharedpreference.setfirstname(EditProfile.this, login.getData().get(0).getFullName() + "");
                                                sharedpreference.setusername(EditProfile.this, login.getData().get(0).getUsername() + "");
                                                sharedpreference.setEmail(EditProfile.this, login.getData().get(0).getEmail() + "");
                                                sharedpreference.setBio(EditProfile.this, login.getData().get(0).getBio() + "");
                                                sharedpreference.setDailcode(EditProfile.this, login.getData().get(0).getMobileCode() + "");
                                                sharedpreference.setnumber(EditProfile.this, login.getData().get(0).getMobileNo() + "");
                                                sharedpreference.setphoto(EditProfile.this, login.getData().get(0).getPhoto() + "");
                                                sharedpreference.setCoverPhoto(EditProfile.this, login.getData().get(0).getCoverPhoto() + "");

//                                                dbHelper.updateUserDetails(String.valueOf(sharedpreference.getUserId(EditProfile.this)),
//                                                        login.getData().toString(),
//                                                        login.getOthers().toString(),
//                                                        login.getProfessions().toString(),
//                                                        login.getFriends().toString());
                                                Intent intent = new Intent(EditProfile.this, ProfilePage.class);
                                                intent.putExtra("type", "left");
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.fade_in, R.anim.stay);
                                                finish();
                                            }

                                            @Override
                                            public void onError(Object error) {
                                                Log.e("LLLL_Edit_Error3: ", "Update failed ");
                                                Public_Function.Hide_ProgressDialogue();
                                                Toasty.error(EditProfile.this, error.toString() + "", Toast.LENGTH_SHORT, true).show();
                                            }
                                        });

                                    }
                                } else {
                                    Public_Function.Hide_ProgressDialogue();
                                    Toasty.error(EditProfile.this, login.getMessage() + "", Toast.LENGTH_SHORT, true).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                Public_Function.Hide_ProgressDialogue();
                                Log.d("LLLL_Edit_Error1: ", e.toString());
                                Toasty.error(EditProfile.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                            }
                        } else {
                            Public_Function.Hide_ProgressDialogue();
                            Log.d("LLLL_Edit_Error2: ", "14");
                            Toasty.error(EditProfile.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                        }
                    });
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void GetProfile() {

//        if (dbHelper.chceckUserDetails(Integer.parseInt(sharedpreference.getUserId(EditProfile.this)))) {
//            GetProfile login = getUserDetails();
//
//            Log.e("LLLLL_Login_data: ", String.valueOf(getUserDetails().getData()));
//            profilemodel = login.getData();
//            profesiionarray = login.getProfessions();
//
//            if (!profilemodel.isEmpty()) {
//                txtdone.setVisibility(View.VISIBLE);
//                edtfnamae.setText(profilemodel.get(0).getFullName());
//                edtuname.setText(profilemodel.get(0).getUsername());
//                edtemail.setText(profilemodel.get(0).getEmail());
//                edtmobile.setText(profilemodel.get(0).getMobileNo());
//                edtdescription.setText(profilemodel.get(0).getBio());
//
//                ccp.setCountryForPhoneCode(Integer.parseInt(profilemodel.get(0).getMobileCode()));
//
//                sharedpreference.setusername(EditProfile.this, profilemodel.get(0).getUsername() + "");
//                sharedpreference.setUserId(EditProfile.this, String.valueOf(profilemodel.get(0).getId()));
//                sharedpreference.setnumber(EditProfile.this, profilemodel.get(0).getMobileNo());
//                sharedpreference.setDailcode(EditProfile.this, profilemodel.get(0).getMobileCode());
//                sharedpreference.setfirstname(EditProfile.this, profilemodel.get(0).getFullName());
//                sharedpreference.setEmail(EditProfile.this, profilemodel.get(0).getEmail());
//                sharedpreference.setBio(EditProfile.this, profilemodel.get(0).getBio() + "");
//
//                if (!profilemodel.get(0).getPhoto().equals("") && !profilemodel.get(0).getPhoto().equals(null) && !profilemodel.get(0).getPhoto().equals("null")) {
//                    sharedpreference.setphoto(EditProfile.this, profilemodel.get(0).getPhoto());
//                    imguser.setVisibility(View.VISIBLE);
//                    Glide.with(EditProfile.this)
//                            .load(profilemodel.get(0).getPhoto())
//                            .dontAnimate()
//                            .into(imguser);
//
//
//                }
//            }
//
//            profession_adapter = new Profession_Adapter(EditProfile.this, profesiionarray) {
//                public View getView(int position, View convertView, ViewGroup parent) {
//                    TextView tv = (TextView) super.getView(position, convertView, parent);
//                    if (sharedpreference.getTheme(EditProfile.this).equalsIgnoreCase("white")) {
//                        tv.setTextColor(getResources().getColor(RM.color.black));
//                    } else {
//                        tv.setTextColor(getResources().getColor(RM.color.white));
//                    }
//                    tv.setBackgroundColor(getResources().getColor(android.RM.color.transparent));
//                    return tv;
//                }
//
//                @Override
//                public View getDropDownView(int position, View convertView, ViewGroup parent) {
//                    View v = null;
//                    v = super.getDropDownView(position, null, parent);
//                    TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
//                    tv.setTextColor(getResources().getColor(RM.color.black));
//
//                    if (position == mSelectedIndexmain) {
//                        tv.setBackgroundColor(getResources().getColor(RM.color.darkgrey));
//                        tv.setTextColor(Color.WHITE);
//
//                    } else {
//                        tv.setBackgroundColor(Color.WHITE);
//                        tv.setTextColor(getResources().getColor(RM.color.black));
//
//                    }
//                    return tv;
//                }
//            };
//
//            spinnerprofession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    // adapterView.getChildAt(0).setVisibility(View.GONE);
//                    mSelectedIndexmain = i;
//                    profession_id = profesiionarray.get(i).getId() + "";
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
//            spinnerprofession.setAdapter(profession_adapter);
//
//            if (!profilemodel.isEmpty()) {
//
//
//                profession_id = profilemodel.get(0).getProfessionId() + "";
//
//                for (int i = 0; i < profesiionarray.size(); i++) {
//
//                    if (Integer.parseInt(profession_id) == profesiionarray.get(i).getId()) {
//                        spinnerprofession.setSelection(i);
//                    }
//                }
//
//            }
//
//
//        }
//        else {
        if (!isInternetPresent) {
            Toasty.warning(EditProfile.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
        } else {
            ArrayList<param> paramArrayList = new ArrayList<>();
            paramArrayList.add(new param("user_id", sharedpreference.getUserId(EditProfile.this)));

            new geturl().getdata(EditProfile.this, new MyAsyncTaskCallback() {
                @Override
                public void onAsyncTaskComplete(String data) {
                    try {
                        JSONObject object = new JSONObject(data);
                        boolean message = object.optBoolean("success");
                        String status = "";
                        status = object.optString("message");

                        Log.e("LLLLLL_resProfile: ",data);

                        if (message) {
                            GetProfile login = new Gson().fromJson(data, GetProfile.class);
                            profilemodel = login.getData();
                            profesiionarray = login.getProfessions();

                            if (!dbHelper.chceckUserDetails(Integer.parseInt(sharedpreference.getUserId(EditProfile.this)))) {
                                dbHelper.insertUserDetails(String.valueOf(sharedpreference.getUserId(EditProfile.this)),
                                        login.getData().toString(),
                                        login.getOthers().toString(),
                                        login.getProfessions().toString(),
                                        login.getFriends().toString());
                            } else {
                                dbHelper.updateUserDetails(String.valueOf(sharedpreference.getUserId(EditProfile.this)),
                                        login.getData().toString(),
                                        login.getOthers().toString(),
                                        login.getProfessions().toString(),
                                        login.getFriends().toString());
                            }

                            if (!profilemodel.isEmpty()) {
                                txtdone.setVisibility(View.VISIBLE);
                                edtfnamae.setText(profilemodel.get(0).getFullName());
                                edtuname.setText(profilemodel.get(0).getUsername());
                                edtemail.setText(profilemodel.get(0).getEmail());
                                edtmobile.setText(profilemodel.get(0).getMobileNo());
                                edtdescription.setText(profilemodel.get(0).getBio());

                                ccp.setCountryForPhoneCode(Integer.parseInt(profilemodel.get(0).getMobileCode()));

                                sharedpreference.setusername(EditProfile.this, profilemodel.get(0).getUsername() + "");
                                sharedpreference.setUserId(EditProfile.this, String.valueOf(profilemodel.get(0).getId()));
                                sharedpreference.setnumber(EditProfile.this, profilemodel.get(0).getMobileNo());
                                sharedpreference.setDailcode(EditProfile.this, profilemodel.get(0).getMobileCode());
                                sharedpreference.setfirstname(EditProfile.this, profilemodel.get(0).getFullName());
                                sharedpreference.setEmail(EditProfile.this, profilemodel.get(0).getEmail());
                                sharedpreference.setBio(EditProfile.this, profilemodel.get(0).getBio() + "");

                                if (!profilemodel.get(0).getPhoto().equals("") && profilemodel.get(0).getPhoto() != null && !profilemodel.get(0).getPhoto().equals("null")) {
                                    sharedpreference.setphoto(EditProfile.this, profilemodel.get(0).getPhoto());
                                    imguser.setVisibility(View.VISIBLE);
                                    Glide.with(EditProfile.this)
                                            .load(profilemodel.get(0).getPhoto())
                                            .dontAnimate()
                                            .into(imguser);
                                }

                                if (!profilemodel.get(0).getCoverPhoto().equals("") && profilemodel.get(0).getCoverPhoto() != null && !profilemodel.get(0).getCoverPhoto().equals("null")) {
                                    sharedpreference.setCoverPhoto(EditProfile.this, profilemodel.get(0).getCoverPhoto());
                                    imguser.setVisibility(View.VISIBLE);
                                    Glide.with(EditProfile.this)
                                            .load(profilemodel.get(0).getCoverPhoto())
                                            .dontAnimate()
                                            .into(imgCover);
                                }
                            }

                            profession_adapter = new Profession_Adapter(EditProfile.this, profesiionarray) {
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    TextView tv = (TextView) super.getView(position, convertView, parent);
                                    if (sharedpreference.getTheme(EditProfile.this).equalsIgnoreCase("white")) {
                                        tv.setTextColor(getResources().getColor(R.color.black));
                                    } else {
                                        tv.setTextColor(getResources().getColor(R.color.white));
                                    }
                                    tv.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                                    return tv;
                                }

                                @Override
                                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                    View v = null;
                                    v = super.getDropDownView(position, null, parent);
                                    TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
                                    tv.setTextColor(getResources().getColor(R.color.black));

                                    if (position == mSelectedIndexmain) {
                                        tv.setBackgroundColor(getResources().getColor(R.color.darkgrey));
                                        tv.setTextColor(Color.WHITE);

                                    } else {
                                        tv.setBackgroundColor(Color.WHITE);
                                        tv.setTextColor(getResources().getColor(R.color.black));

                                    }
                                    return tv;
                                }
                            };

                            spinnerprofession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    // adapterView.getChildAt(0).setVisibility(View.GONE);
                                    mSelectedIndexmain = i;
                                    profession_id = profesiionarray.get(i).getId() + "";
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            spinnerprofession.setAdapter(profession_adapter);

                            if (!profilemodel.isEmpty()) {


                                profession_id = profilemodel.get(0).getProfessionId() + "";

                                for (int i = 0; i < profesiionarray.size(); i++) {

                                    if (Integer.parseInt(profession_id) == profesiionarray.get(i).getId()) {
                                        spinnerprofession.setSelection(i);
                                    }
                                }

                            }

                        }


                    } catch (Exception e) {
                        // Toasty.error(Login.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                        e.printStackTrace();
                    }
//                if (profilemodel.isEmpty()){
//                    profilemodel.addAll(getPostRecords());
//                }

                }
            }, paramArrayList, "get_profile");
        }
//        }
    }

    private GetProfile getUserDetails() {

        GetProfile getProfile = new GetProfile();
        Cursor cur = dbHelper.getAllUserDetails();

        Datum[] datum;
        Other[] other;
        DatumProfession[] profession;
        Friend[] friend;
        Gson gson = new Gson();
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                GetProfile getProfile1 = new GetProfile();
                datum = gson.fromJson(cur.getString(2), Datum[].class);
                getProfile1.setData(Arrays.asList(datum));

                other = gson.fromJson(cur.getString(3), Other[].class);
                getProfile1.setOthers(Arrays.asList(other));

                profession = gson.fromJson(cur.getString(4), DatumProfession[].class);
                getProfile1.setProfessions(Arrays.asList(profession));

//                friend = gson.fromJson(cur.getString(5), Friend[].class);
//                getProfile1.setFriends(Arrays.asList(friend));

                getProfile = getProfile1;
            } while (cur.moveToNext());
            cur.close();
        }
        return getProfile;
    }


    private ArrayList<Post> getPostRecords() {
        Cursor cur = dbHelper.getAll_Records();
        ArrayList<Post> posts = new ArrayList<>();
        ArrayList<Image> images = null;
        ArrayList<Comment> comments = new ArrayList<>();
        Image[] image;
        Comment[] comment;
        Gson gson = new Gson();
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                Post post = new Post();

                post.setId(cur.getInt(1));
                post.setUserId(cur.getInt(2));
                post.setDescription(cur.getString(3));
                post.setLocation(cur.getString(4));
                post.setLat(cur.getString(5));
                post.setLang(cur.getString(6));
                post.setUsername(cur.getString(7));
                post.setFullName(cur.getString(8));
                post.setPhoto(cur.getString(9));
                post.setImage(cur.getString(10));
                post.setFileType(cur.getString(11));
                image = gson.fromJson(cur.getString(12), Image[].class);
                post.setImages(Arrays.asList(image));
                comment = gson.fromJson(cur.getString(13), Comment[].class);
                post.setComments(Arrays.asList(comment));
                post.setMultipleImages(cur.getInt(14));
                post.setBookmarked(cur.getInt(15));
                post.setMore_comments(cur.getInt(16));
                post.setTime_duration(cur.getString(17));
                post.setHeight(cur.getString(18));
                post.setWidth(cur.getString(19));

                posts.add(post);
            } while (cur.moveToNext());
            cur.close();
        }
        return posts;
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
                        if (isCoverSelect) {
                            isCover = true;
                            selectedCoverImg = f;
                            imgCover.setImageBitmap(bitmap);
                        } else {
                            flag = true;
                            selectedimage = f;
                            imguser.setImageBitmap(bitmap);
                        }

                        //   uploadpic();
                        Log.e("selectedfile", f + "");
                        FileOutputStream fo = new FileOutputStream(f);
                        fo.write(bytes.toByteArray());
                        // MediaScannerConnection.scanFile(ProfilePage.this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
                        fo.close();
                        //  Toasty.success(Admin_Add_Gallery.this, "Image Saved!", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        //    Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (FileNotFoundException e) {
                    //  Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (IOException e) {
                    // Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = res.getError();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int length = grantResults.length;
        if (length > 0) {
            int grantResult = grantResults[0];
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(EditProfile.this);
            } else {
            }
        }
    }
}
