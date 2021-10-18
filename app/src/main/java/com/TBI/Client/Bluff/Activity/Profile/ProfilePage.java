package com.TBI.Client.Bluff.Activity.Profile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Home.OpenCamera1;
import com.TBI.Client.Bluff.Activity.Mains.BottombarActivity;
import com.TBI.Client.Bluff.Activity.Settings.Saved_Post;
import com.TBI.Client.Bluff.Adapter.Post.UserFeed_Adapter;
import com.TBI.Client.Bluff.Adapter.Profile.DrawerAdapter;
import com.TBI.Client.Bluff.Adapter.Profile.Sub_Category_Adapter;
import com.TBI.Client.Bluff.Database.DBHelper;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.Model.GetFeed.Image;
import com.TBI.Client.Bluff.Model.GetProfession.DatumProfession;
import com.TBI.Client.Bluff.Model.GetProfile.Datum;
import com.TBI.Client.Bluff.Model.GetProfile.Friend;
import com.TBI.Client.Bluff.Model.GetProfile.GetProfile;
import com.TBI.Client.Bluff.Model.GetProfile.Other;
import com.TBI.Client.Bluff.Model.GetProfile.Post;
import com.TBI.Client.Bluff.Model.Get_Notification.GetNotification;
import com.TBI.Client.Bluff.Model.LoadMore_Post.LoadMorePost;
import com.TBI.Client.Bluff.Model.PostDetail.Comment;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.UserPages.WelcomeActivity;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.ListViewUtil;
import com.TBI.Client.Bluff.Utils.MyAsyncTaskCallback;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.listners.AlLogoutHandler;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

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
import im.ene.toro.widget.Container;

import static com.TBI.Client.Bluff.Activity.Post.PostDetailPage.isDelete;

public class ProfilePage extends AppCompatActivity {

    public static List<Post> postarray = new ArrayList<>();
    public static int delete = -1;
    public List<Post> demo = new ArrayList<>();
    public List<Integer> postid = new ArrayList<>();
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;
    @BindView(R.id.nav_list)
    ListViewUtil nav_list;
    Dialog dialogelogout;
    Button cancle, ok;
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
    @BindView(R.id.lnnopic)
    LinearLayout lnnopic;
    @BindView(R.id.ll_bottom)
    LinearLayout lnbottom;
    @BindView(R.id.lnfollower)
    LinearLayout lnfollower;
    @BindView(R.id.lnfollowing)
    LinearLayout lnfollowing;
    @BindView(R.id.lnlocation)
    LinearLayout lnlocation;
    @BindView(R.id.txttoolbar)
    TextView txttoolbar;
    @BindView(R.id.txtfollowers)
    TextView txtfollowers;
    @BindView(R.id.txtfollowing)
    TextView txtfollowing;
    @BindView(R.id.textlooks)
    TextView textlooks;
    @BindView(R.id.textlocation)
    TextView textlocation;
    @BindView(R.id.imgsetting)
    ImageView imgsetting;
    @BindView(R.id.imgprofile1)
    CircleImageView imgprofile1;

    @BindView(R.id.framenotification)
    FrameLayout framenotification;
    @BindView(R.id.imgtransparent)
    ImageView imgtransparent;

    @BindView(R.id.nestedscroll)
    NestedScrollView nestedscroll;

    @BindView(R.id.recyclefeed)
    Container recyclefeed;
    @BindView(R.id.txtfname)
    TextView txtfname;

    @BindView(R.id.imgMyLocation)
    ImageView imgMyLocation;
    @BindView(R.id.imgwall)
    ImageView imgwall;
    @BindView(R.id.imgcamera)
    ImageView imgcamera;
    @BindView(R.id.imgchat)
    ImageView imgchat;
    @BindView(R.id.imgProfile)
    ImageView imgProfile;
    @BindView(R.id.imgnotification)
    ImageView imgnotification;
    @BindView(R.id.simmar_id)
    ShimmerFrameLayout simmar_id;

    @BindView(R.id.simmar_id1)
    ShimmerFrameLayout simmar_id1;
    @BindView(R.id.simmar_id2)
    ShimmerFrameLayout simmar_id2;
    @BindView(R.id.simmar_id3)
    ShimmerFrameLayout simmar_id3;
    @BindView(R.id.ll_simmar)
    LinearLayout ll_simmar;
    @BindView(R.id.frame_rating)
    LinearLayout frame_rating;
    @BindView(R.id.textrating)
    TextView textrating;
    @BindView(R.id.looks)
    TextView looks;
    @BindView(R.id.back1)
    ImageView back1;
    @BindView(R.id.txtpostempty)
    TextView txtpostempty;


    List<String> abc = new ArrayList<>();
    int columns = 3;
    ConnectionDetector cd;
    boolean isInternetPresent = false;
    List<Datum> profilearray = new ArrayList<Datum>();
    List<DatumProfession> professioarraty = new ArrayList<>();
    @BindView(R.id.rlLoading)
    RelativeLayout rlLoading;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    int accountprivacy = 0;
    UserFeed_Adapter userFeed_adapter;
    int random;
    boolean isLoadData = true;
    String type = "";
    String theme1 = "";
    Dialog dialog;
    DBHelper dbHelper;
    DrawerAdapter adapter;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private ArrayList<String> menuItems = new ArrayList<>();
    private ArrayList<Integer> menuimages = new ArrayList<>();

