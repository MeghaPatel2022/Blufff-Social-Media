package com.TBI.Client.Bluff.Activity.Post;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.TBI.Client.Bluff.Activity.Home.OpenCamera1;
import com.TBI.Client.Bluff.Activity.Mains.BottombarActivity;
import com.TBI.Client.Bluff.Model.Jsoncreate.Person;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.otaliastudios.cameraview.VideoResult;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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

import static com.TBI.Client.Bluff.Activity.Post.PostCreate.longitude;

public class PostVideoActivity extends AppCompatActivity {

    private static VideoResult videoResult;

    List<File> imagesrray = new ArrayList<>();
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_done)
    TextView tv_done;
    @BindView(R.id.edtdescription)
    SocialAutoCompleteTextView edtdescription;
    @BindView(R.id.txtaddlocation)
    TextView txtaddlocation;
    @BindView(R.id.txttagpeople)
    TextView txttagpeople;
    @BindView(R.id.img_profile)
    CircleImageView img_profile;
    @BindView(R.id.player)
    VideoView videoView;
    private Dialog dialog;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_video);

        ButterKnife.bind(PostVideoActivity.this);

        file = new File(getIntent().getStringExtra("File"));
        Log.e("LLLLL_FILEPATH: ", file.getAbsolutePath());
        imagesrray.add(file);

        tv_back.setOnClickListener(v -> onBackPressed());

//        videoView.setOnClickListener(view -> playVideo());

        Log.e("LLLLL_Image_Pr: ",sharedpreference.getphoto(PostVideoActivity.this));
        Glide
                .with(PostVideoActivity.this)
                .load(sharedpreference.getphoto(PostVideoActivity.this))
                .error(R.drawable.placeholder)
                .into(img_profile);

        videoView.setVideoURI(Uri.parse(file.toString()));
        videoView.start();
        videoView.setOnPreparedListener(mp -> {
            mp.setVolume(0f, 0f);
            mp.setLooping(true);
        });
        videoView.setOnCompletionListener(mp -> {
            videoView.setVideoURI(Uri.parse(file.toString()));
            mp.start();
        });

        txtaddlocation.setOnClickListener(view -> {
            Intent i1 = new Intent(PostVideoActivity.this, Locationsearch.class);
            startActivity(i1);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Person> personList = new ArrayList<Person>();

                for (int i = 0; i < TagUser.instaarray.size(); i++) {

                    List<com.TBI.Client.Bluff.Model.Jsoncreate.Datum> data = new ArrayList<com.TBI.Client.Bluff.Model.Jsoncreate.Datum>();
                    for (int j = 0; j < TagUser.instaarray.get(i).getTags().size(); j++) {
                        com.TBI.Client.Bluff.Model.Jsoncreate.Datum datum = new com.TBI.Client.Bluff.Model.Jsoncreate.Datum(TagUser.instaarray.get(i).getTags().get(j).getX_co_ord(), TagUser.instaarray.get(i).getTags().get(j).getY_co_ord(), TagUser.instaarray.get(i).getTags().get(j).getUnique_tag_id());
                        data.add(datum);
                    }
                    if (!data.isEmpty()) {
                        personList.add(new Person(i, data));
                    }

                }
                Gson gson = new Gson();
                JsonElement element = gson.toJsonTree(personList, new TypeToken<List<Person>>() {
                }.getType());

                if (!element.isJsonArray()) {
                    try {
                        throw new Exception();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                JsonArray jsonArray = element.getAsJsonArray();
                Log.d("mn13personalarraylist:", personList.toString() + "\n" + jsonArray.toString());

                if (!Public_Function.isInternetConnected(PostVideoActivity.this)) {
                    Toasty.warning(PostVideoActivity.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {

                    dialog = new android.app.Dialog(PostVideoActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                    dialog.setContentView(R.layout.dialoge_post_image);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.black);
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    dialog.show();
                    Bitmap bmThumbnail;

                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(imagesrray.get(0).getAbsolutePath(), MediaStore.Images.Thumbnails.MICRO_KIND);
                    Bitmap myBitmap = BitmapFactory.decodeFile(imagesrray.get(0).getAbsolutePath());

                    CircleImageView img_post;

                    img_post = dialog.findViewById(R.id.img_post);
                    img_post.setImageBitmap(bmThumbnail);

                    CreatePost(jsonArray.toString());
                }

            }
        });

        txttagpeople.setOnClickListener(view -> {
            Intent intent = new Intent(PostVideoActivity.this, TagUser.class);
            intent.putExtra("imagarray", (Serializable) imagesrray);
            intent.putExtra("fileType", "video");
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        videoView.stopPlayer();
        Intent intent = new Intent(PostVideoActivity.this, OpenCamera1.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.stay, R.anim.fade_out);
//        } else {
//            super.onBackPressed();
//            overridePendingTransition(R.anim.stay, R.anim.fade_out);
//        }
    }

    public void CreatePost(String s) {

        try {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for (int i = 0; i < imagesrray.size(); i++) {
                builder.addFormDataPart("photo", imagesrray.get(i).getName(), RequestBody.create(MediaType.parse("image/*"), imagesrray.get(i)));
            }

            builder.addFormDataPart("user_id", sharedpreference.getUserId(PostVideoActivity.this));
            builder.addFormDataPart("description", edtdescription.getText().toString());
            builder.addFormDataPart("location", PostCreate.location);
            builder.addFormDataPart("lat", PostCreate.latitude);
            builder.addFormDataPart("lang", longitude);
            builder.addFormDataPart("file_type", "video");
            builder.addFormDataPart("tag_user_ids", "");
            builder.addFormDataPart("timezone", Public_Function.gettimezone());
            builder.addFormDataPart("post_date", Public_Function.getcurrentdatetime());
            builder.addFormDataPart("tag_people", s + "");
            builder.addFormDataPart("height", PostCreate.height + "");
            builder.addFormDataPart("width", PostCreate.width + "");

            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
                    .addHeader("Authorization", sharedpreference.getUserToken(PostVideoActivity.this))
                    .url(getString(R.string.url1) + "save_post")
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
                    PostVideoActivity.this.runOnUiThread(() -> {
                        Public_Function.Hide_ProgressDialogue();
                        Toasty.error(PostVideoActivity.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                    });

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    PostVideoActivity.this.runOnUiThread(() -> {
                        Log.e("LLLLL_Video: ", response.toString());

                        if (response.message().equalsIgnoreCase("OK")) {

                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                if (dialog != null && dialog.isShowing())
                                    dialog.dismiss();

                                deleteFile(file.getName());
                                Log.e("LLLLLL_Del: ", String.valueOf(file.delete()));

                                Intent i = new Intent(PostVideoActivity.this, BottombarActivity.class);
                                i.putExtra("type", "home");
                                startActivity(i);
                                finishAffinity();
                                overridePendingTransition(R.anim.fade_in, R.anim.stay);
                            }, 1000);

                            try {
                            } catch (Exception e) {
                                Toasty.error(PostVideoActivity.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                            }
                        } else {
                            Public_Function.Hide_ProgressDialogue();
                            Log.d("mn13error:", "14");
                            Toasty.error(PostVideoActivity.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
