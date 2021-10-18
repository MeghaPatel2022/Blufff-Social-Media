package com.TBI.Client.Bluff.StoryStatus;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.Model.GetFeed.Image_;
import com.TBI.Client.Bluff.Model.GetFeed.Story;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.StoryStatus.glideProgressBar.DelayBitmapTransformation;
import com.TBI.Client.Bluff.StoryStatus.glideProgressBar.LoggingListener;
import com.TBI.Client.Bluff.StoryStatus.glideProgressBar.ProgressTarget;
import com.TBI.Client.Bluff.Utils.MyAsyncTaskCallback;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.TBI.Client.Bluff.Adapter.Post.Stories_Adapter.selectmodel;
import static com.TBI.Client.Bluff.Fragment.Look_Fragment.imgadd2;
import static com.TBI.Client.Bluff.Fragment.Look_Fragment.stories_adapter;


public class StatusStoriesActivity extends AppCompatActivity implements StoryStatusView.UserInteractionListener {

    public static final String STATUS_RESOURCES_KEY = "statusStoriesResources";
    public static final String STATUS_DURATION_KEY = "statusStoriesDuration";
    public static final String STATUS_DURATIONS_ARRAY_KEY = "statusStoriesDurations";
    public static final String IS_IMMERSIVE_KEY = "isImmersive";
    public static final String IS_CACHING_ENABLED_KEY = "isCaching";
    public static final String IS_TEXT_PROGRESS_ENABLED_KEY = "isText";

    private static StoryStatusView storyStatusView;
    private static boolean isTextEnabled = true;
    Story storymodel;
    @BindView(R.id.txttime)
    TextView txttime;
    @BindView(R.id.txtname)
    TextView txtname;
    @BindView(R.id.imgavtar)
    CircleImageView imgavtar;
    @BindView(R.id.imgclose)
    ImageView imgclose;
    int position = 0;
    String from = "";
    private ImageView image;
    private int counter = 0;
    private List<Image_> statusResources = new ArrayList<>();
    //    private long[] statusResourcesDuration;
    private long statusDuration;
    private boolean isImmersive = true;
    private boolean isCaching = true;
    private ProgressTarget<String, Bitmap> target;
    private boolean isSpeakButtonLongPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_stories);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(StatusStoriesActivity.this));
        ButterKnife.bind(this);
        from = getIntent().getExtras().getString("from");
        // statusResources = getIntent().getStringArrayExtra(STATUS_RESOURCES_KEY);
        storymodel = (Story) getIntent().getSerializableExtra(STATUS_RESOURCES_KEY);
        statusDuration = getIntent().getLongExtra(STATUS_DURATION_KEY, 5000L);
