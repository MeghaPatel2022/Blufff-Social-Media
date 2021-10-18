package com.TBI.Client.Bluff.Activity.Post;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.TBI.Client.Bluff.Activity.Profile.OtherUserProfile;
import com.TBI.Client.Bluff.Activity.Profile.ProfilePage;
import com.TBI.Client.Bluff.Adapter.Post.Comment_Adapter;
import com.TBI.Client.Bluff.Adapter.Post.HashTagAdapter;
import com.TBI.Client.Bluff.Adapter.Post.MentionUserAdapter;
import com.TBI.Client.Bluff.Fragment.Look_Fragment;
import com.TBI.Client.Bluff.Model.AddComment.AddComment;
import com.TBI.Client.Bluff.Model.LoadMore_Comment.LoadComments;
import com.TBI.Client.Bluff.Model.PostDetail.Comment;
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
import com.google.gson.Gson;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.hendraanggrian.appcompat.widget.SocialTextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class CommentActivity extends AppCompatActivity {

    @BindView(R.id.lstcomment)
    ListView lstcomment;
    @BindView(R.id.imgprofilepic)
    CircleImageView imgprofilepic;
    @BindView(R.id.txtuname)
    TextView txtuname;
    @BindView(R.id.txtctime)
    TextView txtctime;
    @BindView(R.id.txtcomment)
    SocialTextView txtcomment;
    @BindView(R.id.imgdone)
    ImageView imgdone;
    @BindView(R.id.edtdescription)
    SocialAutoCompleteTextView edtdescription;
    @BindView(R.id.et_desc)
    TextView et_desc;
    @BindView(R.id.fm_last)
    RelativeLayout fm_last;
    @BindView(R.id.fm_main)
    FrameLayout fm_main;
    @BindView(R.id.img_back)
    ImageView img_back;
    List<Comment> commentarraylist = new ArrayList<Comment>();
    List<Hastag> hastags = new ArrayList<>();
    HashTagAdapter hashTag_adapter;
    MentionUserAdapter mentionUserAdapter;
    List<Datum> userlist = new ArrayList<Datum>();
    int position;
    int selectposition;
    String post_id = "";
    int more_comment = 0;
    int offset = 1, limit = 20;
    String comment = "", come = "";
    private Comment_Adapter comment_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        ButterKnife.bind(CommentActivity.this);

        comment = getIntent().getExtras().getString("comment");
        position = getIntent().getExtras().getInt("position");
        come = getIntent().getExtras().getString("come");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (come.equalsIgnoreCase("wall")) {
            post_id = Look_Fragment.postarray.get(position).getId() + "";
            more_comment = Look_Fragment.postarray.get(position).getMore_comments();

            if (!Look_Fragment.postarray.get(position).getPhoto().equals("") && !Look_Fragment.postarray.get(position).getPhoto().equals(null) && !Look_Fragment.postarray.get(position).getPhoto().equals("null")) {
                Glide.with(CommentActivity.this)
                        .load(Look_Fragment.postarray.get(position).getPhoto())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .dontAnimate()
                        .into(imgprofilepic);
            } else {
                imgprofilepic.setImageDrawable(getResources().getDrawable(R.drawable.placeholder));
            }

            txtuname.setText(Look_Fragment.postarray.get(position).getFullName());
            txtctime.setText(Look_Fragment.postarray.get(position).getTime_duration());
            txtcomment.setText(Look_Fragment.postarray.get(position).getDescription());

        } else if (come.equalsIgnoreCase("profile")) {

            if (!ProfilePage.postarray.get(position).getPhoto().equals("") && !ProfilePage.postarray.get(position).getPhoto().equals(null) && !ProfilePage.postarray.get(position).getPhoto().equals("null")) {
                Glide.with(CommentActivity.this)
                        .load(ProfilePage.postarray.get(position).getPhoto())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .dontAnimate()
                        .into(imgprofilepic);
            } else {
                imgprofilepic.setImageDrawable(getResources().getDrawable(R.drawable.placeholder));
            }
            txtuname.setText(ProfilePage.postarray.get(position).getFullName());
            txtctime.setText(ProfilePage.postarray.get(position).getTime_duration());
            txtcomment.setText(ProfilePage.postarray.get(position).getDescription());
            post_id = ProfilePage.postarray.get(position).getId() + "";
            more_comment = ProfilePage.postarray.get(position).getMore_comments();
        } else if (come.equalsIgnoreCase("otherprofile")) {
            post_id = OtherUserProfile.postarray.get(position).getId() + "";
            more_comment = OtherUserProfile.postarray.get(position).getMore_comments();

            if (!OtherUserProfile.postarray.get(position).getPhoto().equals("") && !OtherUserProfile.postarray.get(position).getPhoto().equals(null) && !OtherUserProfile.postarray.get(position).getPhoto().equals("null")) {
                Glide.with(CommentActivity.this)
                        .load(OtherUserProfile.postarray.get(position).getPhoto())
                        .error(R.drawable.placeholder)
                        .dontAnimate()
                        .into(imgprofilepic);
            } else {
                imgprofilepic.setImageDrawable(getResources().getDrawable(R.drawable.placeholder));
            }
            txtuname.setText(OtherUserProfile.postarray.get(position).getFullName());
            txtctime.setText(OtherUserProfile.postarray.get(position).getTime_duration());
            txtcomment.setText(OtherUserProfile.postarray.get(position).getDescription());
        }

        comment_adapter = new Comment_Adapter(CommentActivity.this, commentarraylist);
        lstcomment.setAdapter(comment_adapter);

        lstcomment.setOnItemClickListener((adapterView, view, i, l) -> {

            Log.d("mn13click", "12");

            if (sharedpreference.getUserId(CommentActivity.this).equalsIgnoreCase(commentarraylist.get(i).getUserId() + "")) {
                Intent i1 = new Intent(CommentActivity.this, ProfilePage.class);
                i1.putExtra("type", "left");
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            } else {
                Intent i1 = new Intent(CommentActivity.this, OtherUserProfile.class);
                i1.putExtra("other_id", commentarraylist.get(i).getUserId() + "");
                i1.putExtra("other_username", "");
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }
        });

        Load_morecomment();
        lstcomment.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                Log.d("mn13morecomment", more_comment + "");
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (more_comment == 1) {

                        if (offset > 20) {
                            offset = offset + 20;
                        } else {

                        }
                        Load_morecomment();
                    }
                }
            }
        });

        imgdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Public_Function.isInternetConnected(CommentActivity.this)) {
                    Toasty.warning(CommentActivity.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    Public_Function.hideKeyboard(CommentActivity.this);
                    Add_Comment();
                }
            }
        });


        et_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fm_last.getVisibility() == View.VISIBLE) {
                    fm_last.setVisibility(View.GONE);
                    fm_main.setVisibility(View.VISIBLE);
                    Public_Function.showSoftKeyboard(edtdescription, CommentActivity.this);
                    edtdescription.setCursorVisible(true);
                } else {
                    fm_last.setVisibility(View.VISIBLE);
                    fm_main.setVisibility(View.GONE);
                }
            }
        });

        edtdescription.setHashtagEnabled(true);
        edtdescription.setMentionEnabled(true);

        mentionUserAdapter = new MentionUserAdapter(CommentActivity.this);
        hashTag_adapter = new HashTagAdapter(CommentActivity.this);

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

                if (!TextUtils.isEmpty(s) && start < s.length()) {
                    switch (s.charAt(start)) {
                        case '#':
                            //startindex = start;
                            Log.d("mn13taguser:", "" + s.toString().subSequence(start, start + i2));
                            if (!s.toString().subSequence(start, start + i2).toString().replace("#", "").equalsIgnoreCase("")) {
                                edtdescription.setHashtagEnabled(true);
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

                if (s.length() > 0) {
//                    imgdone.setImageDrawable(getResources().getDrawable(RM.drawable.send_commment_active));
                    imgdone.setEnabled(true);
                } else {
//                    imgdone.setImageDrawable(getResources().getDrawable(RM.drawable.send_commment_inactive));
                    imgdone.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtdescription.setOnFocusChangeListener((view, b) -> {

            if (b) {
                Log.d("mn13visivl", "yes");
                edtdescription.setActivated(true);
                edtdescription.setPressed(true);
                edtdescription.setSelected(true);


            } else {
                Log.d("mn13visivl", "no" +
                        "");
            }
        });

    }

    public void Load_morecomment() {

        Log.e("LLLLL_Post_id; ", post_id + "   " + limit + "       " + offset + Public_Function.gettimezone());
        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(CommentActivity.this)));
        paramArrayList.add(new param("post_id", post_id + ""));
        paramArrayList.add(new param("limit", limit + ""));
        paramArrayList.add(new param("offset", offset + ""));
        paramArrayList.add(new param("timezone", Public_Function.gettimezone()));

        new geturl().getdata(CommentActivity.this, data -> {
            try {
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("message");

                if (message) {
                    Log.e("LLLL_Comments: ", data);
                    LoadComments login = new Gson().fromJson(data, LoadComments.class);
                    List<Comment> commentarray = new ArrayList<Comment>();
                    commentarray = login.getComments();
                    comment_adapter.AppendAll(commentarray);
                    more_comment = login.getMoreComments();
                } else {
                    // Toasty.error(getActivity(), status + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                Toasty.error(CommentActivity.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }

            if (offset < 20) {
                offset = 20;
            }
        }, paramArrayList, "view_post_comments");
    }

    public void Add_Comment() {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(CommentActivity.this)));
        paramArrayList.add(new param("post_id", post_id));
        paramArrayList.add(new param("comment", edtdescription.getText().toString()));
        paramArrayList.add(new param("timezone", Public_Function.gettimezone()));
        paramArrayList.add(new param("comment_date", Public_Function.getcurrentdatetime()));

        new geturl().getdata(CommentActivity.this, data -> {
            try {
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("message");

                if (message) {
                    fm_last.setVisibility(View.VISIBLE);
                    fm_main.setVisibility(View.GONE);

                    AddComment login = new Gson().fromJson(data, AddComment.class);
                    comment_adapter.add(login.getComments());

                    Log.e("LLLL_Comment: ", data);
//                    if (come.equalsIgnoreCase("wall")) {
//                        List<Comment> carray = new ArrayList<>();
//                        carray.addAll(Look_Fragment.postarray.get(selectposition).getComments());
//                        carray.add(login.getComments());
//                        Look_Fragment.postarray.get(selectposition).setComments(carray);
//                    } else if (come.equalsIgnoreCase("profile")) {
//                        List<Comment> carray = new ArrayList<>();
//                        carray.addAll(ProfilePage.postarray.get(selectposition).getComments());
//                        carray.add(login.getComments());
//                        ProfilePage.postarray.get(selectposition).setComments(carray);
//                    } else if (come.equalsIgnoreCase("otherprofile")) {
//                        List<Comment> carray = new ArrayList<>();
//                        carray.addAll(OtherUserProfile.postarray.get(selectposition).getComments());
//                        carray.add(login.getComments());
//                        OtherUserProfile.postarray.get(selectposition).setComments(carray);
//                    }

//                    Load_morecomment();
                    edtdescription.setText("");
                    imgdone.setEnabled(false);
                    Public_Function.hideKeyboard(CommentActivity.this);
                    lstcomment.setSelection(comment_adapter.getCount() - 1);

                } else {
                }
            } catch (Exception e) {
                Log.e("LLLLL_Exception: ", Objects.requireNonNull(e.getMessage()));
                e.printStackTrace();
            }
        }, paramArrayList, "save_comment");
    }

    public void Searchhashtag(CharSequence charSequence) {


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("keyword", charSequence.toString().replace("#", "")));

        new geturl().getdata(CommentActivity.this, data -> {
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
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(CommentActivity.this)));
        paramArrayList.add(new param("keyword", s + ""));
        paramArrayList.add(new param("filter_by", "all"));

        new geturl().getdata(CommentActivity.this, data -> {
            userlist = new ArrayList<>();
            try {
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
                    Toasty.error(CommentActivity.this, status + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                Log.e("LLLL_Error: ", e.getMessage());
                Toasty.error(CommentActivity.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }
        }, paramArrayList, "user_listing");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fm_main.getVisibility() == View.VISIBLE) {
            Public_Function.hideKeyboard(CommentActivity.this);
            fm_main.setVisibility(View.GONE);
            fm_last.setVisibility(View.VISIBLE);

            new CountDownTimer(500, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }.start();

        } else {
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }
}
