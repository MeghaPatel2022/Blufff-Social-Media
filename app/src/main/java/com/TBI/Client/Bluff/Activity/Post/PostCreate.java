package com.TBI.Client.Bluff.Activity.Post;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.TBI.Client.Bluff.Activity.Mains.BottombarActivity;
import com.TBI.Client.Bluff.Adapter.Post.HashTagAdapter;
import com.TBI.Client.Bluff.Adapter.Post.MentionUserAdapter;
import com.TBI.Client.Bluff.Adapter.Post.PostImage_Adapter;
import com.TBI.Client.Bluff.Adapter.Post.ShowImages_Adapter;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.Model.Jsoncreate.Person;
import com.TBI.Client.Bluff.Model.SearchHashtag.SearchHashtag;
import com.TBI.Client.Bluff.Model.SearchUser.Datum;
import com.TBI.Client.Bluff.Model.SearchUser.SearchUser;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;

import org.json.JSONObject;

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

public class PostCreate extends AppCompatActivity {

    public static boolean islocation, istagged;
    public static String latitude = "", longitude = "", location = "";
    public static int height, width;
    @BindView(R.id.recyimages)
    RecyclerView recyimages;
    @BindView(R.id.txtlocation)
    TextView txtlocation;
    @BindView(R.id.txttagpeople)
    TextView txttagpeople;
    /* @BindView(RM.id.edtdescription)
     EditText edtdescription;*/
    @BindView(R.id.tv_done)
    TextView imgdone;
    @BindView(R.id.lntagpeople)
    LinearLayout lntagpeople;

    //    @BindView(RM.id.llnaddlocation)
//    LinearLayout llnaddlocation;
//    @BindView(RM.id.imglocation)
//    ImageView imglocation;
    @BindView(R.id.imgtagpeople)
    ImageView imgtagpeople;
//    @BindView(R.id.reladdlocation)
//    RelativeLayout reladdlocation;
    @BindView(R.id.relselectlocation)
    RelativeLayout relselectlocation;
    @BindView(R.id.imgcancel)
    ImageView imgcancel;
    @BindView(R.id.edtdescription)
    SocialAutoCompleteTextView edtdescription;
    @BindView(R.id.tv_back)
    TextView img_back;
    @BindView(R.id.img_profile)
    CircleImageView img_profile;
    @BindView(R.id.txtaddlocation)
            TextView txtaddlocation;
    PostImage_Adapter postImageAdapter;
    List<String> abc = new ArrayList<>();
    List<File> imagesrray = new ArrayList<>();
    Dialog dialogshowimages;
    ViewPager viewshowimages;
    ShowImages_Adapter adapter;
    ConnectionDetector cd;
    boolean isInternetPresent = false;
    List<com.TBI.Client.Bluff.Model.SearchHashtag.Hastag> hastags = new ArrayList<>();
    HashTagAdapter hashTag_adapter;
    MentionUserAdapter mentionUserAdapter;
    List<Datum> userlist = new ArrayList<Datum>();
    private Dialog dialog;

    String file_tpe = "image";
    String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (sharedpreference.getTheme(PostCreate.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcreate);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(PostCreate.this));
        imagesrray = (List<File>) getIntent().getSerializableExtra("imagesrray");

        postImageAdapter = new PostImage_Adapter(PostCreate.this, imagesrray, PostCreate.this);
        recyimages.setLayoutManager(new LinearLayoutManager(PostCreate.this, LinearLayoutManager.HORIZONTAL, false));
        recyimages.setAdapter(postImageAdapter);

        img_back.setOnClickListener(v -> onBackPressed());
        Glide.with(PostCreate.this)
                .load(sharedpreference.getphoto(PostCreate.this))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(img_profile);

