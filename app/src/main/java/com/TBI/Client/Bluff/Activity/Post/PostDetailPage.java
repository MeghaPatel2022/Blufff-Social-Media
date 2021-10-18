package com.TBI.Client.Bluff.Activity.Post;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.TBI.Client.Bluff.Activity.Profile.OtherUserProfile;
import com.TBI.Client.Bluff.Activity.Profile.ProfilePage;
import com.TBI.Client.Bluff.Adapter.Post.FollowFrendAdapter;
import com.TBI.Client.Bluff.Adapter.Post.HashTagAdapter;
import com.TBI.Client.Bluff.Adapter.Post.MentionUserAdapter;
import com.TBI.Client.Bluff.Adapter.Post.Postdetail_ImageAdapter1;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.Fragment.Look_Fragment;
import com.TBI.Client.Bluff.Model.LoadMore_Comment.LoadComments;
import com.TBI.Client.Bluff.Model.PostDetail.Comment;
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
import com.TBI.Client.Bluff.gesture.SimpleGestureFilter;
import com.TBI.Client.Bluff.view.NonSwipeableViewPager;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.api.conversation.MobiComConversationService;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.harsh.instatag.InstaTag;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.hendraanggrian.appcompat.widget.SocialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import ooo.oxo.library.widget.PullBackLayout;

public class PostDetailPage extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener, Postdetail_ImageAdapter1.OnPostClickListener, PullBackLayout.Callback {

    @BindView(R.id.lstcomment)
    ListView lstcomment;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.txttoolbar)
    TextView txttoolbar;
    @BindView(R.id.txtuserfullname)
    TextView txtuserfullname;
    @BindView(R.id.txtname)
    TextView txtname;
    @BindView(R.id.edtdescription)
    SocialAutoCompleteTextView edtdescription;
    @BindView(R.id.imgdone)
    ImageView imgdone;
    @BindView(R.id.imguser)
    NonSwipeableViewPager imguser;
    @BindView(R.id.txtbio)
    SocialTextView txtbio;
    @BindView(R.id.imgsaved)
    ImageView imgsaved;
    @BindView(R.id.imgdot)
    ImageView imgdot;
    @BindView(R.id.tv_on_notify)
    TextView tv_on_notify;
    @BindView(R.id.txtviemore)
    TextView txtviemore;
    @BindView(R.id.txtlocation)
    TextView txtlocation;
    @BindView(R.id.imguserpic)
    ImageView imguserpic;
    @BindView(R.id.img_prev)
    ImageView img_prev;
    @BindView(R.id.img_next)
    ImageView img_next;
    @BindView(R.id.img_ad_prev)
    ImageView img_ad_prev;
    @BindView(R.id.img_close)
    ImageView img_close;
    @BindView(R.id.img_ad_next)
    ImageView img_ad_next;
    @BindView(R.id.imgcomment)
    ImageView imgcomment;
    @BindView(R.id.imgdraw)
    ImageView imgdraw;
    @BindView(R.id.lncomment)
    LinearLayout lncomment;
    @BindView(R.id.ll_last)
    LinearLayout ll_last;
    @BindView(R.id.lnimages)
    LinearLayout lnimages;
    @BindView(R.id.imgdownarro)
    ImageView imgdownarro;
    @BindView(R.id.imgprofilepic)
    CircleImageView imgprofilepic;
    @BindView(R.id.lngesture)
    LinearLayout lngesture;
    @BindView(R.id.txttime)
    TextView txttime;
    @BindView(R.id.imgtagpeople)
    ImageView imgtagpeople;
    @BindView(R.id.imgtag)
    ImageView imgtag;
    @BindView(R.id.instatagview)
    InstaTag instatagview;
    @BindView(R.id.lnswipedown)
    LinearLayout lnswipedown;
//    @BindView(RM.id.ll_touch)
//    RelativeLayout ll_touch;

    @BindView(R.id.imgsend)
    ImageView imgsend;
    @BindView(R.id.ll_more)
    LinearLayout ll_more;
    @BindView(R.id.tv_delete)
    TextView tv_delete;
    @BindView(R.id.ll_send)
    LinearLayout ll_send;
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
    @BindView(R.id.lnbottom1)
    LinearLayout lnbottom1;

    @BindView(R.id.searchdiscover)
    SearchView searchdiscover;
    @BindView(R.id.tv_unfollow)
    TextView tv_unfollow;
    @BindView(R.id.tv_report)
    TextView tv_report;

    BottomSheetBehavior behavior;

    @BindView(R.id.ll_cancel)
    LinearLayout ll_cancel;
    @BindView(R.id.rlLike)
    RelativeLayout rlLike;
    @BindView(R.id.imgLike)
    ImageView imgLike;
    @BindView(R.id.rlClick)
    RelativeLayout rlClick;

    @BindView(R.id.cl_main)
    CoordinatorLayout cl_main;
    @BindView(R.id.add_view)
    CardView add_view;
    @BindView(R.id.ad_view)
    UnifiedNativeAdView adView;

    //    Comment_Adapter comment_adapter;
    List<Comment> commentarraylist = new ArrayList<Comment>();
    String post_id = "";
    String post_url = "";
    int like = 0;
    int count_turned_on = 0;
    int reported = 0;

    ConnectionDetector cd;
    boolean isInternetPresent = false;
    List<Hastag> hastags = new ArrayList<>();
    HashTagAdapter hashTag_adapter;
    MentionUserAdapter mentionUserAdapter;

    List<Datum> userlist = new ArrayList<Datum>();
    Postdetail_ImageAdapter1 postdetail_imageAdapter1;

    int saved;
    int other_user_id;

    boolean expandable = false;
    //GetViewPost login;
    String theme = "";
    String comment = "", come = "", tag = "";
    int position;
    int selectposition;
    int more_comment = 0;
    int offset = 0, limit = 20;
    boolean flag = false;
    GestureDetectorCompat gestureDetectorCompat;
    List<Following> followingarray = new ArrayList<>();

    // The AdLoader used to load ads.
    private AdLoader adLoader;
    // List of native ads that have been successfully loaded.
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    private FollowFrendAdapter followFrendAdapter;

    android.app.Dialog dialog;

    private String path = "";
    private File shareFile;

    GestureDetector gestureDetector;
    public static boolean isDelete = false;

    private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds
    boolean tap = true;
    long lastClickTime = 0;


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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("mn13theme", sharedpreference.getTheme(PostDetailPage.this) + "11");
        if (sharedpreference.getTheme(PostDetailPage.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postdetailpage);
        ButterKnife.bind(this);
        AndroidNetworking.initialize(PostDetailPage.this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(PostDetailPage.this));

        if (sharedpreference.getTheme(PostDetailPage.this).equalsIgnoreCase("white")) {
            imgtag.setImageDrawable(getResources().getDrawable(R.drawable.tag1));
        } else {
            imgtag.setImageDrawable(getResources().getDrawable(R.drawable.tag1_white));
        }

        ViewTreeObserver vto = instatagview.getTagImageView().getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                instatagview.getTagImageView().getViewTreeObserver().removeOnPreDrawListener(this);
                Postdetail_ImageAdapter1.height = (float) instatagview.getTagImageView().getMeasuredHeight();
                Postdetail_ImageAdapter1.width = (float) instatagview.getTagImageView().getMeasuredWidth();
                Log.d("mn13height", Postdetail_ImageAdapter1.height + "::" + Postdetail_ImageAdapter1.height);

                if (postdetail_imageAdapter1 != null) {
                    postdetail_imageAdapter1.notifyDataSetChanged();
                }
                return true;
            }
        });

        theme = sharedpreference.getTheme(PostDetailPage.this);

        if (sharedpreference.getTheme(PostDetailPage.this).equalsIgnoreCase("white")) {
            ColorStateList csl = AppCompatResources.getColorStateList(PostDetailPage.this, R.color.black);
            ImageViewCompat.setImageTintList(img_back, csl);
        } else {
            ColorStateList csl = AppCompatResources.getColorStateList(PostDetailPage.this, R.color.white);
            ImageViewCompat.setImageTintList(img_back, csl);
        }

        imgtagpeople.setVisibility(View.GONE);
        //post_id = getIntent().getExtras().getString("post_id");
        comment = getIntent().getExtras().getString("comment");
        tag = getIntent().getExtras().getString("tag");
        position = getIntent().getExtras().getInt("position");
        come = getIntent().getExtras().getString("come");


        img_back.setOnClickListener(v -> onBackPressed());

        Log.e("LLL_Come3: ", come);

        imgtag.setOnClickListener(v -> {
            Log.e("LLL_Bool: ", String.valueOf(Look_Fragment.postarray.get(imguser.getCurrentItem()).getImages().get(0).getTagPeople().isEmpty()));
            if (!Look_Fragment.postarray.get(imguser.getCurrentItem()).getImages().get(0).getTagPeople().isEmpty()) {
                setmodel(Look_Fragment.postarray.get(imguser.getCurrentItem()).getImages().get(0));
            }
        });

        if (tag != null) {
            if (tag.equalsIgnoreCase("yes")) {
                if (!Look_Fragment.postarray.get(imguser.getCurrentItem()).getImages().get(0).getTagPeople().isEmpty()) {
                    Postdetail_ImageAdapter1.Tagshow = true;
                    Log.e("LLL_Bool: ", String.valueOf(Look_Fragment.postarray.get(position).getImages().get(0).getTagPeople().isEmpty()));
                    setmodel(Look_Fragment.postarray.get(imguser.getCurrentItem()).getImages().get(0));
                }
            }
        }

        rlClick.setOnClickListener(v ->
                runOnUiThread(() -> {
                    // Code to run on UI thread
                    long clickTime = System.currentTimeMillis();
                    if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                        if (img_dot_smile.getVisibility() == View.VISIBLE)
                            setLike(0);
                        else
                            setLike(1);
                        tap = false;
                    } else
                        tap = true;

                    lastClickTime = clickTime;
                }));

        gestureDetector = new GestureDetector(PostDetailPage.this, new GestureListener());

