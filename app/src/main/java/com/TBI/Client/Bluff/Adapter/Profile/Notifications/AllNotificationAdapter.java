package com.TBI.Client.Bluff.Adapter.Profile.Notifications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Mains.BottombarActivity;
import com.TBI.Client.Bluff.Activity.Post.PostDetailPage;
import com.TBI.Client.Bluff.Activity.Post.PostDetailPage1;
import com.TBI.Client.Bluff.Activity.Profile.OtherUserProfile;
import com.TBI.Client.Bluff.Activity.Profile.ProfilePage;
import com.TBI.Client.Bluff.Fragment.Look_Fragment;
import com.TBI.Client.Bluff.Model.GetOtherUser.GetOtherUser;
import com.TBI.Client.Bluff.Model.getNewNotification.AllNotification;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class AllNotificationAdapter extends RecyclerView.Adapter<AllNotificationAdapter.MyClassView> {

    String username = "", fullname = "";
    private List<AllNotification> allNotifications;
    private Activity activity;

    public AllNotificationAdapter(List<AllNotification> allNotifications, Activity activity) {
        this.allNotifications = allNotifications;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_all_notification, parent, false);

        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {

        AllNotification allNotification = allNotifications.get(position);

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(activity) + ""));
        paramArrayList.add(new param("other_user_id", allNotification.getFrom() + ""));

        new geturl().getdata(activity, data -> {

            try {
                Public_Function.Hide_ProgressDialogue();
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                if (message) {
                    GetOtherUser login = new Gson().fromJson(data, GetOtherUser.class);
                    if (!login.getData().isEmpty()) {
                        username = login.getData().get(0).getUsername();
                        fullname = login.getData().get(0).getFullName();

                        Log.e("LLLLL_Fullname: ",fullname);
                        Log.e("LLLLL_Username: ",username);

                        holder.tv_fullname.setText(fullname);
                        holder.tv_username.setText(username);

                    }
                }
            } catch (Exception e) {
                Log.e("LLLL_Other_USer_E: ", e.getMessage());
                Toasty.error(activity, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }

        }, paramArrayList, "view_profile");

        String type = allNotification.getType();

        if (type.equals("Like")) {

            holder.img_post.setVisibility(View.VISIBLE);
            holder.ll_rate.setVisibility(View.GONE);

            Glide
                    .with(activity)
                    .load(allNotification.getUimage())
                    .error(R.drawable.placeholder)
                    .into(holder.img_user_profile);
            Glide
                    .with(activity)
                    .load(allNotification.getPimage())
                    .error(R.drawable.placeholder)
                    .into(holder.img_post);

            holder.tv_post_type.setText(allNotification.getDescription());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int spo = position;

                    int pos = 0;
                    for (int i = 0; i < ProfilePage.postarray.size(); i++) {
                        if (ProfilePage.postarray.get(i).getId().equals(ProfilePage.postarray.get(spo).getId())){
                            pos = i;
                        }
                    }

                    Intent i1 = new Intent(holder.itemView.getContext(), PostDetailPage.class);
                    i1.putExtra("comment", "no");
                    i1.putExtra("come", "profile");
                    i1.putExtra("position", pos);
                    holder.itemView.getContext().startActivity(i1);
                    ((Activity) holder.itemView.getContext()).overridePendingTransition(R.anim.fade_in, R.anim.stay);
                }
            });

        } else if (type.equalsIgnoreCase("comment") ||
                type.equalsIgnoreCase("share_post") ||
                type.equalsIgnoreCase("like_post")){

            holder.img_post.setVisibility(View.VISIBLE);
            holder.ll_rate.setVisibility(View.GONE);

            Glide
                    .with(activity)
                    .load(allNotification.getUimage())
                    .error(R.drawable.placeholder)
                    .into(holder.img_user_profile);
            Glide
                    .with(activity)
                    .load(allNotification.getPimage())
                    .error(R.drawable.placeholder)
                    .into(holder.img_post);

            holder.tv_post_type.setText(allNotification.getDescription());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int spo = position;

                    int pos = 0;
                    for (int i = 0; i < ProfilePage.postarray.size(); i++) {
                        if (ProfilePage.postarray.get(i).getId().equals(ProfilePage.postarray.get(spo).getId())){
                            pos = i;
                        }
                    }

                    Intent i1 = new Intent(holder.itemView.getContext(), PostDetailPage.class);
                    i1.putExtra("comment", "no");
                    i1.putExtra("come", "profile");
                    i1.putExtra("position", pos);
                    holder.itemView.getContext().startActivity(i1);
                    ((Activity) holder.itemView.getContext()).overridePendingTransition(R.anim.fade_in, R.anim.stay);
                }
            });

        }else if (type.equalsIgnoreCase("rate_user")){

            Glide
                    .with(activity)
                    .load(allNotification.getUimage())
                    .error(R.drawable.placeholder)
                    .into(holder.img_user_profile);

            holder.img_post.setVisibility(View.GONE);
            holder.ll_rate.setVisibility(View.VISIBLE);
            holder.ratingBar.setIsIndicator(true);

            holder.ratingBar.setRating(Float.parseFloat(allNotification.getRating()));
            holder.tv_post_type.setText(allNotification.getDescription());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i1 = new Intent(holder.itemView.getContext(), ProfilePage.class);
                    i1.putExtra("type", "left");
                    holder.itemView.getContext().startActivity(i1);
                    ((Activity) holder.itemView.getContext()).overridePendingTransition(R.anim.fade_in, R.anim.stay);
                }
            });
        } else if (type.equalsIgnoreCase("sos")){

            Glide
                    .with(activity)
                    .load(allNotification.getUimage())
                    .error(R.drawable.placeholder)
                    .into(holder.img_user_profile);

            holder.img_post.setVisibility(View.GONE);
            holder.ll_rate.setVisibility(View.GONE);

            holder.tv_post_type.setText(allNotification.getDescription());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), BottombarActivity.class);
                    intent.putExtra("type", "map");
                    holder.itemView.getContext().startActivity(intent);
                    ((Activity) holder.itemView.getContext()).finish();
                    ((Activity) holder.itemView.getContext()).overridePendingTransition(R.anim.fade_in, R.anim.stay);
                }
            });
        } else if (type.equalsIgnoreCase("follower")){
            Glide
                    .with(activity)
                    .load(allNotification.getUimage())
                    .error(R.drawable.placeholder)
                    .into(holder.img_user_profile);

            holder.img_post.setVisibility(View.GONE);
            holder.ll_rate.setVisibility(View.GONE);
            holder.tv_post_type.setText(allNotification.getDescription());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i1 = new Intent(holder.itemView.getContext(), OtherUserProfile.class);
                    i1.putExtra("other_id", allNotification.getFrom() + "");
                    i1.putExtra("other_username", "");
                    holder.itemView.getContext().startActivity(i1);
                    ((Activity) holder.itemView.getContext()).overridePendingTransition(R.anim.fade_in, R.anim.stay);
                }
            });
        }
    }

    public void addAll(List<AllNotification> allNotificationList) {
        allNotifications.clear();
        allNotifications.addAll(allNotificationList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return allNotifications.size();
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_list)
        RelativeLayout rl_list;
        @BindView(R.id.img_user_profile)
        CircleImageView img_user_profile;
        @BindView(R.id.tv_username)
        TextView tv_username;
        @BindView(R.id.tv_post_time)
        TextView tv_post_time;
        @BindView(R.id.tv_fullname)
        TextView tv_fullname;
        @BindView(R.id.tv_post_type)
        TextView tv_post_type;
        @BindView(R.id.img_post)
        ImageView img_post;
        @BindView(R.id.ll_rate)
        LinearLayout ll_rate;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;

        public MyClassView(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
