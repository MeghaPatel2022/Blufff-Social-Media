package com.TBI.Client.Bluff.Activity.Post;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.ColorMatrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaActionSound;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Adapter.FiltersAdapter;
import com.TBI.Client.Bluff.Adapter.Post.Color_Adapter;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.KeyboardUtils;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.SwipeDismissBaseActivity;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.airbnb.lottie.LottieAnimationView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraLogger;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.VideoResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.controls.Grid;
import com.otaliastudios.cameraview.controls.Preview;
import com.otaliastudios.cameraview.filter.Filters;

import net.iquesoft.iquephoto.models.Filter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.TBI.Client.Bluff.Activity.Profile.EditProfile.PERMISSION_ALL;
import static com.TBI.Client.Bluff.Utils.Public_Function.hasPermissions;

public class CreateStory extends SwipeDismissBaseActivity {

    private final static CameraLogger LOG = CameraLogger.create("DemoApp");
    private final static boolean USE_FRAME_PROCESSOR = false;
    private final static boolean DECODE_BITMAP = true;
    private static final String IMAGE_DIRECTORY = "/Blufff";
    public static EditText edttext;
    public static Drawable gradintdrawable;
    private final Filters[] mAllFilters = Filters.values();
    @BindView(R.id.relcamera)
    RelativeLayout relcamera;
    @BindView(R.id.txttimer)
    TextView txttimer;
    @BindView(R.id.lnclick)
    LinearLayout lnclick;
    @BindView(R.id.recyclefilter)
    RecyclerView recyclefilter;
    @BindView(R.id.imgfilter)
    ImageView imgfilter;
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
    @BindView(R.id.imgclick)
    LottieAnimationView imgclick;
    @BindView(R.id.imggrid)
    ImageView imggrid;
    @BindView(R.id.framecreate)
    FrameLayout framecreate;
    @BindView(R.id.relativecamera)
    RelativeLayout relativecamera;
    @BindView(R.id.imgsktrech)
    ImageView imgsktrech;
    @BindView(R.id.imgclose)
    ImageView imgclose;
    @BindView(R.id.recyclecolor)
    RecyclerView recyclecolor;
    @BindView(R.id.imgborderback)
    ImageView imgborderback;
    @BindView(R.id.imgedit)
    ImageView imgedit;
    @BindView(R.id.imgback)
    ImageView imgback;
    @BindView(R.id.imgalin)
    ImageView imgalin;
    @BindView(R.id.txtfont)
    TextView txtfont;
    @BindView(R.id.imggradint)
    ImageView imggradint;
    @BindView(R.id.frameeditimage)
    FrameLayout frameeditimage;
    @BindView(R.id.lngravity)
    FrameLayout lngravity;
    @BindView(R.id.frameadd)
    FrameLayout frameadd;
    boolean flag = false, flash_flag = false, timer_flag = false;
    int timer = 0;
    CountDownTimer waitTimer;

    FiltersAdapter filtersAdapter;
    List<Integer> fontarray = new ArrayList<Integer>();
    List<Drawable> gradintarray = new ArrayList<Drawable>();
    int fontposition = 0, gradintcolor = 0, backgroundpostiion = 0;
    boolean aBoolean = false;
    int align = 0;
    Color_Adapter color_adapter;
    ArrayList<Integer> colorarray = new ArrayList<Integer>();
    Integer coloback;
    private CameraView camera;
    private long mCaptureTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (sharedpreference.getTheme(CreateStory.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storycreate);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(CreateStory.this));
        edttext = findViewById(R.id.edttext);
        coloback = R.color.white;

        colorarray.add(R.color.color1);
        colorarray.add(R.color.color2);
        colorarray.add(R.color.color3);
        colorarray.add(R.color.color4);
        colorarray.add(R.color.color5);
        colorarray.add(R.color.color6);
        colorarray.add(R.color.color7);
        colorarray.add(R.color.color8);
        colorarray.add(R.color.color9);
        colorarray.add(R.color.color10);
        colorarray.add(R.color.color11);
        colorarray.add(R.color.color12);
        colorarray.add(R.color.color13);
        colorarray.add(R.color.color14);
        colorarray.add(R.color.color15);
        colorarray.add(R.color.color16);
        colorarray.add(R.color.color17);
        colorarray.add(R.color.color18);
        colorarray.add(R.color.color19);
        colorarray.add(R.color.color20);
        colorarray.add(R.color.color21);
        colorarray.add(R.color.color22);

        color_adapter = new Color_Adapter(CreateStory.this, colorarray);
        recyclecolor.setLayoutManager(new LinearLayoutManager(CreateStory.this, LinearLayoutManager.HORIZONTAL, false));
        recyclecolor.setAdapter(color_adapter);

