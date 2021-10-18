package com.TBI.Client.Bluff.Activity.Home;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ImageFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.media.FaceDetector;
import android.media.MediaActionSound;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Mains.BottombarActivity;
import com.TBI.Client.Bluff.Activity.Post.Post3dVideoActivity;
import com.TBI.Client.Bluff.Activity.Post.PostVideoActivity;
import com.TBI.Client.Bluff.Activity.Profile.ProfilePage;
import com.TBI.Client.Bluff.Adapter.FiltersAdapter;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.Fragment.Map_Fragment;
import com.TBI.Client.Bluff.Model.GetFriends.GetCloseFrend;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.UserPages.WelcomeActivity;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.clans.fab.FloatingActionMenu;
import com.github.tcking.giraffecompressor.GiraffeCompressor;
import com.nagihong.videocompressor.VideoCompressor;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraLogger;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.FaceDetection.FaceOverlayView;
import com.otaliastudios.cameraview.FaceDetection.FaceUtils;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.VideoResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.controls.Grid;
import com.otaliastudios.cameraview.controls.Mode;
import com.otaliastudios.cameraview.controls.Preview;
import com.otaliastudios.cameraview.filter.Filters;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;
import com.otaliastudios.cameraview.size.Size;

import net.iquesoft.iquephoto.models.Filter;
import net.iquesoft.iquephoto.ui.activities.EditorActivity;
import net.iquesoft.iquephoto.utils.SharedPrefrence;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class OpenCamera1 extends AppCompatActivity implements SurfaceHolder.Callback {


    private final static CameraLogger LOG = CameraLogger.create("DemoApp");
    private final static boolean USE_FRAME_PROCESSOR = true;
    private final static boolean DECODE_BITMAP = true;
    private static final String IMAGE_DIRECTORY = "/Blufff/temp";
    private static final int SELECT_IMAGE = 200;
    private static Activity activity;
    private static ArrayList<GetCloseFrend> getClose_Frends = new ArrayList();
    private static String token = "";
    private final Filters[] mAllFilters = Filters.values();
    public ImageView miaImmagine2;
    public int offset = 0;
    @BindView(R.id.cl_main)
    CoordinatorLayout cl_main;
    @BindView(R.id.rv_main)
    RelativeLayout rv_main;
    @BindView(R.id.changecmaera)
    ImageView imgflip;
    @BindView(R.id.imggrid)
    ImageView imggrid;
    //    @BindView(RM.id.imgclick)
//    LottieAnimationView imgclick;
    @BindView(R.id.leftoption)
    FloatingActionMenu leftoption;
    @BindView(R.id.rightoption)
    FloatingActionMenu rightoption;
    @BindView(R.id.imgchat)
    ImageView imgchat;
    @BindView(R.id.imgProfile)
    ImageView imgprofile;
    @BindView(R.id.imgtimer)
    ImageView imgtimer;
    @BindView(R.id.imggallery)
    ImageView imggallery;
    @BindView(R.id.flash_off)
    ImageView flash_off;
    @BindView(R.id.flashauto)
    ImageView flashauto;
    @BindView(R.id.lnflash)
    LinearLayout lnflash;
    @BindView(R.id.imgflash)
    ImageView imgflash;
    @BindView(R.id.flashon)
    ImageView flashon;
    @BindView(R.id.relclick)
    RelativeLayout relclick;
    @BindView(R.id.timer_off)
    ImageView timer_off;
    @BindView(R.id.timer_2)
    ImageView timer_2;
    @BindView(R.id.timer_5)
    ImageView timer_5;
    @BindView(R.id.timer_10)
    ImageView timer_10;
    @BindView(R.id.lntimer)
    LinearLayout lntimer;
    @BindView(R.id.txttimer)
    TextView txttimer;
    @BindView(R.id.lnclick)
    LinearLayout lnclick;
    @BindView(R.id.recyclefilter)
    RecyclerView recyclefilter;
    @BindView(R.id.imgfilter)
    ImageView imgfilter;
    @BindView(R.id.img_rotate)
    ImageView img_rotate;
    @BindView(R.id.rl_up)
    RelativeLayout rl_up;

    @BindView(R.id.imgMyLocation)
    ImageView imgMyLocation;
    @BindView(R.id.imgwall)
    ImageView imgwall;
    @BindView(R.id.imgcamera)
    ImageView imgcamera;
    @BindView(R.id.img_video)
    ImageView img_video;
    @BindView(R.id.img_video_paus)
    ImageView dot;
    @BindView(R.id.ll_3d)
    LinearLayout ll_3d;
    @BindView(R.id.img_profile)
    CircleImageView img_profile;

    @BindView(R.id.img3d)
    ImageView img3d;
    @BindView(R.id.imgvideo)
    ImageView imgVideo;
    @BindView(R.id.ll_flux_select)
    LinearLayout ll_flux_select;
    @BindView(R.id.ll_video_select)
    LinearLayout ll_video_select;
    @BindView(R.id.imgCamera1)
    ImageView imgCamera1;
    @BindView(R.id.imgCamera2)
    ImageView imgCamera2;
    @BindView(R.id.img_video2)
    ImageView img_video2;
    @BindView(R.id.img_video1)
    ImageView img_video1;
    @BindView(R.id.imgGallery)
    ImageView imgGallery;
    @BindView(R.id.tv_style)
    TextView tv_style;

    @BindView(R.id.lnadd)
    LinearLayout lnadd;
    FaceOverlayView mFaceView;
    Map_Fragment map_fragment;
    FragmentManager fm;
    boolean flag = false, flash_flag = false, timer_flag = false;
    int timer = 0;
    CountDownTimer waitTimer;
    FiltersAdapter filtersAdapter;
    ProgressDialog progressDialog;
    int mFaceWidth;
    int mFaceHeight;
    int cameraType = 1; // front
    int frame_sec = 1000000;
    byte[] callbackBuffer;
    Camera.PreviewCallback cb;
    Bitmap bmp;
    Bitmap b;
    MediaMetadataRetriever mediaMetadataRetriever = null;
    FaceDetector.Face[] faces;
    private CameraView camera;
    private long mCaptureTime;
    private int mCurrentFilter = 0;
    private int mOrientation;
    private int mOrientationCompensation;
    private OrientationEventListener mOrientationEventListener;
    private Camera mCamera;
    // The surface view for the camera data
    private SurfaceView mView;
    // Let's keep track of the display rotation and orientation also:
    private int mDisplayRotation;
    private int mDisplayOrientation;
    private Camera.FaceDetectionListener faceDetectionListener = (faces, camera) -> {
        Log.e("LLLL_onFaceDetection", "Number of Faces:" + faces.length);
        // Update the view now!
        mFaceView.setFaces(faces);
    };
    private MediaPlayer player;
    private boolean is3d = false;


    public static void getFrendsData() {
        getClose_Frends.clear();
        AndroidNetworking.post(geturl.BASE_URL + "manage_closed_friends")
                .addHeaders("Authorization", token)
                .addBodyParameter("user_id", sharedpreference.getUserId(activity))
                .addBodyParameter("friend_ids", "")
                .addBodyParameter("action", "2")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                GetCloseFrend getCloseFrend = new GetCloseFrend();
                                getCloseFrend.setUserId(jsonObject.getInt("user_id"));
                                getCloseFrend.setUsername(jsonObject.getString("username"));
                                getCloseFrend.setFullName(jsonObject.getString("full_name"));
                                getCloseFrend.setLat(jsonObject.getString("lat"));
                                getCloseFrend.setLongt(jsonObject.getString("longt"));
                                getCloseFrend.setPhoto(jsonObject.getString("photo"));
                                getClose_Frends.add(getCloseFrend);
                            }

                            if (!Public_Function.getCloseFrends.isEmpty()) {
                                Public_Function.getCloseFrends.clear();
                                Public_Function.getCloseFrends.addAll(getClose_Frends);
                            } else {
                                Public_Function.getCloseFrends.addAll(getClose_Frends);
                            }

                            if (!Public_Function.getCloseFrends.isEmpty()) {
                                Public_Function.getCloseFrends.clear();
                                Public_Function.getCloseFrends.addAll(getClose_Frends);
                            } else {
                                Public_Function.getCloseFrends.addAll(getClose_Frends);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLL_Error_Friends: ", anError.getMessage());
                    }
                });
    }

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (sharedpreference.getTheme(OpenCamera1.this).equalsIgnoreCase("")) {
            sharedpreference.setTheme(OpenCamera1.this, "black");
            SharedPrefrence.setTheme(OpenCamera1.this, "black");
            Applozic logic = new Applozic(OpenCamera1.this);
            logic.sharedPreferences.edit().putString("theme", "black").commit();
        }

        new Handler().postDelayed(() -> {
            if (MobiComUserPreference.getInstance(OpenCamera1.this).isLoggedIn()) {
                if (sharedpreference.getUserId(OpenCamera1.this).equals("")) {
                    Intent i = new Intent(OpenCamera1.this, WelcomeActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.stay);
                } else {
                    ApplozicClient.getInstance(OpenCamera1.this).setContextBasedChat(true).setHandleDial(true).setIPCallEnabled(true);
                }
            } else {
                Intent i = new Intent(OpenCamera1.this, WelcomeActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }
        }, 1000);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera1);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(OpenCamera1.this));
        progressDialog = new ProgressDialog(OpenCamera1.this);
        progressDialog.setMessage("Please wait...");

