package com.TBI.Client.Bluff.Adapter.Post;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Post.PostDetailPage;
import com.TBI.Client.Bluff.Activity.Post.PostDetailPage1;
import com.TBI.Client.Bluff.Model.GetProfile.Post;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.vr.sdk.widgets.video.VrVideoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;

import static com.google.vr.cardboard.ThreadUtils.runOnUiThread;

public class UserFeed_Adapter extends RecyclerView.Adapter<UserFeed_Adapter.RecyclerObjectHolder> {
    Activity context;
    List<Post> item = new ArrayList<>();
    int selection;
    String come = "";

    public UserFeed_Adapter(Activity Context, List<Post> getcategoryarray, String come) {
        this.context = Context;
        this.item = getcategoryarray;
        this.come = come;
    }

    @Override
    public RecyclerObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DataObjectHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerObjectHolder viewHolder, int position) {
        viewHolder.ratingBar.setVisibility(View.GONE);
        viewHolder.simmar_id.startShimmer();
        Log.d("mn13itemsize", item.size() + "");
        viewHolder.roundedImageView.setClipToOutline(true);

        viewHolder.txtdescription.setText(item.get(position).getDescription());
        viewHolder.roundedImageView.setClipToOutline(true);
        viewHolder.img_user.setVisibility(View.GONE);
        viewHolder.tv_username.setVisibility(View.GONE);
        viewHolder.txtdescription.setVisibility(View.GONE);
        viewHolder.tv_duration.setVisibility(View.GONE);

        if (!item.get(position).getImage().equals("") && item.get(position).getImage() != null) {

            switch (item.get(position).getFileType()) {
                case "video":
                    viewHolder.roundedImageView.setVisibility(View.GONE);
                    viewHolder.cv_VideoView.setVisibility(View.VISIBLE);
                    viewHolder.cv_VrView.setVisibility(View.GONE);
                    Log.e("LLLL_URL: ", Objects.requireNonNull(item.get(position).getImage()));
                    viewHolder.bind(Uri.parse(item.get(position).getImage()));

                    break;
                case "image":
                    viewHolder.cv_VideoView.setVisibility(View.GONE);
                    viewHolder.roundedImageView.setVisibility(View.VISIBLE);
                    viewHolder.cv_VrView.setVisibility(View.GONE);
                    Glide
                            .with(context)
                            .load(item.get(position).getImage())
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    viewHolder.simmar_id.stopShimmer();
                                    viewHolder.simmar_id.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .centerCrop()
                            .into(viewHolder.roundedImageView);
                    break;
                case "3Dvideo":
                    viewHolder.cv_VideoView.setVisibility(View.GONE);
                    viewHolder.roundedImageView.setVisibility(View.GONE);
                    viewHolder.cv_VrView.setVisibility(View.VISIBLE);

                    VideoLoaderTask mBackgroundVideoLoaderTask = new VideoLoaderTask(viewHolder, item.get(position).getImage());
                    mBackgroundVideoLoaderTask.execute();

                    viewHolder.vrView.playVideo();
                    viewHolder.vrView.setVolume(0);
                    break;
            }

        }


        viewHolder.frametop.setTag(position);
        viewHolder.frametop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int spo = (int) view.getTag();

                Public_Function.hideKeyboard(context);
                if (come.equalsIgnoreCase("globalsearch")) {
                    Intent i1 = new Intent(context, PostDetailPage1.class);
                    i1.putExtra("post_id", item.get(spo).getId() + "");
                    i1.putExtra("tag", "no");
                    i1.putExtra("comment", "no");
                    i1.putExtra("showsubscribe", "no");
                    context.startActivity(i1);
                    context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    Intent i1 = new Intent(context, PostDetailPage.class);
                    i1.putExtra("comment", "no");
                    i1.putExtra("tag", "no");
                    i1.putExtra("come", come + "");
                    i1.putExtra("position", spo);
                    context.startActivity(i1);
                    context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        });

    }

    class VideoLoaderTask extends AsyncTask<Void, Void, Boolean> {

        RecyclerObjectHolder viewHolder;
        String url;

        public VideoLoaderTask(RecyclerObjectHolder viewHolder, String url) {
            this.viewHolder = viewHolder;
            this.url = url;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            VrVideoView.Options options = new VrVideoView.Options();
            options.inputType = VrVideoView.Options.TYPE_MONO;
            runOnUiThread(() -> {
                try {
                    viewHolder.simmar_id.stopShimmer();
                    viewHolder.simmar_id.setVisibility(View.GONE);
                    viewHolder.vrView.loadVideo(Uri.parse(url), options);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            return true;
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }


    public void addAll(List<Post> additem) {
        item.addAll(additem);
        Log.e("LLLLLL_Post", additem.size() + "");
        notifyDataSetChanged();
    }

    public void removeAll() {
        item.clear();
        notifyDataSetChanged();
    }

    public void setNotifydone(int delete) {
        item.remove(delete);
        notifyDataSetChanged();
    }

    public void randome(int position) {
        selection = position;
    }


    static class RecyclerObjectHolder extends RecyclerView.ViewHolder  {
        @BindView(R.id.txtdescription)
        TextView txtdescription;
        @BindView(R.id.frametop)
        FrameLayout frametop;
        @BindView(R.id.roundedImageView)
        ImageView roundedImageView;
        @BindView(R.id.simmar_id)
        ShimmerFrameLayout simmar_id;
        @BindView(R.id.img_user)
        CircleImageView img_user;
        @BindView(R.id.tv_username)
        TextView tv_username;
        @BindView(R.id.tv_duration)
        TextView tv_duration;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        @BindView(R.id.vrView)
        VrVideoView vrView;
        @BindView(R.id.cv_VideoView)
        CardView cv_VideoView;
        @BindView(R.id.cv_VrView)
        CardView cv_VrView;
        Uri mediaUri;

        public RecyclerObjectHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("ClickableViewAccessibility")
        void bind(Uri media) {
            this.mediaUri = media;
        }
    }

    static class DataObjectHolder extends RecyclerObjectHolder implements ToroPlayer {

        PlayerView playerView;
        ExoPlayerViewHelper helper;
        Uri mediaUri;

        DataObjectHolder(View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.player);
        }

        @NonNull
        @Override
        public View getPlayerView() {
            return playerView;
        }

        @NonNull
        @Override
        public PlaybackInfo getCurrentPlaybackInfo() {
            return helper != null ? helper.getLatestPlaybackInfo() : new PlaybackInfo();
        }

        @Override
        public void initialize(@NonNull Container container, @NonNull PlaybackInfo playbackInfo) {
            simmar_id.startShimmer();
            simmar_id.setVisibility(View.VISIBLE);

            if (helper == null) {
                helper = new ExoPlayerViewHelper(this, mediaUri);
//                if (!helper.isPlaying()) {
//                    Log.e("LLLLLL_Play: ","true");
//                    helper.play();
//                    mute();
//                    new Handler().postDelayed(() -> {
//                        simmar_id.stopShimmer();
//                        simmar_id.setVisibility(View.GONE);
//                    }, 1000);
//                }
            }else {
                Log.e("LLLLLL_Play: ","false");
            }
            helper.initialize(container,playbackInfo);
        }

        @Override
        public void play() {
            Log.e("LLLLLL_Play: ", Objects.requireNonNull(mediaUri.getPath()));
            if (helper != null) {
                helper.play();
                mute();
                new Handler().postDelayed(() -> {
                    simmar_id.stopShimmer();
                    simmar_id.setVisibility(View.GONE);
                }, 500);
            } else {
                Log.e("LLLLLL_Play: ","false");
            }
        }

        @Override
        public void pause() {
            if (helper != null) helper.pause();
        }

        @Override
        public boolean isPlaying() {
            return helper != null && helper.isPlaying();
        }

        @Override
        public void release() {
            if (helper != null) {
                simmar_id.startShimmer();
                simmar_id.setVisibility(View.VISIBLE);
                helper.release();
                helper = null;
            }
        }

        @Override
        public boolean wantsToPlay() {
            return ToroUtil.visibleAreaOffset(this, itemView.getParent()) >= 0.85;
        }

        @Override
        public int getPlayerOrder() {
            Log.e("LLLLL_ListNo.: ", String.valueOf(getAdapterPosition()));
            return getAdapterPosition();
        }

        private void mute() {
            this.setVolume(0);
        }

        private void unMute() {
            this.setVolume(100);
        }

        void bind(Uri media) {
            this.mediaUri = media;
        }

        private void setVolume(int amount) {
            final int max = 100;
            final double numerator = max - amount > 0 ? Math.log(max - amount) : 0;
            final float volume = (float) (1 - (numerator / Math.log(max)));

            this.helper.setVolume(volume);

        }
    }

}

