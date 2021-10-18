package com.TBI.Client.Bluff.Adapter.Post;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.TBI.Client.Bluff.Activity.Post.PostDetailPage;
import com.TBI.Client.Bluff.Model.GetProfile.Post;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.bumptech.glide.Glide;
import com.google.vr.sdk.widgets.video.VrVideoView;
import com.harsh.instatag.InstaTag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.google.vr.cardboard.ThreadUtils.runOnUiThread;

public class Postdetail_ImageAdapter1 extends PagerAdapter {

    // A menu item view type.
    private static final int MENU_ITEM_VIEW_TYPE = 0;

    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;
    public static Float width = Float.valueOf(0);
    public static Float height = Float.valueOf(0);
    public static boolean Tagshow = true;
    List<Post> images = new ArrayList<>();
    boolean showtag = false;
    private LayoutInflater inflater;
    private PostDetailPage context;
    VrVideoView vrView;
    private OnPostClickListener listener;

    public static long back_pressed;

    private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds
    boolean tap = true;
    long lastClickTime = 0;

    public Postdetail_ImageAdapter1(PostDetailPage context, List<Post> images, OnPostClickListener listener) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }


    @Override
    public int getCount() {
        return images.size();
    }


    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        ImageView imgtagpeople;
        ImageView roundedImageView;
        VideoView videoView;

        InstaTag instatagview;

        View myImageLayout = inflater.inflate(R.layout.layout_viewpageritem, view, false);

        videoView = myImageLayout.findViewById(R.id.videoView);
        vrView = myImageLayout.findViewById(R.id.vrView);

        imgtagpeople = myImageLayout.findViewById(R.id.imgtagpeople);
        roundedImageView = myImageLayout.findViewById(R.id.roundedImageView);
        instatagview = myImageLayout.findViewById(R.id.instatagview);
        instatagview.setloginusername(sharedpreference.getusername(context));

        if (Tagshow) {
            instatagview.setVisibility(View.VISIBLE);
        } else {
            instatagview.setVisibility(View.INVISIBLE);
        }

        images.get(position).getWidth();
        if (!images.get(position).getImages().isEmpty()) {
            Float oldheight = Float.parseFloat(images.get(position).getHeight());
            Float oldwidth = Float.parseFloat(images.get(position).getWidth());

            if (!width.equals(null)) {

                for (int i = 0; i < images.get(position).getImages().get(0).getTagPeople().size(); i++) {
                    Float x = Float.valueOf(images.get(position).getImages().get(0).getTagPeople().get(i).getX());
                    Float y = Float.valueOf(images.get(position).getImages().get(0).getTagPeople().get(i).getY());
                    Log.e("mn13height1", oldheight + "\n" + oldwidth + "\n" + width + "\n" + height);
                    Float newx = Public_Function.convertheight(oldwidth, width, x);
                    Float newy = Public_Function.convertheight(oldheight, height, y);

                    instatagview.addTag(newx, newy, images.get(position).getImages().get(0).getTagPeople().get(i).getUsername() + "");
                }
            }

            if (images.get(position).getImages().get(0).getTagPeople().isEmpty()) {
                imgtagpeople.setVisibility(View.GONE);
            } else {
                imgtagpeople.setVisibility(View.VISIBLE);
            }

            Log.d("mn13tagshow1", instatagview.isTagShowing() + "");
            if (images.get(position).getImages().get(0).isTagshow()) {
                instatagview.showTags();
            } else {
                instatagview.hideTags();
            }

            imgtagpeople.setTag(position);
            imgtagpeople.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("mn13click", "Click");
                    int pos = (int) view.getTag();

                    if (!images.get(pos).getImages().isEmpty()) {
                        context.setmodel(images.get(pos).getImages().get(0));
                    }
                    /*if (context instanceof PostDetailPage1) {
                        PostDetailPage1 detailPage1 = (PostDetailPage1) context;

                        detailPage1.setmodel(images.get(pos));
                    }*/
                }
            });

        }
        roundedImageView.setClipToOutline(true);
        if (!images.get(position).getImage().equals("") && !images.get(position).getImage().equals(null) && !images.get(position).getImage().equals("null")) {

            if (images.get(position).getFileType().equals("video")) {
                videoView.setVisibility(View.VISIBLE);
                roundedImageView.setVisibility(View.GONE);
                vrView.setVisibility(View.GONE);

                videoView.setVideoURI(Uri.parse(images.get(position).getImage()));
                videoView.start();
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setVolume(1f, 1f);
                        mp.setLooping(true);
                    }
                });
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        videoView.setVideoURI(Uri.parse(images.get(position).getImage()));
                        mp.start();
                    }
                });

                videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        videoView.setVisibility(View.VISIBLE);
                        roundedImageView.setVisibility(View.GONE);
                        vrView.setVisibility(View.GONE);
                        return true;
                    }
                });

            } else if (images.get(position).getFileType().equals("image") || images.get(position).getFileType().equals("")) {
                videoView.setVisibility(View.GONE);
                roundedImageView.setVisibility(View.VISIBLE);
                vrView.setVisibility(View.GONE);

                Glide
                        .with(context)
                        .load(images.get(position).getImage())
                        .placeholder(R.drawable.grey_placeholder)
                        .error(R.drawable.grey_placeholder)
                        .into(roundedImageView);

            } else if (images.get(position).getFileType().equals("3Dvideo")) {
                videoView.setVisibility(View.GONE);
                roundedImageView.setVisibility(View.GONE);
                vrView.setVisibility(View.VISIBLE);

                VideoLoaderTask mBackgroundVideoLoaderTask = new VideoLoaderTask(images.get(position).getImage());
                mBackgroundVideoLoaderTask.execute();

                vrView.playVideo();

            }


        } else {
            roundedImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.grey_placeholder));
        }

        myImageLayout.setOnClickListener(v -> {
            Toasty.error(context,"Double Tap: ").show();
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                Log.e("LLLLL_Pres:: ","Double tap..");
                if (images.get(position).getLikedPost() == 0)
                    listener.onPostLiked(1);
                else
                    listener.onPostLiked(0);
                tap = false;
            } else
                tap = true;

            lastClickTime = clickTime;

        });


        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    class VideoLoaderTask extends AsyncTask<Void, Void, Boolean> {

        String url;

        public VideoLoaderTask(String url) {
            this.url = url;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            VrVideoView.Options options = new VrVideoView.Options();
            options.inputType = VrVideoView.Options.TYPE_MONO;
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        vrView.loadVideo(Uri.parse(url), options);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            return true;
        }
    }

    public interface OnPostClickListener {
        void onPostLiked(int status);
    }

}