//        statusResourcesDuration = getIntent().getLongArrayExtra(STATUS_DURATIONS_ARRAY_KEY);
        isImmersive = getIntent().getBooleanExtra(IS_IMMERSIVE_KEY, true);
        isCaching = getIntent().getBooleanExtra(IS_CACHING_ENABLED_KEY, true);
        isTextEnabled = getIntent().getBooleanExtra(IS_TEXT_PROGRESS_ENABLED_KEY, true);

        ProgressBar imageProgressBar = findViewById(R.id.imageProgressBar);
        TextView textView = findViewById(R.id.textView);
        image = findViewById(R.id.image);

        statusResources = storymodel.getImages();

        if (storymodel != null) {
            if (!storymodel.getPhoto().equals("") && !storymodel.getPhoto().equals(null) && !storymodel.getPhoto().equals("null")) {
                Glide.with(StatusStoriesActivity.this)
                        .load(storymodel.getPhoto())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .dontAnimate()
                        .into(imgavtar);
            } else {
            }

            txtname.setText(storymodel.getUsername() + "");
            Log.d("mn13position:", position + "\n");
            for (int i = 0; i < storymodel.getImages().size(); i++) {

                if (storymodel.getImages().get(i).getSeen() == 1) {
                    position = i;
                }
            }

            if (position == storymodel.getImages().size() - 1) {
                position = 0;
                Log.d("mn13position1;", position + "");

            }

            Log.d("mn13position;", position + "");


        }
        storyStatusView = findViewById(R.id.storiesStatus);
        storyStatusView.setStoriesCount(statusResources.size());
        storyStatusView.setStoryDuration(statusDuration);
        storyStatusView.setactivity(StatusStoriesActivity.this);
        // or
        // statusView.setStoriesCountWithDurations(statusResourcesDuration);
        storyStatusView.setUserInteractionListener(this);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                storyStatusView.playStories();
            }
        });

       /* for (int i = 0; i < position; i++) {

            Log.d("mn13pos", i + "\n");
        }*/

        for (int i = 0; i < position; i++) {

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    storyStatusView.skip();
                }
            });
        }

        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
            }
        });
        target = new MyProgressTarget<>(new BitmapImageViewTarget(image), imageProgressBar, textView);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyStatusView.skip();
            }
        });
        /*new Handler().post(new Runnable() {
            @Override
            public void run() {
                storyStatusView.pause();
            }
        });*/
        target.setModel(statusResources.get(counter));
        txttime.setText(statusResources.get(counter).getStoryDate() + "");
        Glide.with(image.getContext())
                .load(target.getModel().getFilename())
                .asBitmap()
                .crossFade()
                .skipMemoryCache(!isCaching)
                .diskCacheStrategy(isCaching ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE)
                .transform(new CenterCrop(image.getContext()), new DelayBitmapTransformation(1000))
                .listener(new LoggingListener<String, Bitmap>())
                .into(target);

        // bind reverse view
        findViewById(R.id.reverse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        storyStatusView.reverse();
                    }
                });
            }
        });

        // bind skip view
        findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        storyStatusView.skip();
                    }
                });

            }
        });


        findViewById(R.id.actions).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isSpeakButtonLongPressed = true;
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        storyStatusView.pause();
                    }
                });
                return false;
            }
        });

        findViewById(R.id.actions).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.onTouchEvent(motionEvent);
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    // We're only interested in anything if our speak button is currently pressed.
                    if (isSpeakButtonLongPressed) {
                        // Do something when the button is released.
                        isSpeakButtonLongPressed = false;
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                storyStatusView.resume();
                            }
                        });
                    }
                }

                /*new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        storyStatusView.pause();
                    }
                });*/


              /*  if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            storyStatusView.pause();
                        }
                    });

                    //storyStatusView.pause();
                } else {
                    storyStatusView.resume();
                }*/
                return true;
            }
        });
    }

    @Override
    public void onNext() {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                storyStatusView.pause();
            }
        });

        ++counter;
        target.setModel(statusResources.get(counter));
        txttime.setText(statusResources.get(counter).getStoryDate() + "");
        Glide.with(getApplicationContext())
                .load(target.getModel().getFilename())
                .asBitmap()
                .crossFade()
                .centerCrop()
                .skipMemoryCache(!isCaching)
                .diskCacheStrategy(isCaching ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE)
                .transform(new CenterCrop(image.getContext()), new DelayBitmapTransformation(1000))
                .listener(new LoggingListener<String, Bitmap>())
                .into(target);
    }

    @Override
    public void onPrev() {

        if (counter - 1 < 0) return;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                storyStatusView.pause();
            }
        });
        // storyStatusView.pause();
        --counter;
        target.setModel(statusResources.get(counter));
        txttime.setText(statusResources.get(counter).getStoryDate() + "");
        Glide.with(image.getContext())
                .load(target.getModel().getFilename())
                .asBitmap()
                .centerCrop()
                .crossFade()
                .skipMemoryCache(!isCaching)
                .diskCacheStrategy(isCaching ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE)
                .transform(new CenterCrop(image.getContext()), new DelayBitmapTransformation(1000))
                .listener(new LoggingListener<String, Bitmap>())
                .into(target);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isImmersive && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            if (hasFocus) {
                getWindow().getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

    @Override
    public void onComplete() {

        finish();
    }

    @Override
    protected void onDestroy() {
        // Very important !
        storyStatusView.destroy();
        super.onDestroy();
    }

    public void checkapicall(int current) {


        if (!statusResources.isEmpty()) {
            Log.d("mn13statusstory:", statusResources.get(current).toString());

            if (statusResources.get(current).getSeen() == 0) {
                Seenstory(statusResources.get(current), current);
            }
            //if(statusResources.get(current).get)
        }
    }

    public void Seenstory(Image_ newText, int current) {


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(StatusStoriesActivity.this)));
        paramArrayList.add(new param("story_id", newText.getId() + ""));

        new geturl().getdata(StatusStoriesActivity.this, new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {

                try {
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("message");

                    if (message) {
                        selectmodel.getImages().get(current).setSeen(1);
                        boolean nofisyflag = true;
                        for (int i = 0; i < selectmodel.getImages().size(); i++) {

                            if (selectmodel.getImages().get(i).getSeen() == 0) {
                                nofisyflag = false;
                            }
                        }

                        if (nofisyflag) {
                            selectmodel.setAll_seen(1);
                        } else {
                            selectmodel.setAll_seen(0);
                        }

                        if (from.equalsIgnoreCase("Adapter")) {
                            stories_adapter.notifyDataSetChanged();
                        } else {
                            if (nofisyflag) {
                                imgadd2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.darkgrey)));
                            }
                        }

                        Log.d("mn13seenstatus:", selectmodel.toString() + "");

                    } else {
                    }
                } catch (Exception e) {
                    //Toasty.error(StatusStoriesActivity.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                    e.printStackTrace();
                }
                //
            }
        }, paramArrayList, "seen_story");
    }

    /**
     * Demonstrates 3 different ways of showing the progress:
     * <ul>
     * <li>Update a full fledged progress bar</li>
     * <li>Update a text view to display size/percentage</li>
     * <li>Update the placeholder via Drawable.level</li>
     * </ul>
     * This last one is tricky: the placeholder that Glide sets can be used as a progress drawable
     * without any extra Views in the view hierarchy if it supports levels via <code>usesLevel="true"</code>
     * or <code>level-list</code>.
     *
     * @param <Z> automatically match any real Glide target so it can be used flexibly without reimplementing.
     */
    @SuppressLint("SetTextI18n") // text set only for debugging
    private static class MyProgressTarget<Z> extends ProgressTarget<String, Z> {
        private final TextView text;
        private final ProgressBar progress;

        public MyProgressTarget(Target<Z> target, ProgressBar progress, TextView text) {
            super(target);
            this.progress = progress;
            this.text = text;
            Log.d("mn13text", "1");
        }

        @Override
        public float getGranualityPercentage() {
            return 0.1f; // this matches the format string for #text below
        }

        @Override
        protected void onConnecting() {
            progress.setProgress(10);
            progress.setIndeterminate(false);
            progress.setVisibility(View.VISIBLE);

            if (isTextEnabled) {
                text.setVisibility(View.VISIBLE);
                text.setText("connecting");
            } else {
                text.setVisibility(View.INVISIBLE);
            }

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    storyStatusView.pause();
                }
            });

            Log.d("mn13text", "2");
            // storyStatusView.pause();
        }

        @Override
        protected void onDownloading(long bytesRead, long expectedLength) {
            progress.setVisibility(View.VISIBLE);
            progress.setIndeterminate(false);
            progress.setProgress((int) (100 * bytesRead / expectedLength));

            if (isTextEnabled) {
                text.setVisibility(View.VISIBLE);
                text.setText(String.format(Locale.ROOT, "downloading %.2f/%.2f MB %.1f%%",
                        bytesRead / 1e6, expectedLength / 1e6, 100f * bytesRead / expectedLength));
            } else {
                text.setVisibility(View.INVISIBLE);
            }


            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    storyStatusView.pause();
                }
            });
            //storyStatusView.pause();
            Log.d("mn13text", "3");
        }

        @Override
        protected void onDownloaded() {
            progress.setIndeterminate(false);
            if (isTextEnabled) {
                text.setVisibility(View.VISIBLE);
                text.setText("decoding and transforming");
            } else {
                text.setVisibility(View.INVISIBLE);
            }


            //storyStatusView.pause();
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    storyStatusView.pause();
                }
            });
            Log.d("mn13text", "4");
        }

        @Override
        protected void onDelivered() {
            progress.setVisibility(View.INVISIBLE);
            text.setVisibility(View.INVISIBLE);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    storyStatusView.resume();
                }
            });

            Log.d("mn13text", "5");
        }
    }

}
