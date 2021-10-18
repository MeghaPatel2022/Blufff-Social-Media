package com.TBI.Client.Bluff.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.TBI.Client.Bluff.Activity.Post.Locationsearch;
import com.TBI.Client.Bluff.Activity.Post.PostCreate;
import com.TBI.Client.Bluff.Adapter.BusinessType_Adapter;
import com.TBI.Client.Bluff.Model.GetBusinessType.Datum;
import com.TBI.Client.Bluff.Model.GetBusinessType.Getbusinesstype;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.allcontrols;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.applozic.mobicomkit.api.attachment.FileClientService;
import com.applozic.mobicommons.file.FileUtils;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.google.gson.Gson;
import com.rilixtech.CountryCodePicker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.TBI.Client.Bluff.Activity.Post.PostCreate.islocation;
import static com.TBI.Client.Bluff.Utils.Public_Function.emailPattern;
import static com.TBI.Client.Bluff.Utils.Public_Function.hasPermissions;

public class BusinessForm extends AppCompatActivity {

    public static final int PERMISSION_ALL = 200;
    private static final String IMAGE_DIRECTORY = "/Blufff";
    private static final int FILE_SELECT_CODE = 0;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.edtuname)
    EditText edtuname;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    @BindView(R.id.ccpbusiness)
    CountryCodePicker ccpbusiness;
    @BindView(R.id.edtmobile)
    EditText edtmobile;
    @BindView(R.id.edtemail)
    EditText edtemail;
    @BindView(R.id.edtdob)
    EditText edtdob;
    @BindView(R.id.edtaddress)
    EditText edtaddress;
    @BindView(R.id.edtlegaldocument)
    TextView edtlegaldocument;
    @BindView(R.id.txtblog)
    TextView txtblog;
    @BindView(R.id.txtbusiness)
    TextView txtbusiness;
    @BindView(R.id.edtname)
    EditText edtname;
    @BindView(R.id.edtblogo)
    TextView edtblogo;
    @BindView(R.id.edtphoto)
    TextView edtphoto;
    @BindView(R.id.edtmobilebusi)
    EditText edtmobilebusi;
    @BindView(R.id.edtbusinessemail)
    EditText edtbusinessemail;
    @BindView(R.id.edtbusinesslocation)
    EditText edtbusinesslocation;
    @BindView(R.id.btnsubmit)
    Button btnsubmit;
    @BindView(R.id.edtsubtopic)
    EditText edtsubtopic;
    @BindView(R.id.edtbusinessproof)
    TextView edtbusinessproof;
    @BindView(R.id.lnlogo)
    LinearLayout lnlogo;
    @BindView(R.id.lnphoto)
    LinearLayout lnphoto;
    @BindView(R.id.lnlegaldocument)
    LinearLayout lnlegaldocument;
    @BindView(R.id.lnbusinessproof)
    LinearLayout lnbusinessproof;
    @BindView(R.id.autobusinesstype)
    AutoCompleteTextView autobusinesstype;
    Calendar calander;
    File filelogo, filephoto, filelegaldoc, filebusinessproof;
    String type = "", documenttype = "";
    String legaldoctype = "", businesprooftype = "";


    ConnectionDetector cd;
    boolean isInternetPresent = false;
    String businessTag = "Blog";

    List<Datum> typearray = new ArrayList<Datum>();
    BusinessType_Adapter businessType_adapter;
    int autocompleid = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (sharedpreference.getTheme(BusinessForm.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setContentView(R.layout.activity_businessform);
        ButterKnife.bind(this);

        islocation = false;
        PostCreate.location = "";
        PostCreate.latitude = "";
        PostCreate.longitude = "";

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (sharedpreference.getTheme(BusinessForm.this).equalsIgnoreCase("white")) {
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        } else {
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }

        Typeface myFont = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            myFont = getResources().getFont(R.font.poppins_regular);
            ccp.setTypeFace(myFont);
            ccpbusiness.setTypeFace(myFont);
        }
        autobusinesstype.setThreshold(1);//will start working from first character
        //  autobusinesstype.setAdapter(adapter);

        autobusinesstype.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.d("mn13hellowolrd", "enter");
                if (!charSequence.toString().equalsIgnoreCase("")) {
                    GetBusinesstype(charSequence.toString());
                } else {
                    autocompleid = -1;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        autobusinesstype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                autobusinesstype.setText(typearray.get(i).getName());
                autocompleid = typearray.get(i).getId();
                autobusinesstype.post(() -> autobusinesstype.dismissDropDown());

                autobusinesstype.dismissDropDown();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        edtbusinesslocation.setOnClickListener(view -> {

            Intent i1 = new Intent(BusinessForm.this, Locationsearch.class);
            startActivity(i1);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);

        });


        txtblog.setOnClickListener(view -> {

            businessTag = "Blog";

            if (sharedpreference.getTheme(BusinessForm.this).equalsIgnoreCase("white")) {
                txtblog.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                txtblog.setTextColor(getResources().getColor(R.color.white));
                txtbusiness.setTextColor(getResources().getColor(R.color.black));
            } else {
                txtblog.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                txtblog.setTextColor(getResources().getColor(R.color.black));
                txtbusiness.setTextColor(getResources().getColor(R.color.white));
            }

            txtbusiness.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blacklight)));
        });


        txtbusiness.setOnClickListener(view -> {

            businessTag = "Business";

            if (sharedpreference.getTheme(BusinessForm.this).equalsIgnoreCase("white")) {
                txtbusiness.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                txtbusiness.setTextColor(getResources().getColor(R.color.white));
                txtblog.setTextColor(getResources().getColor(R.color.black));
            } else {
                txtbusiness.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                txtbusiness.setTextColor(getResources().getColor(R.color.black));
                txtblog.setTextColor(getResources().getColor(R.color.white));
            }

            txtblog.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blacklight)));
        });

        edtuname.setText(sharedpreference.getusername(BusinessForm.this));

        calander = Calendar.getInstance();
        MonthAdapter.CalendarDay mindaytoday = new MonthAdapter.CalendarDay(calander.get(Calendar.YEAR), calander.get(Calendar.MONTH), calander.get(Calendar.DAY_OF_MONTH));

        allcontrols.setdatepicker_validation1(edtdob, BusinessForm.this, null, mindaytoday);


        lnlogo.setOnClickListener(view -> {

            type = "logo";
            String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CAMERA};

            if (!hasPermissions(BusinessForm.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(BusinessForm.this, PERMISSIONS, PERMISSION_ALL);
            } else {
                //selectImage();
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(BusinessForm.this);
            }


        });


        lnphoto.setOnClickListener(view -> {
            type = "photo";

            String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CAMERA};

            if (!hasPermissions(BusinessForm.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(BusinessForm.this, PERMISSIONS, PERMISSION_ALL);
            } else {
                //selectImage();
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(BusinessForm.this);
            }
        });

        lnlegaldocument.setOnClickListener(view -> {

            documenttype = "legaldoc";

            String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CAMERA};

            if (!hasPermissions(BusinessForm.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(BusinessForm.this, PERMISSIONS, PERMISSION_ALL);
            } else {
                showFileChooser();
            }


        });

        lnbusinessproof.setOnClickListener(view -> {

            documenttype = "businessproof";

            String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CAMERA};

            if (!hasPermissions(BusinessForm.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(BusinessForm.this, PERMISSIONS, PERMISSION_ALL);
            } else {
                showFileChooser();
            }


        });


        btnsubmit.setOnClickListener(view -> {


            edtuname.setError(null);
            edtmobile.setError(null);
            edtemail.setError(null);
            edtdob.setError(null);
            edtaddress.setError(null);
            edtlegaldocument.setError(null);
            edtname.setError(null);
            edtblogo.setError(null);
            edtphoto.setError(null);
            edtmobilebusi.setError(null);
            edtbusinessemail.setError(null);
            edtbusinesslocation.setError(null);
            edtsubtopic.setError(null);
            edtbusinessproof.setError(null);


            if (edtuname.getText().toString().equalsIgnoreCase("")) {
                edtuname.setError("Required");
                edtuname.requestFocus();
            } else if (edtmobile.getText().toString().equalsIgnoreCase("")) {
                edtmobile.setError("Required");
                edtmobile.requestFocus();
            } else if (edtemail.getText().toString().equalsIgnoreCase("")) {
                edtemail.setError("Required");
                edtemail.requestFocus();
            } else if (!edtemail.getText().toString().trim().matches(emailPattern)) {
                Toasty.warning(BusinessForm.this, "Please enter valid Email Id...", Toasty.LENGTH_LONG).show();
            } else if (edtdob.getText().toString().equalsIgnoreCase("")) {
                edtdob.setError("Required");
                edtdob.requestFocus();
            } else if (edtaddress.getText().toString().equalsIgnoreCase("")) {
                edtaddress.setError("Required");
                edtaddress.requestFocus();
            } else if (edtlegaldocument.getText().toString().equalsIgnoreCase("")) {
                edtlegaldocument.setError("Required");
                edtlegaldocument.requestFocus();
            } else if (edtname.getText().toString().equalsIgnoreCase("")) {
                edtname.setError("Required");
                edtname.requestFocus();
            } else if (edtblogo.getText().toString().equalsIgnoreCase("")) {
                edtblogo.setError("Required");
                edtblogo.requestFocus();
            } else if (edtphoto.getText().toString().equalsIgnoreCase("")) {
                edtphoto.setError("Required");
                edtphoto.requestFocus();
            } else if (edtmobilebusi.getText().toString().equalsIgnoreCase("")) {
                edtmobilebusi.setError("Required");
                edtmobilebusi.requestFocus();
            } else if (edtbusinessemail.getText().toString().equalsIgnoreCase("")) {
                edtbusinessemail.setError("Required");
                edtbusinessemail.requestFocus();
            } else if (!edtbusinessemail.getText().toString().trim().matches(emailPattern)) {
                Toasty.warning(BusinessForm.this, "Please enter valid Business Email Id...", Toasty.LENGTH_LONG).show();
            } else if (edtbusinesslocation.getText().toString().equalsIgnoreCase("")) {
                edtbusinesslocation.setError("Required");
                edtbusinesslocation.requestFocus();
            } else if (autocompleid == -1) {
                Toasty.warning(BusinessForm.this, "Please enter business type", Toasty.LENGTH_LONG).show();
            } else if (edtsubtopic.getText().toString().equalsIgnoreCase("")) {
                edtsubtopic.setError("Required");
                edtsubtopic.requestFocus();
            } else if (edtbusinessproof.getText().toString().equalsIgnoreCase("")) {
                edtbusinessproof.setError("Required");
                edtbusinessproof.requestFocus();
            } else {


                cd = new ConnectionDetector(BusinessForm.this);
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    Toasty.warning(BusinessForm.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    SubmitForm();
                }


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult res = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = res.getUri();
                String name = "";
                name = resultUri.toString().substring(resultUri.toString().lastIndexOf("/") + 1);
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
                        if (type.equalsIgnoreCase("logo")) {
                            filelogo = f;
                            edtblogo.setText(name);
                        } else {
                            filephoto = f;
                            edtphoto.setText(name);
                        }

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
            }
        } else {
            switch (requestCode) {
                case FILE_SELECT_CODE:
                    if (resultCode == RESULT_OK) {
                        // Get the Uri of the selected file
                        Uri selectedFileUri = data.getData();
                        Log.d("File Uri: ", selectedFileUri.toString());

                        ContentResolver cR = getContentResolver();
                        String mime = cR.getType(data.getData());

                        if (selectedFileUri != null) {
                            String fileName = null;
                            try {
                                String mimeType = FileUtils.getMimeTypeByContentUriOrOther(this, selectedFileUri);
                                if (TextUtils.isEmpty(mimeType)) {
                                    return;
                                }
                                @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                fileName = FileUtils.getFileName(this, selectedFileUri);
                                String fileFormat = FileUtils.getFileFormat(fileName);
                                String fileNameToWrite;
                                if (TextUtils.isEmpty(fileFormat)) {
                                    String format = FileUtils.getFileFormat(FileUtils.getFile(this, selectedFileUri).getAbsolutePath());
                                    if (TextUtils.isEmpty(format)) {
                                        return;
                                    }
                                    fileNameToWrite = timeStamp + "." + format;
                                } else {
                                    fileNameToWrite = timeStamp + "." + fileFormat;
                                }

                                File mediaFile = FileClientService.getFilePath(fileNameToWrite, getApplicationContext(), mimeType);
                                mediaFile.createNewFile();
                                mediaFile.getAbsoluteFile();
                                if (documenttype.equalsIgnoreCase("legaldoc")) {
                                    edtlegaldocument.setText(fileName);
                                    legaldoctype = mime;
                                    filelegaldoc = mediaFile;
                                } else {
                                    edtbusinessproof.setText(fileName);
                                    businesprooftype = mime;
                                    filebusinessproof = mediaFile;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                        /*;
                        Log.d("mn13enter", "enter   " + mime);
                        //getFileName(data.getData());
                        String path = getPath(data.getData());
                        Log.d("mn13path", path.toString());

                        if (documenttype.equalsIgnoreCase("legaldoc")) {
                            edtlegaldocument.setText(getFileName(data.getData()));
                            legaldoctype = mime;
                            filelegaldoc = new File(uri.getPath());
                        } else {
                            edtbusinessproof.setText(getFileName(data.getData()));
                            businesprooftype = mime;
                            filebusinessproof = new File(uri.getPath());
                        }*/
                    }
                    break;
            }


        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (type.equalsIgnoreCase("logo") || type.equalsIgnoreCase("photo")) {
            int length = grantResults.length;
            if (length > 0) {
                int grantResult = grantResults[0];
                if (grantResult == PackageManager.PERMISSION_GRANTED) {

                    CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(BusinessForm.this);
                } else {
                }
            }
        }
    }

    private void showFileChooser() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        try {
            startActivityForResult(chooseFile, FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    public void SubmitForm() {

        Public_Function.Show_Progressdialog(BusinessForm.this);

        try {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("document", edtlegaldocument.getText().toString(), RequestBody.create(MediaType.parse(legaldoctype), filelegaldoc));
            builder.addFormDataPart("business_proof", edtbusinessproof.getText().toString(), RequestBody.create(MediaType.parse(businesprooftype), filelegaldoc));
            builder.addFormDataPart("business_photos", filephoto.getName(), RequestBody.create(MediaType.parse("image/*"), filephoto));
            builder.addFormDataPart("logo", filelogo.getName(), RequestBody.create(MediaType.parse("image/*"), filelogo));
            builder.addFormDataPart("user_id", sharedpreference.getUserId(BusinessForm.this));
            builder.addFormDataPart("name", edtuname.getText().toString());
            builder.addFormDataPart("dob", edtdob.getText().toString());
            builder.addFormDataPart("email", edtemail.getText().toString());
            builder.addFormDataPart("mobile_code", ccp.getSelectedCountryCodeWithPlus());
            builder.addFormDataPart("mobile_number", edtmobile.getText().toString());
            builder.addFormDataPart("address", edtaddress.getText().toString());
            builder.addFormDataPart("catch_display_name", edtname.getText().toString().trim());
            builder.addFormDataPart("business_mobile_code", ccpbusiness.getSelectedCountryCodeWithPlus().trim());
            builder.addFormDataPart("business_number", edtmobilebusi.getText().toString().trim());
            builder.addFormDataPart("business_email", edtbusinessemail.getText().toString().trim());
            builder.addFormDataPart("business_location", edtbusinesslocation.getText().toString().trim());
            builder.addFormDataPart("business_type", autocompleid + "");
            builder.addFormDataPart("business_sub_topics", edtsubtopic.getText().toString().trim());
            builder.addFormDataPart("business_buttons", businessTag);
            builder.addFormDataPart("lat", PostCreate.latitude);
            builder.addFormDataPart("lang", PostCreate.longitude);

            // Log.d("mn13adddata:", typearray.get(selecpositon).getId() + "");

            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
                    .url(getString(R.string.url1) + "saveCatchForm")
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
                    BusinessForm.this.runOnUiThread(() -> {
                        Public_Function.Hide_ProgressDialogue();
                        Toasty.error(BusinessForm.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                    });

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    Log.d("mn13response", response.body().toString());

                    BusinessForm.this.runOnUiThread(() -> {

                        Public_Function.Hide_ProgressDialogue();

                        if (response.message().equalsIgnoreCase("OK")) {

                            try {

                                JSONObject object = new JSONObject(response.body().string());
                                boolean message = object.optBoolean("success");
                                String status = "";
                                status = object.optString("message");
                                if (message) {

                                    finish();
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                                } else {
                                    Toasty.error(BusinessForm.this, status + "", Toast.LENGTH_SHORT, true).show();
                                }
                            } catch (IOException e) {
                                Public_Function.Hide_ProgressDialogue();
                                e.printStackTrace();
                                Log.d("mn13error1:", e.toString());
                                Toasty.error(BusinessForm.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Public_Function.Hide_ProgressDialogue();
                            Log.d("mn13error:", "14");
                            Toasty.error(BusinessForm.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                        }
                    });
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (islocation) {
            islocation = false;
            edtbusinesslocation.setText(PostCreate.location);
        }

    }

    public void GetBusinesstype(String s) {


        ArrayList<param> paramArrayList = new ArrayList<>();
        //paramArrayList.add(new param("user_id", sharedpreference.getUserId(BusinessForm.this) + ""));
        paramArrayList.add(new param("keyword", s + ""));

        new geturl().getdata(BusinessForm.this, data -> {
            typearray = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("msg");
                if (message) {
                    Getbusinesstype login = new Gson().fromJson(data, Getbusinesstype.class);
                    typearray = login.getData();


                } else {
                    //  Toasty.error(BusinessForm.this, status + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                // Toasty.error(BusinessForm.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }
            BusinessType_Adapter businessType_adapter = new BusinessType_Adapter(BusinessForm.this, typearray);
            autobusinesstype.setAdapter(businessType_adapter);

            if (!typearray.isEmpty()) {
                autobusinesstype.showDropDown();
            }

        }, paramArrayList, "get_business_types");
    }

}