//        rlLike.setOnTouchListener((view, motionEvent) -> {
////            Log.e("LLL_Touch: ", "Down");
////            gestureDetector.onTouchEvent(motionEvent);
////            return true;
////        });


        imgdraw.setOnClickListener(view -> {

            dialog = new android.app.Dialog(PostDetailPage.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
            dialog.setContentView(R.layout.dialoge_token);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.black);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            dialog.show();

            ImageView img_close, img_token_like, img_token_dislike;

            img_close = dialog.findViewById(R.id.img_close);
            img_token_like = dialog.findViewById(R.id.img_token_like);
            img_token_dislike = dialog.findViewById(R.id.img_token_dislike);

            img_token_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Public_Function.isInternetConnected(PostDetailPage.this)) {
                        sendGesture(1);
                    } else {
                        Toasty.error(PostDetailPage.this, "Please check your internet connection.").show();
                    }
                }
            });

            img_token_dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Public_Function.isInternetConnected(PostDetailPage.this)) {
                        sendGesture(0);
                    } else {
                        Toasty.error(PostDetailPage.this, "Please check your internet connection.").show();
                    }
                }
            });

            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                }
            });
//            Intent intent = new Intent(PostDetailPage.this, DrawAtivity.class);
//            intent.putExtra("comment", comment);
//            intent.putExtra("position", position);
//            intent.putExtra("come", come);
//            startActivity(intent);
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

//            finish();
        });

        if (comment.equalsIgnoreCase("yes")) {

            new CountDownTimer(500, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    Public_Function.showSoftKeyboard(edtdescription, PostDetailPage.this);
                    lnimages.setVisibility(View.GONE);
                    ll_last.setVisibility(View.GONE);
                    lncomment.setVisibility(View.VISIBLE);
                    lstcomment.setVisibility(View.VISIBLE);
                    edtdescription.setCursorVisible(true);
                    imgdownarro.setVisibility(View.VISIBLE);
                }
            }.start();
        } else {

        }
//        comment_adapter = new Comment_Adapter(PostDetailPage.this, commentarraylist);
//        lstcomment.setAdapter(comment_adapter);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        loadNativeAds();

        adView.setMediaView(adView.findViewById(R.id.ad_media));

        // Register the view used for each individual asset.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setIconView(adView.findViewById(R.id.ad_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        if (come.equalsIgnoreCase("wall")) {
            Log.e("LLLLLLL_Index: ", imguser.getCurrentItem() + "             " + position);
            more_comment = Look_Fragment.postarray.get(position).getMore_comments();
            other_user_id = Look_Fragment.postarray.get(position).getUserId();
            txttime.setText(Look_Fragment.postarray.get(position).getTime_duration());
            for (int i = 0; i < Look_Fragment.postarray.size(); i++) {
                for (int j = 0; j < Look_Fragment.postarray.get(i).getImages().size(); j++) {
                    Look_Fragment.postarray.get(i).getImages().get(j).setTagshow(false);
                }
            }
            post_id = Look_Fragment.postarray.get(position).getId() + "";
            post_url = Look_Fragment.postarray.get(position).getImage() + "";

            postdetail_imageAdapter1 = new Postdetail_ImageAdapter1(PostDetailPage.this, Look_Fragment.postarray,this);
            imguser.setAdapter(postdetail_imageAdapter1);
            imguser.setCurrentItem(position);

            if (Look_Fragment.postarray.get(imguser.getCurrentItem()).getLikedPost() == 0) {
                img_dot_smile.setVisibility(View.INVISIBLE);
            } else {
                img_dot_smile.setVisibility(View.VISIBLE);
            }

            if ((imguser.getCurrentItem()) == Look_Fragment.postarray.size() - 1) {
                img_next.setVisibility(View.GONE);
            }

            if ((imguser.getCurrentItem()) == 0) {
                img_prev.setVisibility(View.GONE);
            }

            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add_view.setVisibility(View.GONE);
                }
            });

            img_ad_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add_view.setVisibility(View.GONE);

                    if ((imguser.getCurrentItem()) < Look_Fragment.postarray.size() - 1) {

                        if (Look_Fragment.postarray.get(imguser.getCurrentItem() + 1).getLikedPost() == 0) {
                            img_dot_smile.setVisibility(View.INVISIBLE);
                        } else {
                            img_dot_smile.setVisibility(View.VISIBLE);
                        }

                        imguser.setCurrentItem(imguser.getCurrentItem() + 1);
                        post_id = Look_Fragment.postarray.get(imguser.getCurrentItem() + 1).getId() + "";
                        post_url = Look_Fragment.postarray.get(imguser.getCurrentItem() + 1).getImage() + "";

                        getPostLike();

                    }
                    if ((imguser.getCurrentItem()) == Look_Fragment.postarray.size() - 1) {
                        img_next.setVisibility(View.GONE);
                    }
                    if ((imguser.getCurrentItem()) != 0) {
                        img_prev.setVisibility(View.VISIBLE);
                    }
                }
            });

            img_ad_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add_view.setVisibility(View.GONE);


