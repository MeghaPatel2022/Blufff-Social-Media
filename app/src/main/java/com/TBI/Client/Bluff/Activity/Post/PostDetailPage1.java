package com.TBI.Client.Bluff.Activity.Post;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Profile.OtherUserProfile;
import com.TBI.Client.Bluff.Activity.Profile.ProfilePage;
import com.TBI.Client.Bluff.Activity.Settings.Saved_Post;
import com.TBI.Client.Bluff.Adapter.Post.Comment_Adapter;
import com.TBI.Client.Bluff.Adapter.Post.FollowFrendAdapter;
import com.TBI.Client.Bluff.Adapter.Post.HashTagAdapter;
import com.TBI.Client.Bluff.Adapter.Post.MentionUserAdapter;
import com.TBI.Client.Bluff.Adapter.Post.Postdetail_ImageAdapter;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.Model.AddComment.AddComment;
import com.TBI.Client.Bluff.Model.LoadMore_Comment.LoadComments;
import com.TBI.Client.Bluff.Model.PostDetail.Comment;
import com.TBI.Client.Bluff.Model.PostDetail.GetViewPost;
import com.TBI.Client.Bluff.Model.PostDetail.Image;
import com.TBI.Client.Bluff.Model.SearchHashtag.Hastag;
import com.TBI.Client.Bluff.Model.SearchHashtag.SearchHashtag;
import com.TBI.Client.Bluff.Model.SearchUser.Datum;
import com.TBI.Client.Bluff.Model.SearchUser.SearchUser;
import com.TBI.Client.Bluff.Model.View_Connection.Following;
import com.TBI.Client.Bluff.Model.View_Connection.ViewConnection;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.KeyboardUtils;
import com.TBI.Client.Bluff.Utils.MyAsyncTaskCallback;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.TBI.Client.Bluff.view.NonSwipeableViewPager;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.harsh.instatag.InstaTag;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.hendraanggrian.appcompat.widget.SocialTextView;
import com.hendraanggrian.appcompat.widget.SocialView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.TBI.Client.Bluff.Adapter.Post.Postdetail_ImageAdapter.showinstag;

public class PostDetailPage1 extends AppCompatActivity {

    @BindView(R.id.lstcomment)
    ListView lstcomment;
    @BindView(R.id.txttoolbar)
    TextView txttoolbar;
    @BindView(R.id.txtname)
    TextView txtname;
    @BindView(R.id.edtdescription)
    SocialAutoCompleteTextView edtdescription;
    @BindView(R.id.imgdone)
    ImageView imgdone;
    @BindView(R.id.imguser)
    NonSwipeableViewPager imguser;
    @BindView(R.id.img_prev)
    ImageView img_prev;
    @BindView(R.id.img_next)
    ImageView img_next;
    @BindView(R.id.txtbio)
    SocialTextView txtbio;
    @BindView(R.id.imgsaved)
    ImageView imgsaved;
    @BindView(R.id.imgdot)
    ImageView imgdot;
    @BindView(R.id.txtviemore)
    TextView txtviemore;
    @BindView(R.id.txtlocation)
    TextView txtlocation;
    @BindView(R.id.imguserpic)
    ImageView imguserpic;
    @BindView(R.id.imgcomment)
    ImageView imgcomment;
    @BindView(R.id.lncomment)
    LinearLayout lncomment;
    @BindView(R.id.lnimages)
    LinearLayout lnimages;
    @BindView(R.id.imgdownarro)
    ImageView imgdownarro;
    @BindView(R.id.imgprofilepic)
    CircleImageView imgprofilepic;
    @BindView(R.id.txttime)
    TextView txttime;
    @BindView(R.id.instatagview)
    InstaTag instatagview;
    @BindView(R.id.imgtagpeople)
    ImageView imgtagpeople;
    @BindView(R.id.imgtag)
    ImageView imgtag;
    @BindView(R.id.lnsubscribed)
    LinearLayout lnsubscribed;
    @BindView(R.id.imgsubscribed)
    ImageView imgsubscribed;
    @BindView(R.id.imgshare)
    ImageView imgshare;
    @BindView(R.id.txtthirdparty)
    TextView txtthirdparty;
    @BindView(R.id.txtsubscribed)
    TextView txtsubscribed;
    @BindView(R.id.lnthird)
    LinearLayout lnthird;
    @BindView(R.id.lnsub)
    LinearLayout lnsub;
    @BindView(R.id.imgsend)
    ImageView imgsend;
    @BindView(R.id.rv_frend_list)
    RecyclerView rv_frend_list;
    @BindView(R.id.et_share_text)
    SocialAutoCompleteTextView et_share_text;
    @BindView(R.id.tv_send)
    TextView tv_send;
    @BindView(R.id.img_dot_smile)
    ImageView img_dot_smile;
    @BindView(R.id.imgsmile)
    ImageView imgsmile;

