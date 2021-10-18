package com.TBI.Client.Bluff.Activity.Post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.TBI.Client.Bluff.Activity.Mains.BottombarActivity;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.MoveListner.MultiTouchListener;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.sharedpreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
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

import static com.TBI.Client.Bluff.Activity.Post.CreateStory.edttext;
import static com.TBI.Client.Bluff.Activity.Post.CreateStory.gradintdrawable;

public class Post_Story extends AppCompatActivity {

    // Drawable gradintcolor;
    private static final String IMAGE_DIRECTORY = "/Blufff";
    @BindView(R.id.btnshare)
    Button btnshare;
    @BindView(R.id.imgclose)
    ImageView imgclose;
    @BindView(R.id.imageset)
    ImageView imageset;
    @BindView(R.id.framecreate)
    FrameLayout framecreate;
    @BindView(R.id.imgcreate)
    ImageView imgcreate;
    @BindView(R.id.edtcreate)
    TextView edtcreate;
    String imagepath = "";
    File uploadfile;
    ConnectionDetector cd;
    boolean isInternetPresent = false;
    int filetype = 1;
    String type = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (sharedpreference.getTheme(Post_Story.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poststoty);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(Post_Story.this));
        imageset.setClipToOutline(true);
        imgcreate.setClipToOutline(true);
        type = getIntent().getExtras().getString("type");
        if (type.equalsIgnoreCase("gradiant")) {

            framecreate.setVisibility(View.VISIBLE);
            imageset.setVisibility(View.GONE);
            if (!edttext.getText().toString().trim().equalsIgnoreCase("")) {

                edtcreate.setText(edttext.getText().toString());
                edtcreate.setTextColor(edttext.getTextColors());
                edtcreate.setBackground(edttext.getBackground());
                edtcreate.setBackgroundTintList(edttext.getBackgroundTintList());
                edtcreate.setTypeface(edttext.getTypeface());
                edtcreate.setLayoutParams(edttext.getLayoutParams());
                edtcreate.setOnTouchListener(new MultiTouchListener());

            }
            framecreate.setBackground(gradintdrawable);

        } else {
            imagepath = getIntent().getExtras().getString("imagepath");
            Bitmap myBitmap = BitmapFactory.decodeFile(imagepath);
            imageset.setImageBitmap(myBitmap);
            uploadfile = new File(imagepath);
        }

        imgclose.setOnClickListener(view -> {
            finish();
            overridePendingTransition(R.anim.stay, R.anim.slide_down);
        });

        btnshare.setOnClickListener(view -> {

            if (type.equalsIgnoreCase("gradiant")) {

                Bitmap bitmap;
                framecreate.setDrawingCacheEnabled(true);
                framecreate.buildDrawingCache();
                bitmap = framecreate.getDrawingCache();

              /*  Bitmap bitmap = Bitmap.createBitmap(framecreate.getWidth(), framecreate.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                framecreate.draw(canvas);*/


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
                    uploadfile = f;
                    Log.e("selectedfile", f + "");
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                    MediaScannerConnection.scanFile(Post_Story.this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    //    Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                cd = new ConnectionDetector(Post_Story.this);
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    Toasty.warning(Post_Story.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    Upload_Story();
                }

            } else {
                cd = new ConnectionDetector(Post_Story.this);
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    Toasty.warning(Post_Story.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    Upload_Story();
                }
            }

        });
    }

    public void Upload_Story() {

        Public_Function.Show_Progressdialog(Post_Story.this);

        try {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("file", uploadfile.getName(), RequestBody.create(MediaType.parse("image/*"), uploadfile));


            builder.addFormDataPart("user_id", sharedpreference.getUserId(Post_Story.this));
            builder.addFormDataPart("filetype", filetype + "");
            builder.addFormDataPart("story_date", Public_Function.getcurrentdatetime());
            builder.addFormDataPart("timezone", Public_Function.gettimezone());

            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
                    .url(getString(R.string.url1) + "create_story")
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
                    Post_Story.this.runOnUiThread(() -> {
                        Public_Function.Hide_ProgressDialogue();
                        Toasty.error(Post_Story.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                    });

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    Log.d("mn13response", response.body().toString());

                    Post_Story.this.runOnUiThread(() -> {

                        Public_Function.Hide_ProgressDialogue();

                        if (response.message().equalsIgnoreCase("OK")) {

                            try {
                                JSONObject object = new JSONObject(response.body().string());
                                boolean success = object.optBoolean("success");
                                String message = object.optString("message");

                                if (success) {
                                    Intent i1 = new Intent(Post_Story.this, BottombarActivity.class);
                                    i1.putExtra("type", "home");
                                    startActivity(i1);
                                    overridePendingTransition(R.anim.fade_in, R.anim.stay);

                                } else {
                                    Toasty.error(Post_Story.this, message + "", Toast.LENGTH_SHORT, true).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.d("mn13error1:", e.toString());
                                Toasty.error(Post_Story.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {

                            Public_Function.Hide_ProgressDialogue();
                            Log.d("mn13error:", "14");
                            Toasty.error(Post_Story.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                        }

                    });
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);

    }


}
