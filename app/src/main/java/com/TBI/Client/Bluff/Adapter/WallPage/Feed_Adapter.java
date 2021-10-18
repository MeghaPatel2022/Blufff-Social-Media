package com.TBI.Client.Bluff.Adapter.WallPage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
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
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Post.CommentActivity;
import com.TBI.Client.Bluff.Activity.Post.DrawAtivity;
import com.TBI.Client.Bluff.Activity.Post.PostDetailPage;
import com.TBI.Client.Bluff.Activity.Profile.OtherUserProfile;
import com.TBI.Client.Bluff.Activity.WallPage.PersonKnowActivity;
import com.TBI.Client.Bluff.Adapter.Post.NearByPeopleAdapter;
import com.TBI.Client.Bluff.Fragment.Look_Fragment;
import com.TBI.Client.Bluff.Model.GetFeed.NearByUser;
import com.TBI.Client.Bluff.Model.GetFeed.PersonYouKnow;
import com.TBI.Client.Bluff.Model.GetFeed.Rating;
import com.TBI.Client.Bluff.Model.GetFeed.RequestedUser;
import com.TBI.Client.Bluff.Model.GetProfile.Post;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.bubbleactions.BubbleActions;
import com.TBI.Client.Bluff.bubbleactions.Callback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.vr.sdk.widgets.video.VrVideoView;

import java.io.IOException;
import java.util.ArrayList;
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