    @BindView(R.id.img_ad_prev)
    ImageView img_ad_prev;
    @BindView(R.id.img_close)
    ImageView img_close;
    @BindView(R.id.img_ad_next)
    ImageView img_ad_next;

    @BindView(R.id.imgdraw)
    ImageView imgdraw;

    @BindView(R.id.ll_last)
    LinearLayout ll_last;

    @BindView(R.id.lngesture)
    LinearLayout lngesture;
    @BindView(R.id.lnswipedown)
    LinearLayout lnswipedown;
    @BindView(R.id.ll_send)
    LinearLayout ll_send;

    Comment_Adapter comment_adapter;
    List<Comment> commentarraylist = new ArrayList<Comment>();
    String post_id = "";

    ConnectionDetector cd;
    boolean isInternetPresent = false;
    List<Hastag> hastags = new ArrayList<>();
    HashTagAdapter hashTag_adapter;
    MentionUserAdapter mentionUserAdapter;

    List<Datum> userlist = new ArrayList<Datum>();
    Postdetail_ImageAdapter postdetail_imageAdapter;

    List<Image> imagesarray = new ArrayList<>();
    int saved;
    int other_user_id;
    String showsubscribe;

    boolean expandable = false;
    GetViewPost login;
    String theme = "";
    String comment = "";

    int more_comment = 0;
    int offset = 0, limit = 20;

    float oldheight, oldwidth, height, width;

    InstaTag instatagview1;
    int followpost;

    BottomSheetBehavior behavior;

    List<Following> followingarray = new ArrayList<>();

    // The AdLoader used to load ads.
    private AdLoader adLoader;
    // List of native ads that have been successfully loaded.
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    private FollowFrendAdapter followFrendAdapter;

    android.app.Dialog dialog;
    int like = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("mn13theme", sharedpreference.getTheme(PostDetailPage1.this) + "11");
        if (sharedpreference.getTheme(PostDetailPage1.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postdetailpage);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(PostDetailPage1.this));

        if (sharedpreference.getTheme(PostDetailPage1.this).equalsIgnoreCase("white")) {
            imgtag.setImageDrawable(getResources().getDrawable(R.drawable.tag1));
        } else {
            imgtag.setImageDrawable(getResources().getDrawable(R.drawable.tag1_white));
        }

        theme = sharedpreference.getTheme(PostDetailPage1.this);


        imgtagpeople.setVisibility(View.GONE);
        post_id = getIntent().getExtras().getString("post_id");
        comment = getIntent().getExtras().getString("comment");
        showsubscribe = getIntent().getExtras().getString("showsubscribe");

        if (showsubscribe.equalsIgnoreCase("yes")) {
            lnsubscribed.setVisibility(View.VISIBLE);
            lnimages.setVisibility(View.GONE);
        } else {
            lnsubscribed.setVisibility(View.GONE);
            lnimages.setVisibility(View.VISIBLE);
        }