        imgdone.setOnClickListener(view -> {

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

            cd = new ConnectionDetector(PostCreate.this);
            isInternetPresent = cd.isConnectingToInternet();

            if (!isInternetPresent) {
                Toasty.warning(PostCreate.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
            } else {

                dialog = new android.app.Dialog(PostCreate.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.dialoge_post_image);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.black);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                dialog.show();

                Bitmap myBitmap = BitmapFactory.decodeFile(imagesrray.get(0).getAbsolutePath());

                CircleImageView img_post;

                img_post = dialog.findViewById(R.id.img_post);
                img_post.setImageBitmap(myBitmap);

                CreatePost(jsonArray.toString());
            }

        });


        txtaddlocation.setOnClickListener(view -> {

            Intent i1 = new Intent(PostCreate.this, Locationsearch.class);
            startActivity(i1);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);

        });

        txttagpeople.setOnClickListener(view -> {

            Intent intent = new Intent(PostCreate.this, TagUser.class);
            intent.putExtra("imagarray", (Serializable) imagesrray);
            intent.putExtra("fileType", "image");
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);

        });

        imgcancel.setOnClickListener(view -> {

            YoYo.with(Techniques.SlideInDown)
                    .duration(2000)
                    .onEnd(animator -> {

                    })
                    .playOn(relselectlocation);

            relselectlocation.setVisibility(View.GONE);
//            reladdlocation.setVisibility(View.VISIBLE);
            location = "";
            latitude = "";
            longitude = "";

//            imglocation.setImageTintList(ColorStateList.valueOf(getResources().getColor(RM.color.lightdrey)));
        });

        edtdescription.setHashtagEnabled(true);
        edtdescription.setMentionEnabled(true);
        edtdescription.setHyperlinkEnabled(false);


        mentionUserAdapter = new MentionUserAdapter(PostCreate.this);
        hashTag_adapter = new HashTagAdapter(PostCreate.this);

        edtdescription.setMentionTextChangedListener((view, text) -> {

            Log.d("mn13mentiom", "12" + text);
            SearchUser(text.toString());

        });


        edtdescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int i1, int i2) {

                int startundex = 0;
                if (!TextUtils.isEmpty(s) && start < s.length()) {
                    switch (s.charAt(start)) {
                        case '#':
                            //startindex = start;
                            Log.d("mn13taguser:", "" + s.toString().subSequence(start, start + i2));
                            if (!s.toString().subSequence(start, start + i2).toString().replace("#", "").equalsIgnoreCase("")) {
                                edtdescription.setHashtagEnabled(true);
                                Searchhashtag(s.toString().subSequence(start, start + i2));
                            }
                            edtdescription.setAdapter(hashTag_adapter);
                            break;
                        case '@':
                            edtdescription.setAdapter(mentionUserAdapter);

                            break;

                    }

                } else {
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_out);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (islocation) {
            islocation = false;
//            imglocation.setImageDrawable(getResources().getDrawable(RM.drawable.add_location_selected));
//            imglocation.setImageTintList(ColorStateList.valueOf(getResources().getColor(RM.color.white)));

            txtaddlocation.setText(location);
            YoYo.with(Techniques.SlideInUp)
                    .duration(1000)
                    .onEnd(animator -> {

                    })
                    .playOn(relselectlocation);
//            reladdlocation.setVisibility(View.GONE);
            relselectlocation.setVisibility(View.VISIBLE);

        }
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

        if (personList.isEmpty()) {
            imgtagpeople.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.lightdrey)));
            txttagpeople.setTextColor(getResources().getColor(R.color.lightdrey));
        } else {
            imgtagpeople.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            txttagpeople.setTextColor(getResources().getColor(R.color.white));
        }

    }


    public void ShowImages(int pos) {

        dialogshowimages = new Dialog(PostCreate.this);
        dialogshowimages.setContentView(R.layout.dialog_showimages);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogshowimages.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.8f);
        int dialogWindowHeight = (int) (displayHeight * 0.5f);
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = dialogWindowHeight;
        layoutParams.gravity = Gravity.CENTER;
        dialogshowimages.getWindow().setAttributes(layoutParams);

        dialogshowimages.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogshowimages.getWindow().getAttributes().windowAnimations = R.style.showimages;

        viewshowimages = dialogshowimages.findViewById(R.id.viewshowimages);

        adapter = new ShowImages_Adapter(PostCreate.this, imagesrray);
        viewshowimages.setAdapter(adapter);

        viewshowimages.setCurrentItem(pos);

        if (!dialogshowimages.isShowing()) {
            dialogshowimages.show();
        }

    }

    public void Searchhashtag(CharSequence charSequence) {


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("keyword", charSequence.toString().replace("#", "")));

        new geturl().getdata(PostCreate.this, data -> {
            hastags = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("message");

                if (message) {
                    SearchHashtag login = new Gson().fromJson(data, SearchHashtag.class);
                    hastags = login.getHastags();

                    hashTag_adapter.clear();
                    hashTag_adapter.addAll(hastags);
                    edtdescription.setHashtagAdapter(hashTag_adapter);
                    edtdescription.showDropDown();


                } else {
                    // Toasty.error(getActivity(), status + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                // Toasty.error(Login.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }
        }, paramArrayList, "search_hashtags");
    }

    public void SearchUser(String s) {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(PostCreate.this) + ""));
        paramArrayList.add(new param("keyword", s + ""));
        paramArrayList.add(new param("filter_by", "all"));

        new geturl().getdata(PostCreate.this, data -> {
            userlist = new ArrayList<>();
            try {
                Public_Function.Hide_ProgressDialogue();
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("msg");
                if (message) {
                    SearchUser login = new Gson().fromJson(data, SearchUser.class);
                    userlist = login.getData();
                    mentionUserAdapter.clear();
                    mentionUserAdapter.addAll(userlist);
                    edtdescription.setMentionAdapter(mentionUserAdapter);
                    edtdescription.showDropDown();

                } else {
                    Toasty.error(PostCreate.this, status + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                Toasty.error(PostCreate.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }
        }, paramArrayList, "user_listing");
    }


    public void CreatePost(String s) {

        try {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for (int i = 0; i < imagesrray.size(); i++) {
                builder.addFormDataPart("photo", imagesrray.get(i).getName(), RequestBody.create(MediaType.parse("image/*"), imagesrray.get(i)));
            }

            builder.addFormDataPart("user_id", sharedpreference.getUserId(PostCreate.this));
            builder.addFormDataPart("description", edtdescription.getText().toString());
            builder.addFormDataPart("location", location);
            builder.addFormDataPart("lat", latitude);
            builder.addFormDataPart("lang", longitude);
            builder.addFormDataPart("file_type", file_tpe);
            builder.addFormDataPart("tag_user_ids", "");
            builder.addFormDataPart("timezone", Public_Function.gettimezone());
            builder.addFormDataPart("post_date", Public_Function.getcurrentdatetime());
            builder.addFormDataPart("tag_people", s + "");
            builder.addFormDataPart("height", height + "");
            builder.addFormDataPart("width", width + "");

            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
                    .addHeader("Authorization", sharedpreference.getUserToken(PostCreate.this))
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
                    Log.e("LLL_Error: ",e.getMessage());
                    PostCreate.this.runOnUiThread(() -> {
                        Toasty.error(PostCreate.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                    });

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    PostCreate.this.runOnUiThread(() -> {

                        if (response.message().equalsIgnoreCase("OK")) {
                            Log.e("LLLLLLLLL_postCreRes: ",response.toString());

                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                if (dialog!=null && dialog.isShowing())
                                    dialog.dismiss();
                                Intent i = new Intent(PostCreate.this, BottombarActivity.class);
                                i.putExtra("type", "home");
                                startActivity(i);
                                finishAffinity();
                                overridePendingTransition(R.anim.fade_in, R.anim.stay);
                            }, 1000);

                            try {
                            } catch (Exception e) {
                                Toasty.error(PostCreate.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
                            }
                        } else {
                            Public_Function.Hide_ProgressDialogue();
                            Log.d("mn13error:", "14");
                            Toasty.error(PostCreate.this, "Ooops something went wrong..!", Toast.LENGTH_SHORT, true).show();
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
    }
}