////        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        if (ContextCompat.checkSelfPermission(OpenCamera1.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(OpenCamera1.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//            }
//        }

//        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        activity = OpenCamera1.this;
        token = sharedpreference.getUserToken(OpenCamera1.this);
        getFrendsData();
        mView = new SurfaceView(this);
        lnadd.addView(mView);

        mFaceView = new FaceOverlayView(this);
        addContentView(mFaceView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        mFaceView.setFaces(c);
//        camera.setFaceDetectionListener(faceDetectionListener);

        // Now create the OverlayView:
//        mFaceView = new FaceOverlayView(this);

//        addContentView(mFaceView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        camera = findViewById(R.id.camera);
        //camera.setFlash(Flash.AUTO);
        camera.setLifecycleOwner(OpenCamera1.this);
        camera.addCameraListener(new Listener());

        map_fragment = new Map_Fragment();
        fm = getSupportFragmentManager();

        mOrientationEventListener = new SimpleOrientationEventListener(this);
        mOrientationEventListener.enable();


        Glide.with(OpenCamera1.this)
                .load(sharedpreference.getphoto(OpenCamera1.this))
                .error(R.drawable.placeholder)
                .into(img_profile);


        img_rotate.setOnClickListener(v -> toggleCamera());

        relclick.setOnClickListener(view -> {

        });

        lnclick.setOnClickListener(view -> {


        });

        imgMyLocation.setOnClickListener(v -> {
            Intent intent = new Intent(OpenCamera1.this, BottombarActivity.class);
            intent.putExtra("type", "map");
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });

        imgwall.setOnClickListener(v -> {
            Intent intent = new Intent(OpenCamera1.this, BottombarActivity.class);
            intent.putExtra("type", "home");
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });

        imgchat.setOnClickListener(view -> {
            Intent intent = new Intent(OpenCamera1.this, BottombarActivity.class);
            intent.putExtra("type", "catch");
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });


        List<Filter> abc = Arrays.asList(
                new Filter("Original", new ColorMatrix(new float[]{
                        1, 0, 0, 0, 0,
                        0, 1, 0, 0, 0,
                        0.50f, 0, 1, 0, 0,
                        0, 0, 0, 1, 0})),
                new Filter("F01", new ColorMatrix(new float[]{
                        1, 0, 0, 0, 0,
                        0, 1, 0, 0, 0,
                        0.50f, 0, 1, 0, 0,
                        0, 0, 0, 1, 0})),
                new Filter("F02", new ColorMatrix(new float[]{
                        2, -1, 0, 0, 0,
                        -1, 2, 0, 0, 0,
                        0, -1, 2, 0, 0,
                        0, 0, 0, 1, 0})),
                new Filter("F12", new ColorMatrix(new float[]{ // TechColor mMatrix
                        1.9125277891456083f, -0.8545344976951645f, -0.09155508482755585f, 0, 11.793603434377337f,
                        -0.3087833385928097f, 1.7658908555458428f, -0.10601743074722245f, 0, -70.35205161461398f,
                        -0.231103377548616f, -0.7501899197440212f, 1.847597816108189f, 0, 30.950940869491138f,
                        0, 0, 0, 1, 0})),
                new Filter("F04", new ColorMatrix(new float[]{
                        1, 0, 0, 0.2f, 0,
                        0, 1, 0, 0, 0,
                        0, 0, 1, 0.2f, 0,
                        0, 0, 0, 1, 0})),
                new Filter("F05", new ColorMatrix(new float[]{
                        1, 0, 0, 0, 0,
                        0, 1.25f, 0, 0, 0,
                        0, 0, 0, 0.5f, 0,
                        0, 0, 0, 1, 0})),
                new Filter("F06", new ColorMatrix(new float[]{
                        1, 0, 0, 0, 0,
                        0, 0.5f, 0, 0, 0,
                        0, 0, 0, 0.5f, 0,
                        0, 0, 0, 1, 0})),
                new Filter("F07", new ColorMatrix(new float[]{
                        1, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 1, 1, 0,
                        0, 0, 0, 1, 0})),
                new Filter("F08", new ColorMatrix(new float[]{ // Polaroid mMatrix
                        1.438f, -0.062f, -0.062f, 0, 0,
                        -0.122f, 1.378f, -0.122f, 0, 0,
                        -0.016f, -0.016f, 1.483f, 0, 0,
                        0, 0, 0, 1, 0})),
                new Filter("F09", new ColorMatrix(new float[]{ // CodaChrome mMatrix
                        1.1285582396593525f, -0.3967382283601348f, -0.03992559172921793f, 0, 63.72958762196502f,
                        -0.16404339962244616f, 1.0835251566291304f, -0.05498805115633132f, 0, 24.732407896706203f,
                        -0.16786010706155763f, -0.5603416277695248f, 1.6014850761964943f, 0, 35.62982807460946f,
                        0, 0, 0, 1, 0})),

                new Filter("F10", new ColorMatrix(new float[]{ // LSD mMatrix
                        2, -0.4f, 0.5f, 0, 0,
                        -0.5f, 2, -0.4f, 0, 0,
                        -0.4f, -0.5f, 3, 0, 0,
                        0, 0, 0, 1, 0})),
                new Filter("F11", new ColorMatrix(new float[]{ // Vintage mMatrix
                        0.6279345635605994f, 0.3202183420819367f, -0.03965408211312453f, 0, 9.651285835294123f,
                        0.02578397704808868f, 0.6441188644374771f, 0.03259127616149294f, 0, 7.462829176470591f,
                        0.0466055556782719f, -0.0851232987247891f, 0.5241648018700465f, 0, 5.159190588235296f,
                        0, 0, 0, 1, 0})),
                new Filter("F13", new ColorMatrix(new float[]{ // Browni mMatrix
                        0.5997023498159715f, 0.34553243048391263f, -0.2708298674538042f, 0, 47.43192855600873f,
                        -0.037703249837783157f, 0.8609577587992641f, 0.15059552388459913f, 0, -36.96841498319127f,
                        0.24113635128153335f, -0.07441037908422492f, 0.44972182064877153f, 0, -7.562075277591283f,
                        0, 0, 0, 1, 0})),
                new Filter("BW01", new ColorMatrix(new float[]{
                        1, 0, 0, 0, 0,
                        1, 0, 0, 0, 0,
                        1, 0, 0, 0, 0,
                        0, 0, 0, 1, 0})),
                new Filter("BW02", new ColorMatrix(new float[]{
                        0, 1, 0, 0, 0,
                        0, 1, 0, 0, 0,
                        0, 1, 0, 0, 0,
                        0, 0, 0, 1, 0})),
                new Filter("BW03", new ColorMatrix(new float[]{
                        0, 0, 1, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 0, 0, 1, 0}))
        );

        filtersAdapter = new FiltersAdapter(OpenCamera1.this, abc, OpenCamera1.this);
        recyclefilter.setLayoutManager(new LinearLayoutManager(OpenCamera1.this, LinearLayoutManager.HORIZONTAL, false));
        recyclefilter.setHasFixedSize(true);
        recyclefilter.setAdapter(filtersAdapter);

        flashauto.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
        timer_off.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));

        flash_off.setOnClickListener(view -> {

            camera.setFlash(Flash.OFF);
            sharedpreference.setFlash(OpenCamera1.this, "Flash.OFF");
            // camera.setFacing();
            flash_flag = false;
            imgflash.setImageDrawable(getResources().getDrawable(R.drawable.flash));

            flashon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            flash_off.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
            flashauto.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

            YoYo.with(Techniques.ZoomOut)
                    .duration(500)
                    .onEnd(animator -> lnflash.setVisibility(View.GONE))
                    .playOn(lnflash);

        });

        imgfilter.setOnClickListener(view -> {

            if (recyclefilter.getVisibility() == View.VISIBLE) {
                recyclefilter.setVisibility(View.GONE);
                imgfilter.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            } else {
                recyclefilter.setVisibility(View.VISIBLE);
                imgfilter.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));

            }
        });


        flashauto.setOnClickListener(view -> {

            camera.setFlash(Flash.AUTO);
            sharedpreference.setFlash(OpenCamera1.this, "Flash.AUTO");
            flash_flag = false;
            imgflash.setImageDrawable(getResources().getDrawable(R.drawable.flash_auto));

            flashon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            flash_off.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            flashauto.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));

            YoYo.with(Techniques.ZoomOut)
                    .duration(500)
                    .onEnd(new YoYo.AnimatorCallback() {
                        @Override
                        public void call(Animator animator) {
                            lnflash.setVisibility(View.GONE);
                        }
                    })
                    .playOn(lnflash);

        });

        flashon.setOnClickListener(view -> {

            camera.setFlash(Flash.ON);
            sharedpreference.setFlash(OpenCamera1.this, "Flash.ON");
            flash_flag = false;
            imgflash.setImageDrawable(getResources().getDrawable(R.drawable.flash_yellow));

            flashon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
            flash_off.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            flashauto.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

            YoYo.with(Techniques.ZoomOut)
                    .duration(500)
                    .onEnd(animator -> lnflash.setVisibility(View.GONE))
                    .playOn(lnflash);

        });

        imgtimer.setOnClickListener(view -> {


            if (timer_flag) {
                timer_flag = false;


                YoYo.with(Techniques.ZoomOut)
                        .duration(500)
                        .onEnd(animator -> lntimer.setVisibility(View.GONE))
                        .playOn(lntimer);
            } else {
                if (lnflash.getVisibility() == View.VISIBLE) {
                    lnflash.setVisibility(View.GONE);
                }

                timer_flag = true;
                lntimer.setVisibility(View.VISIBLE);

                YoYo.with(Techniques.ZoomIn)
                        .duration(500)
                        .onEnd(animator -> {
                        })
                        .playOn(lntimer);

            }
        });

        timer_off.setOnClickListener(view -> {

            timer = 0;
            timer_flag = false;

            imgtimer.setImageDrawable(getResources().getDrawable(R.drawable.timer_off));

            timer_off.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
            timer_2.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            timer_5.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            timer_10.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

            YoYo.with(Techniques.ZoomOut)
                    .duration(500)
                    .onEnd(new YoYo.AnimatorCallback() {
                        @Override
                        public void call(Animator animator) {
                            lntimer.setVisibility(View.GONE);
                        }
                    })
                    .playOn(lntimer);

        });

        timer_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timer = 2;
                timer_flag = false;
                imgtimer.setImageDrawable(getResources().getDrawable(R.drawable.timer2));

                timer_off.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                timer_2.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
                timer_5.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                timer_10.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                YoYo.with(Techniques.ZoomOut)
                        .duration(500)
                        .onEnd(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                lntimer.setVisibility(View.GONE);
                            }
                        })
                        .playOn(lntimer);

            }
        });


        timer_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timer = 5;
                timer_flag = false;
                imgtimer.setImageDrawable(getResources().getDrawable(R.drawable.timer5));

                timer_off.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                timer_2.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                timer_5.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
                timer_10.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                YoYo.with(Techniques.ZoomOut)
                        .duration(500)
                        .onEnd(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                lntimer.setVisibility(View.GONE);
                            }
                        })
                        .playOn(lntimer);

            }
        });

        timer_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timer = 10;
                timer_flag = false;
                imgtimer.setImageDrawable(getResources().getDrawable(R.drawable.timer10));

                timer_off.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                timer_2.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                timer_5.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                timer_10.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));

                YoYo.with(Techniques.ZoomOut)
                        .duration(500)
                        .onEnd(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                lntimer.setVisibility(View.GONE);
                            }
                        })
                        .playOn(lntimer);


            }
        });

        imgflash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flash_flag) {
                    flash_flag = false;

                    YoYo.with(Techniques.ZoomOut)
                            .duration(500)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    lnflash.setVisibility(View.GONE);
                                }
                            })
                            .playOn(lnflash);
                } else {
                    if (lntimer.getVisibility() == View.VISIBLE) {
                        lntimer.setVisibility(View.GONE);
                    }

                    flash_flag = true;
                    lnflash.setVisibility(View.VISIBLE);

                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                }
                            })
                            .playOn(lnflash);

                }

            }
        });