//                    getPostLike();
                    if ((imguser.getCurrentItem()) > 0) {


                        if (Look_Fragment.postarray.get(imguser.getCurrentItem() - 1).getLikedPost() == 0) {
                            img_dot_smile.setVisibility(View.INVISIBLE);
                        } else {
                            img_dot_smile.setVisibility(View.VISIBLE);
                        }

                        post_id = Look_Fragment.postarray.get(imguser.getCurrentItem() - 1).getId() + "";
                        post_url = Look_Fragment.postarray.get(imguser.getCurrentItem() - 1).getImage() + "";
                        imguser.setCurrentItem(imguser.getCurrentItem() - 1);
                    }

                    if ((imguser.getCurrentItem()) == 0) {
                        img_prev.setVisibility(View.GONE);
                    }
                    if ((imguser.getCurrentItem()) != Look_Fragment.postarray.size() - 1) {
                        img_next.setVisibility(View.VISIBLE);
                    }
                }
            });

            int n = Look_Fragment.postarray.size() % 5;
            Log.e("LLLL_Mode: ", n + "");

            img_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if ((imguser.getCurrentItem()) < Look_Fragment.postarray.size() - 1) {
                        if (imguser.getCurrentItem() == 2) {
                            add_view.setVisibility(View.VISIBLE);
                            populateNativeAdView(mNativeAds.get(0), adView);
                        } else {

                            if (Look_Fragment.postarray.get(imguser.getCurrentItem() + 1).getLikedPost() == 0) {
                                img_dot_smile.setVisibility(View.INVISIBLE);
                            } else {
                                img_dot_smile.setVisibility(View.VISIBLE);
                            }

//                            getPostLike();
                            post_id = Look_Fragment.postarray.get(imguser.getCurrentItem() + 1).getId() + "";
                            post_url = Look_Fragment.postarray.get(imguser.getCurrentItem() + 1).getImage() + "";
                            imguser.setCurrentItem(imguser.getCurrentItem() + 1);
                        }
                    }
                    if ((imguser.getCurrentItem()) == Look_Fragment.postarray.size() - 1) {
                        img_next.setVisibility(View.GONE);
                    }
                    if ((imguser.getCurrentItem()) != 0) {
                        img_prev.setVisibility(View.VISIBLE);
                    }
                }
            });

            img_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((imguser.getCurrentItem()) > 0) {
//                        getPostLike();

                        if (Look_Fragment.postarray.get(imguser.getCurrentItem() - 1).getLikedPost() == 0) {
                            img_dot_smile.setVisibility(View.INVISIBLE);
                        } else {
                            img_dot_smile.setVisibility(View.VISIBLE);
                        }

                        if ((imguser.getCurrentItem()) > 0) {
                            post_id = Look_Fragment.postarray.get(imguser.getCurrentItem() - 1).getId() + "";
                            post_url = Look_Fragment.postarray.get(imguser.getCurrentItem() - 1).getImage() + "";
                            imguser.setCurrentItem(imguser.getCurrentItem() - 1);
                        }

                    }

                    if ((imguser.getCurrentItem()) == 0) {
                        img_prev.setVisibility(View.GONE);
                    }
                    if ((imguser.getCurrentItem()) != Look_Fragment.postarray.size() - 1) {
                        img_next.setVisibility(View.VISIBLE);
                    }
                }
            });

            selectposition = position;
            txttoolbar.setText(Look_Fragment.postarray.get(position).getUsername() + "");
            txtuserfullname.setText(Look_Fragment.postarray.get(position).getFullName() + "");
            if (!Look_Fragment.postarray.get(position).getPhoto().equals("") && !Look_Fragment.postarray.get(position).getPhoto().equals(null) && !Look_Fragment.postarray.get(position).getPhoto().equals("null")) {
                Glide.with(PostDetailPage.this)
                        .load(Look_Fragment.postarray.get(position).getPhoto())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .dontAnimate()
                        .into(imgprofilepic);
            }

            txtname.setText(Look_Fragment.postarray.get(position).getFullName() + "");
            txtbio.setText(Look_Fragment.postarray.get(position).getDescription());
            txtlocation.setText(Look_Fragment.postarray.get(position).getLocation());


            saved = Look_Fragment.postarray.get(position).getBookmarked();

            if (saved == 0) {
                imgsaved.setImageDrawable(getResources().getDrawable(R.drawable.save_outline));
            } else if (saved == 1) {
                imgsaved.setImageDrawable(getResources().getDrawable(R.drawable.saved_white));
            }

//            if (sharedpreference.getUserId(PostDetailPage.this).equals(Look_Fragment.postarray.get(position).getUserId() + "")) {
//                imgsaved.setVisibility(View.VISIBLE);
//                imgdot.setVisibility(View.VISIBLE);
//            } else {
//                imgsaved.setVisibility(View.VISIBLE);
//                imgdot.setVisibility(View.GONE);
//            }

//            comment_adapter.Addall(Look_Fragment.postarray.get(position).getComments());
            offset = Look_Fragment.postarray.get(position).getComments().size();

        } else if (come.equalsIgnoreCase("profile")) {
            txttime.setText(ProfilePage.postarray.get(position).getTime_duration() + "");
            more_comment = ProfilePage.postarray.get(position).getMore_comments();
            other_user_id = ProfilePage.postarray.get(position).getUserId();
            post_id = ProfilePage.postarray.get(position).getId() + "";
            post_url = ProfilePage.postarray.get(position).getImage() + "";

            for (int i = 0; i < ProfilePage.postarray.size(); i++) {
                for (int j = 0; j < ProfilePage.postarray.get(i).getImages().size(); j++) {
                    ProfilePage.postarray.get(i).getImages().get(j).setTagshow(false);
                }
            }

            if (ProfilePage.postarray.get(imguser.getCurrentItem()).getLikedPost() != null) {
                if (ProfilePage.postarray.get(imguser.getCurrentItem()).getLikedPost() == 0) {
                    img_dot_smile.setVisibility(View.INVISIBLE);
                } else {
                    img_dot_smile.setVisibility(View.VISIBLE);
                }
            }

            if (sharedpreference.getUserId(PostDetailPage.this).equals(String.valueOf(ProfilePage.postarray.get(position).getUserId()))) {
                imgsaved.setVisibility(View.VISIBLE);
                imgdot.setVisibility(View.VISIBLE);
            } else {
                imgsaved.setVisibility(View.VISIBLE);
                imgdot.setVisibility(View.GONE);
            }

            postdetail_imageAdapter1 = new Postdetail_ImageAdapter1(PostDetailPage.this, ProfilePage.postarray,this);
            imguser.setAdapter(postdetail_imageAdapter1);
            imguser.setCurrentItem(position);

            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add_view.setVisibility(View.GONE);
                }
            });

            img_ad_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add_view.setVisibility(View.GONE);
//                    getPostLike();

                    if ((imguser.getCurrentItem()) < ProfilePage.postarray.size() - 1) {

                        if (ProfilePage.postarray.get(imguser.getCurrentItem() + 1).getLikedPost() == 0) {
                            img_dot_smile.setVisibility(View.INVISIBLE);
                        } else {
                            img_dot_smile.setVisibility(View.VISIBLE);
                        }

                        selectposition = imguser.getCurrentItem() + 1;
                        post_id = ProfilePage.postarray.get(selectposition).getId() + "";
                        post_url = ProfilePage.postarray.get(selectposition).getImage() + "";
                        imguser.setCurrentItem(imguser.getCurrentItem() + 1);
                    }
                    if ((imguser.getCurrentItem()) == ProfilePage.postarray.size() - 1) {
                        img_next.setVisibility(View.GONE);
                    }

                    if ((imguser.getCurrentItem()) != 0) {
                        img_prev.setVisibility(View.VISIBLE);
                    }
                }
            });

            img_ad_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add_view.setVisibility(View.GONE);
