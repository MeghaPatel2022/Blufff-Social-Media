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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Profile.OtherUserProfile;
import com.TBI.Client.Bluff.Adapter.Profile.FriendsFollowers.FriendsAdapter;
import com.TBI.Client.Bluff.Model.A2Z.A;
import com.TBI.Client.Bluff.Model.GetOtherUser.GetOtherUser;
import com.TBI.Client.Bluff.Model.getNewNotification.Suggested;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.TBI.Client.Bluff.Utils.geturl.BASE_URL;

public class SuggestedAdapter extends RecyclerView.Adapter<SuggestedAdapter.MyClassView> {

    Activity activity;
    List<Suggested> suggestedList;
    private ArrayList<Integer> account_privacy = new ArrayList<>();

    public SuggestedAdapter(Activity activity, List<Suggested> suggestedList) {
        this.activity = activity;
        this.suggestedList = suggestedList;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_request, parent, false);

        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {
        holder.ll_decline.setVisibility(View.GONE);

        Suggested suggested = suggestedList.get(position);

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(activity) + ""));
        paramArrayList.add(new param("other_user_id", suggested.getId() + ""));
        paramArrayList.add(new param("other_username", suggested.getUsername() + ""));
        paramArrayList.add(new param("timezone", Public_Function.gettimezone()));

        new geturl().getdata(activity, data -> {

            try {
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                if (message) {
                    GetOtherUser login = new Gson().fromJson(data, GetOtherUser.class);
                    if (!login.getData().isEmpty()) {
                        account_privacy.add(login.getData().get(0).getAccountPrivacy());

                        int follow_status = login.getData().get(0).getFollowingByYou();
                        Log.e("LLLL_Request: ", String.valueOf(follow_status));

                        if (follow_status == 0) {
                            holder.tv_follow_accept.setText("FOLLOW");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                holder.tv_follow_accept.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.textcolor)));
                            }

                        } else if (follow_status == 1) {
                            Log.e("LLLL_Request: ", String.valueOf(follow_status));
                            holder.tv_follow_accept.setText("FOLLOWING");

                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                holder.tv_follow_accept.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.darkgrey)));
                            }
                            holder.tv_follow_accept.setText("REQUESTED");

                        }


                    }
                }
            } catch (Exception e) {
                Log.e("LLLL_Other_USer: ", e.getMessage());
                Toasty.error(activity, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }

        }, paramArrayList, "view_profile");

        Glide.with(activity)
                .load(suggested.getPhoto())
                .error(R.drawable.placeholder)
                .into(holder.img_user);

        String[] s1 = suggested.getFullName().split("\\s+");
        if (s1.length > 1) {
            if (!s1[0].isEmpty())
                holder.user_first_name.setText(s1[0]);
            if (!s1[1].isEmpty())
                holder.user_last_name.setText(s1[1]);
        } else {
            holder.user_first_name.setText(suggested.getFullName());
            holder.user_last_name.setText("");
        }

        holder.ll_follow_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Public_Function.isInternetConnected(activity)) {
                    Toasty.warning(activity, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {

                    Public_Function.Show_Progressdialog(activity);
                    setFollowStatus(holder,suggested);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(holder.itemView.getContext(), OtherUserProfile.class);
                i1.putExtra("other_id", suggested.getId() + "");
                i1.putExtra("other_username", "");
                holder.itemView.getContext().startActivity(i1);
                ((Activity) holder.itemView.getContext()).overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }
        });

    }

    public void addAll(List<Suggested> suggesteds) {
        suggestedList.clear();
        suggestedList.addAll(suggesteds);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return suggestedList.size();
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_follow_accept)
        TextView tv_follow_accept;
        @BindView(R.id.img_user)
        CircleImageView img_user;
        @BindView(R.id.user_first_name)
        TextView user_first_name;
        @BindView(R.id.user_last_name)
        TextView user_last_name;
        @BindView(R.id.ll_follow_req)
        LinearLayout ll_follow_req;
        @BindView(R.id.ll_decline)
        LinearLayout ll_decline;

        public MyClassView(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

    }

    private void setFollowStatus(MyClassView holder, Suggested suggested){
        AndroidNetworking.post(BASE_URL+"follow_user")
                .addHeaders("Authorization", sharedpreference.getUserToken(activity))
                .addBodyParameter("user_id", String.valueOf(suggested.getId()))
                .addBodyParameter("followed_by",sharedpreference.getUserId(activity))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Public_Function.Hide_ProgressDialogue();
                        int follow_status = 0;
                        try {
                            follow_status = response.getInt("following_by_you");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("LLLL_Request: ", String.valueOf(follow_status));


                        if (follow_status == 0) {
                            holder.tv_follow_accept.setText("FOLLOW");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                holder.ll_follow_req.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.textcolor)));
                            }

                        } else if (follow_status == 1) {
                            Log.e("LLLL_Request: ", String.valueOf(follow_status));
                            holder.tv_follow_accept.setText("FOLLOWING");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                holder.ll_follow_req.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.follow_accept_color)));
                            }
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                holder.ll_follow_req.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.darkgrey)));
                            }
                            holder.tv_follow_accept.setText("REQUESTED");

                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }
}