        lnsub.setOnClickListener(view -> {

            cd = new ConnectionDetector(PostDetailPage1.this);
            isInternetPresent = cd.isConnectingToInternet();

            if (!isInternetPresent) {
                Toasty.warning(PostDetailPage1.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
            } else {
                FollowPost();
            }

        });


        if (comment.equalsIgnoreCase("yes")) {


            new CountDownTimer(500, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    Public_Function.showSoftKeyboard(edtdescription, PostDetailPage1.this);
                    lnimages.setVisibility(View.GONE);
                    lncomment.setVisibility(View.VISIBLE);
                    lstcomment.setVisibility(View.VISIBLE);
                    edtdescription.setCursorVisible(true);
                    imgdownarro.setVisibility(View.VISIBLE);
                    // instatagview.setVisibility(View.GONE);
                }
            }.start();


        } else {

        }

        ViewTreeObserver vto = instatagview.getTagImageView().getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                instatagview.getTagImageView().getViewTreeObserver().removeOnPreDrawListener(this);
                height = instatagview.getTagImageView().getMeasuredHeight();
                width = instatagview.getTagImageView().getMeasuredWidth();
                Log.d("mn13height", height + "::" + width);
                return true;
            }
        });

        imgdownarro.setOnClickListener(view -> {


            Public_Function.hideKeyboard(PostDetailPage1.this);
            lncomment.setVisibility(View.GONE);
            lnimages.setVisibility(View.VISIBLE);
            lstcomment.setVisibility(View.GONE);
            imgdownarro.setVisibility(View.GONE);


            if (postdetail_imageAdapter != null) {
                if (imagesarray.get(imguser.getCurrentItem()).isTagshow()) {
                    showinstag = true;
                    postdetail_imageAdapter.notifyDataSetChanged();
                }


            }

            //instatagview.setVisibility(View.VISIBLE);
        });


        behavior = BottomSheetBehavior.from(ll_send);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

        rv_frend_list.setLayoutManager(new LinearLayoutManager(PostDetailPage1.this, RecyclerView.VERTICAL, false));

        GetFrindlist();

        imgsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        getPostLike();
        imgsmile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (like == 0)
                    setLike(1);
                else
                    setLike(0);
            }
        });

        imgcomment.setOnClickListener(view -> {
            edtdescription.setText("");
            Public_Function.showSoftKeyboard(edtdescription, PostDetailPage1.this);
            lnimages.setVisibility(View.GONE);
            lncomment.setVisibility(View.VISIBLE);
            lstcomment.setVisibility(View.VISIBLE);
            edtdescription.setCursorVisible(true);
            imgdownarro.setVisibility(View.VISIBLE);

            Intent intent = new Intent(PostDetailPage1.this, CommentActivity.class);
            intent.putExtra("comment", "yes");
            intent.putExtra("come", "wall");
            intent.putExtra("position", imguser.getCurrentItem());
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

            if (postdetail_imageAdapter != null) {
                if (imagesarray.get(imguser.getCurrentItem()).isTagshow()) {
                    showinstag = false;
                    postdetail_imageAdapter.notifyDataSetChanged();
                }
            }

            //instatagview.setVisibility(View.GONE);

        });

        imgtagpeople.setOnClickListener(view -> {

            /*if (instatagview.isTagShowing()) {
                instatagview.hideTags();
            } else {
                instatagview.showTags();
            }*/
        });
        edtdescription.setHashtagEnabled(true);
        edtdescription.setMentionEnabled(true);

        mentionUserAdapter = new MentionUserAdapter(PostDetailPage1.this);
        hashTag_adapter = new HashTagAdapter(PostDetailPage1.this);

        edtdescription.setMentionTextChangedListener(new SocialView.OnChangedListener() {
            @Override
            public void onChanged(@NonNull SocialView view, @NonNull CharSequence text) {

                Log.d("mn13mentiom", "12" + text);
                SearchUser(text.toString());

            }
        });

        edtdescription.setOnHashtagClickListener((view, text) -> Log.d("mn13click", text.toString()));
       /* instatagview.setloginusername(sharedpreference.getusername(PostDetailPage1.this));
        instatagview.setVisibility(View.VISIBLE);*/


        imgdot.setOnClickListener(view -> {


            ContextThemeWrapper ctw = new ContextThemeWrapper(PostDetailPage1.this, R.style.CustomPopupTheme);
            PopupMenu popup = new PopupMenu(ctw, imgdot);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.post_delete, popup.getMenu());
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {

                    Log.d("mn13item", item.toString());
                    if (item.getItemId() == R.id.edit) {

                        if (login != null) {
                            Intent i1 = new Intent(PostDetailPage1.this, EditPost.class);
                            i1.putExtra("model", login);
                            i1.putExtra("save", "yes");
                            startActivity(i1);
                            overridePendingTransition(R.anim.fade_in, R.anim.stay);
                        }

                    } else if (item.getItemId() == R.id.delete) {

                        cd = new ConnectionDetector(PostDetailPage1.this);
                        isInternetPresent = cd.isConnectingToInternet();

                        if (!isInternetPresent) {
                            Toasty.warning(PostDetailPage1.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                        } else {
                            Delete_post();
                        }

                    }
                    return true;
                }
            });

            popup.show();

        });

        lstcomment.setOnItemClickListener((adapterView, view, i, l) -> {

            Log.d("mn13click", "12");

            if (sharedpreference.getUserId(PostDetailPage1.this).equalsIgnoreCase("" + commentarraylist.get(i).getUserId())) {
                Intent i1 = new Intent(PostDetailPage1.this, ProfilePage.class);
                i1.putExtra("type", "left");
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            } else {
                Intent i1 = new Intent(PostDetailPage1.this, OtherUserProfile.class);
                i1.putExtra("other_id", commentarraylist.get(i).getUserId() + "");
                i1.putExtra("other_username", "");
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }
        });

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
                    imgdone.setImageDrawable(getResources().getDrawable(R.drawable.send_commment_active));
                    imgdone.setEnabled(true);
                } else {
                    imgdone.setImageDrawable(getResources().getDrawable(R.drawable.send_commment_inactive));
                    imgdone.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        imgdone.setOnClickListener(view -> {


            cd = new ConnectionDetector(PostDetailPage1.this);
            isInternetPresent = cd.isConnectingToInternet();

            if (!isInternetPresent) {
                Toasty.warning(PostDetailPage1.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
            } else {
                Add_Comment();
            }

        });

        txtbio.setHashtagEnabled(true);
        txtbio.setMentionEnabled(true);
        txtbio.setOnMentionClickListener((view, text) -> {

            Log.d("mn13user:", text.toString());

            if (sharedpreference.getusername(PostDetailPage1.this).equalsIgnoreCase("" + text)) {

                Intent i1 = new Intent(PostDetailPage1.this, ProfilePage.class);
                i1.putExtra("type", "left");
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            } else {
                Intent i1 = new Intent(PostDetailPage1.this, OtherUserProfile.class);
                i1.putExtra("other_id", "");
                i1.putExtra("other_username", text.toString());
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }


        });

        txtbio.setOnHashtagClickListener((view, text) -> Log.d("mn13tag:", text.toString()));

        txtbio.setMaxLines(4);
        txtbio.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (txtbio.getLineCount() > 4) {
                txtviemore.setVisibility(View.GONE);
                   /* ObjectAnimator animation = ObjectAnimator.ofInt(txtbio, "maxLines", );
                    animation.setDuration(0).start();*/
            } else {
                txtviemore.setVisibility(View.GONE);
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
                   /* if (flag_loading == false) {
                        flag_loading = true;
                        additems();
                    }*/
                }
            }
        });

        txttoolbar.setOnClickListener(view -> {

            if (sharedpreference.getUserId(PostDetailPage1.this).equalsIgnoreCase("" + other_user_id)) {

                Intent i1 = new Intent(PostDetailPage1.this, ProfilePage.class);
                i1.putExtra("type", "left");
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            } else {
                Intent i1 = new Intent(PostDetailPage1.this, OtherUserProfile.class);
                i1.putExtra("other_id", other_user_id + "");
                i1.putExtra("other_username", "");
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }


        });


        txtviemore.setOnClickListener(view -> {

            if (expandable) {
                expandable = false;
                txtbio.setMaxLines(4);
                txtviemore.setText("...View More");
                txtbio.setNestedScrollingEnabled(false);
            } else {
                expandable = true;
                txtbio.setMaxLines(Integer.MAX_VALUE);
                txtviemore.setText("View Less");//As in the android sourcecode
            }

        });

        imgsaved.setOnClickListener(view -> {

            if (saved == 0) {
                Save_Post(1);
            } else if (saved == 1) {
                Save_Post(0);
            }

        });
        comment_adapter = new Comment_Adapter(PostDetailPage1.this, commentarraylist);
        lstcomment.setAdapter(comment_adapter);


        KeyboardUtils.addKeyboardToggleListener(this, isVisible -> {

            if (lncomment.getVisibility() == View.VISIBLE) {
                if (!isVisible) {
                    lncomment.setVisibility(View.GONE);
                    lnimages.setVisibility(View.VISIBLE);
                    lstcomment.setVisibility(View.GONE);
                    imgdownarro.setVisibility(View.GONE);

                    if (postdetail_imageAdapter != null) {
                        if (imagesarray.get(imguser.getCurrentItem()).isTagshow()) {
                            showinstag = true;
                            postdetail_imageAdapter.notifyDataSetChanged();
                        }
                    }

                    // instatagview.setVisibility(View.VISIBLE);
                }
                Log.d("keyboard", "keyboard visible: " + isVisible);
            } else {

            }
        });

    }

    private void setLike(int status) {
        AndroidNetworking.post(geturl.BASE_URL + "like_unlike_post")
                .addHeaders("Authorization", sharedpreference.getUserToken(PostDetailPage1.this))
                .addBodyParameter("user_id", sharedpreference.getUserId(PostDetailPage1.this))
                .addBodyParameter("post_id", post_id)
                .addBodyParameter("status", String.valueOf(status))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getBoolean("success")) {

                                if (status == 0) {
                                    img_dot_smile.setVisibility(View.GONE);
                                } else {
                                    img_dot_smile.setVisibility(View.VISIBLE);
                                }
                            }

                            Log.e("LLLLLL_Like_Point: ", String.valueOf(like));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLLLLL_Like_Error: ", anError.getLocalizedMessage());
                    }
                });
    }


    public void GetFrindlist() {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(PostDetailPage1.this)));
        paramArrayList.add(new param("other_user_id", ""));

        new geturl().getdata(PostDetailPage1.this, new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {
                followingarray = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("message");

                    if (message) {
                        ViewConnection login = new Gson().fromJson(data, ViewConnection.class);

                        Log.e("LLLL_Friend: ", login.getFollowing().toString());
                        followingarray = login.getFollowing();
                        Public_Function.followingarray1.addAll(login.getFollowing());
                        followFrendAdapter = new FollowFrendAdapter(PostDetailPage1.this, followingarray);
                        rv_frend_list.setAdapter(followFrendAdapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, paramArrayList, "user_connection");
    }

    public void Load_morecomment() {


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(PostDetailPage1.this)));
        paramArrayList.add(new param("post_id", post_id + ""));
        paramArrayList.add(new param("limit", limit + ""));
        paramArrayList.add(new param("offset", offset + ""));
        paramArrayList.add(new param("timezone", Public_Function.gettimezone()));


        new geturl().getdata(PostDetailPage1.this, data -> {
            try {
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("message");

                if (message) {
                    LoadComments login = new Gson().fromJson(data, LoadComments.class);
                    List<Comment> commentarray = new ArrayList<Comment>();
                    commentarray = login.getComments();
                    comment_adapter.AppendAll(commentarray);
                    more_comment = login.getMoreComments();
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (offset < 20) {
                offset = 20;
            }
        }, paramArrayList, "view_post_comments");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (lncomment.getVisibility() == View.VISIBLE) {
            Public_Function.hideKeyboard(PostDetailPage1.this);

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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.d("mn13back", "back");

        if (lncomment.getVisibility() == View.VISIBLE) {
            Public_Function.hideKeyboard(PostDetailPage1.this);

            new CountDownTimer(500, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }.start();

        }

        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void Save_Post(Integer id) {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(PostDetailPage1.this)));
        paramArrayList.add(new param("post_id", post_id + ""));
        paramArrayList.add(new param("keep", id + ""));

        new geturl().getdata(PostDetailPage1.this, data -> {
            try {
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("message");

                if (message) {
                    saved = id;
                    if (saved == 0) {
                        imgsaved.setImageDrawable(getResources().getDrawable(R.drawable.save_outline));
                    } else if (saved == 1) {
                        imgsaved.setImageDrawable(getResources().getDrawable(R.drawable.saved_white));
                    }

                    Toast.makeText(PostDetailPage1.this, status + "", Toast.LENGTH_LONG).show();
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, paramArrayList, "bookmark_post");
    }

    public void GetPost() {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(PostDetailPage1.this)));
        paramArrayList.add(new param("post_id", post_id));
        paramArrayList.add(new param("timezone", Public_Function.gettimezone()));

        new geturl().getdata(PostDetailPage1.this, data -> {
            login = null;
            commentarraylist = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("message");

                if (message) {

//                    Log.e("LLLLLLL_Post: ", data);

                    login = new Gson().fromJson(data, GetViewPost.class);
                    if (!login.getData().isEmpty()) {

                        txttoolbar.setText(login.getData().get(0).getUsername());
                        txtname.setText(login.getData().get(0).getFull_name());
                        txtbio.setText(login.getData().get(0).getDescription());
                        other_user_id = login.getData().get(0).getUserId();
                        if (!login.getData().get(0).getPhoto().equals("") && !login.getData().get(0).getPhoto().equals(null) && !login.getData().get(0).getPhoto().equals("null")) {
                            Glide.with(PostDetailPage1.this)
                                    .load(login.getData().get(0).getPhoto())
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .dontAnimate()
                                    .into(imgprofilepic);
                        }

                        oldheight = Float.parseFloat(login.getData().get(0).getHeight());
                        oldwidth = Float.parseFloat(login.getData().get(0).getWidth());

                        if (sharedpreference.getUserId(PostDetailPage1.this).equals(login.getData().get(0).getUserId() + "")) {
                            imgsaved.setVisibility(View.VISIBLE);
                            imgdot.setVisibility(View.VISIBLE);
                        } else {
                            imgsaved.setVisibility(View.VISIBLE);
                            imgdot.setVisibility(View.GONE);
                        }
                        if (login.getData().get(0).getLocation().equalsIgnoreCase("")) {
                            txtlocation.setVisibility(View.GONE);
                        } else {
                            txtlocation.setText(login.getData().get(0).getLocation() + "");
                            txtlocation.setVisibility(View.VISIBLE);
                        }

                    }


                    imagesarray = login.getImages();
                    Log.e("LLL_Size: ", String.valueOf(imagesarray.size()));
                    postdetail_imageAdapter = new Postdetail_ImageAdapter(PostDetailPage1.this, imagesarray, oldheight, oldwidth, height, width);
                    imguser.setAdapter(postdetail_imageAdapter);
                    imguser.setOffscreenPageLimit(imagesarray.size());
                    commentarraylist = login.getComments();
                    comment_adapter.Addall(commentarraylist);

                    if ((imguser.getCurrentItem()) == imagesarray.size() - 1) {
                        img_next.setVisibility(View.GONE);
                    }

                    if ((imguser.getCurrentItem()) == 0) {
                        img_next.setVisibility(View.GONE);
                    }

                    img_next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ((imguser.getCurrentItem()) < imagesarray.size() - 1) {
                                imguser.setCurrentItem(imguser.getCurrentItem() + 1);
                            }
                            if ((imguser.getCurrentItem()) == imagesarray.size() - 1) {
                                img_next.setVisibility(View.GONE);
                            }
                        }
                    });

                    img_prev.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ((imguser.getCurrentItem()) > 0) {
                                imguser.setCurrentItem(imguser.getCurrentItem() - 1);
                            }

                            if ((imguser.getCurrentItem()) == 0) {
                                img_next.setVisibility(View.GONE);
                            }
                        }
                    });

                    if (comment.equalsIgnoreCase("yes")) {
                        if (postdetail_imageAdapter != null) {
                            showinstag = true;
                            postdetail_imageAdapter.notifyDataSetChanged();
                        }
                    }

                    saved = login.getBookmarked();
                    more_comment = login.getMore_comments();
                    txttime.setText(login.getTime_duration() + "");

                    if (saved == 0) {
                        imgsaved.setImageDrawable(getResources().getDrawable(R.drawable.save_outline));
                    } else if (saved == 1) {
                        imgsaved.setImageDrawable(getResources().getDrawable(R.drawable.saved_white));
                    }

                    offset = commentarraylist.size();
                    followpost = login.getFollow_post();
                    if (followpost == 0) {

                        if (sharedpreference.getTheme(PostDetailPage1.this).equalsIgnoreCase("white")) {
                            imgsubscribed.setImageDrawable(getResources().getDrawable(R.drawable.gesture1));
                        } else {
                            imgsubscribed.setImageDrawable(getResources().getDrawable(R.drawable.gesture1_white));
                        }
                        txtsubscribed.setText("Subscribed");
                    } else {
                        txtsubscribed.setText("Unsubscribed");
                        if (sharedpreference.getTheme(PostDetailPage1.this).equalsIgnoreCase("white")) {
                            imgsubscribed.setImageDrawable(getResources().getDrawable(R.drawable.blufffscribed));
                        } else {
                            imgsubscribed.setImageDrawable(getResources().getDrawable(R.drawable.blufffscribed_white));
                        }
                    }

                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, paramArrayList, "view_post");
    }

    public void Add_Comment() {


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(PostDetailPage1.this)));
        paramArrayList.add(new param("post_id", post_id));
        paramArrayList.add(new param("comment", edtdescription.getText().toString()));
        paramArrayList.add(new param("timezone", Public_Function.gettimezone()));
        paramArrayList.add(new param("comment_date", Public_Function.getcurrentdatetime()));

        new geturl().getdata(PostDetailPage1.this, data -> {
            try {
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("message");

                if (message) {
                    AddComment login = new Gson().fromJson(data, AddComment.class);
                    //   commentarraylist=login.getComments();
                    comment_adapter.add(login.getComments());
                    edtdescription.setText("");
                    imgdone.setImageDrawable(getResources().getDrawable(R.drawable.send_commment_inactive));
                    imgdone.setEnabled(false);
                    Public_Function.hideKeyboard(PostDetailPage1.this);
                    lstcomment.setSelection(comment_adapter.getCount() - 1);
                    offset = comment_adapter.getCount() - 1;
                } else {
                    // Toasty.error(getActivity(), status + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                // Toasty.error(Login.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }
        }, paramArrayList, "save_comment");
    }

    public void Searchhashtag(CharSequence charSequence) {


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("keyword", charSequence.toString().replace("#", "")));

        new geturl().getdata(PostDetailPage1.this, data -> {
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
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(PostDetailPage1.this) + ""));
        paramArrayList.add(new param("keyword", s + ""));
        paramArrayList.add(new param("filter_by", "all"));

        new geturl().getdata(PostDetailPage1.this, data -> {
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
                    Toasty.error(PostDetailPage1.this, status + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                Toasty.error(PostDetailPage1.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }
        }, paramArrayList, "user_listing");
    }

    public void Delete_post() {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("post_id", post_id));

        new geturl().getdata(PostDetailPage1.this, data -> {
            try {
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("message");

                if (message) {
                    Saved_Post.deletepost = true;
                    finish();
                    overridePendingTransition(R.anim.slide_enter, R.anim.slide_out);

                } else {
                    // Toasty.error(getActivity(), status + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                // Toasty.error(Login.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }
        }, paramArrayList, "delete_post");
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (theme.equalsIgnoreCase(sharedpreference.getTheme(PostDetailPage1.this))) {

        } else {
            PostDetailPage1.this.recreate();
            PostDetailPage1.this.overridePendingTransition(0, 0);
        }


        cd = new ConnectionDetector(PostDetailPage1.this);
        isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent) {
            Toasty.warning(PostDetailPage1.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
        } else {
            GetPost();
        }

        if (!sharedpreference.getphoto(PostDetailPage1.this).equals("") && !sharedpreference.getphoto(PostDetailPage1.this).equals(null) && !sharedpreference.getphoto(PostDetailPage1.this).equals("null")) {
            Glide.with(PostDetailPage1.this)
                    .load(sharedpreference.getphoto(PostDetailPage1.this))
                    .placeholder(R.drawable.profile_placeholder1)
                    .error(R.drawable.profile_placeholder1)
                    .dontAnimate()
                    .into(imguserpic);
        } else {

        }

    }

    public void setmodel(Image image) {

        if (image.isTagshow()) {
            image.setTagshow(false);
        } else {
            image.setTagshow(true);
        }

        if (postdetail_imageAdapter != null) {
            postdetail_imageAdapter.notifyDataSetChanged();
        }

        Log.d("mn13click", "Done");


    }

    public void setintattag(InstaTag instatagview) {
        instatagview1 = instatagview;
    }

    private void getPostLike() {
        AndroidNetworking.post(geturl.BASE_URL + "view_post")
                .addHeaders("Authorization", sharedpreference.getUserToken(PostDetailPage1.this))
                .addBodyParameter("user_id", sharedpreference.getUserId(PostDetailPage1.this))
                .addBodyParameter("post_id", post_id)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            like = response.getInt("liked");

                            if (like == 0) {
                                img_dot_smile.setVisibility(View.GONE);
                            } else {
                                img_dot_smile.setVisibility(View.VISIBLE);
                            }

                            Log.e("LLLLLL_Like_Point: ", String.valueOf(like));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLLLLL_Like_Error: ", anError.getLocalizedMessage());
                    }
                });
    }

    public void FollowPost() {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(PostDetailPage1.this)));
        paramArrayList.add(new param("post_id", post_id + ""));
        paramArrayList.add(new param("type", "post"));
        if (followpost == 0) {
            paramArrayList.add(new param("status", "1"));
        } else {
            paramArrayList.add(new param("status", "0"));
        }


        new geturl().getdata(PostDetailPage1.this, data -> {
            try {
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("message");

                if (message) {
                    if (followpost == 0) {
                        followpost = 1;
                    } else {
                        followpost = 0;
                    }


                    if (followpost == 0) {

                        if (sharedpreference.getTheme(PostDetailPage1.this).equalsIgnoreCase("white")) {
                            imgsubscribed.setImageDrawable(getResources().getDrawable(R.drawable.gesture1));
                        } else {
                            imgsubscribed.setImageDrawable(getResources().getDrawable(R.drawable.gesture1_white));
                        }
                        txtsubscribed.setText("Subscribed");
                    } else {

                        txtsubscribed.setText("Unsubscribed");

                        if (sharedpreference.getTheme(PostDetailPage1.this).equalsIgnoreCase("white")) {
                            imgsubscribed.setImageDrawable(getResources().getDrawable(R.drawable.blufffscribed));
                        } else {
                            imgsubscribed.setImageDrawable(getResources().getDrawable(R.drawable.blufffscribed_white));
                        }
                    }

                } else {
                    // Toasty.error(getActivity(), status + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                // Toasty.error(Login.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }
        }, paramArrayList, "followPost");
    }

}