//                    getPostLike();

                    if ((imguser.getCurrentItem()) > 0) {

                        if (ProfilePage.postarray.get(imguser.getCurrentItem() - 1).getLikedPost() == 0) {
                            img_dot_smile.setVisibility(View.INVISIBLE);
                        } else {
                            img_dot_smile.setVisibility(View.VISIBLE);
                        }

                        selectposition = imguser.getCurrentItem() - 1;
                        post_id = ProfilePage.postarray.get(selectposition).getId() + "";
                        post_url = ProfilePage.postarray.get(selectposition).getImage() + "";
                        imguser.setCurrentItem(imguser.getCurrentItem() - 1);
                    }

                    if ((imguser.getCurrentItem()) == 0) {
                        img_prev.setVisibility(View.GONE);
                    }

                    if ((imguser.getCurrentItem()) != ProfilePage.postarray.size() - 1) {
                        img_next.setVisibility(View.VISIBLE);
                    }
                }
            });


            selectposition = position;
            Log.e("LLLL_Position: ", imguser.getCurrentItem() + "    " + ProfilePage.postarray.size());
            if ((imguser.getCurrentItem()) == ProfilePage.postarray.size() - 1) {
                img_next.setVisibility(View.GONE);
            }

            if ((imguser.getCurrentItem()) == 0) {
                img_prev.setVisibility(View.GONE);
            }

            int n = ProfilePage.postarray.size() % 5;
            Log.e("LLLL_Mode_Profile: ", n + "");
            img_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((imguser.getCurrentItem()) < ProfilePage.postarray.size() - 1) {
                        if (imguser.getCurrentItem() == 2) {
                            add_view.setVisibility(View.VISIBLE);
                            populateNativeAdView(mNativeAds.get(0), adView);
                        } else {
//                            getPostLike();

                            if (ProfilePage.postarray.get(imguser.getCurrentItem() + 1).getLikedPost() == 0) {
                                img_dot_smile.setVisibility(View.INVISIBLE);
                            } else {
                                img_dot_smile.setVisibility(View.VISIBLE);
                            }

                            selectposition = imguser.getCurrentItem() + 1;
                            post_id = ProfilePage.postarray.get(selectposition).getId() + "";
                            post_url = ProfilePage.postarray.get(selectposition).getImage() + "";
                            imguser.setCurrentItem(imguser.getCurrentItem() + 1);
                        }
                    }
                    if ((imguser.getCurrentItem()) == ProfilePage.postarray.size() - 1) {
                        img_next.setVisibility(View.GONE);
                    }

                    if ((imguser.getCurrentItem()) != 0) {
                        img_prev.setVisibility(View.VISIBLE);
                    }
                }
            });

            img_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((imguser.getCurrentItem()) > 0) {
//                        getPostLike();

                        if (ProfilePage.postarray.get(imguser.getCurrentItem() - 1).getLikedPost() == 0) {
                            img_dot_smile.setVisibility(View.INVISIBLE);
                        } else {
                            img_dot_smile.setVisibility(View.VISIBLE);
                        }

                        selectposition = imguser.getCurrentItem() - 1;
                        post_id = ProfilePage.postarray.get(selectposition).getId() + "";
                        post_url = ProfilePage.postarray.get(selectposition).getImage() + "";
                        imguser.setCurrentItem(imguser.getCurrentItem() - 1);
                    }

                    if ((imguser.getCurrentItem()) == 0) {
                        img_prev.setVisibility(View.GONE);
                    }

                    if ((imguser.getCurrentItem()) != ProfilePage.postarray.size() - 1) {
                        img_next.setVisibility(View.VISIBLE);
                    }
                }
            });

            txttoolbar.setText(ProfilePage.postarray.get(position).getUsername());
            txtuserfullname.setText(ProfilePage.postarray.get(position).getFullName());
            txtname.setText(ProfilePage.postarray.get(position).getFullName());
            txtbio.setText(ProfilePage.postarray.get(position).getDescription());
            txtlocation.setText(ProfilePage.postarray.get(position).getLocation());

            saved = ProfilePage.postarray.get(position).getBookmarked();


            if (saved == 0) {
                imgsaved.setImageDrawable(getResources().getDrawable(R.drawable.save_outline));
            } else if (saved == 1) {
                imgsaved.setImageDrawable(getResources().getDrawable(R.drawable.saved_white));
            }

            if (!ProfilePage.postarray.get(position).getPhoto().equals("") && !ProfilePage.postarray.get(position).getPhoto().equals(null) && !ProfilePage.postarray.get(position).getPhoto().equals("null")) {
                Glide.with(PostDetailPage.this)
                        .load(ProfilePage.postarray.get(position).getPhoto())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .dontAnimate()
                        .into(imgprofilepic);
            }

