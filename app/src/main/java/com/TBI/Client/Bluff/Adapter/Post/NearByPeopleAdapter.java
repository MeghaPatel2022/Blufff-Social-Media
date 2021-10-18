package com.TBI.Client.Bluff.Adapter.Post;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Profile.OtherUserProfile;
import com.TBI.Client.Bluff.Model.GetFeed.NearByUser;
import com.TBI.Client.Bluff.Model.GetOtherUser.GetOtherUser;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class NearByPeopleAdapter extends RecyclerView.Adapter<NearByPeopleAdapter.MyClassView> {

    List<NearByUser> nearByUsers;
    Activity context;
    private ArrayList<Integer> account_privacy = new ArrayList<>();

    public NearByPeopleAdapter(List<NearByUser> nearByUsers, Activity context) {
        this.nearByUsers = nearByUsers;
        this.context = context;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_people_you_know, parent, false);

        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {
        NearByUser nearBy = nearByUsers.get(position);

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(context) + ""));
        paramArrayList.add(new param("other_user_id", nearBy.getId() + ""));
        paramArrayList.add(new param("other_username", nearBy.getUsername() + ""));
        paramArrayList.add(new param("timezone", Public_Function.gettimezone()));

        Log.e("LLL_other_user: ", "id: " + sharedpreference.getUserId(context) + ""
                + "      other_user_id: " + nearBy.getId() + "     other_username: " + nearBy.getUsername() + "      timezone: " + Public_Function.gettimezone());

        int follow_status = nearBy.getFollowByUser();
        Log.e("LLLL_Request: ", String.valueOf(follow_status));

        if (follow_status == 0) {
            holder.tv_req_follow.setText("FOLLOW");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.ll_follow_req.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.textcolor)));
            }

        } else if (follow_status == 1) {
            Log.e("LLLL_Request: ", String.valueOf(follow_status));
            holder.tv_req_follow.setText("FOLLOWING");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.ll_follow_req.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.following_color)));
            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.ll_follow_req.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.darkgrey)));
            }
            holder.tv_req_follow.setText("REQUESTED");

        }

//        new geturl().getdata(context, data -> {
//
//            try {
//                Public_Function.Hide_ProgressDialogue();
//                JSONObject object = new JSONObject(data);
//                boolean message = object.optBoolean("success");
//                if (message) {
//                    GetOtherUser login = new Gson().fromJson(data, GetOtherUser.class);
//                    if (!login.getData().isEmpty()) {
//                        account_privacy.add(login.getData().get(0).getAccountPrivacy());
//
//                        int follow_status = login.getData().get(0).getFollowingByYou();
////                        Log.e("LLLL_Request: ", String.valueOf(follow_status));
//
//                        if (follow_status == 0) {
//                            holder.tv_req_follow.setText("FOLLOW");
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                holder.tv_req_follow.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.textcolor)));
//                            }
//
//                        } else if (follow_status == 1) {
////                            Log.e("LLLL_Request: ", String.valueOf(follow_status));
//                            holder.tv_req_follow.setText("FOLLOWING");
//
//                        } else {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                holder.tv_req_follow.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.darkgrey)));
//                            }
//                            holder.tv_req_follow.setText("REQUESTED");
//
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                Log.e("LLLL_Other_USer: ", e.getMessage());
//                Toasty.error(context, e.getMessage(), Toast.LENGTH_LONG, true).show();
//                e.printStackTrace();
//            }
//
//        }, paramArrayList, "view_profile");

        Glide.with(context)
                .load(nearBy.getPhoto())
                .error(R.drawable.placeholder)
                .into(holder.img_user);

        String[] s1 = nearBy.getFullName().split("\\s+");
        if (s1.length > 1) {
            if (!s1[0].isEmpty())
                holder.user_first_name.setText(s1[0]);
            if (!s1[1].isEmpty())
                holder.user_last_name.setText(s1[1]);
        } else {
            holder.user_first_name.setText(nearBy.getFullName());
            holder.user_last_name.setText("");
        }

        holder.ll_follow_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Public_Function.isInternetConnected(context)) {
                    Toasty.warning(context, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {

                    Public_Function.Show_Progressdialog(context);

                    ArrayList<param> paramArrayList = new ArrayList<>();
                    paramArrayList.add(new param("user_id", nearBy.getId() + ""));
                    paramArrayList.add(new param("followed_by", sharedpreference.getUserId(context) + ""));
                    paramArrayList.add(new param("account_privacy", account_privacy.get(position) + ""));

                    new geturl().getdata(context, data -> {
                        try {
                            Public_Function.Hide_ProgressDialogue();
                            JSONObject object = new JSONObject(data);
                            boolean message = object.optBoolean("success");
                            String status = "";
                            status = object.optString("msg");
                            if (message) {
                                context.runOnUiThread(new Runnable() {
                                    public void run() {
                                        int follow_status = object.optInt("following_by_you");
                                        Log.e("LLLL_Request: ", String.valueOf(follow_status));

                                        if (follow_status == 0) {
                                            holder.tv_req_follow.setText("FOLLOW");
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                holder.tv_req_follow.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.textcolor)));
                                            }

                                        } else if (follow_status == 1) {
                                            Log.e("LLLL_Request: ", String.valueOf(follow_status));
                                            holder.tv_req_follow.setText("FOLLOWING");

                                        } else {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                holder.tv_req_follow.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.darkgrey)));
                                            }
                                            holder.tv_req_follow.setText("REQUESTED");

                                        }
                                    }
                                });
                            } else {
                                Toasty.error(context, status + "", Toast.LENGTH_LONG, true).show();
                            }
                        } catch (Exception e) {
                            Toasty.error(context, Objects.requireNonNull(e.getMessage()), Toast.LENGTH_LONG, true).show();
                            e.printStackTrace();
                        }
                    }, paramArrayList, "follow_user");
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(holder.itemView.getContext(), OtherUserProfile.class);
                i1.putExtra("other_id", nearBy.getId() + "");
                i1.putExtra("other_username", "");
                holder.itemView.getContext().startActivity(i1);
                ((Activity) holder.itemView.getContext()).overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }
        });

    }

    @Override
    public int getItemCount() {
        return nearByUsers.size();
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        @BindView(R.id.img_user)
        CircleImageView img_user;
        @BindView(R.id.user_first_name)
        TextView user_first_name;
        @BindView(R.id.user_last_name)
        TextView user_last_name;
        @BindView(R.id.ll_follow_req)
        LinearLayout ll_follow_req;
        @BindView(R.id.tv_req_follow)
        TextView tv_req_follow;
        @BindView(R.id.ll_decline)
        LinearLayout ll_decline;


        public MyClassView(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
