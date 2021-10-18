package com.TBI.Client.Bluff.Activity.Profile;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Post.PostDetailPage;
import com.TBI.Client.Bluff.Activity.Post.ReportedActivity;
import com.TBI.Client.Bluff.Adapter.Post.FollowFrendAdapter;
import com.TBI.Client.Bluff.Adapter.Profile.FriendAdapter;
import com.TBI.Client.Bluff.Adapter.Profile.FriendList_Adapter;
import com.TBI.Client.Bluff.Adapter.Post.UserFeed_Adapter;
import com.TBI.Client.Bluff.Database.DBHelper;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.Fragment.Look_Fragment;
import com.TBI.Client.Bluff.Model.GetOtherUser.Datum;
import com.TBI.Client.Bluff.Model.GetOtherUser.GetOtherUser;
import com.TBI.Client.Bluff.Model.GetProfile.Friend;
import com.TBI.Client.Bluff.Model.GetProfile.Other;
import com.TBI.Client.Bluff.Model.GetProfile.Post;
import com.TBI.Client.Bluff.Model.LoadMore_Post.LoadMorePost;
import com.TBI.Client.Bluff.Model.View_Connection.Follower;
import com.TBI.Client.Bluff.Model.View_Connection.Following;
import com.TBI.Client.Bluff.Model.View_Connection.Request;
import com.TBI.Client.Bluff.Model.View_Connection.ViewConnection;
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
import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.TBI.Client.Bluff.Activity.Profile.ProfilePage.collapse;
import static com.TBI.Client.Bluff.Activity.Profile.ProfilePage.expand;

public class OtherUserProfile extends AppCompatActivity {


    public static List<Post> postarray = new ArrayList<>();
    public static boolean is_shimmar = true;
    public List<Post> demo = new ArrayList<>();
    public List<Integer> postid = new ArrayList<>();