//            comment_adapter.Addall(ProfilePage.postarray.get(position).getComments());
            offset = ProfilePage.postarray.get(position).getComments().size();

        } else if (come.equalsIgnoreCase("otherprofile")) {
            txttime.setText(OtherUserProfile.postarray.get(position).getTime_duration());
            more_comment = OtherUserProfile.postarray.get(position).getMore_comments();
            other_user_id = OtherUserProfile.postarray.get(position).getUserId();

            if (OtherUserProfile.postarray.get(imguser.getCurrentItem()).getLikedPost() == 0) {
                img_dot_smile.setVisibility(View.INVISIBLE);
            } else {
                img_dot_smile.setVisibility(View.VISIBLE);
            }

            for (int i = 0; i < OtherUserProfile.postarray.size(); i++) {
                for (int j = 0; j < OtherUserProfile.postarray.get(i).getImages().size(); j++) {
                    OtherUserProfile.postarray.get(i).getImages().get(j).setTagshow(false);
                }
            }

//            if (sharedpreference.getUserId(PostDetailPage.this).equals(OtherUserProfile.postarray.get(position).getUserId() + "")) {
//                imgsaved.setVisibility(View.VISIBLE);
//                imgdot.setVisibility(View.VISIBLE);
//            } else {
//                imgsaved.setVisibility(View.VISIBLE);
//                imgdot.setVisibility(View.GONE);
//            }

            postdetail_imageAdapter1 = new Postdetail_ImageAdapter1(PostDetailPage.this, OtherUserProfile.postarray,this);
            imguser.setAdapter(postdetail_imageAdapter1);
            imguser.setCurrentItem(position);
            selectposition = position;
            txttoolbar.setText(OtherUserProfile.postarray.get(position).getUsername());
            txtuserfullname.setText(OtherUserProfile.postarray.get(position).getFullName());
            txtbio.setText(OtherUserProfile.postarray.get(position).getDescription());
            txtlocation.setText(OtherUserProfile.postarray.get(position).getLocation());

            saved = OtherUserProfile.postarray.get(position).getBookmarked();
            post_id = OtherUserProfile.postarray.get(position).getId() + "";
            post_url = OtherUserProfile.postarray.get(position).getImage() + "";

            if (!OtherUserProfile.postarray.get(position).getPhoto().equals("") && !OtherUserProfile.postarray.get(position).getPhoto().equals(null) && !OtherUserProfile.postarray.get(position).getPhoto().equals("null")) {
                Glide.with(PostDetailPage.this)
                        .load(OtherUserProfile.postarray.get(position).getPhoto())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .dontAnimate()
                        .into(imgprofilepic);
            }

            if (saved == 0) {
                imgsaved.setImageDrawable(getResources().getDrawable(R.drawable.save_outline));
            } else if (saved == 1) {
                imgsaved.setImageDrawable(getResources().getDrawable(R.drawable.saved_white));
            }

//            comment_adapter.Addall(OtherUserProfile.postarray.get(position).getComments());
            offset = OtherUserProfile.postarray.get(position).getComments().size();
        }

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

        rv_frend_list.setLayoutManager(new LinearLayoutManager(PostDetailPage.this, RecyclerView.VERTICAL, false));

        GetFrindlist();

        searchdiscover.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                followFrendAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                followFrendAdapter.getFilter().filter(newText);
                return false;
            }
        });

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Public_Function.isInternetConnected(PostDetailPage.this)) {
                    if (!Public_Function.shareUserList.isEmpty()) {
                        sharePost();
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                } else {
                    Toasty.error(PostDetailPage.this, "Please Check your internet connection...");
                }
            }
        });

        imgsend.setOnClickListener(v -> behavior.setState(BottomSheetBehavior.STATE_EXPANDED));

        imgdownarro.setOnClickListener(view -> {
            Log.e("LLLL_down_aarow: ", "..");
            Public_Function.hideKeyboard(PostDetailPage.this);
            edtdescription.clearFocus();
            ll_last.setVisibility(View.VISIBLE);
            lncomment.setVisibility(View.GONE);
            lnimages.setVisibility(View.VISIBLE);
            lstcomment.setVisibility(View.GONE);
            imgdownarro.setVisibility(View.GONE);

            if (postdetail_imageAdapter1 != null) {
                if (come.equalsIgnoreCase("wall")) {

                    if (!Look_Fragment.postarray.get(imguser.getCurrentItem()).getImages().isEmpty()) {
                        if (Look_Fragment.postarray.get(imguser.getCurrentItem()).getImages().get(0).isTagshow()) {
                            Postdetail_ImageAdapter1.Tagshow = true;
                            postdetail_imageAdapter1.notifyDataSetChanged();
                        }
                    }
                } else if (come.equalsIgnoreCase("profile")) {

                    if (!ProfilePage.postarray.get(imguser.getCurrentItem()).getImages().isEmpty()) {
                        if (ProfilePage.postarray.get(imguser.getCurrentItem()).getImages().get(0).isTagshow()) {
                            Postdetail_ImageAdapter1.Tagshow = true;
                            postdetail_imageAdapter1.notifyDataSetChanged();
                        }
                    }

                } else if (come.equalsIgnoreCase("otherprofile")) {


                    if (!OtherUserProfile.postarray.get(imguser.getCurrentItem()).getImages().isEmpty()) {
                        if (ProfilePage.postarray.get(imguser.getCurrentItem()).getImages().get(0).isTagshow()) {
                            Postdetail_ImageAdapter1.Tagshow = true;
                            postdetail_imageAdapter1.notifyDataSetChanged();
                        }
                    }
                }
            }

        });

        lstcomment.setOnItemClickListener((adapterView, view, i, l) -> {

            Log.d("mn13click", "12");

            if (sharedpreference.getUserId(PostDetailPage.this).equalsIgnoreCase(commentarraylist.get(i).getUserId() + "")) {
                Intent i1 = new Intent(PostDetailPage.this, ProfilePage.class);
                i1.putExtra("type", "left");
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            } else {
                Intent i1 = new Intent(PostDetailPage.this, OtherUserProfile.class);
                i1.putExtra("other_id", commentarraylist.get(i).getUserId() + "");
                i1.putExtra("other_username", "");
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
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

        imguser.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageSelected(int position) {
                Postdetail_ImageAdapter1.Tagshow = true;

                if (postdetail_imageAdapter1 != null) {

                }
                selectposition = position;
                if (come.equalsIgnoreCase("wall")) {
                    txttime.setText(Look_Fragment.postarray.get(position).getTime_duration());
                    more_comment = Look_Fragment.postarray.get(position).getMore_comments();
                    other_user_id = Look_Fragment.postarray.get(position).getUserId();
                    txttoolbar.setText(Look_Fragment.postarray.get(position).getUsername());
                    txtuserfullname.setText(Look_Fragment.postarray.get(position).getFullName());
                    txtbio.setText(Look_Fragment.postarray.get(position).getDescription());
                    txtlocation.setText(Look_Fragment.postarray.get(position).getLocation());

//                    comment_adapter.Addall(Look_Fragment.postarray.get(position).getComments());
                    post_id = Look_Fragment.postarray.get(position).getId() + "";
                    post_url = Look_Fragment.postarray.get(position).getImage() + "";

//                    if (sharedpreference.getUserId(PostDetailPage.this).equals(Look_Fragment.postarray.get(position).getUserId() + "")) {
//                        imgsaved.setVisibility(View.VISIBLE);
//                        imgdot.setVisibility(View.VISIBLE);
//                    } else {
//                        imgsaved.setVisibility(View.VISIBLE);
//                        imgdot.setVisibility(View.GONE);
//                    }

                    saved = Look_Fragment.postarray.get(position).getBookmarked();
                    if (saved == 0) {
                        imgsaved.setImageDrawable(getResources().getDrawable(R.drawable.save_outline));
                    } else if (saved == 1) {
                        imgsaved.setImageDrawable(getResources().getDrawable(R.drawable.saved_white));
                    }

                    if (!Look_Fragment.postarray.get(position).getPhoto().equals("") && !Look_Fragment.postarray.get(position).getPhoto().equals(null) && !Look_Fragment.postarray.get(position).getPhoto().equals("null")) {
                        Glide.with(PostDetailPage.this)
                                .load(Look_Fragment.postarray.get(position).getPhoto())
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.placeholder)
                                .dontAnimate()
                                .into(imgprofilepic);
                    } else {
                        imgprofilepic.setImageDrawable(getResources().getDrawable(R.drawable.placeholder));
                    }
                    offset = Look_Fragment.postarray.get(position).getComments().size();

                } else if (come.equalsIgnoreCase("profile")) {
                    Log.d("mn13datetime", ProfilePage.postarray.get(position).getTime_duration() + "11");
                    txttime.setText(ProfilePage.postarray.get(position).getTime_duration());
                    other_user_id = ProfilePage.postarray.get(position).getUserId();
                    more_comment = ProfilePage.postarray.get(position).getMore_comments();
                    txttoolbar.setText(ProfilePage.postarray.get(position).getUsername());
                    txtuserfullname.setText(ProfilePage.postarray.get(position).getFullName());
                    txtbio.setText(ProfilePage.postarray.get(position).getDescription());
                    txtlocation.setText(ProfilePage.postarray.get(position).getLocation());

//                    comment_adapter.Addall(ProfilePage.postarray.get(position).getComments());
                    post_id = ProfilePage.postarray.get(position).getId() + "";
                    post_url = ProfilePage.postarray.get(position).getImage() + "";

                    saved = ProfilePage.postarray.get(position).getBookmarked();
                    if (saved == 0) {
                        imgsaved.setImageDrawable(getResources().getDrawable(R.drawable.save_outline));
                    } else if (saved == 1) {
                        imgsaved.setImageDrawable(getResources().getDrawable(R.drawable.saved_white));
                    }

                    if (sharedpreference.getUserId(PostDetailPage.this).equals(ProfilePage.postarray.get(position).getUserId() + "")) {
                        imgsaved.setVisibility(View.VISIBLE);
                        imgdot.setVisibility(View.VISIBLE);
                    } else {
                        imgsaved.setVisibility(View.VISIBLE);
                        imgdot.setVisibility(View.GONE);
                    }

                    offset = ProfilePage.postarray.get(position).getComments().size();
                    if (!ProfilePage.postarray.get(position).getPhoto().equals("") && !ProfilePage.postarray.get(position).getPhoto().equals(null) && !ProfilePage.postarray.get(position).getPhoto().equals("null")) {
                        Glide.with(PostDetailPage.this)
                                .load(ProfilePage.postarray.get(position).getPhoto())
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.placeholder)
                                .dontAnimate()
                                .into(imgprofilepic);
                    } else {
                        imgprofilepic.setImageDrawable(getResources().getDrawable(R.drawable.placeholder));
                    }

                } else if (come.equalsIgnoreCase("otherprofile")) {
                    txttime.setText(OtherUserProfile.postarray.get(position).getTime_duration());
                    more_comment = OtherUserProfile.postarray.get(position).getMore_comments();
                    other_user_id = OtherUserProfile.postarray.get(position).getUserId();
                    txttoolbar.setText(OtherUserProfile.postarray.get(position).getUsername());
                    txtuserfullname.setText(OtherUserProfile.postarray.get(position).getFullName());
                    txtbio.setText(OtherUserProfile.postarray.get(position).getDescription());
                    txtlocation.setText(OtherUserProfile.postarray.get(position).getLocation());

//                    comment_adapter.Addall(OtherUserProfile.postarray.get(position).getComments());
                    post_id = OtherUserProfile.postarray.get(position).getId() + "";
                    post_url = OtherUserProfile.postarray.get(position).getImage() + "";
                    offset = OtherUserProfile.postarray.get(position).getComments().size();
                    saved = OtherUserProfile.postarray.get(position).getBookmarked();
                    if (saved == 0) {
                        imgsaved.setImageDrawable(getResources().getDrawable(R.drawable.save_outline));
                    } else if (saved == 1) {
                        imgsaved.setImageDrawable(getResources().getDrawable(R.drawable.saved_white));
                    }

//                    if (sharedpreference.getUserId(PostDetailPage.this).equals(OtherUserProfile.postarray.get(position).getUserId() + "")) {
//                        imgsaved.setVisibility(View.VISIBLE);
//                        imgdot.setVisibility(View.VISIBLE);
//                    } else {
//                        imgsaved.setVisibility(View.VISIBLE);
//                        imgdot.setVisibility(View.GONE);
//                    }

                    if (!OtherUserProfile.postarray.get(position).getPhoto().equals("") && !OtherUserProfile.postarray.get(position).getPhoto().equals(null) && !OtherUserProfile.postarray.get(position).getPhoto().equals("null")) {
                        Glide.with(PostDetailPage.this)
                                .load(OtherUserProfile.postarray.get(position).getPhoto())
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.placeholder)
                                .dontAnimate()
                                .into(imgprofilepic);
                    } else {
                        imgprofilepic.setImageDrawable(getResources().getDrawable(R.drawable.placeholder));
                    }

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imgcomment.setOnClickListener(view -> {
//            edtdescription.setText("");
//            Public_Function.showSoftKeyboard(edtdescription, PostDetailPage.this);
//            lnimages.setVisibility(View.GONE);
//            ll_last.setVisibility(View.GONE);
//            lncomment.setVisibility(View.VISIBLE);
//            lstcomment.setVisibility(View.VISIBLE);
//            edtdescription.setCursorVisible(true);
//            imgdownarro.setVisibility(View.VISIBLE);

            if (postdetail_imageAdapter1 != null) {
                if (come.equalsIgnoreCase("wall")) {

                    Intent intent = new Intent(PostDetailPage.this, CommentActivity.class);
                    intent.putExtra("comment", "yes");
                    intent.putExtra("come", "wall");
                    intent.putExtra("position", imguser.getCurrentItem());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

//                    if (!Look_Fragment.postarray.get(imguser.getCurrentItem()).getImages().isEmpty()) {
//                        if (Look_Fragment.postarray.get(imguser.getCurrentItem()).getImages().get(0).isTagshow()) {
//                            Postdetail_ImageAdapter1.Tagshow = false;
//                            postdetail_imageAdapter1.notifyDataSetChanged();
//                        }
//                    }
                } else if (come.equalsIgnoreCase("profile")) {

                    Intent intent = new Intent(PostDetailPage.this, CommentActivity.class);
                    intent.putExtra("comment", "yes");
                    intent.putExtra("come", "profile");
                    intent.putExtra("position", imguser.getCurrentItem());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

//                    if (!ProfilePage.postarray.get(imguser.getCurrentItem()).getImages().isEmpty()) {
//                        if (ProfilePage.postarray.get(imguser.getCurrentItem()).getImages().get(0).isTagshow()) {
//                            Postdetail_ImageAdapter1.Tagshow = false;
//                            postdetail_imageAdapter1.notifyDataSetChanged();
//                        }
//                    }

                } else if (come.equalsIgnoreCase("otherprofile")) {

                    Intent intent = new Intent(PostDetailPage.this, CommentActivity.class);
                    intent.putExtra("comment", "yes");
                    intent.putExtra("come", "otherprofile");
                    intent.putExtra("position", imguser.getCurrentItem());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

//                    if (!OtherUserProfile.postarray.get(imguser.getCurrentItem()).getImages().isEmpty()) {
//                        if (ProfilePage.postarray.get(imguser.getCurrentItem()).getImages().get(0).isTagshow()) {
//                            Postdetail_ImageAdapter1.Tagshow = false;
//                            postdetail_imageAdapter1.notifyDataSetChanged();
//                        }
//                    }

                }
            }


        });

        tv_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postdetail_imageAdapter1 != null) {
                    if (ll_more.getVisibility() == View.VISIBLE) {
                        collapse(ll_more);
                    } else {
                        expand(ll_more);
                    }
                    if (come.equalsIgnoreCase("wall")) {

                        Intent intent = new Intent(PostDetailPage.this, ReportedActivity.class);
                        intent.putExtra("come", "wall");
                        intent.putExtra("report", "post");
                        intent.putExtra("u_id", Look_Fragment.postarray.get(imguser.getCurrentItem()).getUserId());
                        intent.putExtra("position", imguser.getCurrentItem());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

                    } else if (come.equalsIgnoreCase("profile")) {

                        Intent intent = new Intent(PostDetailPage.this, ReportedActivity.class);
                        intent.putExtra("come", "profile");
                        intent.putExtra("report", "post");
                        intent.putExtra("u_id", ProfilePage.postarray.get(imguser.getCurrentItem()).getUserId());
                        intent.putExtra("position", imguser.getCurrentItem());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

                    } else if (come.equalsIgnoreCase("otherprofile")) {

                        Intent intent = new Intent(PostDetailPage.this, ReportedActivity.class);
                        intent.putExtra("come", "otherprofile");
                        intent.putExtra("report", "post");
                        intent.putExtra("u_id", OtherUserProfile.postarray.get(imguser.getCurrentItem()).getUserId());
                        intent.putExtra("position", imguser.getCurrentItem());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

                    }
                }
            }
        });

        edtdescription.setHashtagEnabled(true);
        edtdescription.setMentionEnabled(true);

        mentionUserAdapter = new MentionUserAdapter(PostDetailPage.this);
        hashTag_adapter = new HashTagAdapter(PostDetailPage.this);

        edtdescription.setMentionTextChangedListener((view, text) -> {

            Log.d("mn13mentiom", "12" + text);
            SearchUser(text.toString());

        });

        edtdescription.setOnHashtagClickListener((view, text) -> Log.d("mn13click", text.toString()));

        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapse(ll_more);
            }
        });

        imgdot.setOnClickListener(view -> {
            if (ll_more.getVisibility() == View.VISIBLE) {
                collapse(ll_more);
            } else {
                expand(ll_more);
                if (come.equalsIgnoreCase("profile")){
                    tv_delete.setVisibility(View.VISIBLE);
                    tv_unfollow.setVisibility(View.GONE);
                } else {
                    tv_unfollow.setVisibility(View.VISIBLE);
                    tv_delete.setVisibility(View.GONE);
                }
            }

        });

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete_post();
                if (ll_more.getVisibility() == View.VISIBLE) {
                    collapse(ll_more);
                } else {
                    expand(ll_more);
                    if (come.equalsIgnoreCase("profile")){
                        tv_delete.setVisibility(View.VISIBLE);
                    } else {
                        tv_delete.setVisibility(View.GONE);
                    }
                }
            }
        });

        lstcomment.setOnItemClickListener((adapterView, view, i, l) -> {

            Log.d("mn13click", "12");

            if (sharedpreference.getUserId(PostDetailPage.this).equalsIgnoreCase("" + commentarraylist.get(i).getUserId())) {
                Intent i1 = new Intent(PostDetailPage.this, ProfilePage.class);
                i1.putExtra("type", "left");
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            } else {
                Intent i1 = new Intent(PostDetailPage.this, OtherUserProfile.class);
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

        txtbio.setHashtagEnabled(true);
        txtbio.setMentionEnabled(true);

        txtbio.setOnMentionClickListener((view, text) -> {

            Log.d("mn13user:", text.toString());

            if (sharedpreference.getusername(PostDetailPage.this).equalsIgnoreCase("" + text)) {

                Intent i1 = new Intent(PostDetailPage.this, ProfilePage.class);
                i1.putExtra("type", "left");
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            } else {
                Intent i1 = new Intent(PostDetailPage.this, OtherUserProfile.class);
                i1.putExtra("other_id", "");
                i1.putExtra("other_username", text.toString());
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }


        });

        txtbio.setOnHashtagClickListener((view, text) -> Log.d("mn13tag:", text.toString()));


        txtbio.setMaxLines(4);
        txtbio.getViewTreeObserver().

                addOnGlobalLayoutListener(() -> {
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

        txttoolbar.setOnClickListener(view -> {

            if (sharedpreference.getUserId(PostDetailPage.this).equalsIgnoreCase("" + other_user_id)) {
                Intent i1 = new Intent(PostDetailPage.this, ProfilePage.class);
                i1.putExtra("type", "left");
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            } else {
                Log.e("LLL_User_id: ", String.valueOf(other_user_id));
//                addRating(String.valueOf(other_user_id), rating);
                Intent i1 = new Intent(PostDetailPage.this, OtherUserProfile.class);
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


            /*FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(Environment.getExternalStorageDirectory() + "/Blufff/test123.mp4", 256, 256);
            try {
                recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);
                recorder.setFormat("mp4");
                recorder.setFrameRate(30);
                recorder.setPixelFormat(0);
                recorder.setVideoBitrate(1200);
                recorder.start();
                for (int i = 0; i < 5; i++) {
                    view.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(lndraw.getDrawingCache());
                    view.setDrawingCacheEnabled(false);
                    org.bytedeco.javacv.AndroidFrameConverter converter2 = new AndroidFrameConverter();
                    Frame frames = converter2.convert(bitmap);
                    recorder.record(frames);
                }
                recorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            if (saved == 0) {
                Save_Post(1);
            } else if (saved == 1) {
                Save_Post(0);
            }

        });

        KeyboardUtils.addKeyboardToggleListener(this, isVisible -> {

            if (lncomment.getVisibility() == View.VISIBLE) {
                if (!isVisible) {
                    ll_last.setVisibility(View.VISIBLE);
                    lncomment.setVisibility(View.GONE);
                    lnimages.setVisibility(View.VISIBLE);
                    lstcomment.setVisibility(View.GONE);
                    imgdownarro.setVisibility(View.GONE);

                    if (postdetail_imageAdapter1 != null) {
                        if (come.equalsIgnoreCase("wall")) {

                            if (!Look_Fragment.postarray.get(imguser.getCurrentItem()).getImages().isEmpty()) {
                                if (Look_Fragment.postarray.get(imguser.getCurrentItem()).getImages().get(0).isTagshow()) {
                                    Postdetail_ImageAdapter1.Tagshow = true;
                                    postdetail_imageAdapter1.notifyDataSetChanged();
                                }
                            }
                        } else if (come.equalsIgnoreCase("profile")) {

                            if (!ProfilePage.postarray.get(imguser.getCurrentItem()).getImages().isEmpty()) {
                                if (ProfilePage.postarray.get(imguser.getCurrentItem()).getImages().get(0).isTagshow()) {
                                    Postdetail_ImageAdapter1.Tagshow = true;
                                    postdetail_imageAdapter1.notifyDataSetChanged();
                                }
                            }

                        } else if (come.equalsIgnoreCase("otherprofile")) {


                            if (!OtherUserProfile.postarray.get(imguser.getCurrentItem()).getImages().isEmpty()) {
                                if (ProfilePage.postarray.get(imguser.getCurrentItem()).getImages().get(0).isTagshow()) {
                                    Postdetail_ImageAdapter1.Tagshow = true;
                                    postdetail_imageAdapter1.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }
                Log.d("keyboard", "keyboard visible: " + isVisible);
            } else {

            }
        });

//        getPostLike();

        imgsmile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDetailPage.this, LikeActivity.class);
                intent.putExtra("PostId", post_id);
                startActivity(intent);
//                if (img_dot_smile.getVisibility() == View.VISIBLE)
//                    setLike(0);
//                else
//                    setLike(1);
            }
        });

        tv_on_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (come.equalsIgnoreCase("wall")) {
                    other_user_id = Look_Fragment.postarray.get(imguser.getCurrentItem()).getUserId();
                    if (count_turned_on == 0)
                        setTurnOnNotification(1);
                    else
                        setTurnOnNotification(0);
                } else if (come.equalsIgnoreCase("profile")) {
                    other_user_id = ProfilePage.postarray.get(imguser.getCurrentItem()).getUserId();
                    if (count_turned_on == 0)
                        setTurnOnNotification(1);
                    else
                        setTurnOnNotification(0);
                } else if (come.equalsIgnoreCase("otherprofile")) {
                    other_user_id = OtherUserProfile.postarray.get(imguser.getCurrentItem()).getUserId();
                    if (count_turned_on == 0)
                        setTurnOnNotification(1);
                    else
                        setTurnOnNotification(0);
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (lncomment.getVisibility() == View.VISIBLE) {
            Public_Function.hideKeyboard(PostDetailPage.this);

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


        //overridePendingTransition(RM.anim.slide_enter, RM.anim.slide_out);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.d("mn13back", "back");

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();

            new CountDownTimer(500, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }.start();
        }

        if (lncomment.getVisibility() == View.VISIBLE) {
            Public_Function.hideKeyboard(PostDetailPage.this);

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
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(PostDetailPage.this)));
        paramArrayList.add(new param("post_id", post_id + ""));
        paramArrayList.add(new param("keep", id + ""));

        new geturl().getdata(PostDetailPage.this, data -> {
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
                    if (come.equalsIgnoreCase("wall")) {
                        Look_Fragment.postarray.get(selectposition).setBookmarked(saved);
                    } else if (come.equalsIgnoreCase("profile")) {
                        ProfilePage.postarray.get(selectposition).setBookmarked(saved);
                    } else if (come.equalsIgnoreCase("otherprofile")) {
                        OtherUserProfile.postarray.get(selectposition).setBookmarked(saved);
                    }
                    Toast.makeText(PostDetailPage.this, status + "", Toast.LENGTH_LONG).show();
                } else {
                    // Toasty.error(getActivity(), status + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                // Toasty.error(Login.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }
        }, paramArrayList, "bookmark_post");
    }

    public void Load_morecomment() {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(PostDetailPage.this)));
        paramArrayList.add(new param("post_id", post_id + ""));
        paramArrayList.add(new param("limit", limit + ""));
        paramArrayList.add(new param("offset", offset + ""));
        paramArrayList.add(new param("timezone", Public_Function.gettimezone()));

        new geturl().getdata(PostDetailPage.this, data -> {
            try {
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("message");

                if (message) {
                    LoadComments login = new Gson().fromJson(data, LoadComments.class);
                    List<Comment> commentarray = new ArrayList<Comment>();
                    commentarray = login.getComments();
//                    comment_adapter.AppendAll(commentarray);
                    more_comment = login.getMoreComments();
                } else {
                    // Toasty.error(getActivity(), status + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                // Toasty.error(Login.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }

            if (offset < 20) {
                offset = 20;
            }
        }, paramArrayList, "view_post_comments");
    }


    public void Searchhashtag(CharSequence charSequence) {


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("keyword", charSequence.toString().replace("#", "")));

        new geturl().getdata(PostDetailPage.this, data -> {
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
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(PostDetailPage.this)));
        paramArrayList.add(new param("keyword", s + ""));
        paramArrayList.add(new param("filter_by", "all"));

        new geturl().getdata(PostDetailPage.this, data -> {
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
                    Toasty.error(PostDetailPage.this, status + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                Toasty.error(PostDetailPage.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }
        }, paramArrayList, "user_listing");
    }

    private void sendGesture(int like) {
        AndroidNetworking.post(geturl.BASE_URL + "like_dislike_gesture")
                .addHeaders("Authorization", sharedpreference.getUserToken(PostDetailPage.this))
                .addBodyParameter("user_id", sharedpreference.getUserId(PostDetailPage.this))
                .addBodyParameter("status", String.valueOf(like))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                Toasty.info(PostDetailPage.this, response.getString("message")).show();
                                if (dialog != null && dialog.isShowing())
                                    dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            Toasty.error(PostDetailPage.this, "Oops something went wrong...").show();
                            Log.e("LLLLLLL_EX: ", e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public void Delete_post() {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("post_id", post_id));

        new geturl().getdata(PostDetailPage.this, data -> {
            try {
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("message");

                if (message) {

                    if (come.equalsIgnoreCase("profile")) {
                        ProfilePage.delete = imguser.getCurrentItem();
                        ProfilePage.postarray.remove(imguser.getCurrentItem());
                    }
                    isDelete = true;
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

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


        if (theme.equalsIgnoreCase(sharedpreference.getTheme(PostDetailPage.this))) {

        } else {
            PostDetailPage.this.recreate();
            PostDetailPage.this.overridePendingTransition(0, 0);
        }


       /* cd = new ConnectionDetector(PostDetailPage.this);
        isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent) {
            Toasty.warning(PostDetailPage.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
        } else {
           // GetPost();
        }*/

        if (!sharedpreference.getphoto(PostDetailPage.this).equals("") && !sharedpreference.getphoto(PostDetailPage.this).equals(null) && !sharedpreference.getphoto(PostDetailPage.this).equals("null")) {
            Glide.with(PostDetailPage.this)
                    .load(sharedpreference.getphoto(PostDetailPage.this))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .dontAnimate()
                    .into(imguserpic);
        } else {

        }

    }

    public void setmodel(com.TBI.Client.Bluff.Model.GetFeed.Image image) {

        if (image.isTagshow()) {
            image.setTagshow(false);
        } else {
            image.setTagshow(true);
        }
        Log.e("LLLL_Tag: ", String.valueOf(image.isTagshow()));
        if (postdetail_imageAdapter1 != null) {
            postdetail_imageAdapter1.notifyDataSetChanged();
        }

        Log.d("mn13click", "Done");

    }

    @Override
    public void onSwipe(int direction) {
        //Detect the swipe gestures and display toast
        String showToastMessage = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT:
                showToastMessage = "You have Swiped Right.";
                break;
            case SimpleGestureFilter.SWIPE_LEFT:
                showToastMessage = "You have Swiped Left.";
                break;
            case SimpleGestureFilter.SWIPE_DOWN:
                showToastMessage = "You have Swiped Down.";
                finish();
                break;
            case SimpleGestureFilter.SWIPE_UP:
                showToastMessage = "You have Swiped Up.";
                break;

        }
        Toast.makeText(PostDetailPage.this, showToastMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDoubleTap() {

    }

    private void loadNativeAds() {

        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.admob_unit_id));
        adLoader = builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // A native ad loaded successfully, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        mNativeAds.add(unifiedNativeAd);
                        Log.e("LLLL_Native: ", String.valueOf(mNativeAds.size()));
                        if (!adLoader.isLoading()) {

                        }
                    }
                }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // A native ad failed to load, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                                + " load another.");
                        if (!adLoader.isLoading()) {
                            loadNativeAds();
                        }
                    }
                }).build();

        // Load the Native ads.
        adLoader.loadAds(new AdRequest.Builder().build(), 1);
    }

    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
//        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }

    public void GetFrindlist() {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(PostDetailPage.this)));
        paramArrayList.add(new param("other_user_id", ""));

        new geturl().getdata(PostDetailPage.this, new MyAsyncTaskCallback() {
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
                        followFrendAdapter = new FollowFrendAdapter(PostDetailPage.this, followingarray);
                        rv_frend_list.setAdapter(followFrendAdapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, paramArrayList, "user_connection");
    }

    private void sharePost() {

        Log.e("LLLLL_USer_List: ", sharedpreference.getUserId(PostDetailPage.this)+"    "+
                post_id + "     " + Public_Function.shareUserList.toString());
        String str = Public_Function.shareUserList.toString().replace("[","");
        String str1 = str.replace("]","");
        AndroidNetworking.post(geturl.BASE_URL + "share_post")
                .addHeaders("Authorization", sharedpreference.getUserToken(PostDetailPage.this))
                .addBodyParameter("user_id", sharedpreference.getUserId(PostDetailPage.this))
                .addBodyParameter("post_id", post_id)
                .addBodyParameter("shared_user_ids", str1)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("LLLLLLL_Post_Share: ", response.toString());
                        new RetrieveFeedTask().execute();

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLLLLLL_Post_Error: ", anError.getMessage());
                    }
                });
    }

    @Override
    public void onPostLiked(int status) {
        rlLike.setVisibility(View.VISIBLE);
        AndroidNetworking.post(geturl.BASE_URL + "like_unlike_post")
                .addHeaders("Authorization", sharedpreference.getUserToken(PostDetailPage.this))
                .addBodyParameter("user_id", sharedpreference.getUserId(PostDetailPage.this))
                .addBodyParameter("post_id", post_id)
                .addBodyParameter("status", String.valueOf(status))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getBoolean("success")) {

                                if (response.getInt("liked") == 0) {
                                    img_dot_smile.setVisibility(View.INVISIBLE);
                                } else {
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            rlLike.setVisibility(View.GONE);
                                        }
                                    }, 2000);
                                    img_dot_smile.setVisibility(View.VISIBLE);
                                }
                            }

                            Log.e("LLLLLL_Like_Point: ", String.valueOf(response.getInt("liked")));
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

    @Override
    public void onPullStart() {

    }

    @Override
    public void onPull(float v) {

    }

    @Override
    public void onPullCancel() {

    }

    @Override
    public void onPullComplete() {
        onBackPressed();
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        Bitmap myBitmap;

        protected String doInBackground(String... urls) {

            try {
                Log.e("LLLLLL_URL: ", post_url);
                URL url = new URL(post_url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);

            } catch (Exception e) {
                Log.e("LLLL_PostImg: ", e.getMessage());
                // Log exception
                return null;
            }
            return "1";
        }

        protected void onPostExecute(String feed) {
            if (feed.equals("1"))
                SaveImage(myBitmap);
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Blufff");
        File mySubDir = new File(myDir.getAbsolutePath() + "/Sent");
//        path = myDir.getAbsolutePath();

        myDir.mkdirs();
        mySubDir.mkdirs();

        String currentDateAndTime = getCurrentDateAndTime();

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        if (!mySubDir.exists()) {
            mySubDir.mkdirs();
        }

        File file = new File(mySubDir, "image_" + currentDateAndTime + ".jpg");
        path = file.getAbsolutePath();
        Log.d("Data", "data: " + path);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            shareFile = file;

            for (int i = 0; i < Public_Function.shareUserList.size(); i++) {
                MobiComUserPreference userPreferences = MobiComUserPreference.getInstance(PostDetailPage.this);
                Message messageToSend = new Message();
                messageToSend.setTo(String.valueOf(Public_Function.shareUserList.get(i)));
                messageToSend.setContactIds(String.valueOf(Public_Function.shareUserList.get(i)));
                messageToSend.setRead(Boolean.TRUE);
                messageToSend.setStoreOnDevice(Boolean.TRUE);
                messageToSend.setSendToDevice(Boolean.FALSE);
                messageToSend.setType(Message.MessageType.MT_OUTBOX.getValue());
                messageToSend.setMessage("Shared Post");
                List<String> filePaths = new ArrayList<String>();
                filePaths.add(path);
                messageToSend.setFilePaths(filePaths);
                messageToSend.setDeviceKeyString(userPreferences.getDeviceKeyString());
                messageToSend.setSource(Message.Source.MT_MOBILE_APP.getValue());

                new MobiComConversationService(PostDetailPage.this).sendMessage(messageToSend);
            }
            Public_Function.shareUserList.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
//        galleryAddPic(file);
       /* sendBroadcast(new Intent(
                Intent.ACTION_MEDIA_MOUNTED,
                Uri.parse("file://" + Environment.getExternalStorageDirectory())));*/
    }

    private String getCurrentDateAndTime() {
        Calendar calendar = Calendar.getInstance();
        // Setting format of the time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        // Getting the formated date as a string
        String formattedDate = dateFormat.format(calendar.getTime());

        return formattedDate;
    }

    private void setLike(int status) {
        imgLike.animate()
                .translationY(imgLike.getHeight())
                .alpha(0.0f)
                .setDuration(2000)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        imgLike.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
        AndroidNetworking.post(geturl.BASE_URL + "like_unlike_post")
                .addHeaders("Authorization", sharedpreference.getUserToken(PostDetailPage.this))
                .addBodyParameter("user_id", sharedpreference.getUserId(PostDetailPage.this))
                .addBodyParameter("post_id", post_id)
                .addBodyParameter("status", String.valueOf(status))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getBoolean("success")) {

                                if (response.getInt("liked") == 0) {
                                    img_dot_smile.setVisibility(View.INVISIBLE);
                                } else {
                                    imgLike.animate()
                                            .translationY(imgLike.getHeight())
                                            .alpha(1.0f)
                                            .setDuration(2000)
                                            .setListener(new Animator.AnimatorListener() {
                                                @Override
                                                public void onAnimationStart(Animator animation) {

                                                }

                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    imgLike.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onAnimationCancel(Animator animation) {

                                                }

                                                @Override
                                                public void onAnimationRepeat(Animator animation) {

                                                }
                                            });
                                    img_dot_smile.setVisibility(View.VISIBLE);
                                }
                            }

                            Log.e("LLLLLL_Like_Point: ", String.valueOf(response.getInt("liked")));
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

    private void setTurnOnNotification(int status) {
        AndroidNetworking.post(geturl.BASE_URL + "turn_on_notification")
                .addHeaders("Authorization", sharedpreference.getUserToken(PostDetailPage.this))
                .addBodyParameter("user_id", sharedpreference.getUserId(PostDetailPage.this))
                .addBodyParameter("sender_id", String.valueOf(other_user_id))
                .addBodyParameter("status", String.valueOf(status))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getBoolean("success")) {


                                if (response.getInt("turned_on") == 0) {
                                    tv_on_notify.setText("Turn on look notifications.");
                                } else {
                                    tv_on_notify.setText("Turn off look notifications.");
                                }
                            }

                            Log.e("LLLLLL_Like_Point: ", String.valueOf(response.getInt("turned_on") == 0));
                        } catch (JSONException e) {
                            Log.e("LLLL_Notify: ", e.getLocalizedMessage());
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLLLLL_Notify_Error: ", anError.getLocalizedMessage());
                    }
                });
    }

    private void getPostLike() {
        AndroidNetworking.post(geturl.BASE_URL + "view_post")
                .addHeaders("Authorization", sharedpreference.getUserToken(PostDetailPage.this))
                .addBodyParameter("user_id", sharedpreference.getUserId(PostDetailPage.this))
                .addBodyParameter("post_id", post_id)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            like = response.getInt("liked");
                            count_turned_on = response.getInt("notification_turned_on");

                            if (count_turned_on == 0) {
                                tv_on_notify.setText("Turn on look notifications.");
                            } else {
                                tv_on_notify.setText("Turn off look notifications.");
                            }

                            if (like == 0) {
                                img_dot_smile.setVisibility(View.INVISIBLE);
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


    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = false;
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        Log.d("LLLLLL_", "bottom");
                        onSwipeBottom();
                        result = true;
                    } else {
                        onSwipeTop();
                        result = false;
                        Log.d("mn13bottom", "top");
                    }

                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }

        void onSwipeRight() {
        }

        void onSwipeLeft() {
        }

        void onSwipeTop() {
            Log.e("LLLLLL_Swipe: ","Down: ");
        }

        void onSwipeBottom() {
            onBackPressed();
        }
    }

}