    void startAnim(){
        rlLoading.setVisibility(View.VISIBLE);
        avi.smoothToShow();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        rlLoading.setVisibility(View.GONE);
        avi.smoothToHide();
        // or avi.smoothToHide();
    }

    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        theme1 = sharedpreference.getTheme(ProfilePage.this);

        Log.d("mn13theme", sharedpreference.getTheme(ProfilePage.this) + "11");
        if (sharedpreference.getTheme(ProfilePage.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        ButterKnife.bind(this);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(ProfilePage.this));
        dbHelper = new DBHelper(ProfilePage.this);
        simmar_id.startShimmer();

        startAnim();
        type = getIntent().getExtras().getString("type");

        //  setThemeBaseIcon();

        random = generaterandom();
        Log.d("mn13random", generaterandom() + "");
        userFeed_adapter = new UserFeed_Adapter(ProfilePage.this, demo, "profile");
        userFeed_adapter.randome(random);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ProfilePage.this, 2);

        recyclefeed.setLayoutManager(gridLayoutManager);
        recyclefeed.setAdapter(userFeed_adapter);
        recyclefeed.setNestedScrollingEnabled(false);

        apicalling();

        txtname.setText(sharedpreference.getfirstname(ProfilePage.this) + "");
        txttoolbar.setText(sharedpreference.getfirstname(ProfilePage.this) + "");
        if (!sharedpreference.getBio(ProfilePage.this).equals("")) {
            txtbio.setVisibility(View.VISIBLE);
            txtbio.setText(sharedpreference.getBio(ProfilePage.this) + "");
        } else {
            txtbio.setVisibility(View.GONE);
        }

        imgMyLocation.setOnClickListener(v -> {
            mapclick();
            Intent intent = new Intent(ProfilePage.this, BottombarActivity.class);
            intent.putExtra("type", "map");
            startActivity(intent);
        });

        imgwall.setOnClickListener(v -> {
            lookclick();
            Intent intent = new Intent(ProfilePage.this, BottombarActivity.class);
            intent.putExtra("type", "home");
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });

        imgchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, BottombarActivity.class);
                intent.putExtra("type", "catch");
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }
        });

        imgcamera.setOnClickListener(view -> {
            Intent intent = new Intent(ProfilePage.this, OpenCamera1.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });

        imgsetting.setOnClickListener(view -> {
            drawer_layout.openDrawer(GravityCompat.START);
        });
        setNavigationDrawerMenu();

        framenotification.setOnClickListener(view -> {

            Intent intent = new Intent(ProfilePage.this, Notification.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });

        GetNotification();

        if (!sharedpreference.getphoto(ProfilePage.this).equals("") && sharedpreference.getphoto(ProfilePage.this) != null && !sharedpreference.getphoto(ProfilePage.this).equals("null")) {
            Glide.with(ProfilePage.this)
                    .load(sharedpreference.getphoto(ProfilePage.this))
                    .placeholder(R.drawable.profile_placeholder1)
                    .error(R.drawable.profile_placeholder1)
                    .dontAnimate()
                    .into(imgprofile1);

            simmar_id.stopShimmer();
            simmar_id.setVisibility(View.GONE);
            lnnopic.setVisibility(View.GONE);
            imgprofile1.setVisibility(View.VISIBLE);
        } else {
            lnnopic.setVisibility(View.VISIBLE);
            imgprofile1.setVisibility(View.GONE);
        }


        if (!sharedpreference.getCoverPhoto(ProfilePage.this).equals("") && sharedpreference.getCoverPhoto(ProfilePage.this) != null && !sharedpreference.getCoverPhoto(ProfilePage.this).equals("null")){

            Glide.with(ProfilePage.this)
                    .load(sharedpreference.getCoverPhoto(ProfilePage.this))
                    .placeholder(R.drawable.profile_placeholder1)
                    .error(R.drawable.profile_placeholder1)
                    .dontAnimate()
                    .into(imguser);

            simmar_id.stopShimmer();
            simmar_id.setVisibility(View.GONE);
            lnnopic.setVisibility(View.GONE);
            imgprofile1.setVisibility(View.VISIBLE);

        }

        imgtransparent.setVisibility(View.VISIBLE);


        lnfollower.setOnClickListener(view -> {

            Intent i1 = new Intent(ProfilePage.this, FriendList.class);
            i1.putExtra("otherid", "");
            i1.putExtra("type", "follower");
            i1.putExtra("accountprivacy", accountprivacy);
            i1.putExtra("othername", sharedpreference.getusername(ProfilePage.this));
            startActivity(i1);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);

        });

        lnfollowing.setOnClickListener(view -> {

            Intent i1 = new Intent(ProfilePage.this, FriendList.class);
            i1.putExtra("type", "following");
            i1.putExtra("otherid", "");
            i1.putExtra("accountprivacy", accountprivacy);
            i1.putExtra("othername", sharedpreference.getusername(ProfilePage.this));
            startActivity(i1);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });

        Sub_Category_Adapter mAdapter = new Sub_Category_Adapter(ProfilePage.this, abc);
        final GridLayoutManager mng_layout = new GridLayoutManager(ProfilePage.this, 3/*In your case 4*/);
        mng_layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mAdapter.getItemViewType(position)) {
                    case 2:
                        return 2;
                    case 1:
                        return 2;
                    default:
                        return 1;
                }
            }
        });

        frame_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frame_rating.setOnClickListener(view -> {
                    dialog = new Dialog(ProfilePage.this);
                    dialog.setContentView(R.layout.activity_rating);
                    dialog.getWindow().setBackgroundDrawableResource(R.color.alpha_black);
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    dialog.show();

                    RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
                    ImageView img_done, img_back;
                    TextView tv_avg_rating;

                    ratingBar.setEnabled(false);
                    ratingBar.setRating(Float.parseFloat(sharedpreference.getRate(ProfilePage.this)));

                    img_done = dialog.findViewById(R.id.img_done);
                    img_back = dialog.findViewById(R.id.img_back);
                    tv_avg_rating = dialog.findViewById(R.id.tv_avg_rating);

                    tv_avg_rating.setText("Avarage Rating: " + sharedpreference.getRate(ProfilePage.this));

                    img_done.setOnClickListener(view1 -> {
                        String rating = String.valueOf(ratingBar.getRating());

                        Log.e("LLL_rate: ", rating);

                    });

                    img_back.setOnClickListener(v1 -> {
                        if (dialog.isShowing())
                            dialog.dismiss();
                    });

                    img_done.setVisibility(View.GONE);

                });
            }
        });

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
                    back1.setVisibility(View.VISIBLE);
                    txttoolbar.setVisibility(View.INVISIBLE);
                    isShow = true;
                } else if (isShow) {
                    back1.setVisibility(View.GONE);
                    lnbottom.setVisibility(View.VISIBLE);
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    txttoolbar.setVisibility(View.INVISIBLE);
                    isShow = false;
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
                            //GetFeed();
                            simmar_id.stopShimmer();
                            simmar_id.setVisibility(View.GONE);
                            lnnopic.setVisibility(View.GONE);
                            imgprofile1.setVisibility(View.VISIBLE);
                            imgtransparent.setVisibility(View.VISIBLE);
                            LoadMorePost();
                        }
                    }
                }
            }
        });
    }

    private void setNavigationDrawerMenu() {

        menuItems.add("Edit profile");
        menuItems.add("Saved post");
        menuItems.add("Activity");
        menuItems.add("Close friends");
        menuItems.add("Invite friends");
        menuItems.add("Notify");
        menuItems.add("Privacy");
        menuItems.add("Security");
        menuItems.add("Accounts");
        menuItems.add("Help");
        menuItems.add("About");
        menuItems.add("LogOut");

        menuimages.add(R.drawable.edit_profile1);
        menuimages.add(R.drawable.saved_post);
        menuimages.add(R.drawable.activity);
        menuimages.add(R.drawable.close_friends);
        menuimages.add(R.drawable.about);
        menuimages.add(R.drawable.notification);
        menuimages.add(R.drawable.privacy);
        menuimages.add(R.drawable.security);
        menuimages.add(R.drawable.accounts);
        menuimages.add(R.drawable.help);
        menuimages.add(R.drawable.about);
        menuimages.add(R.drawable.black_icon);


        nav_list.setAdapter(new DrawerAdapter(this, menuItems, menuimages));
        nav_list.setOnItemClickListener((parent, view, position, id) -> {

            switch (position) {
                case 0:
                    Intent intent = new Intent(ProfilePage.this, EditProfile.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.stay);
                    finish();
                    break;
                case 1:
                    Intent intent1 = new Intent(ProfilePage.this, Saved_Post.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.fade_in, R.anim.stay);
                    break;
                case 2:
//                        Intent intent2 = new Intent(ProfilePage.this, Disclaimer.class);
//                        startActivity(intent2);
//                        overridePendingTransition(RM.anim.left_to_right, RM.anim.right_to_left);
                    break;
                case 3:
//                        Intent intent3 = new Intent(ProfilePage.this, Series.class);
//                        startActivity(intent3);
//                        overridePendingTransition(RM.anim.left_to_right, RM.anim.right_to_left);
                    break;
                case 4:
//                        Intent intent4 = new Intent(ProfilePage.this, NoAds.class);
//                        startActivity(intent4);
//                        overridePendingTransition(RM.anim.left_to_right, RM.anim.right_to_left);
                    break;
                case 5:
//                        Intent intent4 = new Intent(ProfilePage.this, NoAds.class);
//                        startActivity(intent4);
//                        overridePendingTransition(RM.anim.left_to_right, RM.anim.right_to_left);
                    break;
                case 6:
//                        Intent intent4 = new Intent(ProfilePage.this, NoAds.class);
//                        startActivity(intent4);
//                        overridePendingTransition(RM.anim.left_to_right, RM.anim.right_to_left);
                    break;
                case 7:
//                        Intent intent4 = new Intent(ProfilePage.this, NoAds.class);
//                        startActivity(intent4);
//                        overridePendingTransition(RM.anim.left_to_right, RM.anim.right_to_left);
                    break;
                case 8:
//                        Intent intent4 = new Intent(ProfilePage.this, NoAds.class);
//                        startActivity(intent4);
//                        overridePendingTransition(RM.anim.left_to_right, RM.anim.right_to_left);
                    break;
                case 9:
//                        Intent intent4 = new Intent(ProfilePage.this, NoAds.class);
//                        startActivity(intent4);
//                        overridePendingTransition(RM.anim.left_to_right, RM.anim.right_to_left);
                    break;
                case 10:
//                        Intent intent4 = new Intent(ProfilePage.this, NoAds.class);
//                        startActivity(intent4);
//                        overridePendingTransition(RM.anim.left_to_right, RM.anim.right_to_left);
                    break;
                case 11:
                    drawer_layout.closeDrawer(GravityCompat.START);
                    logout();
                    break;
                default:
                    break;
            }

            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START);
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
    public void onBackPressed() {
        finishAffinity();
        overridePendingTransition(R.anim.stay, R.anim.fade_out);
    }

    @Override
    public void onResume() {
        super.onResume();

        txtname.setText(sharedpreference.getfirstname(ProfilePage.this) + "");
        txttoolbar.setText(sharedpreference.getfirstname(ProfilePage.this) + "");
        if (!sharedpreference.getBio(ProfilePage.this).equals("")) {
            txtbio.setVisibility(View.VISIBLE);
            txtbio.setText(sharedpreference.getBio(ProfilePage.this) + "");
        } else {
            txtbio.setVisibility(View.GONE);
        }

        if (userFeed_adapter != null) {
            if (delete > -1) {
                userFeed_adapter.setNotifydone(delete);
                delete = -1;

            }
        }

        if (!sharedpreference.getphoto(ProfilePage.this).equals("") && !sharedpreference.getphoto(ProfilePage.this).equals(null) && !sharedpreference.getphoto(ProfilePage.this).equals("null")) {
            Glide.with(ProfilePage.this)
                    .load(sharedpreference.getphoto(ProfilePage.this))
                    .placeholder(R.drawable.profile_placeholder1)
                    .error(R.drawable.profile_placeholder1)
                    .dontAnimate()
                    .into(imgprofile1);

            lnnopic.setVisibility(View.GONE);
            imgprofile1.setVisibility(View.VISIBLE);
        } else {
            lnnopic.setVisibility(View.VISIBLE);
            imgprofile1.setVisibility(View.GONE);
        }

        if (!sharedpreference.getCoverPhoto(ProfilePage.this).equals("") && sharedpreference.getCoverPhoto(ProfilePage.this) != null && !sharedpreference.getCoverPhoto(ProfilePage.this).equals("null")){

            Glide.with(ProfilePage.this)
                    .load(sharedpreference.getCoverPhoto(ProfilePage.this))
                    .placeholder(R.drawable.profile_placeholder1)
                    .error(R.drawable.profile_placeholder1)
                    .dontAnimate()
                    .into(imguser);

            simmar_id.stopShimmer();
            simmar_id.setVisibility(View.GONE);
            lnnopic.setVisibility(View.GONE);
            imgprofile1.setVisibility(View.VISIBLE);

        }

        imgtransparent.setVisibility(View.VISIBLE);

        if (sharedpreference.getTheme(ProfilePage.this).equalsIgnoreCase("white")) {
            getTheme().applyStyle(R.style.ActivityTheme_Primary_Base_Light, true);
        } else {
            getTheme().applyStyle(R.style.ActivityTheme_Primary_Base_Dark, true);
        }

        if (theme1.equalsIgnoreCase(sharedpreference.getTheme(ProfilePage.this))) {

        } else {
            Intent i = getIntent();
            overridePendingTransition(0, 0);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(i);
        }
    }


    public void GetProfile() {

        if (!isInternetPresent) {
            Toasty.error(ProfilePage.this, "Please check your internet connection...").show();
//            if (dbHelper.chceckUserDetails(Integer.parseInt(sharedpreference.getUserId(ProfilePage.this)))) {
//                GetProfile login = getUserDetails();
//
//                postarray = new ArrayList<>();
//
//                postid = new ArrayList<>();
//                try {
//                    profilearray = login.getData();
//                    professioarraty = login.getProfessions();
//
//                    postarray = getPostRecords();
//
//                    Log.e("LLLL_DB_Data: ", String.valueOf(getPostRecords().size()));
//
//                    stopAnim();
//                    simmar_id1.stopShimmer();
//                    simmar_id2.stopShimmer();
//                    simmar_id3.stopShimmer();
//                    ll_simmar.setVisibility(View.GONE);
//                    recyclefeed.setVisibility(View.VISIBLE);
//                    userFeed_adapter.addAll(postarray);
//
//                    if (!profilearray.isEmpty()) {
//
//                        txtname.setText(profilearray.get(0).getFullName());
//                        txttoolbar.setText(profilearray.get(0).getFullName());
//                        sharedpreference.setusername(ProfilePage.this, profilearray.get(0).getUsername() + "");
//                        sharedpreference.setUserId(ProfilePage.this, String.valueOf(profilearray.get(0).getId()));
//                        sharedpreference.setnumber(ProfilePage.this, profilearray.get(0).getMobileNo());
//                        sharedpreference.setDailcode(ProfilePage.this, profilearray.get(0).getMobileCode());
//                        sharedpreference.setfirstname(ProfilePage.this, profilearray.get(0).getFullName());
//                        sharedpreference.setEmail(ProfilePage.this, profilearray.get(0).getEmail());
//                        sharedpreference.setBio(ProfilePage.this, profilearray.get(0).getBio() + "");
//                        sharedpreference.setRate(ProfilePage.this, profilearray.get(0).getRateAvg() + "");
//
//                        accountprivacy = profilearray.get(0).getAccountPrivacy();
//                        if (!profilearray.get(0).getBio().equals("")) {
//                            txtbio.setVisibility(View.VISIBLE);
//                            txtbio.setText(profilearray.get(0).getBio());
//                        } else {
//                            txtbio.setVisibility(View.GONE);
//                        }
//
//                        textrating.setText(new DecimalFormat("##.##").format(profilearray.get(0).getRateAvg()) + "/5");
//
//                        if (!profilearray.get(0).getPhoto().equals("") && profilearray.get(0).getPhoto() != null && !profilearray.get(0).getPhoto().equals("null")) {
//                            simmar_id.stopShimmer();
//                            simmar_id.setVisibility(View.GONE);
//                            sharedpreference.setphoto(ProfilePage.this, profilearray.get(0).getPhoto());
//                            sharedpreference.setCoverPhoto(ProfilePage.this, profilearray.get(0).getCoverPhoto());
//                            imgprofile1.setVisibility(View.VISIBLE);
//                            Glide.with(ProfilePage.this)
//                                    .load(profilearray.get(0).getPhoto())
//                                    .error(R.drawable.profile_placeholder1)
//                                    .dontAnimate()
//                                    .centerCrop()
//                                    .into(imgprofile1);
//
//                            lnnopic.setVisibility(View.GONE);
//                            imgprofile1.setVisibility(View.VISIBLE);
//                        } else {
//                            lnnopic.setVisibility(View.VISIBLE);
//                            imgprofile1.setVisibility(View.GONE);
//                        }
//                        imgtransparent.setVisibility(View.VISIBLE);
//
//                        if (!profilearray.get(0).getCoverPhoto().equals("") && profilearray.get(0).getCoverPhoto() != null && !profilearray.get(0).getCoverPhoto().equals("null")){
//                            Glide.with(ProfilePage.this)
//                                    .load(profilearray.get(0).getCoverPhoto())
//                                    .error(R.drawable.profile_placeholder1)
//                                    .dontAnimate()
//                                    .centerCrop()
//                                    .into(imguser);
//
//                        }
//                    }
//
//                    List<Other> othernodel = login.getOthers();
//                    if (!othernodel.isEmpty()) {
//                        txtfollowers.setText(othernodel.get(0).getFollowers() + "");
//                        txtfollowing.setText(othernodel.get(0).getFollowing() + "");
//                        textlooks.setText(othernodel.get(0).getPosts() + "");
//                    }
//                } catch (Exception e) {
//                    Log.e("LLL_Error: ", e.getMessage());
//                    e.printStackTrace();
//                    Toasty.error(ProfilePage.this, e.getMessage() + "", Toast.LENGTH_LONG, true).show();
//                }
//                if (postarray.isEmpty()) {
//                    postarray = getPostRecords();
//                    Log.e("LLLL_DB_Data: ", String.valueOf(getPostRecords().size()));
//                    simmar_id1.stopShimmer();
//                    simmar_id2.stopShimmer();
//                    simmar_id3.stopShimmer();
//                    ll_simmar.setVisibility(View.GONE);
//                    recyclefeed.setVisibility(View.VISIBLE);
//                    userFeed_adapter.addAll(postarray);
//                    if (postarray.isEmpty()) {
//                        isLoadData = false;
//
//                    }
//                } else {
//                    isLoadData = true;
//
//                }
//                for (int i = 0; i < postarray.size(); i++) {
//                    postid.add(postarray.get(i).getId());
//                }
//
//            }
        } else {
            ArrayList<param> paramArrayList = new ArrayList<>();
            paramArrayList.add(new param("user_id", String.valueOf(sharedpreference.getUserId(ProfilePage.this))));
            paramArrayList.add(new param("timezone", Public_Function.gettimezone()));

            Log.e("LLL_Param: ", sharedpreference.getUserId(ProfilePage.this) + "            " + Public_Function.gettimezone());

            new geturl().getdata(ProfilePage.this, data -> {
                postarray = new ArrayList<>();
                Log.e("LLLLL_Data: ", data);

                postid = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("message");
                    stopAnim();
                    if (message) {

                        GetProfile login = new Gson().fromJson(data, GetProfile.class);
                        profilearray = login.getData();
                        professioarraty = login.getProfessions();
//                        Log.e("LLLL_Data1: ", object.getJSONArray("friends") + "");

//                        if (!dbHelper.chceckUserDetails(Integer.parseInt(sharedpreference.getUserId(ProfilePage.this)))) {
//                            dbHelper.insertUserDetails(String.valueOf(sharedpreference.getUserId(ProfilePage.this)),
//                                    object.getJSONArray("data") + "",
//                                    object.getJSONArray("others") + "",
//                                    object.getJSONArray("professions") + "",
//                                    friends+"");
//                        } else {
//                            dbHelper.updateUserDetails(String.valueOf(sharedpreference.getUserId(ProfilePage.this)),
//                                    object.getJSONArray("data") + "",
//                                    object.getJSONArray("others") + "",
//                                    object.getJSONArray("professions") + "",
//                                    friends+"");
//                        }
//                        for (int i = 0; i < login.getPosts().size(); i++) {
//                            if (dbHelper.Check_Contact_Exist(login.getPosts().get(i).getId(), login.getPosts().get(i).getUserId())) {
//
//                                dbHelper.InsertData_Post(login.getPosts().get(i).getId(),
//                                        login.getPosts().get(i).getUserId(),
//                                        login.getPosts().get(i).getDescription(),
//                                        login.getPosts().get(i).getLocation(),
//                                        login.getPosts().get(i).getLat(),
//                                        login.getPosts().get(i).getLang(),
//                                        login.getPosts().get(i).getUsername(),
//                                        login.getPosts().get(i).getFullName(),
//                                        login.getPosts().get(i).getPhoto(),
//                                        login.getPosts().get(i).getImage(),
//                                        login.getPosts().get(i).getFileType(),
//                                        object.getJSONArray("posts").getJSONObject(i).getJSONArray("images") + "",
//                                        object.getJSONArray("posts").getJSONObject(i).getJSONArray("comments") + "",
//                                        login.getPosts().get(i).getMultipleImages(),
//                                        login.getPosts().get(i).getBookmarked(),
//                                        login.getPosts().get(i).getMore_comments(),
//                                        login.getPosts().get(i).getTime_duration(),
//                                        login.getPosts().get(i).getHeight(),
//                                        login.getPosts().get(i).getWidth());
//
//                            } else {
//                                Log.e("LLL_Data: ", "....");
//                            }
//                        }

                        postarray = login.getPosts();

                        Log.e("LLLL_DB_Data: ", String.valueOf(postarray.size()));

                        simmar_id1.stopShimmer();
                        simmar_id2.stopShimmer();
                        simmar_id3.stopShimmer();
                        ll_simmar.setVisibility(View.GONE);
                        recyclefeed.setVisibility(View.VISIBLE);
                        userFeed_adapter.addAll(postarray);

                        if (!profilearray.isEmpty()) {

                            txtname.setText(profilearray.get(0).getFullName());
                            txttoolbar.setText(profilearray.get(0).getFullName());
                            sharedpreference.setusername(ProfilePage.this, profilearray.get(0).getUsername() + "");
                            sharedpreference.setUserId(ProfilePage.this, String.valueOf(profilearray.get(0).getId()));
                            sharedpreference.setnumber(ProfilePage.this, profilearray.get(0).getMobileNo());
                            sharedpreference.setDailcode(ProfilePage.this, profilearray.get(0).getMobileCode());
                            sharedpreference.setfirstname(ProfilePage.this, profilearray.get(0).getFullName());
                            sharedpreference.setEmail(ProfilePage.this, profilearray.get(0).getEmail());
                            sharedpreference.setBio(ProfilePage.this, profilearray.get(0).getBio() + "");
                            sharedpreference.setRate(ProfilePage.this, profilearray.get(0).getRateAvg() + "");

                            accountprivacy = profilearray.get(0).getAccountPrivacy();
                            if (!profilearray.get(0).getBio().equals("")) {
                                txtbio.setVisibility(View.VISIBLE);
                                txtbio.setText(profilearray.get(0).getBio());
                            } else {
                                txtbio.setVisibility(View.GONE);
                            }

                            textrating.setText(new DecimalFormat("##.##").format(profilearray.get(0).getRateAvg()) + "/5");

                            if (!profilearray.get(0).getPhoto().equals("") && !profilearray.get(0).getPhoto().equals(null) && !profilearray.get(0).getPhoto().equals("null")) {
                                simmar_id.stopShimmer();
                                simmar_id.setVisibility(View.GONE);
                                sharedpreference.setphoto(ProfilePage.this, profilearray.get(0).getPhoto());
                                sharedpreference.setCoverPhoto(ProfilePage.this, profilearray.get(0).getCoverPhoto());
                                imgprofile1.setVisibility(View.VISIBLE);
                                Glide.with(ProfilePage.this)
                                        .load(profilearray.get(0).getPhoto())
                                        .placeholder(R.drawable.profile_placeholder1)
                                        .error(R.drawable.profile_placeholder1)
                                        .dontAnimate()
                                        .into(imgprofile1);
                                lnnopic.setVisibility(View.GONE);
                                imgprofile1.setVisibility(View.VISIBLE);
                            } else {
                                lnnopic.setVisibility(View.VISIBLE);
                                imgprofile1.setVisibility(View.GONE);
                            }
                            imgtransparent.setVisibility(View.VISIBLE);
                        }

                        List<Other> othernodel = login.getOthers();
                        if (!othernodel.isEmpty()) {
                            txtfollowers.setText(othernodel.get(0).getFollowers() + "");
                            txtfollowing.setText(othernodel.get(0).getFollowing() + "");
                            textlooks.setText(othernodel.get(0).getPosts() + "");
                        }

                    } else {
                        Toasty.error(ProfilePage.this, status + "", Toast.LENGTH_LONG, true).show();
                    }
                } catch (Exception e) {
                    Log.e("LLLLLL_Error: ", e.getMessage());
                    e.printStackTrace();
                    Toasty.error(ProfilePage.this, "Oops something went wrong..." + "", Toast.LENGTH_LONG, true).show();
                }
                if (postarray.isEmpty()) {
                    postarray = getPostRecords();
                    Log.e("LLLL_DB_Data: ", String.valueOf(getPostRecords().size()));

                    simmar_id1.stopShimmer();
                    simmar_id2.stopShimmer();
                    simmar_id3.stopShimmer();
                    ll_simmar.setVisibility(View.GONE);
                    recyclefeed.setVisibility(View.VISIBLE);
                    userFeed_adapter.addAll(postarray);
                    if (postarray.size() == 0) {
                        isLoadData = false;
                        looks.setVisibility(View.GONE);
                        txtpostempty.setVisibility(View.VISIBLE);
                        gridpost.setVisibility(View.GONE);
                    }
                } else {
                    isLoadData = true;
                    looks.setVisibility(View.VISIBLE);
                    txtpostempty.setVisibility(View.GONE);
                    gridpost.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < postarray.size(); i++) {
                    postid.add(postarray.get(i).getId());
                }

            }, paramArrayList, "get_profile");
        }
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
                getProfile.setData(Arrays.asList(datum));

                other = gson.fromJson(cur.getString(3), Other[].class);
                getProfile.setOthers(Arrays.asList(other));

                profession = gson.fromJson(cur.getString(4), DatumProfession[].class);
                getProfile.setProfessions(Arrays.asList(profession));

//                friend = gson.fromJson(cur.getString(5), Friend[].class);
//                getProfile.setFriends(Arrays.asList(friend));

                getProfile = getProfile1;
            } while (cur.moveToNext());
            cur.close();
        }
        return getProfile;
    }

    public void apicalling() {
        cd = new ConnectionDetector(ProfilePage.this);
        isInternetPresent = cd.isConnectingToInternet();
        simmar_id1.startShimmer();
        simmar_id2.startShimmer();
        simmar_id3.startShimmer();
        recyclefeed.setVisibility(View.GONE);
        ll_simmar.setVisibility(View.VISIBLE);
        GetProfile();
    }

    public void LoadMorePost() {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(ProfilePage.this) + ""));
        paramArrayList.add(new param("exclude_post_ids", TextUtils.join(",", postid).replaceAll(",$", "")));
        paramArrayList.add(new param("timezone", Public_Function.gettimezone()));

        new geturl().getdata(ProfilePage.this, data -> {
            postid = new ArrayList<>();
            try {

                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("msg");
                if (message) {

                    LoadMorePost login = new Gson().fromJson(data, LoadMorePost.class);
                    List<Post> othernodel = new ArrayList<Post>();
                    othernodel = login.getPosts();

                    for (int i = 0; i < login.getPosts().size(); i++) {
                        if (dbHelper.Check_Contact_Exist(login.getPosts().get(i).getId(), login.getPosts().get(i).getUserId())) {

                            dbHelper.InsertData_Post(login.getPosts().get(i).getId(),
                                    login.getPosts().get(i).getUserId(),
                                    login.getPosts().get(i).getDescription(),
                                    login.getPosts().get(i).getLocation(),
                                    login.getPosts().get(i).getLat(),
                                    login.getPosts().get(i).getLang(),
                                    login.getPosts().get(i).getUsername(),
                                    login.getPosts().get(i).getFullName(),
                                    login.getPosts().get(i).getPhoto(),
                                    login.getPosts().get(i).getImage(),
                                    login.getPosts().get(i).getFileType(),
                                    object.getJSONArray("posts").getJSONObject(i).getJSONArray("images") + "",
                                    object.getJSONArray("posts").getJSONObject(i).getJSONArray("comments") + "",
                                    login.getPosts().get(i).getMultipleImages(),
                                    login.getPosts().get(i).getBookmarked(),
                                    login.getPosts().get(i).getMore_comments(),
                                    login.getPosts().get(i).getTime_duration(),
                                    login.getPosts().get(i).getHeight(),
                                    login.getPosts().get(i).getWidth());

                        } else {
                            Log.e("LLL_Data: ", "....");
                        }
                    }

                    if (postarray.isEmpty())
                        postarray.addAll(getPostRecords());
                    else {
                        postarray.clear();
                        postarray.addAll(getPostRecords());
                    }

//                    postarray.addAll(othernodel);
                    simmar_id1.stopShimmer();
                    simmar_id2.stopShimmer();
                    simmar_id3.stopShimmer();
                    ll_simmar.setVisibility(View.GONE);
                    recyclefeed.setVisibility(View.VISIBLE);
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

    public void mapclick() {
        imgMyLocation.setImageDrawable(getResources().getDrawable(R.drawable.map1_selected));
        imgwall.setImageDrawable(getResources().getDrawable(R.drawable.look1_unselected));
        imgchat.setImageDrawable(getResources().getDrawable(R.drawable.catch_unselected));
        imgProfile.setImageDrawable(getResources().getDrawable(R.drawable.me_unselected));
    }

    public void lookclick() {
        imgMyLocation.setImageDrawable(getResources().getDrawable(R.drawable.map1_unselected));
        imgwall.setImageDrawable(getResources().getDrawable(R.drawable.look1_selected));
        imgchat.setImageDrawable(getResources().getDrawable(R.drawable.catch_unselected));
        imgProfile.setImageDrawable(getResources().getDrawable(R.drawable.me_unselected));
    }

    public void chatclick() {
        imgMyLocation.setImageDrawable(getResources().getDrawable(R.drawable.map1_unselected));
        imgwall.setImageDrawable(getResources().getDrawable(R.drawable.look1_unselected));
        imgchat.setImageDrawable(getResources().getDrawable(R.drawable.catch_selelcted));
        imgProfile.setImageDrawable(getResources().getDrawable(R.drawable.me_unselected));
    }

    public void profileclick() {
        imgMyLocation.setImageDrawable(getResources().getDrawable(R.drawable.map1_unselected));
        imgwall.setImageDrawable(getResources().getDrawable(R.drawable.look1_unselected));
        imgchat.setImageDrawable(getResources().getDrawable(R.drawable.catch_unselected));
        imgProfile.setImageDrawable(getResources().getDrawable(R.drawable.me_selected));
    }


//    public void setThemeBaseIcon() {
//        if (sharedpreference.getTheme(ProfilePage.this).equalsIgnoreCase("white")) {
//            imgsetting.setImageTintList(ColorStateList.valueOf(getResources().getColor(RM.color.black)));
//            imgnotification.setImageTintList(ColorStateList.valueOf(getResources().getColor(RM.color.black)));
//        } else {
//            imgsetting.setImageTintList(ColorStateList.valueOf(getResources().getColor(RM.color.white)));
//            imgnotification.setImageTintList(ColorStateList.valueOf(getResources().getColor(RM.color.white)));
//        }
//    }

    //  public void setThemeBaseIcon() {
    //  if (sharedpreference.getTheme(ProfilePage.this).equalsIgnoreCase("white")) {
    //     imgsetting.setImageTintList(ColorStateList.valueOf(getResources().getColor(RM.color.black)));
    //     imgnotification.setImageTintList(ColorStateList.valueOf(getResources().getColor(RM.color.black)));
//            tv_map.setTextColor(ColorStateList.valueOf(getResources().getColor(RM.color.black)));
//            tv_look.setTextColor(ColorStateList.valueOf(getResources().getColor(RM.color.black)));
//            tv_profile.setTextColor(ColorStateList.valueOf(getResources().getColor(RM.color.black)));
//            tv_talk.setTextColor(ColorStateList.valueOf(getResources().getColor(RM.color.black)));
//            tv_view.setTextColor(ColorStateList.valueOf(getResources().getColor(RM.color.black)));
    //  } else {
    //     imgsetting.setImageTintList(ColorStateList.valueOf(getResources().getColor(RM.color.white)));
    //     imgnotification.setImageTintList(ColorStateList.valueOf(getResources().getColor(RM.color.white)));
//            tv_map.setTextColor(ColorStateList.valueOf(getResources().getColor(RM.color.white)));
//            tv_look.setTextColor(ColorStateList.valueOf(getResources().getColor(RM.color.white)));
//            tv_profile.setTextColor(ColorStateList.valueOf(getResources().getColor(RM.color.white)));
//            tv_talk.setTextColor(ColorStateList.valueOf(getResources().getColor(RM.color.white)));
//            tv_view.setTextColor(ColorStateList.valueOf(getResources().getColor(RM.color.white)));
    // }
//    }

    public void GetNotification() {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(ProfilePage.this)));

        new geturl().getdata(ProfilePage.this, data -> {

            try {
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                Log.e("LLL_Notification_data: ", data);
                if (message) {
                    int total = 0;
                    GetNotification login = new Gson().fromJson(data, GetNotification.class);
                    for (int i = 0; i < login.getData().size(); i++) {
                        total = total + login.getData().get(i).getList().size();
                    }
                    txtfname.setText(String.valueOf(total));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }, paramArrayList, "get_notification");
    }

    public void logout() {
        dialogelogout = new Dialog(ProfilePage.this);
        dialogelogout.setContentView(R.layout.dialoge_logout);
        dialogelogout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cancle = dialogelogout.findViewById(R.id.cancle);
        ok = dialogelogout.findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialogelogout.isShowing()) {
                    dialogelogout.dismiss();
                }

            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                cd = new ConnectionDetector(ProfilePage.this);
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    Toasty.warning(ProfilePage.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    Log_Out();
                }
            }
        });

        if (!dialogelogout.isShowing()) {
            dialogelogout.show();
        }

    }

    public void Log_Out() {

        Public_Function.Show_Progressdialog(ProfilePage.this);


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(ProfilePage.this)));
        paramArrayList.add(new param("token", sharedpreference.getfirebasetoken(ProfilePage.this)));


        new geturl().getdata(ProfilePage.this, new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {
                try {

                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("msg");
                    if (message) {

                        Applozic.logoutUser(ProfilePage.this, new AlLogoutHandler() {
                            @Override
                            public void onSuccess(Context context) {
                                Public_Function.Hide_ProgressDialogue();

                                String token = "";
                                token = sharedpreference.getfirebasetoken(ProfilePage.this);
                                sharedpreference.clearAll(ProfilePage.this);
                                sharedpreference.setfirebasetoken(ProfilePage.this, token);
                                dbHelper.delete_table();

                                Intent i = new Intent(ProfilePage.this, WelcomeActivity.class);
                                startActivity(i);
                                finishAffinity();
                                overridePendingTransition(R.anim.fade_in, R.anim.stay);

                            }

                            @Override
                            public void onFailure(Exception exception) {
                                Public_Function.Hide_ProgressDialogue();
                                Log.d("mn13error", exception.getMessage() + "");
                                Toasty.error(ProfilePage.this, exception.getMessage() + "", Toast.LENGTH_LONG, true).show();

                            }
                        });

                    } else {
                        Public_Function.Hide_ProgressDialogue();
                        Toasty.error(ProfilePage.this, status + "", Toast.LENGTH_LONG, true).show();
                    }
                } catch (Exception e) {
                    Public_Function.Hide_ProgressDialogue();
                    Toasty.error(ProfilePage.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                    e.printStackTrace();
                }
            }
        }, paramArrayList, "logout");
    }



}