    @BindView(R.id.ll_more)
    LinearLayout ll_more;
    @BindView(R.id.tv_mute)
    TextView tv_mute;
    @BindView(R.id.tv_block)
    TextView tv_block;
    @BindView(R.id.tv_report)
    TextView tv_report;
    @BindView(R.id.ll_cancel)
    LinearLayout ll_cancel;
    @BindView(R.id.tv_share_profile)
    TextView tv_share_profile;
    @BindView(R.id.tv_send)
    TextView tv_send;
    @BindView(R.id.rv_frend_list)
    RecyclerView rv_frend_list;
    @BindView(R.id.ll_send)
    LinearLayout ll_send;
    @BindView(R.id.app_bar_layout)
    AppBarLayout app_bar_layout;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.gridpost)
    ConstraintLayout gridpost;
    @BindView(R.id.txtname)
    TextView txtname;
    @BindView(R.id.txtbio)
    TextView txtbio;
    @BindView(R.id.imguser)
    ImageView imguser;

    @BindView(R.id.imgprofile1)
    CircleImageView imgprofile1;
    @BindView(R.id.simmar_id)
    ShimmerFrameLayout simmar_id;
    @BindView(R.id.txttoolbar)
    TextView txttoolbar;
    @BindView(R.id.txtfolllow)
    TextView txtfolllow;
    @BindView(R.id.imgdot)
    ImageView imgdot;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.nestedscroll)
    NestedScrollView nestedscroll;
    @BindView(R.id.nestedscroll4)
    NestedScrollView nestedscroll4;
    @BindView(R.id.txtfollowers)
    TextView txtfollowers;
    @BindView(R.id.txtfollowing)
    TextView txtfollowing;
    @BindView(R.id.textlooks)
    TextView textlooks;
    @BindView(R.id.lnfollowing)
    LinearLayout lnfollowing;
    @BindView(R.id.lnfollower)
    LinearLayout lnfollower;
    @BindView(R.id.lnfollowrate)
    LinearLayout lnfollowrate;
    @BindView(R.id.lnacceptrate)
    LinearLayout lnacceptrate;
    @BindView(R.id.lnaccpet)
    LinearLayout lnaccpet;
    @BindView(R.id.lndecline)
    LinearLayout lndecline;
    @BindView(R.id.lnrate)
    LinearLayout lnrate;
    @BindView(R.id.lnrateus)
    LinearLayout lnrateus;
    @BindView(R.id.recyclefeed)
    RecyclerView recyclefeed;
    @BindView(R.id.txtpostempty)
    TextView txtpostempty;
    @BindView(R.id.txtlook)
    TextView txtlook;
    @BindView(R.id.txtratename)
    TextView txtratename;
    @BindView(R.id.ratename)
    TextView ratename;
    @BindView(R.id.imgchat)
    ImageView imgchat;
    @BindView(R.id.frame_rating)
    LinearLayout frame_rating;
    @BindView(R.id.lnfriends)
    LinearLayout lnfriends;
    @BindView(R.id.lnlook)
    LinearLayout lnlook;
    @BindView(R.id.txtviewall)
    TextView txtviewall;
    @BindView(R.id.gridfriends)
    ConstraintLayout gridfriends;
    @BindView(R.id.recycledriends)
    RecyclerView recycledriends;
    FriendAdapter friendAdapter;
    List<Follower> frdlist = new ArrayList<>();
    @BindView(R.id.textrating)
    TextView textrating;
    ConnectionDetector cd;
    boolean isInternetPresent = false;
    String other_id = "", username = "", other_username = "";
    int account_privacy, blockeduser;
    Dialog dialogblock;
    TextView txttitle, txtblock, txtcancel, txtdescription;
    boolean follow;
    UserFeed_Adapter userFeed_adapter;
    int random;
    boolean isLoadData = true;
    Dialog dialog;
    private boolean hasRated = false;
    private boolean is_follow = false;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private DBHelper dbHelper;

    List<Following> followingarray = new ArrayList<>();
    private FollowFrendAdapter followFrendAdapter;

    BottomSheetBehavior behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //sharedpreference.setTheme(OtherUserProfile.this, "black");
        Log.d("mn13theme", sharedpreference.getTheme(OtherUserProfile.this) + "11");
        if (sharedpreference.getTheme(OtherUserProfile.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otheruserprofile);
        ButterKnife.bind(this);
        dbHelper = new DBHelper(OtherUserProfile.this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(OtherUserProfile.this));
        simmar_id.startShimmer();

        postarray = new ArrayList<>();

        other_id = getIntent().getExtras().getString("other_id");
        other_username = getIntent().getExtras().getString("other_username");
        Log.e("LLLLLL_Other_Id: ", other_id);

        random = generaterandom();
        Log.d("mn13random", generaterandom() + "");
        userFeed_adapter = new UserFeed_Adapter(OtherUserProfile.this, demo, "otherprofile");
        userFeed_adapter.randome(random);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(OtherUserProfile.this, 2);

        recyclefeed.setLayoutManager(gridLayoutManager);
        recyclefeed.setAdapter(userFeed_adapter);
        recyclefeed.setNestedScrollingEnabled(false);

        cd = new ConnectionDetector(OtherUserProfile.this);
        isInternetPresent = cd.isConnectingToInternet();

        GetOtherUserProfile();
        GetFrindlist();

        collapsingToolbarLayout.setTitle(" ");
        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(" ");
                    back.setVisibility(View.VISIBLE);
                    txttoolbar.setVisibility(View.INVISIBLE);
//                    lnprofile.setVisibility(View.INVISIBLE);
                    // lnbottom.setVisibility(View.INVISIBLE);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    back.setVisibility(View.GONE);
                    txttoolbar.setVisibility(View.INVISIBLE);
//                    lnprofile.setVisibility(View.VISIBLE);
                    //   lnbottom.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });

        txtviewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(OtherUserProfile.this, FriendList.class);
                i1.putExtra("type", "follower");
                i1.putExtra("otherid", other_id);
                i1.putExtra("othername", username);
                i1.putExtra("accountprivacy", 1);
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }
        });

        lnfollower.setOnClickListener(view -> {
            if (follow) {
                Intent i1 = new Intent(OtherUserProfile.this, FriendList.class);
                i1.putExtra("type", "follower");
                i1.putExtra("otherid", other_id);
                i1.putExtra("othername", username);
                i1.putExtra("accountprivacy", 1);
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }

        });

        lnfollowing.setOnClickListener(view -> {
            if (follow) {
                Intent i1 = new Intent(OtherUserProfile.this, FriendList.class);
                i1.putExtra("type", "following");
                i1.putExtra("otherid", other_id);
                i1.putExtra("othername", username);
                i1.putExtra("accountprivacy", 1);

                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }
        });

        lnaccpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                cd = new ConnectionDetector(OtherUserProfile.this);
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    Toasty.warning(OtherUserProfile.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    Accept_RejectRequest("1");
                }

            }
        });
        lndecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                cd = new ConnectionDetector(OtherUserProfile.this);
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    Toasty.warning(OtherUserProfile.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    Accept_RejectRequest("0");
                }

            }
        });

        txtfolllow.setOnClickListener(view -> {

            if (txtfolllow.getText().toString().equalsIgnoreCase("UNBLOCK")) {
                showbloack("unblock");
            } else {
                cd = new ConnectionDetector(OtherUserProfile.this);
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    Toasty.warning(OtherUserProfile.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    Send_Request();
                }


            }


        });


        imgchat.setOnClickListener(view -> {

            Applozic.init(OtherUserProfile.this, getString(R.string.application_key));

            if (TextUtils.isEmpty(other_id)) {
                return;
            }
            Intent intent = new Intent(OtherUserProfile.this, ConversationActivity.class);
            intent.putExtra(ConversationUIService.USER_ID, other_id);
            startActivity(intent);
        });

        imgdot.setOnClickListener(view -> {
            if (ll_more.getVisibility() == View.VISIBLE) {
                collapse(ll_more);
            } else {
                expand(ll_more);
            }

        });

        tv_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (ll_more.getVisibility() == View.VISIBLE) {
                        collapse(ll_more);
                    } else {
                        expand(ll_more);
                    }
                        Intent intent = new Intent(OtherUserProfile.this, ReportedActivity.class);
                        intent.putExtra("come", "otherprofile");
                        intent.putExtra("report", "post");
                        intent.putExtra("u_id", other_id);
                        intent.putExtra("position", 0);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

            }
        });

        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_more.getVisibility() == View.VISIBLE) {
                    collapse(ll_more);
                } else {
                    expand(ll_more);
                }
            }
        });

        tv_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_more.getVisibility() == View.VISIBLE) {
                    collapse(ll_more);
                } else {
                    expand(ll_more);
                }
            }
        });

        tv_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_more.getVisibility() == View.VISIBLE) {
                    collapse(ll_more);
                } else {
                    expand(ll_more);
                }
            }
        });

        nestedscroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {

                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if (isLoadData) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            isLoadData = false;
                            simmar_id.stopShimmer();
                            simmar_id.setVisibility(View.GONE);
                            //GetFeed();
                            LoadMorePost();
                        }
                    }
                }
            }
        });

        frame_rating.setOnClickListener(view -> {

            dialog = new Dialog(OtherUserProfile.this);
            dialog.setContentView(R.layout.activity_rating);
            dialog.getWindow().setBackgroundDrawableResource(R.color.alpha_black);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            dialog.show();

            RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
            ImageView img_done, img_back;

            img_done = dialog.findViewById(R.id.img_done);
            img_back = dialog.findViewById(R.id.img_back);
            Log.e("LLL_rate: ", String.valueOf(Float.parseFloat(sharedpreference.getRate(OtherUserProfile.this))));
            if (!sharedpreference.getRate(OtherUserProfile.this).equalsIgnoreCase("")) {
                ratingBar.setRating(Float.parseFloat(sharedpreference.getRate(OtherUserProfile.this)));
            } else {
                ratingBar.setRating(0);
            }

            img_done.setOnClickListener(v -> {
                String rating = String.valueOf(ratingBar.getRating());

                Log.e("LLL_rate: ", rating);

                if (!rating.equals("0.0")) {
                    addRating(other_id, rating);
                } else {
                    if (dialog.isShowing())
                        dialog.dismiss();
                }

            });

            img_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        finish();
                    }
                }
            });

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

        GetFrindlist1();

        tv_share_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_more.getVisibility() == View.VISIBLE) {
                    collapse(ll_more);
                } else {
                    expand(ll_more);
                }
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Public_Function.isInternetConnected(OtherUserProfile.this)){
                    if (!Public_Function.shareUserList.isEmpty()){
                        sharePost();
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                } else {
                    Toasty.error(OtherUserProfile.this,"Please Check your internet connection...");
                }
            }
        });

    }

    public int generaterandom() {
        Random r = new Random();
        int i1 = r.nextInt(10) + 0;
        if (i1 % 2 == 0) {
            i1 = 3;
        } else {
            i1 = 5;
        }
        Log.d("mn13post:", i1 + "");
        return i1;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_out);
        return super.onOptionsItemSelected(item);
    }

    private void sharePost(){

        Log.e("LLLLL_USer_List: ",Public_Function.shareUserList.toString());
        AndroidNetworking.post(geturl.BASE_URL + "share_user")
                .addHeaders("Authorization", sharedpreference.getUserToken(OtherUserProfile.this))
                .addBodyParameter("user_id",sharedpreference.getUserId(OtherUserProfile.this))
                .addBodyParameter("other_user_id",other_id)
                .addBodyParameter("shared_user_ids", Public_Function.shareUserList.toString())
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Public_Function.shareUserList.clear();
                        try {
                            Log.e("LLLLLLL_Post_Share: ", String.valueOf(response.getBoolean("success")));
                            Log.e("LLLLLLL_Post_Share: ", response.getString("message"));
                        } catch (JSONException e) {
                            Log.e("LLLLLLL_Post_Error: ",e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLLLLLL_Post_Error: ",anError.getMessage());
                    }
                });
    }

    public void Send_Request() {

        Public_Function.Show_Progressdialog(OtherUserProfile.this);

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", other_id + ""));
        paramArrayList.add(new param("followed_by", sharedpreference.getUserId(OtherUserProfile.this) + ""));
        paramArrayList.add(new param("account_privacy", account_privacy + ""));

        new geturl().getdata(OtherUserProfile.this, data -> {
            try {
                Public_Function.Hide_ProgressDialogue();
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("msg");
                if (message) {

                    int follow_status = object.optInt("following_by_you");
                    Log.e("LLLL_Request: ", String.valueOf(follow_status));

                    if (follow_status == 0) {
                        txtfolllow.setText("FOLLOW");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
                        }

                        if (account_privacy == 1) {
                            follow = true;
                            postarray = new ArrayList<>();

                            userFeed_adapter.removeAll();

                        } else {
                            nestedscroll.setVisibility(View.GONE);
                            nestedscroll4.setVisibility(View.VISIBLE);
                        }

                        nestedscroll4.setVisibility(View.GONE);
                        nestedscroll.setVisibility(View.VISIBLE);
                        GetOtherUserProfile();
                    } else if (follow_status == 1) {
                        Log.e("LLLL_Request: ", String.valueOf(follow_status));
                        txtfolllow.setText("FOLLOWING");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
                        }
                        nestedscroll4.setVisibility(View.GONE);
                        nestedscroll.setVisibility(View.VISIBLE);
                        GetOtherUserProfile();
                        if (account_privacy == 2) {
                            follow = true;
                            postarray = new ArrayList<>();

                            userFeed_adapter.removeAll();


                            GetOtherUserProfile();
                        }

                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.darkgrey)));
                        }
                        txtfolllow.setText("REQUESTED");
                        nestedscroll.setVisibility(View.GONE);

                        nestedscroll4.setVisibility(View.VISIBLE);
                    }

                } else {
                    Toasty.error(OtherUserProfile.this, status + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                Toasty.error(OtherUserProfile.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }
        }, paramArrayList, "follow_user");
    }

    public void Block_User() {

        Public_Function.Show_Progressdialog(OtherUserProfile.this);

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", other_id + ""));
        paramArrayList.add(new param("blocked_by", sharedpreference.getUserId(OtherUserProfile.this) + ""));

        new geturl().getdata(OtherUserProfile.this, data -> {
            try {
                Public_Function.Hide_ProgressDialogue();
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("msg");
                if (message) {
                    if (dialogblock.isShowing()) {
                        dialogblock.dismiss();
                    }

                    int blocked = object.optInt("blocked");
                    blockeduser = blocked;
                    if (blocked == 1) {
                        txtfolllow.setText("UNBLOCK");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
                        }
                    } else if (blocked == 0) {
                        txtfolllow.setText("FOLLOW");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
                        }
                    }

                } else {
                    Toasty.error(OtherUserProfile.this, status + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                Toasty.error(OtherUserProfile.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }
        }, paramArrayList, "block_unblock");
    }

    public void GetOtherUserProfile() {

        if (!isInternetPresent) {
            Toasty.warning(OtherUserProfile.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
            if (dbHelper.chceckOtherUSer(Integer.parseInt(other_id))) {
                GetOtherUser login = getUserDetails();
                List<Other> othernodel = login.getOthers();

                if (!othernodel.isEmpty()) {
                    other_id = login.getData().get(0).getId() + "";
                    txtfollowers.setText(String.valueOf(othernodel.get(0).getFollowers()));
                    textlooks.setText(String.valueOf(othernodel.get(0).getPosts()));
                    txtfollowing.setText(String.valueOf(othernodel.get(0).getFollowing()));
                }

                if (!login.getData().isEmpty()) {
                    account_privacy = login.getData().get(0).getAccountPrivacy();
                    txtname.setText(login.getData().get(0).getFullName());
                    txttoolbar.setText(login.getData().get(0).getFullName());

                    username = login.getData().get(0).getUsername();
                    if (!login.getData().get(0).getBio().equals("")) {
                        txtbio.setVisibility(View.VISIBLE);
                        txtbio.setText(login.getData().get(0).getBio());
                    } else {
                        txtbio.setVisibility(View.GONE);
                    }

                    hasRated = login.getData().get(0).getHasRated() != 0;
                    sharedpreference.setRate(OtherUserProfile.this, login.getData().get(0).getRateAvg() + "");
                    Log.e("LLL_follow_status: ", login.getData().get(0).getFollowingByYou().toString());
                    Log.e("LLL_has_rated: ", String.valueOf(hasRated));
                    Log.e("LLL_follow_status: ", login.getData().get(0).getFollowByUser().toString());

                    if (login.getData().get(0).getFollowByUser() == 2) {
                        lnfollowrate.setVisibility(View.GONE);
                        lnacceptrate.setVisibility(View.VISIBLE);
                    } else {
                        lnfollowrate.setVisibility(View.VISIBLE);
                        lnacceptrate.setVisibility(View.GONE);
                    }

                    if (login.getData().get(0).getFollowingByYou() == 0) {
                        txtfolllow.setText("FOLLOW");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
                        }

                        nestedscroll4.setVisibility(View.GONE);
                        nestedscroll.setVisibility(View.VISIBLE);

                        if (account_privacy == 1) {
                            follow = true;
                            postarray = new ArrayList<>();

                            userFeed_adapter.removeAll();


                        } else {
                            nestedscroll.setVisibility(View.GONE);

                            nestedscroll4.setVisibility(View.VISIBLE);
                        }

                    } else if (login.getData().get(0).getFollowingByYou() == 1) {
                        txtfolllow.setText("FOLLOWING");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
                        }
                        nestedscroll4.setVisibility(View.GONE);
                        nestedscroll.setVisibility(View.VISIBLE);

                        if (account_privacy == 1) {
                            follow = true;
                            postarray = new ArrayList<>();

                            userFeed_adapter.removeAll();


                        } else {
                            nestedscroll.setVisibility(View.GONE);

                            nestedscroll4.setVisibility(View.VISIBLE);
                        }

                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.darkgrey)));
                        }
                        txtfolllow.setText("REQUESTED");
                        nestedscroll.setVisibility(View.GONE);

                        nestedscroll4.setVisibility(View.VISIBLE);
                    }

                    textrating.setText(new DecimalFormat("##.##").format(login.getData().get(0).getRateAvg()) + "/5");

                    txtname.setText(login.getData().get(0).getFullName());
                    if (!login.getData().get(0).getBio().equalsIgnoreCase("")) {
                        txtbio.setText(login.getData().get(0).getBio());
                    }

                    if (!login.getData().get(0).getPhoto().equalsIgnoreCase("")) {
                        imgprofile1.setVisibility(View.VISIBLE);

                        Glide.with(OtherUserProfile.this)
                                .load(login.getData().get(0).getPhoto())
                                .placeholder(R.drawable.profile_placeholder1)
                                .error(R.drawable.profile_placeholder1)
                                .dontAnimate()
                                .into(imgprofile1);

                    }


                    if (!login.getData().get(0).getCoverPhoto().equalsIgnoreCase("")){
                        imguser.setVisibility(View.VISIBLE);
                        simmar_id.stopShimmer();
                        simmar_id.setVisibility(View.GONE);
                        Glide.with(OtherUserProfile.this)
                                .load(login.getData().get(0).getCoverPhoto())
                                .placeholder(R.drawable.profile_placeholder1)
                                .error(R.drawable.profile_placeholder1)
                                .dontAnimate()
                                .into(imguser);

                    } else {
                        imguser.setVisibility(View.VISIBLE);
                        simmar_id.stopShimmer();
                        simmar_id.setVisibility(View.GONE);
                        Glide.with(OtherUserProfile.this)
                                .load(R.drawable.profile_placeholder1)
                                .placeholder(R.drawable.profile_placeholder1)
                                .error(R.drawable.profile_placeholder1)
                                .dontAnimate()
                                .into(imguser);
                    }

                    blockeduser = login.getData().get(0).getBlocked();
                    if (login.getData().get(0).getBlocked() == 0) {
                        if (login.getData().get(0).getAccountPrivacy() == 1) {
                            follow = true;
                            postarray = login.getPosts();

                                /*userPost_adapter.appendItems(moarItems(postarray.size()));
                                userPost_adapter.additem(postarray);*/
                            List<Post> dummyarray;
                            dummyarray = login.getPosts();
                            userFeed_adapter.addAll(dummyarray);
                            isLoadData = !dummyarray.isEmpty();

                        } else {
                            if (login.getData().get(0).getFollowingByYou() == 1) {
                                txtfolllow.setText("FOLLOWING");
                                follow = true;
                                List<Post> dummyarray;
                                dummyarray = login.getPosts();
                                postarray = login.getPosts();

                                userFeed_adapter.addAll(dummyarray);
                                isLoadData = !dummyarray.isEmpty();


                                    /*userPost_adapter.appendItems(moarItems(postarray.size()));
                                    userPost_adapter.additem(postarray);*/


                                gridpost.setVisibility(View.VISIBLE);

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
                                }
                            } else if (login.getData().get(0).getFollowingByYou() == 0) {
                                follow = false;

                                gridpost.setVisibility(View.GONE);
                                txtfolllow.setText("FOLLOW");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
                                }
                            } else {
                                follow = false;

                                gridpost.setVisibility(View.GONE);
                                txtfolllow.setText("REQUESTED");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.darkgrey)));
                                }
                            }
                        }
                    } else {
                        follow = false;
                        txtfolllow.setText("UNBLOCK");

                        gridpost.setVisibility(View.GONE);

                    }
                }
            } else {
//                Toasty.error(OtherUserProfile.this, "Oops something went wrong....", Toast.LENGTH_LONG, true).show();
            }

        } else {

            Public_Function.Show_Progressdialog(OtherUserProfile.this);

            ArrayList<param> paramArrayList = new ArrayList<>();
            paramArrayList.add(new param("user_id", sharedpreference.getUserId(OtherUserProfile.this) + ""));
            paramArrayList.add(new param("other_user_id", other_id + ""));
            paramArrayList.add(new param("other_username", other_username + ""));
            paramArrayList.add(new param("timezone", Public_Function.gettimezone()));

            Log.e("LLL_other_user: ", "id: " + sharedpreference.getUserId(OtherUserProfile.this) + ""
                    + "      other_user_id: " + other_id + "     other_username: " + other_username + "      timezone: " + Public_Function.gettimezone());

            new geturl().getdata(OtherUserProfile.this, data -> {
                postarray = new ArrayList<>();
                postid = new ArrayList<>();
                try {
                    Public_Function.Hide_ProgressDialogue();
                    Log.e("LLLL_Res: ",data);
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("msg");

                    if (message) {

                        GetOtherUser login = new Gson().fromJson(data, GetOtherUser.class);
                        List<Other> othernodel = login.getOthers();

//                        Log.e("LLLLL_Get_View_Prof: ", object.getJSONArray("data") + "");
//                        if (!dbHelper.chceckOtherUSer(Integer.parseInt(other_id))) {
//                            dbHelper.insertOtherUSer(other_id,
//                                    object.getJSONArray("data") + "",
//                                    object.getJSONArray("friends") + "",
//                                    object.getJSONArray("posts") + "",
//                                    object.getJSONArray("others") + "");
//                        } else {
//                            dbHelper.updateOtherUSer(other_id,
//                                    object.getJSONArray("data") + "",
//                                    object.getJSONArray("friends") + "",
//                                    object.getJSONArray("posts") + "",
//                                    object.getJSONArray("others") + "");
//                        }

                        if (!othernodel.isEmpty()) {
                            other_id = login.getData().get(0).getId() + "";
                            txtfollowers.setText(String.valueOf(othernodel.get(0).getFollowers()));
                            textlooks.setText(String.valueOf(othernodel.get(0).getPosts()));
                            txtfollowing.setText(String.valueOf(othernodel.get(0).getFollowing()));
                        }

                        if (!login.getData().isEmpty()) {
                            account_privacy = login.getData().get(0).getAccountPrivacy();
                            txtname.setText(login.getData().get(0).getFullName());
                            txttoolbar.setText(login.getData().get(0).getFullName());

                            username = login.getData().get(0).getUsername();
                            if (!login.getData().get(0).getBio().equals("")) {
                                txtbio.setVisibility(View.VISIBLE);
                                txtbio.setText(login.getData().get(0).getBio());
                            } else {
                                txtbio.setVisibility(View.GONE);
                            }

                            hasRated = login.getData().get(0).getHasRated() != 0;
                            sharedpreference.setRate(OtherUserProfile.this, login.getData().get(0).getRateAvg() + "");
                            Log.e("LLL_has_rated: ", String.valueOf(hasRated));
                            Log.e("LLL_follow_status_you: ", login.getData().get(0).getFollowingByYou().toString());
                            Log.e("LLL_follow_status: ", login.getData().get(0).getFollowByUser().toString());

                            if (login.getData().get(0).getFollowByUser() == 2) {
                                lnfollowrate.setVisibility(View.GONE);
                                lnacceptrate.setVisibility(View.VISIBLE);
                            } else {
                                lnfollowrate.setVisibility(View.VISIBLE);
                                lnacceptrate.setVisibility(View.GONE);
                            }
                            if (login.getData().get(0).getFollowingByYou() == 0) {
                                txtfolllow.setText("FOLLOW");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
                                }

                                nestedscroll4.setVisibility(View.GONE);
                                nestedscroll.setVisibility(View.VISIBLE);

                                if (account_privacy == 1) {
                                    follow = true;
                                    postarray = new ArrayList<>();

                                    userFeed_adapter.removeAll();

                                } else {
                                    nestedscroll.setVisibility(View.GONE);

                                    nestedscroll4.setVisibility(View.VISIBLE);
                                }

                            } else if (login.getData().get(0).getFollowingByYou() == 1) {
                                txtfolllow.setText("FOLLOWING");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
                                }
                                nestedscroll4.setVisibility(View.GONE);
                                nestedscroll.setVisibility(View.VISIBLE);

                                if (account_privacy == 1) {
                                    follow = true;
                                    postarray = new ArrayList<>();

                                    userFeed_adapter.removeAll();


                                }

                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.darkgrey)));
                                }
                                txtfolllow.setText("REQUESTED");
                                nestedscroll.setVisibility(View.GONE);

                                nestedscroll4.setVisibility(View.VISIBLE);
                            }


                            textrating.setText(new DecimalFormat("##.##").format(login.getData().get(0).getRateAvg()) + "/5");

                            txtname.setText(login.getData().get(0).getFullName());
                            String[] s1 = login.getData().get(0).getFullName().split("\\s+");
                            if (!s1[0].isEmpty()) {
                                ratename.setText(s1[0]);
                                txtratename.setText(s1[0]);

                            }
                            if (!login.getData().get(0).getBio().equalsIgnoreCase("")) {
                                txtbio.setText(login.getData().get(0).getBio());
                            }

                            if (!login.getData().get(0).getPhoto().equalsIgnoreCase("")) {
                                imgprofile1.setVisibility(View.VISIBLE);

                                Glide.with(OtherUserProfile.this)
                                        .load(login.getData().get(0).getPhoto())
                                        .placeholder(R.drawable.profile_placeholder1)
                                        .error(R.drawable.profile_placeholder1)
                                        .dontAnimate()
                                        .into(imgprofile1);

                            }


                            if (!login.getData().get(0).getCoverPhoto().equalsIgnoreCase("")){
                                imguser.setVisibility(View.VISIBLE);
                                simmar_id.stopShimmer();
                                simmar_id.setVisibility(View.GONE);
                                Glide.with(OtherUserProfile.this)
                                        .load(login.getData().get(0).getCoverPhoto())
                                        .placeholder(R.drawable.profile_placeholder1)
                                        .error(R.drawable.profile_placeholder1)
                                        .dontAnimate()
                                        .into(imguser);

                            } else {
                                imguser.setVisibility(View.VISIBLE);
                                simmar_id.stopShimmer();
                                simmar_id.setVisibility(View.GONE);
                                Glide.with(OtherUserProfile.this)
                                        .load(R.drawable.profile_placeholder1)
                                        .placeholder(R.drawable.profile_placeholder1)
                                        .error(R.drawable.profile_placeholder1)
                                        .dontAnimate()
                                        .into(imguser);
                            }

                            blockeduser = login.getData().get(0).getBlocked();
                            if (login.getData().get(0).getBlocked() == 0) {

                                if (login.getData().get(0).getAccountPrivacy() == 1) {
                                    follow = true;
                                    postarray = login.getPosts();

                                /*userPost_adapter.appendItems(moarItems(postarray.size()));
                                userPost_adapter.additem(postarray);*/
                                    List<Post> dummyarray;
                                    dummyarray = login.getPosts();
                                    userFeed_adapter.addAll(dummyarray);
                                    isLoadData = !dummyarray.isEmpty();

//                                if (login.getData().get(0).getFollowingByYou() == 1) {
//                                    txtfolllow.setText("FOLLOWING");
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                        txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(RM.color.textcolor)));
//                                    }
//                                } else if (login.getData().get(0).getFollowingByYou() == 0) {
//                                    // follow = false;
//                                    txtfolllow.setText("FOLLOW");
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                        txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(RM.color.textcolor)));
//                                    }
//                                } else {
//                                    //follow = false;
//                                    txtfolllow.setText("REQUESTED");
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                        txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(RM.color.darkgrey)));
//                                    }
//                                }
                                } else {
                                    if (login.getData().get(0).getFollowingByYou() == 1) {
                                        txtfolllow.setText("FOLLOWING");
                                        follow = true;
                                        List<Post> dummyarray;
                                        dummyarray = login.getPosts();
                                        postarray = login.getPosts();

                                        userFeed_adapter.addAll(dummyarray);
                                        isLoadData = !dummyarray.isEmpty();


                                    /*userPost_adapter.appendItems(moarItems(postarray.size()));
                                    userPost_adapter.additem(postarray);*/


                                        gridpost.setVisibility(View.VISIBLE);

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
                                        }
                                    } else if (login.getData().get(0).getFollowingByYou() == 0) {
                                        follow = false;

                                        gridpost.setVisibility(View.GONE);
                                        txtfolllow.setText("FOLLOW");
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
                                        }
                                    } else {
                                        follow = false;

                                        gridpost.setVisibility(View.GONE);
                                        txtfolllow.setText("REQUESTED");
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            txtfolllow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.darkgrey)));
                                        }
                                    }
                                }
                            } else {
                                follow = false;
                                txtfolllow.setText("UNBLOCK");
                                gridpost.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        Toasty.error(OtherUserProfile.this, status + "", Toast.LENGTH_LONG, true).show();
                    }
                } catch (Exception e) {
                    Log.e("LLLL_Other_Error: ", e.getMessage());
                    Toasty.error(OtherUserProfile.this, e.getMessage(), Toast.LENGTH_LONG, true).show();
                    e.printStackTrace();
                }

                if (postarray.size() == 0) {
                    isLoadData = false;

                    txtpostempty.setVisibility(View.VISIBLE);
                    txtlook.setVisibility(View.GONE);
                    gridpost.setVisibility(View.GONE);
                } else {
                    isLoadData = true;
                    txtpostempty.setVisibility(View.GONE);
                    txtlook.setVisibility(View.VISIBLE);
                    gridpost.setVisibility(View.VISIBLE);
                }

                for (int i = 0; i < postarray.size(); i++) {
                    postid.add(postarray.get(i).getId());
                }

            }, paramArrayList, "view_profile");
        }
    }

    private GetOtherUser getUserDetails() {

        GetOtherUser getOtherUser = new GetOtherUser();
        Cursor cur = dbHelper.getAllOtherUSer();

        Datum[] datum;
        Friend[] friends;
        Post[] posts;
        Other[] others;
        Gson gson = new Gson();
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                GetOtherUser getOtherUser1 = new GetOtherUser();
                datum = gson.fromJson(cur.getString(2), Datum[].class);
                getOtherUser1.setData(Arrays.asList(datum));

//                friends = gson.fromJson(cur.getString(3), Friend[].class);
//                getOtherUser1.setFriends(Arrays.asList(friends));

                posts = gson.fromJson(cur.getString(4), Post[].class);
                getOtherUser1.setPosts(Arrays.asList(posts));

                others = gson.fromJson(cur.getString(5), Other[].class);
                getOtherUser1.setOthers(Arrays.asList(others));

                getOtherUser1 = getOtherUser1;
            } while (cur.moveToNext());
            cur.close();
        }
        return getOtherUser;
    }


    public void LoadMorePost() {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", other_id + ""));
        paramArrayList.add(new param("exclude_post_ids", TextUtils.join(",", postid).replaceAll(",$", "")));
        paramArrayList.add(new param("timezone", Public_Function.gettimezone()));

        new geturl().getdata(OtherUserProfile.this, data -> {
            postid = new ArrayList<>();
            try {

                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("msg");
                if (message) {

                    LoadMorePost login = new Gson().fromJson(data, LoadMorePost.class);
                    List<Post> othernodel;
                    othernodel = login.getPosts();

                    postarray.addAll(othernodel);
                    userFeed_adapter.addAll(othernodel);
                    isLoadData = !othernodel.isEmpty();

                    for (int i = 0; i < postarray.size(); i++) {
                        postid.add(postarray.get(i).getId());
                    }
                } else {
                    //Toasty.error(OtherUserProfile.this, status + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                //Toasty.error(OtherUserProfile.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }
        }, paramArrayList, "load_user_post");
    }

    public void showbloack(String s) {

        dialogblock = new Dialog(OtherUserProfile.this);
        dialogblock.setContentView(R.layout.dialoge_blockuser);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogblock.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.8f);
        int dialogWindowHeight = (int) (displayHeight * 0.75f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        dialogblock.getWindow().setAttributes(layoutParams);

        dialogblock.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogblock.getWindow().getAttributes().windowAnimations = R.style.animationName;


        txttitle = dialogblock.findViewById(R.id.txttitle);
        txtdescription = dialogblock.findViewById(R.id.txtdescription);
        txtblock = dialogblock.findViewById(R.id.txtblock);
        txtcancel = dialogblock.findViewById(R.id.txtcancel);

        if (s.equalsIgnoreCase("Block User")) {
            txttitle.setText("Block " + username + "?");
            txtdescription.setText("They wont't be able to find your profile,posts or story on Blufff. Blufff won't let then know you blocked them.");
            txtblock.setText("Block");
        } else {
            txttitle.setText("Unblock " + username + "?");
            txtdescription.setText("They will now be able to request to follow you on Blufff. Blufff won't let then know you unblocked them.");
            txtblock.setText("Unblock");

        }

        txtcancel.setOnClickListener(view -> {

            if (dialogblock.isShowing()) {
                dialogblock.dismiss();
            }
        });

        txtblock.setOnClickListener(view -> {


            cd = new ConnectionDetector(OtherUserProfile.this);
            isInternetPresent = cd.isConnectingToInternet();

            if (!isInternetPresent) {
                Toasty.warning(OtherUserProfile.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
            } else {
                Block_User();
            }

        });

        if (!dialogblock.isShowing()) {
            dialogblock.show();
        }
    }

    public void addRating(String user_id, String rate) {
        AndroidNetworking.post(geturl.BASE_URL + "rateUser")
                .addHeaders("Authorization", sharedpreference.getUserToken(OtherUserProfile.this))
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("from_id", sharedpreference.getUserId(OtherUserProfile.this))
                .addBodyParameter("rates", rate)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("success").equals("true")) {
                                Log.e("LLL_rate_Res; ", response.toString());
                                Log.e("LLL_otheruser_id; ", user_id);
                                if (dialog.isShowing())
                                    dialog.dismiss();
                                Log.e("LLL_Error_Rate: ", rate);
                                GetOtherUserProfile();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLL_Error_Rate: ", anError.getMessage());
                    }
                });
    }

    public void Accept_RejectRequest(String status) {

        //Public_Function.Show_Progressdialog(context);


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(OtherUserProfile.this) + ""));
        paramArrayList.add(new param("other_user_id", other_id + ""));
        paramArrayList.add(new param("status", status + ""));


        new geturl().getdata(OtherUserProfile.this, new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {
                try {
                    Public_Function.Hide_ProgressDialogue();
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    if (message) {

                        if (status.equals("0")) {
                            txtfolllow.setText("FOLLOW");
                            lnfollowrate.setVisibility(View.VISIBLE);
                            lnacceptrate.setVisibility(View.GONE);
                        } else {
                            txtfolllow.setText("FOLLOWING");
                            lnfollowrate.setVisibility(View.VISIBLE);
                            lnacceptrate.setVisibility(View.GONE);
                        }


                    } else {
                        Toasty.error(OtherUserProfile.this, status + "", Toast.LENGTH_LONG, true).show();
                    }
                } catch (Exception e) {
                    Toasty.error(OtherUserProfile.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                    e.printStackTrace();
                }
            }
        }, paramArrayList, "accept_reject");
    }

    public void GetFrindlist() {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", other_id + ""));
        paramArrayList.add(new param("other_user_id", ""));

        new geturl().getdata(OtherUserProfile.this, new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {
                frdlist = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("message");

                    if (message) {
                        ViewConnection login = new Gson().fromJson(data, ViewConnection.class);

                        Log.e("LLLL_Friend: ", login.getFollowers().toString());
                        frdlist = login.getFollowers();
                        friendAdapter = new FriendAdapter(OtherUserProfile.this, frdlist, OtherUserProfile.this);
                        recycledriends.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                        recycledriends.setAdapter(friendAdapter);



                        if (frdlist.size() == 0) {
                            lnfriends.setVisibility(View.GONE);
                        } else {
                            lnfriends.setVisibility(View.VISIBLE);
                        }
                        if (frdlist.size() >= 4) {
                            txtviewall.setVisibility(View.VISIBLE);
                        } else {
                            txtviewall.setVisibility(View.GONE);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, paramArrayList, "user_connection");
    }

    public void GetFrindlist1() {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(OtherUserProfile.this)));
        paramArrayList.add(new param("other_user_id", ""));

        new geturl().getdata(OtherUserProfile.this, new MyAsyncTaskCallback() {
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
                        rv_frend_list.setLayoutManager(new LinearLayoutManager(OtherUserProfile.this, RecyclerView.VERTICAL, false));
                        followingarray = login.getFollowing();
                        followFrendAdapter = new FollowFrendAdapter(OtherUserProfile.this, followingarray);
                        rv_frend_list.setAdapter(followFrendAdapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, paramArrayList, "user_connection");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.fade_out);
    }
}