        recyclecolor.addOnScrollListener(new CustomScrollListener());
        recyclefilter.addOnScrollListener(new CustomScrollListener());
        fontarray.add(R.font.classic);
        fontarray.add(R.font.modern);
        fontarray.add(R.font.neon);
        fontarray.add(R.font.type_writer);
        fontarray.add(R.font.solid);

       /* gradintarray.add(new Gradintcolor(Color.parseColor("#C544F0"), Color.parseColor("#5048F3")));
        gradintarray.add(new Gradintcolor(Color.parseColor("#00C9FF"), Color.parseColor("#92FE9D")));
        gradintarray.add(new Gradintcolor(Color.parseColor("#ee9ca7"), Color.parseColor("#ffdde1")));
        gradintarray.add(new Gradintcolor(Color.parseColor("#556270"), Color.parseColor("#FF6B6B")));
        gradintarray.add(new Gradintcolor(Color.parseColor("#DD2476"), Color.parseColor("#FF512F")));
        gradintarray.add(new Gradintcolor(Color.parseColor("#C6FFDD"), Color.parseColor("#f7797d")));
        gradintarray.add(new Gradintcolor(Color.parseColor("#f7797d"), Color.parseColor("#355C7D")));
        gradintarray.add(new Gradintcolor(Color.parseColor("#fc00ff"), Color.parseColor("#00dbde")));
*/