//        imgCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(OpenCamera1.this, BottombarActivity.class);
//                i.putExtra("type", "home");
//                startActivity(i);
//                overridePendingTransition(RM.anim.slide_in_up, RM.anim.slide_out_up);
//            }
//        });

        imggallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

            }
        });

        imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/* video/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }
        });

        leftoption.setIconAnimated(false);
        leftoption.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {

                if (opened) {
                    leftoption.getMenuIconView().animate().rotation(180).start();
                    leftoption.getMenuIconView().setImageDrawable(getResources().getDrawable(R.drawable.down_arrow1));
                } else {
                    leftoption.getMenuIconView().animate().rotation(360).start();
                    leftoption.getMenuIconView().setImageDrawable(getResources().getDrawable(R.drawable.down_arrow1));
                }
            }
        });


        rightoption.setIconAnimated(false);
        rightoption.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {

                if (opened) {
                    rightoption.getMenuIconView().animate().rotation(180).start();
                    rightoption.getMenuIconView().setImageDrawable(getResources().getDrawable(R.drawable.down_arrow1));
                } else {
                    rightoption.getMenuIconView().animate().rotation(360).start();
                    rightoption.getMenuIconView().setImageDrawable(getResources().getDrawable(R.drawable.down_arrow1));
                }
            }
        });

        imgCamera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.setMode(Mode.PICTURE);
                if (ll_flux_select.getVisibility()==View.VISIBLE)
                    ll_flux_select.setVisibility(View.GONE);
                if (ll_video_select.getVisibility()==View.VISIBLE)
                    ll_video_select.setVisibility(View.GONE);
                if (ll_3d.getVisibility()==View.GONE)
                    ll_3d.setVisibility(View.VISIBLE);
                tv_style.setText("While");
                if (img_video.getVisibility()==View.GONE)
                    img_video.setVisibility(View.VISIBLE);
                if (imgVideo.getVisibility()==View.VISIBLE)
                    imgVideo.setVisibility(View.GONE);
                if (imgcamera.getVisibility()==View.GONE)
                    imgcamera.setVisibility(View.VISIBLE);
                if (img3d.getVisibility()==View.VISIBLE)
                    img3d.setVisibility(View.GONE);
            }
        });

        imgCamera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.setMode(Mode.PICTURE);
                if (ll_flux_select.getVisibility()==View.VISIBLE)
                    ll_flux_select.setVisibility(View.GONE);
                if (ll_video_select.getVisibility()==View.VISIBLE)
                    ll_video_select.setVisibility(View.GONE);
                if (ll_3d.getVisibility()==View.GONE)
                    ll_3d.setVisibility(View.VISIBLE);
                tv_style.setText("While");
                if (img_video.getVisibility()==View.GONE)
                    img_video.setVisibility(View.VISIBLE);
                if (imgVideo.getVisibility()==View.VISIBLE)
                    imgVideo.setVisibility(View.GONE);
                if (imgcamera.getVisibility()==View.GONE)
                    imgcamera.setVisibility(View.VISIBLE);
                if (img3d.getVisibility()==View.VISIBLE)
                    img3d.setVisibility(View.GONE);

            }
        });

        img_video1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.setMode(Mode.VIDEO);

                ll_flux_select.setVisibility(View.GONE);
                tv_style.setText("Video");
                if (imgcamera.getVisibility()==View.VISIBLE){
                    imgcamera.setVisibility(View.GONE);
                    ll_3d.setVisibility(View.GONE);
                    imgVideo.setVisibility(View.VISIBLE);
                    ll_video_select.setVisibility(View.VISIBLE);
                    img_video.setVisibility(View.GONE);
                } else if (img3d.getVisibility()==View.VISIBLE){
                    img3d.setVisibility(View.GONE);
                    ll_3d.setVisibility(View.GONE);
                    imgVideo.setVisibility(View.VISIBLE);
                    ll_video_select.setVisibility(View.VISIBLE);
                    img_video.setVisibility(View.GONE);
                }
            }
        });

        img_video2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.setMode(Mode.VIDEO);
                tv_style.setText("Flux");
                ll_video_select.setVisibility(View.GONE);
                if (imgcamera.getVisibility()==View.VISIBLE){
                    imgcamera.setVisibility(View.GONE);
                    img_video.setVisibility(View.GONE);
                    img3d.setVisibility(View.VISIBLE);
                    ll_flux_select.setVisibility(View.VISIBLE);
                    ll_3d.setVisibility(View.GONE);
                } else if (imgVideo.getVisibility()==View.VISIBLE){
                    imgVideo.setVisibility(View.GONE);
                    img_video.setVisibility(View.GONE);
                    img3d.setVisibility(View.VISIBLE);
                    ll_flux_select.setVisibility(View.VISIBLE);
                    ll_3d.setVisibility(View.GONE);
                }
            }
        });

        ll_3d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.setMode(Mode.VIDEO);

                tv_style.setText("Flux");
                ll_video_select.setVisibility(View.GONE);
                if (imgcamera.getVisibility()==View.VISIBLE){
                    imgcamera.setVisibility(View.GONE);
                    img_video.setVisibility(View.GONE);
                    img3d.setVisibility(View.VISIBLE);
                    ll_flux_select.setVisibility(View.VISIBLE);
                    ll_3d.setVisibility(View.GONE);
                } else if (imgVideo.getVisibility()==View.VISIBLE){
                    imgVideo.setVisibility(View.GONE);
                    img_video.setVisibility(View.GONE);
                    img3d.setVisibility(View.VISIBLE);
                    ll_flux_select.setVisibility(View.VISIBLE);
                    ll_3d.setVisibility(View.GONE);
                }
            }
        });

        img_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.setMode(Mode.VIDEO);

                tv_style.setText("Video");
                ll_flux_select.setVisibility(View.GONE);
                if (imgcamera.getVisibility()==View.VISIBLE){
                    imgcamera.setVisibility(View.GONE);
                    ll_3d.setVisibility(View.GONE);
                    imgVideo.setVisibility(View.VISIBLE);
                    ll_video_select.setVisibility(View.VISIBLE);
                    img_video.setVisibility(View.GONE);
                } else if (img3d.getVisibility()==View.VISIBLE){
                    img3d.setVisibility(View.GONE);
                    ll_3d.setVisibility(View.GONE);
                    imgVideo.setVisibility(View.VISIBLE);
                    ll_video_select.setVisibility(View.VISIBLE);
                    img_video.setVisibility(View.GONE);
                }
            }
        });

        imgVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is3d = false;
                captureVideo(false);
            }
        });

        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.stopVideo();
            }
        });

        img3d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is3d = true;
                captureVideo(true);
            }
        });

        imgcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.setMode(Mode.PICTURE);
                //capturePictureSnapshot();
                if (timer == 0) {
                    capturePictureSnapshot();
                    //capturePicture();
                } else {
                    txttimer.setVisibility(View.VISIBLE);
                    txttimer.setText(timer + "");
                    waitTimer = new CountDownTimer(timer * 1000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                            txttimer.setText(seconds + "");
                        }

                        public void onFinish() {
                            timer = 0;
                            // capturePicture();
                            capturePictureSnapshot();
                            txttimer.setVisibility(View.GONE);
                        }
                    }.start();

                }

            }
        });

        rl_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera.getGrid() == Grid.DRAW_3X3) {
                    camera.setGrid(Grid.OFF);
                } else {
                    camera.setGrid(Grid.DRAW_3X3);
                }
            }
        });

        imggrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (camera.getGrid() == Grid.DRAW_3X3) {
                    camera.setGrid(Grid.OFF);
                } else {
                    camera.setGrid(Grid.DRAW_3X3);
                }


            }
        });

//        camera.getSnapshotSize().getHeight();

        //  camera.setOnTouchListener(this);

//        camera.setFacing(camera.getFacing());

        if (USE_FRAME_PROCESSOR) {
            camera.addFrameProcessor(new FrameProcessor() {
                private long lastTime = System.currentTimeMillis();

                @Override

                public void process(@NonNull Frame frame) {

                    byte[] data = frame.getData();
                    int rotation = frame.getRotation();
                    long time = frame.getTime();
                    Size size = frame.getSize();
                    int format = frame.getFormat();

//                Log.e("LLLL_Frame :", "rotation: " + rotation + "    size: " + size + "     formate" + format);

                    long newTime = frame.getTime();
                    long delay = newTime - lastTime;
                    lastTime = newTime;
//                Log.e("Frame delayMillis:", delay + "FPS:" + 1000 / delay);
                    if (DECODE_BITMAP) {
                        YuvImage yuvImage = new YuvImage(frame.getData(), ImageFormat.NV21,
                                frame.getSize().getWidth(),
                                frame.getSize().getHeight(),
                                null);
                        ByteArrayOutputStream jpegStream = new ByteArrayOutputStream();
                        yuvImage.compressToJpeg(new Rect(0, 0,
                                frame.getSize().getWidth(),
                                frame.getSize().getHeight()), 100, jpegStream);
                        byte[] jpegByteArray = jpegStream.toByteArray();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(jpegByteArray, 0, jpegByteArray.length);
                        //noinspection ResultOfMethodCallIgnored
                        bitmap.toString();
                    }
                }
            });
        }

//        controlPanel = findViewById(RM.id.controls);
//        ViewGroup group = (ViewGroup) controlPanel.getChildAt(0);
        final View watermark = findViewById(R.id.watermark);

        imgflip.setOnClickListener(view -> toggleCamera());
//        List<Option<?>> options = Arrays.asList(
//                // Layout
//                new Option.Width(), new Option.Height(),
//                // Engine and preview
//                new Option.Mode(), new Option.Engine(), new Option.Preview(),
//                // Some controls
//                new Option.Flash(), new Option.WhiteBalance(), new Option.Hdr(),
//                new Option.PictureMetering(), new Option.PictureSnapshotMetering(),
//                // Video recording
//                new Option.VideoCodec(), new Option.Audio(),
//                // Gestures
//                new Option.Pinch(), new Option.HorizontalScroll(), new Option.VerticalScroll(),
//                new Option.Tap(), new Option.LongTap(),
//                // Watermarks
//                new Option.OverlayInPreview(watermark),
//                new Option.OverlayInPictureSnapshot(watermark),
//                new Option.OverlayInVideoSnapshot(watermark),
//                // Other
//                new Option.Grid(), new Option.GridColor(), new Option.UseDeviceOrientation()
//        );
//        List<Boolean> dividers = Arrays.asList(
//                false, true,
//                false, false, true,
//                false, false, false, false, true,
//                false, true,
//                false, false, false, false, true,
//                false, false, true,
//                false, false, true
//        );
//        for (int i = 0; i < options.size(); i++) {
//            OptionView view = new OptionView(this);
//            //noinspection unchecked
//            view.setOption(options.get(i), this);
//            view.setHasDivider(dividers.get(i));
//            group.addView(view,
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//        }
//
//        controlPanel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                BottomSheetBehavior b = BottomSheetBehavior.from(controlPanel);
//                b.setState(BottomSheetBehavior.STATE_HIDDEN);
//            }
//        });

        // Animate the watermark just to show we record the animation in video snapshots
        ValueAnimator animator = ValueAnimator.ofFloat(1F, 0.8F);
        animator.setDuration(300);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (float) animation.getAnimatedValue();
                watermark.setScaleX(scale);
                watermark.setScaleY(scale);
                watermark.setRotation(watermark.getRotation() + 2);
            }
        });
        animator.start();


//        imgfeed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(OpenCamera1.this, BottombarActivity.class);
//                i.putExtra("type", "home");
//                startActivity(i);
//                overridePendingTransition(RM.anim.slide_in_up, RM.anim.slide_out_up);
//            }
//        });
        imgprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(OpenCamera1.this, ProfilePage.class);
                intent.putExtra("type", "left");
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);

               /* BottombarActivity.camera_bottom = "profile";
                finish();
                overridePendingTransition(RM.anim.stay, RM.anim.slide_down);*/
            }
        });

    }

    private void captureVideo(boolean is3d) {
        if (camera.getMode() == Mode.PICTURE) {
            message("Can't record HQ videos while in PICTURE mode.", false);
            return;
        }
        if (camera.isTakingPicture() || camera.isTakingVideo()) return;
        if (!is3d)
            camera.takeVideo(new File(getFilesDir(), "video.mp4"));
        else
            camera.takeVideo(new File(getFilesDir(), "video.mp4"),3000);
    }

    private void captureVideoSnapshot() {
        if (camera.isTakingVideo()) {
            return;
        }
        if (camera.getPreview() != Preview.GL_SURFACE) {
            return;
        }
        camera.takeVideoSnapshot(new File(getFilesDir(), "video.mp4"), 5000);
    }

    public void displayMessage(String message) {
    }

    private void message(@NonNull String content, boolean important) {
        if (important) {
            LOG.w(content);
        } else {
            LOG.e(content);
        }
    }

    public void changeCurrentFilter(int pos) {
        Log.d("mn13postion", pos + "");
        Log.d("mn13position1", mAllFilters.length + "");
        if (camera.getPreview() != Preview.GL_SURFACE) {
            return;
        }
       /* if (mCurrentFilter < mAllFilters.length - 1) {
            mCurrentFilter++;
        } else {
            mCurrentFilter = 0;
        }*/
        /*if (pos < mAllFilters.length - 1) {
            position = pos + 1;
        } else {
            position = 0;
        }*/
        int position = 0;

        if (pos == 0) {
            position = 0;
        } else if (pos == 1) {
            position = 1;
        } else {
            position = pos - 1;
        }

        Filters filter = mAllFilters[position];
        //filter.newInstance().getFragmentShader()
        camera.setFilter(filter.newInstance());
        filter.newInstance().getVertexShader();

        message(filter.toString(), false);

        // To test MultiFilter:
        // DuotoneFilter duotone = new DuotoneFilter();
        // duotone.setFirstColor(Color.RED);
        // duotone.setSecondColor(Color.GREEN);
        // camera.setFilter(new MultiFilter(duotone, filter.newInstance()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void toggleCamera() {
        if (camera.isTakingPicture() || camera.isTakingVideo()) return;
        switch (camera.toggleFacing()) {
            case BACK:
                break;

            case FRONT:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean valid = true;
        for (int grantResult : grantResults) {
            valid = valid && grantResult == PackageManager.PERMISSION_GRANTED;
        }
        if (valid && !camera.isOpened()) {
            camera.open();
        }
    }

    private void capturePictureSnapshot() {
        if (camera.isTakingPicture()) return;
        if (camera.getPreview() != Preview.GL_SURFACE) {
            message("Picture snapshots are only allowed with the GL_SURFACE preview.", true);
            return;
        }
        mCaptureTime = System.currentTimeMillis();
        message("Capturing picture snapshot...", false);
        if (sharedpreference.getFlash(OpenCamera1.this).equalsIgnoreCase(String.valueOf(Flash.OFF))) {
            camera.setFlash(Flash.OFF);
        } else if (sharedpreference.getFlash(OpenCamera1.this).equalsIgnoreCase(String.valueOf(Flash.AUTO))) {
            camera.setFlash(Flash.AUTO);
        } else
            camera.setFlash(Flash.ON);

        camera.setPictureMetering(true);
        camera.takePictureSnapshot();
    }

    private void capturePicture() {
        if (camera.getMode() == Mode.VIDEO) {
            message("Can't take HQ pictures while in VIDEO mode.", false);
            return;
        }
        if (camera.isTakingPicture()) return;
        mCaptureTime = System.currentTimeMillis();
        message("Capturing picture...", false);
        camera.takePicture();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Log.e("LLLLLLL_URI: ",getFileName(data.getData())+" "+getPath(data.getData()));
                    if (!getFileName(data.getData()).contains(".mp4")) {
                        Intent intent = new Intent(OpenCamera1.this, EditorActivity.class);
                        intent.putExtra("imgUrl", sharedpreference.getphoto(OpenCamera1.this));
                        intent.setData(data.getData());
                        startActivity(intent);
                    } else {
                        File file = new File(data.getData().getPath());
                        Intent intent = new Intent(OpenCamera1.this, PostVideoActivity.class);
                        intent.putExtra("File",getPath(data.getData()));
                        startActivity(intent);
                    }
                    overridePendingTransition(R.anim.fade_in, R.anim.stay);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(OpenCamera1.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open(1);
        mCamera.setFaceDetectionListener(faceDetectionListener);
        mCamera.startFaceDetection();
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (Exception e) {
            Log.e("LLLLLLL", "Could not preview the image.", e);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // We have no surface, return immediately:
        if (holder.getSurface() == null) {
            return;
        }
        // Try to stop the current preview:
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // Ignore...
        }
        // Get the supported preview sizes:
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size previewSize = previewSizes.get(0);
        // And set them:
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        mCamera.setParameters(parameters);
        // Now set the display orientation for the camera. Can we do this differently?
        mDisplayRotation = FaceUtils.getDisplayRotation(OpenCamera1.this);
        mDisplayOrientation = FaceUtils.getDisplayOrientation(mDisplayRotation, 0);
        mCamera.setDisplayOrientation(mDisplayOrientation);

        if (mFaceView != null) {
            mFaceView.setDisplayOrientation(mDisplayOrientation);
        }

        // Finally start the camera preview again:
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.setPreviewCallback(null);
        mCamera.setFaceDetectionListener(null);
        mCamera.setErrorCallback(null);
        mCamera.release();
        mCamera = null;
    }

    private class SimpleOrientationEventListener extends OrientationEventListener {

        public SimpleOrientationEventListener(Context context) {
            super(context, SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            // We keep the last known orientation. So if the user first orient
            // the camera then point the camera to floor or sky, we still have
            // the correct orientation.
            if (orientation == ORIENTATION_UNKNOWN) return;
            mOrientation = FaceUtils.roundOrientation(orientation, mOrientation);
            // When the screen is unlocked, display rotation may change. Always
            // calculate the up-to-date orientationCompensation.
            int orientationCompensation = mOrientation
                    + FaceUtils.getDisplayRotation(OpenCamera1.this);
            if (mOrientationCompensation != orientationCompensation) {
                mOrientationCompensation = orientationCompensation;
                mFaceView.setOrientation(mOrientationCompensation);
            }

        }
    }

    private class Listener extends CameraListener {

        @Override
        public void onCameraOpened(@NonNull CameraOptions options) {
            Log.d("mn13option", options.toString());


                /*YoYo.with(Techniques.FadeIn)
                        .duration(500)
                        .playOn(camera);*/
            AnimatorSet set;
            camera.setCameraDistance(10 * camera.getWidth());
            set = (AnimatorSet) AnimatorInflater.loadAnimator(OpenCamera1.this, com.otaliastudios.cameraview.R.anim.flipping);
            set.setTarget(camera);
            set.setDuration(500);
            set.start();

            int cameraid = 0;
            if (camera.getFacing() == Facing.BACK) {
                cameraid = 0;
            } else if (camera.getFacing() == Facing.FRONT) {
                cameraid = 1;
            }

            Log.d("mn13camera", cameraid + ":");
           /* mDisplayRotation = FaceUtils.getDisplayRotation(OpenCamera1.this);
            mDisplayOrientation = FaceUtils.getDisplayOrientation(mDisplayRotation, cameraid);


            if (mFaceView != null) {
                mFaceView.setDisplayOrientation(mDisplayOrientation);
            }*/

            if (!flag) {
                flag = true;
            }
        }

        @Override
        public void onOrientationChanged(int orientation) {
            super.onOrientationChanged(orientation);
            Log.d("mn13option1", orientation + "");

        }

        @Override
        public void onCameraError(@NonNull CameraException exception) {
            super.onCameraError(exception);
            message("Got CameraException #" + exception.getReason(), true);
        }

        @Override
        public void onPictureTaken(@NonNull PictureResult result) {
            super.onPictureTaken(result);

//            if (camera.isTakingVideo()) {
//                message("Captured while taking video. Size=" + result.getSize(), false);
//                return;
//            }

            long callbackTime = System.currentTimeMillis();
            if (mCaptureTime == 0) mCaptureTime = callbackTime - 300;
            LOG.w("onPictureTaken called! Launching activity. Delay:", callbackTime - mCaptureTime);

            String filePath = "";
            FileOutputStream outStream;
            try {
                File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
                if (!wallpaperDirectory.exists()) {
                    wallpaperDirectory.mkdirs();
                }

                try {
                    File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
                    f.createNewFile();
                    f.getAbsoluteFile();
                    //selectedimage = f;
                    Log.e("selectedfile", f + "");
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(result.getData());
                    MediaScannerConnection.scanFile(OpenCamera1.this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
                    fo.close();
                    filePath = f.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                    //    Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                MediaActionSound sound = new MediaActionSound();
//                sound.play(MediaActionSound.SHUTTER_CLICK);

                //Toast.makeText(OpenCamera.this, "Picture Saved: " + "", Toast.LENGTH_LONG).show();

                Log.e("LLLL_filepath: ", filePath);

                    Intent intent = new Intent(OpenCamera1.this, EditorActivity.class);
                    intent.setData(Uri.fromFile(new File(filePath)));
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.stay);

               /* Intent intent = new Intent(OpenCamera1.this, PreviewActivity.class);
                intent.putExtra(PreviewActivity.IMAGE_PATH, filePath);
                startActivity(intent);*/


            } catch (Exception e) {

            }

            mCaptureTime = 0;
            LOG.w("onPictureTaken called! Launched activity.");
        }


        @Override
        public void onVideoTaken(@NonNull VideoResult result) {
            super.onVideoTaken(result);
            Log.e("LLLLLLL_Video: ", "taken");
            LOG.w("onVideoTaken called! Launching activity.");
            if (dot.getVisibility() == View.VISIBLE)
                dot.setVisibility(View.GONE);
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + "/Video");
            File f = new File(wallpaperDirectory, "VID_"+Calendar.getInstance().getTimeInMillis() + ".mp4");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P){

                new AsyncTask<Void, Void, String>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
//                    if (hud!=null || hud.isShowing()){
//                        hud.dismiss();
//                    }
//                    hud = KProgressHUD.create(CreatePostActivity.this)
//                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                            .setDimAmount(0.5f)
//                            .setCancellable(true)
//                            .show();
                    }

                    @Override
                    protected String doInBackground( Void... voids ) {
                        //Do things...
                        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + "/Video");
                        File f = new File(wallpaperDirectory, "VID_"+ Calendar.getInstance().getTimeInMillis() + ".mp4");

                        new VideoCompressor().compressVideo(result.getFile().getAbsolutePath(), f.getAbsolutePath());


                        return f.getAbsolutePath();
                    }

                    @Override
                    protected void onPostExecute(String compressFile) {
                        super.onPostExecute(compressFile);

//                    hud.dismiss();
                        File f = new File(compressFile);

                        float length = f.length() / 1024f; // Size in KB
                        String value;
                        if (length >= 1024)
                            value = length / 1024f + " MB";
                        else
                            value = length + " KB";
                        String text = String.format(Locale.US, "Output: \nName:"+f.getName()+" \nSize: "+ value);
                        Log.e("LLLLL_End: ",text);

                        float length1 = result.getFile().length() / 1024f;
                        String value1;
                        if (length1 >= 1024)
                            value1 = length1 / 1024f + " MB";
                        else
                            value1 = length1 + " KB";
                        String text1 = String.format(Locale.US, "Input: \nName:"+result.getFile().getName()+" \nSize: "+ value1);
                        Log.e("LLLLL_End: ",text1);

                        if (!is3d) {
                            Intent intent = new Intent(OpenCamera1.this, PostVideoActivity.class);
                            intent.putExtra("File", f.getAbsolutePath());
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.stay);
                        } else {
                            Intent intent = new Intent(OpenCamera1.this, Post3dVideoActivity.class);
                            intent.putExtra("File", f.getAbsolutePath());
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.stay);
                        }

                    }
                }.execute();

            } else {

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        new VideoCompressor().compressVideo(result.getFile().getAbsolutePath(), f.getAbsolutePath());
                        float length = f.length() / 1024f; // Size in KB
                        String value;
                        if (length >= 1024)
                            value = length / 1024f + " MB";
                        else
                            value = length + " KB";
                        String text = String.format(Locale.US, "Output: \nName:" + f.getName() + " \nSize: " + value);
                        Log.e("LLLLL_End: ", text);

                        float length1 = result.getFile().length() / 1024f;
                        String value1;
                        if (length1 >= 1024)
                            value1 = length1 / 1024f + " MB";
                        else
                            value1 = length1 + " KB";
                        String text1 = String.format(Locale.US, "Input: \nName:" + result.getFile().getName() + " \nSize: " + value1);
                        Log.e("LLLLL_End: ", text1);

                        if (!is3d) {
                            Intent intent = new Intent(OpenCamera1.this, PostVideoActivity.class);
                            intent.putExtra("File", f.getAbsolutePath());
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.stay);
                        } else {
                            Intent intent = new Intent(OpenCamera1.this, Post3dVideoActivity.class);
                            intent.putExtra("File", f.getAbsolutePath());
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.stay);
                        }
                    }
                }.start();
            }