public class Feed_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // A menu item view type.
    private static final int HEADER = 0;
    private static final int REQ_HEADER = 1;
    private static final int NEAR_BY_HEADER = 2;
    private static final int ITEM = 3;
    public static int selectpostion = -1;
    Activity context;
    List<Post> item = new ArrayList<>();
    private List<NearByUser> nearByUsers;
    private List<PersonYouKnow> personYouKnows;
    private List<RequestedUser> requestedUsers;
    List<Rating> ratings = new ArrayList<>();
    int selection;
    Look_Fragment look_fragment;

    private PeopleKnowAdapter peopleKnowAdapter;
    private RequestedAdapter requestedAdapter;
    private NearByPeopleAdapter nearByUserAdapter;

    public Feed_Adapter(Activity context, List<Post> item, List<Rating> ratings, List<NearByUser> nearByUsers, List<PersonYouKnow> personYouKnows, List<RequestedUser> requestedUsers, Look_Fragment look_fragment) {
        this.context = context;
        this.item = item;
        this.ratings = ratings;
        this.nearByUsers = nearByUsers;
        this.personYouKnows = personYouKnows;
        this.requestedUsers = requestedUsers;
        this.look_fragment = look_fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case HEADER:
                v = layoutInflater.inflate(R.layout.layout_people_you_know, parent, false);
                return new HeaderViewHolder(v);
            case REQ_HEADER:
                v = layoutInflater.inflate(R.layout.layout_people_you_know, parent, false);
                return new ReqHeaderViewHolder(v);
            case NEAR_BY_HEADER:
                v = layoutInflater.inflate(R.layout.layout_people_you_know, parent, false);
                return new NearHeaderViewHolder(v);
            default:
                v = layoutInflater.inflate(R.layout.layout_post, parent, false);
                return new ItemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder.getItemViewType() == ITEM) {
            ((ItemViewHolder) viewHolder).bind(position);
        } else if (viewHolder.getItemViewType() == HEADER) {
            ((HeaderViewHolder) viewHolder).bind();
        } else if (viewHolder.getItemViewType() == REQ_HEADER) {
            ((ReqHeaderViewHolder) viewHolder).bind();
        } else if (viewHolder.getItemViewType() == NEAR_BY_HEADER) {
            ((NearHeaderViewHolder) viewHolder).bind();
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public void addAll(List<Post> additem) {
//        item.addAll(additem);
        item.addAll(additem);
    }

    public void removeAll() {
        item.clear();
        nearByUsers.clear();
        notifyDataSetChanged();
    }

    public void adRatingdAll(List<Rating> rating) {
        ratings.clear();
        ratings.addAll(rating);
        notifyDataSetChanged();
    }

    public void removRatingeAll() {
        ratings.clear();
        notifyDataSetChanged();
    }

    public void addPeopleKnowAll(List<PersonYouKnow> rating,
                                 List<RequestedUser> rating1,
                                 List<NearByUser> additem) {

        personYouKnows.clear();
        requestedUsers.clear();
        nearByUsers.clear();

        Public_Function.personYouKnows.clear();
        Public_Function.requestedUsers.clear();
        Public_Function.nearByUsers.clear();

        personYouKnows.addAll(rating);
        Public_Function.personYouKnows.addAll(rating);

        requestedUsers.addAll(rating1);
        Public_Function.requestedUsers.addAll(rating1);

        nearByUsers.addAll(additem);
        Public_Function.nearByUsers.addAll(additem);

        notifyDataSetChanged();
    }

    public void removPoepleKnowAll() {
        personYouKnows.clear();
        personYouKnows.clear();
        notifyDataSetChanged();
    }

    public void removRequestKnowAll() {
        requestedUsers.clear();
        Public_Function.requestedUsers.clear();
        notifyDataSetChanged();
    }

    public void removNearByAll() {
        nearByUsers.clear();
        Public_Function.nearByUsers.clear();
        notifyDataSetChanged();
    }

    public void randome(int position) {
        selection = position;
    }

    @Override
    public int getItemViewType(int position) {

        if (personYouKnows.isEmpty() && requestedUsers.isEmpty() && nearByUsers.isEmpty()) {
            return ITEM;
        } else if (!personYouKnows.isEmpty() && !requestedUsers.isEmpty() && !nearByUsers.isEmpty()) {
            if (position == 4) {
                return HEADER;
            } else if (position == 9) {
                return REQ_HEADER;
            } else if (position == 14)
                return NEAR_BY_HEADER;
            else {
                return ITEM;
            }
        } else if (personYouKnows.isEmpty() && !requestedUsers.isEmpty() && !nearByUsers.isEmpty()) {
            if (position == 8) {
                return REQ_HEADER;
            } else if (position == 13)
                return NEAR_BY_HEADER;
            else {
                return ITEM;
            }
        } else if (!personYouKnows.isEmpty() && requestedUsers.isEmpty() && !nearByUsers.isEmpty()) {
            if (position == 4) {
                return HEADER;
            } else if (position == 13)
                return NEAR_BY_HEADER;
            else {
                return ITEM;
            }
        } else if (personYouKnows.isEmpty() && !requestedUsers.isEmpty() && nearByUsers.isEmpty()) {
            if (position == 8)
                return REQ_HEADER;
            else {
                return ITEM;
            }
        } else if (!personYouKnows.isEmpty() && requestedUsers.isEmpty() && nearByUsers.isEmpty()) {
            if (position == 4)
                return HEADER;
            else {
                return ITEM;
            }
        } else
            return ITEM;

    }

    public void setpostion(int pos) {
        selectpostion = pos;
        notifyDataSetChanged();
    }

    public void setvibrant() {
        Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(10);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements ToroPlayer {

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
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        @BindView(R.id.vrView)
        VrVideoView vrView;
        @BindView(R.id.cv_VideoView)
        CardView cv_VideoView;
        @BindView(R.id.cv_VrView)
        CardView cv_VrView;
        @BindView(R.id.player)
        PlayerView playerView;
        ExoPlayerViewHelper helper;
        Uri mediaUri;


        ItemViewHolder(View itemView) {
            super(itemView);
            //REPLACE yourId WITH REAL IMAGE VIEW ID
            ButterKnife.bind(this, itemView);
        }

        void bind(int pos) {
            Post post = item.get(pos);
            if (!post.getImage().equals("") && !post.getImage().equals(null) && !post.getImage().equals("null")) {
//                Log.e("LLL_Pos: ", String.valueOf(pos));

                simmar_id.setVisibility(View.VISIBLE);
                simmar_id.startShimmer();
//            Log.d("LLL_mn13itemsize", post.size() + "");
                roundedImageView.setClipToOutline(true);

                ratingBar.setIsIndicator(true);

                if (post.getUserRating() != null)
                    ratingBar.setRating(post.getUserRating());

                tv_username.setClipToOutline(true);

                tv_username.setText(post.getFullName());
                if (!post.getPhoto().equals("") && !post.getPhoto().equals(null) && !post.getPhoto().equals("null")) {

                    Glide
                            .with(context)
                            .load(post.getPhoto())
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    Log.e("LLLLL_ErrorLOadImage: ", e.getMessage());
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    simmar_id.stopShimmer();
                                    simmar_id.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .centerCrop()
                            .into(img_user);
                }

                txtdescription.setText(post.getDescription() + "");
                roundedImageView.setClipToOutline(true);

                if (!post.getImage().equals("") && post.getImage() != null && !post.getImage().equals("null")) {

                    switch (post.getFileType()) {
                        case "video":
                            roundedImageView.setVisibility(View.GONE);
                            cv_VideoView.setVisibility(View.VISIBLE);
                            cv_VrView.setVisibility(View.GONE);
                            bind(Uri.parse(post.getImage()));
                            break;
                        case "image":
                        case "":
                            cv_VideoView.setVisibility(View.GONE);
                            roundedImageView.setVisibility(View.VISIBLE);
                            cv_VrView.setVisibility(View.GONE);
                            Glide
                                    .with(context)
                                    .load(post.getImage())
                                    .listener(new RequestListener<String, GlideDrawable>() {
                                        @Override
                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                            Log.e("LLLLL_ErrorLOadImage: ", String.valueOf(e));
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            simmar_id.stopShimmer();
                                            simmar_id.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .centerCrop()
                                    .into(roundedImageView);
                            break;
                        case "3Dvideo":
                            cv_VideoView.setVisibility(View.GONE);
                            roundedImageView.setVisibility(View.GONE);
                            cv_VrView.setVisibility(View.VISIBLE);

//                        VideoLoaderTask mBackgroundVideoLoaderTask = new VideoLoaderTask(post.getImage());
//                        mBackgroundVideoLoaderTask.execute();
                            VrVideoView.Options options = new VrVideoView.Options();
                            options.inputType = VrVideoView.Options.TYPE_MONO;
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        if (Uri.parse(post.getImage()) != null && !Uri.parse(post.getImage()).equals(Uri.EMPTY)) {
                                            //doTheThing()
                                            vrView.setVolume(0);
                                            vrView.loadVideo(Uri.parse(post.getImage()), options);
                                        } else {
                                            simmar_id.startShimmer();
                                            simmar_id.setVisibility(View.VISIBLE);
                                            vrView.setVisibility(View.GONE);
                                            //followUri is null or empty
                                        }

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            vrView.playVideo();


                            break;
                    }
                }

                frametop.setTag(pos);
                frametop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int spo = (int) view.getTag();

                        Intent i1 = new Intent(itemView.getContext(), PostDetailPage.class);
                        i1.putExtra("comment", "no");
                        i1.putExtra("come", "wall");
                        i1.putExtra("position", spo);
                        itemView.getContext().startActivity(i1);
                        ((Activity) itemView.getContext()).overridePendingTransition(R.anim.fade_in, R.anim.stay);
                    }
                });


                frametop.setOnLongClickListener(view -> {

                    int spo = (int) view.getTag();

                    setvibrant();
                    selectpostion = spo;
                    notifyDataSetChanged();

                    BubbleActions.on(view)
                            .addAction("Tag", R.drawable.tag1_white, new Callback() {
                                @Override
                                public void doAction() {
                                    Intent i1 = new Intent(context, PostDetailPage.class);
                                    i1.putExtra("tag", "yes");
                                    i1.putExtra("comment", "no");
                                    i1.putExtra("come", "wall");
                                    i1.putExtra("position", spo);
                                    context.startActivity(i1);
                                    context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                }
                            })
                            .addAction("Gesture", R.drawable.gesture1_white, new Callback() {
                                @Override
                                public void doAction() {
                                    Intent intent = new Intent(context, DrawAtivity.class);
                                    intent.putExtra("comment", "yes");
                                    intent.putExtra("tag", "no");
                                    intent.putExtra("position", spo);
                                    intent.putExtra("come", "wall");
                                    context.startActivity(intent);
                                    context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                }
                            }).
                            addAction("Comment", R.drawable.comment1_white, new Callback() {
                                @Override
                                public void doAction() {

                                    Intent i1 = new Intent(context, CommentActivity.class);
                                    i1.putExtra("comment", "yes");
                                    i1.putExtra("come", "wall");
                                    i1.putExtra("position", spo);
                                    context.startActivity(i1);
                                    context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                }
                            }).
                            addAction("Save", R.drawable.save, new Callback() {
                                @Override
                                public void doAction() {
                                    look_fragment.Save_Post(item.get(spo).getId());
                                }
                            })
                            .withIndicator(R.drawable.bubble_indicator)
                            .show();


                    return false;
                });
            }
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
                helper.initialize(container, playbackInfo);
                if (!helper.isPlaying()) {
                    Log.e("LLLLLL_Play: ","true");
                    helper.play();
                    mute();
                    new Handler().postDelayed(() -> {
                        simmar_id.stopShimmer();
                        simmar_id.setVisibility(View.GONE);
                    }, 1000);
                } else {
                    Log.e("LLLLLL_Play: ","false");
                }
            }
        }

        @Override
        public void play() {
            Log.e("LLLLLL_Play: ", Objects.requireNonNull(mediaUri.getPath()));
            if (helper != null) {
                Log.e("LLLLLL_Play: ","true");
                helper.play();
                mute();
                new Handler().postDelayed(() -> {
                    simmar_id.stopShimmer();
                    simmar_id.setVisibility(View.GONE);
                }, 1000);
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
            return getAdapterPosition();
        }

        @SuppressLint("ClickableViewAccessibility")
        void bind(Uri media) {
            this.mediaUri = media;
            mute();
        }

        private void mute() {
            this.setVolume(0);
        }

        private void unMute() {
            this.setVolume(100);
        }

        private void setVolume(int amount) {
            final int max = 100;
            final double numerator = max - amount > 0 ? Math.log(max - amount) : 0;
            final float volume = (float) (1 - (numerator / Math.log(max)));

            this.helper.setVolume(volume);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rv_people_know)
        RecyclerView rv_people_know;
        @BindView(R.id.tv_view_all)
        TextView tv_view_all;
        @BindView(R.id.ll_main)
        RelativeLayout ll_main;
        @BindView(R.id.tv_req)
        TextView tv_req;


        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind() {
            rv_people_know.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            peopleKnowAdapter = new PeopleKnowAdapter(context, personYouKnows);
            rv_people_know.setAdapter(peopleKnowAdapter);
            tv_req.setText("People You May Know.");
            tv_view_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PersonKnowActivity.class);
                    intent.putExtra("Activity", "Person");
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.fade_in, R.anim.stay);
                }
            });

        }
    }

    class ReqHeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rv_people_know)
        RecyclerView rv_people_know;
        @BindView(R.id.tv_view_all)
        TextView tv_view_all;
        @BindView(R.id.tv_req)
        TextView tv_req;


        public ReqHeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind() {
            rv_people_know.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            requestedAdapter = new RequestedAdapter(context, Public_Function.requestedUsers);
            rv_people_know.setAdapter(requestedAdapter);

            tv_req.setText("Requested");

            tv_view_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PersonKnowActivity.class);
                    intent.putExtra("Activity", "Request");
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.fade_in, R.anim.stay);
                }
            });
        }
    }

    class NearHeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rv_people_know)
        RecyclerView rv_people_know;
        @BindView(R.id.tv_view_all)
        TextView tv_view_all;
        @BindView(R.id.tv_req)
        TextView tv_req;

        public NearHeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind() {
            rv_people_know.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            nearByUserAdapter = new NearByPeopleAdapter(nearByUsers, context);
            rv_people_know.setAdapter(nearByUserAdapter);

            tv_req.setText("People nearby you");

            tv_view_all.setOnClickListener(v -> {
                Intent intent = new Intent(context, PersonKnowActivity.class);
                intent.putExtra("Activity", "NearBy");
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.fade_in, R.anim.stay);
            });
        }
    }

}