        gradintarray.add(getResources().getDrawable(R.drawable.grad1));
        gradintarray.add(getResources().getDrawable(R.drawable.grad2));
        gradintarray.add(getResources().getDrawable(R.drawable.grad3));
        gradintarray.add(getResources().getDrawable(R.drawable.grad4));
        gradintarray.add(getResources().getDrawable(R.drawable.grad5));
        gradintarray.add(getResources().getDrawable(R.drawable.grad6));
        gradintarray.add(getResources().getDrawable(R.drawable.grad7));
        gradintarray.add(getResources().getDrawable(R.drawable.grad8));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Typeface myFont = getResources().getFont(fontarray.get(0));
            txtfont.setTypeface(myFont);
            edttext.setTypeface(myFont);
            txtfont.setText("Classic");
        }

        framecreate.setBackground(gradintarray.get(0));
        gradintdrawable = gradintarray.get(0);

        imggradint.setClipToOutline(true);
        frameadd.setClipToOutline(true);
        imggradint.setBackground(gradintarray.get(0));

        framecreate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        imggradint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (gradintcolor == gradintarray.size() - 1) {
                    gradintcolor = 0;
                } else {
                    gradintcolor++;
                }

                Log.d("mn13gradint:", gradintarray.get(gradintcolor).toString());
                framecreate.setBackground(gradintarray.get(gradintcolor));
                gradintdrawable = gradintarray.get(gradintcolor);
                imggradint.setBackground(gradintarray.get(gradintcolor));
            }
        });
        txtfont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (fontposition == fontarray.size() - 1) {
                    fontposition = 0;
                } else {
                    fontposition++;
                }

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Typeface myFont = getResources().getFont(fontarray.get(fontposition));
                    txtfont.setTypeface(myFont);
                    edttext.setTypeface(myFont);

                }

                if (fontposition == 0) {
                    txtfont.setText("Classic");
                } else if (fontposition == 1) {
                    txtfont.setText("Modern");
                } else if (fontposition == 2) {
                    txtfont.setText("Neon");
                } else if (fontposition == 3) {
                    txtfont.setText("Typewriter");
                } else if (fontposition == 4) {
                    txtfont.setText("Strong");
                }

            }
        });
        relcamera.setClipToOutline(true);
        camera = findViewById(R.id.camera);
        camera.setClipToOutline(true);
        camera.setLifecycleOwner(this);
        camera.addCameraListener(new Listener());

        imgsktrech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                relativecamera.setVisibility(View.GONE);
                framecreate.setVisibility(View.VISIBLE);
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (backgroundpostiion == 2) {
                    backgroundpostiion = 0;
                } else {
                    backgroundpostiion++;
                }


                if (backgroundpostiion == 0) {

                    edttext.setBackground(null);
                    edttext.setBackgroundTintList(null);
                    imgback.setImageDrawable(getResources().getDrawable(R.drawable.text_style_outline));
                    edttext.setTextColor(getResources().getColor(R.color.white));

                } else if (backgroundpostiion == 1) {

                    edttext.setBackground(getResources().getDrawable(R.drawable.edit_profile));
                    edttext.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    imgback.setImageDrawable(getResources().getDrawable(R.drawable.text_style));
                    edttext.setTextColor(getResources().getColor(R.color.black));

                } else if (backgroundpostiion == 2) {
                    edttext.setBackground(getResources().getDrawable(R.drawable.edit_profile));
                    edttext.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.whitetranspancy)));
                    imgback.setImageDrawable(getResources().getDrawable(R.drawable.text_style_transparent));
                    edttext.setTextColor(getResources().getColor(R.color.white));
                }


            }
        });


        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Public_Function.hideKeyboard(CreateStory.this);
                relativecamera.setVisibility(View.VISIBLE);
                framecreate.setVisibility(View.GONE);
            }
        });

        imgalin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (align == 2) {
                    align = 0;
                } else {
                    align++;
                }

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);


                if (align == 0) {
                    imgalin.setImageDrawable(getResources().getDrawable(R.drawable.centre_justify));
                    params.gravity = Gravity.CENTER;
                    // lngravity.setGravity(Gravity.CENTER);
                } else if (align == 1) {
                    imgalin.setImageDrawable(getResources().getDrawable(R.drawable.left_justify));
                    params.gravity = Gravity.LEFT | Gravity.CENTER;
                    // lngravity.setGravity(Gravity.LEFT|Gravity.CENTER);
                } else if (align == 2) {
                    imgalin.setImageDrawable(getResources().getDrawable(R.drawable.right_justify));
                    params.gravity = Gravity.RIGHT | Gravity.CENTER;
                    //lngravity.setGravity(Gravity.RIGHT|Gravity.CENTER);
                }

                edttext.setLayoutParams(params);
            }
        });


        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {

                if (isVisible) {
                    aBoolean = true;
                    edttext.setHint("Type something....");
                    recyclecolor.setVisibility(View.VISIBLE);
                    frameeditimage.setVisibility(View.VISIBLE);
                    imgclick.setVisibility(View.GONE);
                } else {
                    aBoolean = false;
                    edttext.setHint("Tap to type");
                    recyclecolor.setVisibility(View.GONE);
                    frameeditimage.setVisibility(View.GONE);
                    recyclecolor.setVisibility(View.GONE);
                    imgclick.setVisibility(View.VISIBLE);
                }
                Log.d("keyboard", "keyboard visible: " + isVisible);
            }
        });


        imgclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (framecreate.getVisibility() == View.VISIBLE) {


                    Intent intent = new Intent(CreateStory.this, Post_Story.class);
                    intent.putExtra("type", "gradiant");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.stay);


                } else {
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
                //capturePictureSnapshot();


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

        relclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        lnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
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
               /* new Filter("F03", new ColorMatrix(new float[]{
                        1.5f, 0, 0, 0, -40,
                        0, 1.5f, 0, 0, -40,
                        0, 0, 1.5f, 0, -40,
                        0, 0, 0, 1, 0})),*/
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

        filtersAdapter = new FiltersAdapter(CreateStory.this, abc, CreateStory.this);
        recyclefilter.setLayoutManager(new LinearLayoutManager(CreateStory.this, LinearLayoutManager.HORIZONTAL, false));
        recyclefilter.setHasFixedSize(true);
        recyclefilter.setAdapter(filtersAdapter);

        flashauto.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
        timer_off.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));

        flash_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                camera.setFlash(Flash.OFF);
                flash_flag = false;
                imgflash.setImageDrawable(getResources().getDrawable(R.drawable.flash_off));

                flashon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                flash_off.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
                flashauto.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                YoYo.with(Techniques.ZoomOut)
                        .duration(500)
                        .onEnd(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                lnflash.setVisibility(View.GONE);
                            }
                        })
                        .playOn(lnflash);

            }
        });


        imgfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (recyclefilter.getVisibility() == View.VISIBLE) {
                    recyclefilter.setVisibility(View.GONE);
                    imgfilter.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                } else {
                    recyclefilter.setVisibility(View.VISIBLE);
                    imgfilter.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));

                }
            }
        });


        flashauto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                camera.setFlash(Flash.AUTO);

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

            }
        });

        flashon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                camera.setFlash(Flash.ON);

                flash_flag = false;
                imgflash.setImageDrawable(getResources().getDrawable(R.drawable.flash_on));

                flashon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
                flash_off.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                flashauto.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                YoYo.with(Techniques.ZoomOut)
                        .duration(500)
                        .onEnd(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                lnflash.setVisibility(View.GONE);
                            }
                        })
                        .playOn(lnflash);

            }
        });

        imgtimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (timer_flag) {
                    timer_flag = false;


                    YoYo.with(Techniques.ZoomOut)
                            .duration(500)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    lntimer.setVisibility(View.GONE);
                                }
                            })
                            .playOn(lntimer);
                } else {
                    if (lnflash.getVisibility() == View.VISIBLE) {
                        lnflash.setVisibility(View.GONE);
                    }

                    timer_flag = true;
                    lntimer.setVisibility(View.VISIBLE);

                    YoYo.with(Techniques.ZoomIn)
                            .duration(500)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                }
                            })
                            .playOn(lntimer);

                }


            }
        });

        timer_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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


            }
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


        imggallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

                if (!hasPermissions(CreateStory.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(CreateStory.this, PERMISSIONS, PERMISSION_ALL);
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 1);
                }
               /* Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);*/

            }
        });


    }


    public void setcolorback(Integer integer) {
        coloback = integer;
        imgedit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(integer)));

        if (backgroundpostiion != 0) {
            edttext.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(integer)));
        } else {
            edttext.setTextColor(getResources().getColor(integer));

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (backgroundpostiion == 0) {

            edttext.setBackground(null);
            edttext.setBackgroundTintList(null);
            edttext.setTextColor(getResources().getColor(R.color.white));

        } else if (backgroundpostiion == 1) {

            edttext.setBackground(getResources().getDrawable(R.drawable.edit_profile));
            edttext.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(coloback)));
            edttext.setTextColor(getResources().getColor(R.color.black));

        } else if (backgroundpostiion == 2) {
            edttext.setBackground(getResources().getDrawable(R.drawable.edit_profile));
            edttext.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.whitetranspancy)));
            edttext.setTextColor(getResources().getColor(R.color.white));
        }

        if (gradintdrawable != null) {
            framecreate.setBackground(gradintdrawable);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getPath(selectedImageUri);

                Intent intent = new Intent(CreateStory.this, Post_Story.class);
                intent.putExtra("imagepath", selectedImagePath);
                intent.putExtra("type", "file");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
            }
        }

    }

    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
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
        camera.setFlash(Flash.ON);
        camera.setPictureMetering(true);
        camera.takePictureSnapshot();
    }

    private void message(@NonNull String content, boolean important) {
        if (important) {
            LOG.w(content);
            //Toast.makeText(this, content, Toast.LENGTH_LONG).show();
        } else {
            LOG.i(content);
            //Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
        }
    }

    public void changeCurrentFilter(int pos) {
        Log.d("mn13postion", pos + "");
        Log.d("mn13position1", mAllFilters.length + "");
        if (camera.getPreview() != Preview.GL_SURFACE) {
            message("Filters are supported only when preview is Preview.GL_SURFACE.", true);
            return;
        }

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

    private class Listener extends CameraListener {

        @Override
        public void onCameraOpened(@NonNull CameraOptions options) {
            Log.d("mn13option", options.toString());
            if (flag) {
                AnimatorSet set;
                camera.setCameraDistance(10 * camera.getWidth());
                set = (AnimatorSet) AnimatorInflater.loadAnimator(CreateStory.this, com.otaliastudios.cameraview.R.anim.flipping);
                set.setTarget(camera);
                set.setDuration(500);
                set.start();
            }
            int cameraid = 0;
            if (camera.getFacing() == Facing.BACK) {
                cameraid = 0;
            } else if (camera.getFacing() == Facing.FRONT) {
                cameraid = 1;
            }

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
            if (camera.isTakingVideo()) {
                message("Captured while taking video. Size=" + result.getSize(), false);
                return;
            }

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
                    Log.e("selectedfile", f + "");
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(result.getData());
                    MediaScannerConnection.scanFile(CreateStory.this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
                    fo.close();
                    filePath = f.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MediaActionSound sound = new MediaActionSound();
                sound.play(MediaActionSound.SHUTTER_CLICK);

                Intent intent = new Intent(CreateStory.this, Post_Story.class);
                intent.putExtra("type", "file");
                intent.putExtra("imagepath", filePath);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

            } catch (Exception e) {

            }

            mCaptureTime = 0;
            LOG.w("onPictureTaken called! Launched activity.");
        }

        @Override
        public void onVideoTaken(@NonNull VideoResult result) {
            super.onVideoTaken(result);
            LOG.w("onVideoTaken called! Launching activity.");
           /* VideoPreviewActivity.setVideoResult(result);
            Intent intent = new Intent(CameraActivity.this, VideoPreviewActivity.class);
            startActivity(intent);*/
            LOG.w("onVideoTaken called! Launched activity.");
        }

        @Override
        public void onVideoRecordingStart() {
            super.onVideoRecordingStart();
            LOG.w("onVideoRecordingStart!");
        }

        @Override
        public void onVideoRecordingEnd() {
            super.onVideoRecordingEnd();
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
