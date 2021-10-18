package com.TBI.Client.Bluff.Activity.Post;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.TBI.Client.Bluff.Adapter.Post.EditPost_Adapter;
import com.TBI.Client.Bluff.Adapter.Post.HashTagAdapter;
import com.TBI.Client.Bluff.Adapter.Post.MentionUserAdapter;
import com.TBI.Client.Bluff.Adapter.Post.Postdetail_ImageAdapter;
import com.TBI.Client.Bluff.Adapter.Post.Postdetail_ImageAdapter1;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.Model.GetProfile.Post;
import com.TBI.Client.Bluff.Model.PostDetail.GetViewPost;
import com.TBI.Client.Bluff.Model.PostDetail.Image;
import com.TBI.Client.Bluff.Model.SearchHashtag.Hastag;
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
import com.harsh.instatag.InstaTag;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import me.relex.circleindicator.CircleIndicator;

import static com.TBI.Client.Bluff.Activity.Post.PostCreate.islocation;

public class EditPost extends AppCompatActivity {

    @BindView(R.id.tool_bar)
    Toolbar tool_bar;
    @BindView(R.id.txtdone)
    TextView txtdone;
    @BindView(R.id.viewpagerimage)
    ViewPager viewpagerimage;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.edtdescription)
    SocialAutoCompleteTextView edtdescription;
    @BindView(R.id.txtlocation)
    TextView txtlocation;
    @BindView(R.id.txtuname)
    TextView txtuname;
    @BindView(R.id.imguser)
    CircleImageView imguser;
    @BindView(R.id.instatagview)
    InstaTag instatagview;

    Post model;

    EditPost_Adapter editPost_adapter;
    List<Image> imagesarray = new ArrayList<>();
    int postid;


    GetViewPost viewmodel;

    Postdetail_ImageAdapter postdetail_imageAdapter;

    ConnectionDetector cd;
    boolean isInternetPresent = false;
    List<Hastag> hastags = new ArrayList<>();
    HashTagAdapter hashTag_adapter;
    MentionUserAdapter mentionUserAdapter;
    List<Datum> userlist = new ArrayList<Datum>();
    Dialog dialoglocation;
    TextView txtblock, txtcancel;
    String location = "", lat = "", lon = "", save = "";

    float oldheight, oldwidth, height, width;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (sharedpreference.getTheme(EditPost.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpost);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(EditPost.this));

        ViewTreeObserver vto = instatagview.getTagImageView().getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                instatagview.getTagImageView().getViewTreeObserver().removeOnPreDrawListener(this);
                if (save.equalsIgnoreCase("no")) {
                    EditPost_Adapter.height = instatagview.getTagImageView().getMeasuredHeight();
                    EditPost_Adapter.width = instatagview.getTagImageView().getMeasuredWidth();
                    Log.d("mn13height", Postdetail_ImageAdapter1.height + "::" + Postdetail_ImageAdapter1.height);

                    if (editPost_adapter != null) {
                        editPost_adapter.notifyDataSetChanged();
                    }
                } else {

                }
                height = instatagview.getTagImageView().getMeasuredHeight();
                width = instatagview.getTagImageView().getMeasuredWidth();
                Log.d("mn13heightx", height + "::" + width);
                return true;
            }
        });

        PostCreate.latitude = "";
        PostCreate.longitude = "";
        PostCreate.location = "";

        setSupportActionBar(tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (sharedpreference.getTheme(EditPost.this).equalsIgnoreCase("white")) {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        } else {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }
        edtdescription.setHashtagEnabled(true);
        edtdescription.setMentionEnabled(true);
        save = getIntent().getExtras().getString("save");
        if (save.equalsIgnoreCase("no")) {
            model = (Post) getIntent().getSerializableExtra("model");
            Log.e("LLL_Come_size: ", String.valueOf(model.getImages().size()));
            if (model != null) {

                oldheight = Float.parseFloat(model.getHeight());
                oldwidth = Float.parseFloat(model.getWidth());
                List<com.TBI.Client.Bluff.Model.GetFeed.Image> imagesarray = model.getImages();
                editPost_adapter = new EditPost_Adapter(EditPost.this, imagesarray, oldheight, oldwidth, height, width);
                viewpagerimage.setAdapter(editPost_adapter);
                indicator.setViewPager(viewpagerimage);

                if (model != null) {
                    postid = model.getId();
                    edtdescription.setText(model.getDescription());
                    txtuname.setText(model.getUsername());
                    txtlocation.setText(model.getLocation());
                }

                if (!model.getPhoto().equals("") && !model.getPhoto().equals(null) && !model.getPhoto().equals("null")) {

                    Glide.with(EditPost.this)
                            .load(model.getPhoto())
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .dontAnimate()
                            .into(imguser);
                } else {
                    imguser.setImageDrawable(getResources().getDrawable(R.drawable.placeholder));
                }

            }
        } else {


            viewmodel = (GetViewPost) getIntent().getSerializableExtra("model");
            if (viewmodel != null) {

                oldheight = Float.parseFloat(viewmodel.getData().get(0).getHeight());
                oldwidth = Float.parseFloat(viewmodel.getData().get(0).getWidth());

                List<Image> imagesarray = viewmodel.getImages();
                postdetail_imageAdapter = new Postdetail_ImageAdapter(EditPost.this, imagesarray, oldheight, oldwidth, height, width);
                viewpagerimage.setAdapter(postdetail_imageAdapter);
                indicator.setViewPager(viewpagerimage);

                if (!viewmodel.getData().isEmpty()) {
                    postid = viewmodel.getData().get(0).getId();
                    edtdescription.setText(viewmodel.getData().get(0).getDescription());
                    txtuname.setText(viewmodel.getData().get(0).getUsername());
                    txtlocation.setText(viewmodel.getData().get(0).getLocation());
                }

                if (!viewmodel.getData().get(0).getPhoto().equals("") && !viewmodel.getData().get(0).getPhoto().equals(null) && !viewmodel.getData().get(0).getPhoto().equals("null")) {
                    Glide.with(EditPost.this)
                            .load(viewmodel.getData().get(0).getPhoto())
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .dontAnimate()
                            .into(imguser);
                } else {
                    imguser.setImageDrawable(getResources().getDrawable(R.drawable.placeholder));
                }

            }

        }

        edtdescription.setHashtagEnabled(true);
        edtdescription.setMentionEnabled(true);

        mentionUserAdapter = new MentionUserAdapter(EditPost.this);
        hashTag_adapter = new HashTagAdapter(EditPost.this);

        edtdescription.setMentionTextChangedListener((view, text) -> {

            Log.d("mn13mentiom", "12" + text);
            SearchUser(text.toString());

        });
        edtdescription.setOnHashtagClickListener((view, text) -> Log.d("mn13click", text.toString()));


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
                           /* startundex = start;
                            Log.d("mn13taguser:", "12" + s.toString().subSequence(start, start + i2));
                            if (!s.toString().subSequence(start, start + i2).toString().replace("@", "").equalsIgnoreCase("")) {

                            } else {
                                startundex = 0;
                            }*/
                            break;


                    }

                    /*if (startundex < s.length()) {
                        Log.d("mn13taguser1:", "12" + s.toString().subSequence(start, start + i2));
                        if (!s.toString().subSequence(start, start + i2).toString().equalsIgnoreCase("")) {
                            SearchUser(s.toString().subSequence(start, start + i2).toString());
                            edtdescription.setMentionEnabled(true);
                        } else {
                            hashTag_adapter.clear();
                            mentionUserAdapter.clear();
                            edtdescription.dismissDropDown();
                        }

                    }*/

                } else {
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtdone.setOnClickListener(view -> {


            cd = new ConnectionDetector(EditPost.this);
            isInternetPresent = cd.isConnectingToInternet();

            if (!isInternetPresent) {
                Toasty.warning(EditPost.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
            } else {
                Edit_Post();
            }


        });

        txtlocation.setOnClickListener(view -> {

            if (txtlocation.getText().toString().trim().equalsIgnoreCase("")) {

                Intent i1 = new Intent(EditPost.this, Locationsearch.class);
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);

            } else {
                ShowLocation();
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_out);
        return super.onOptionsItemSelected(item);
    }


    public void Searchhashtag(CharSequence charSequence) {


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("keyword", charSequence.toString().replace("#", "")));

        new geturl().getdata(EditPost.this, data -> {
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
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(EditPost.this) + ""));
        paramArrayList.add(new param("keyword", s + ""));
        paramArrayList.add(new param("filter_by", "all"));

        new geturl().getdata(EditPost.this, data -> {
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
                    Toasty.error(EditPost.this, status + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                Toasty.error(EditPost.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }
        }, paramArrayList, "user_listing");
    }

    public void ShowLocation() {


        dialoglocation = new Dialog(EditPost.this);
        dialoglocation.setContentView(R.layout.dialoge_removelocation);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialoglocation.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.8f);
        int dialogWindowHeight = (int) (displayHeight * 0.75f);
        layoutParams.width = displayWidth;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.BOTTOM;
        dialoglocation.getWindow().setAttributes(layoutParams);

        dialoglocation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialoglocation.getWindow().getAttributes().windowAnimations = R.style.animationName;


        txtblock = dialoglocation.findViewById(R.id.txtblock);
        txtcancel = dialoglocation.findViewById(R.id.txtcancel);


        txtcancel.setOnClickListener(view -> {

            if (dialoglocation.isShowing()) {
                dialoglocation.dismiss();
            }

            Intent i1 = new Intent(EditPost.this, Locationsearch.class);
            startActivity(i1);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);


        });

        txtblock.setOnClickListener(view -> {


            cd = new ConnectionDetector(EditPost.this);
            isInternetPresent = cd.isConnectingToInternet();

            if (!isInternetPresent) {
                Toasty.warning(EditPost.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
            } else {
                Remove_Location();
            }

        });

        if (!dialoglocation.isShowing()) {
            dialoglocation.show();
        }
    }

    public void Remove_Location() {


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("post_id", postid + ""));


        new geturl().getdata(EditPost.this, data -> {
            userlist = new ArrayList<>();
            try {
                Public_Function.Hide_ProgressDialogue();
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("msg");
                if (message) {
                    YoYo.with(Techniques.SlideInUp)
                            .duration(1000)
                            .onEnd(animator -> {

                            })
                            .playOn(txtlocation);
                    txtlocation.setText("");
                    //txtlocation.setVisibility(View.INVISIBLE);

                    location = "";
                    lat = "";
                    lon = "";


                    if (dialoglocation.isShowing()) {
                        dialoglocation.dismiss();
                    }

                } else {
                    Toasty.error(EditPost.this, status + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                Toasty.error(EditPost.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }
        }, paramArrayList, "remove_location");
    }

    public void Edit_Post() {


        Public_Function.Show_Progressdialog(EditPost.this);

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("post_id", postid + ""));
        paramArrayList.add(new param("description", edtdescription.getText().toString() + ""));
        paramArrayList.add(new param("location", location + ""));
        paramArrayList.add(new param("lat", lat + ""));
        paramArrayList.add(new param("lang", lon + ""));

        new geturl().getdata(EditPost.this, data -> {

            Public_Function.Hide_ProgressDialogue();

            userlist = new ArrayList<>();
            try {
                Public_Function.Hide_ProgressDialogue();
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("msg");
                if (message) {

                    finish();
                    overridePendingTransition(R.anim.slide_enter, R.anim.slide_out);

                } else {
                    Toasty.error(EditPost.this, status + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                Toasty.error(EditPost.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }
        }, paramArrayList, "edit_post");
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (islocation) {
            islocation = false;
            txtlocation.setText(PostCreate.location);
            lat = PostCreate.latitude;
            lon = PostCreate.longitude;
            location = PostCreate.location;

            YoYo.with(Techniques.SlideInUp)
                    .duration(1000)
                    .onEnd(animator -> {

                    })
                    .playOn(txtlocation);

        }

    }

    public void setmodel(com.TBI.Client.Bluff.Model.GetFeed.Image image) {

        if (image.isTagshow()) {
            image.setTagshow(false);
        } else {
            image.setTagshow(true);
        }

        if (save.equalsIgnoreCase("no")) {
            if (editPost_adapter != null) {
                editPost_adapter.notifyDataSetChanged();
            }
        } else {
            if (postdetail_imageAdapter != null) {
                postdetail_imageAdapter.notifyDataSetChanged();
            }
        }


        Log.d("mn13click", "Done");


    }

}