//            runOnUiThread(() -> GiraffeCompressor.create() //two implementations: mediacodec and ffmpeg,default is mediacodec
//                    .input(result.getFile()) //set video to be compressed
//                    .output(f) //set compressed video output
//                    .bitRate(Integer.parseInt("2073600"))//set bitrate
//                    .resizeFactor(Float.parseFloat("1.0"))//set video resize factor
//                    .ready()
//                    .subscribe(new Subscriber<GiraffeCompressor.Result>() {
//                        @Override
//                        public void onCompleted() {
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            Log.e("LLLLLLL_FileSie_E: ",e.getMessage());
//                            e.printStackTrace();
//
//                        }
//
//                        @Override
//                        public void onNext(GiraffeCompressor.Result s) {
//                            String msg = String.format("compress completed \ntake time:%s \nout put file:%s", s.getCostTime(), s.getOutput());
//                            msg = msg + "\ninput file size:"+ Formatter.formatFileSize(getApplication(),result.getFile().length());
//                            msg = msg + "\nout file size:"+ Formatter.formatFileSize(getApplication(),new File(s.getOutput()).length());
//                            Log.e("LLLLLLL_FileSie: ",msg);
//
//                            if (!is3d) {
//                                Intent intent = new Intent(OpenCamera1.this, PostVideoActivity.class);
//                                intent.putExtra("File",new File(s.getOutput()).getAbsolutePath());
//                                startActivity(intent);
//                                overridePendingTransition(R.anim.fade_in, R.anim.stay);
//                            } else {
//                                Intent intent = new Intent(OpenCamera1.this, Post3dVideoActivity.class);
//                                intent.putExtra("File",new File(s.getOutput()).getAbsolutePath());
//                                startActivity(intent);
//                                overridePendingTransition(R.anim.fade_in, R.anim.stay);
//                            }
//
//                        }
//                    }));
            LOG.w("onVideoTaken called! Launched activity.");
        }

        @Override
        public void onVideoRecordingStart() {
            super.onVideoRecordingStart();
            Log.e("LLLLLLL_Video: ", "start");
            if (dot.getVisibility() == View.GONE)
                dot.setVisibility(View.VISIBLE);
            LOG.w("onVideoRecordingStart!");
        }

        @Override
        public void onVideoRecordingEnd() {
            super.onVideoRecordingEnd();
            Log.e("LLLLLLL_Video: ", "end");
            message("Video taken. Processing...", false);
            LOG.w("onVideoRecordingEnd!");
        }

        @Override
        public void onExposureCorrectionChanged(float newValue, @NonNull float[] bounds, @Nullable PointF[] fingers) {
            super.onExposureCorrectionChanged(newValue, bounds, fingers);
            message("Exposure correction:" + newValue, false);
        }


        @Override
        public void onZoomChanged(float newValue, @NonNull float[] bounds, @Nullable PointF[] fingers) {
            super.onZoomChanged(newValue, bounds, fingers);
            message("Zoom:" + newValue, false);
        }

    }
}
