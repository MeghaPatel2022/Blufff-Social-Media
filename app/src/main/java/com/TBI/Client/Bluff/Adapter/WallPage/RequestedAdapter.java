package com.TBI.Client.Bluff.Adapter.WallPage;

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
import com.TBI.Client.Bluff.Model.GetFeed.RequestedUser;
import com.TBI.Client.Bluff.Model.View_Connection.Request;
import com.TBI.Client.Bluff.Model.getNewNotification.Requested;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.MyAsyncTaskCallback;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.applozic.mobicommons.commons.core.utils.Utils;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class RequestedAdapter extends RecyclerView.Adapter<RequestedAdapter.MyClassView> {

    List<RequestedUser> requestedUsers = new ArrayList<>();
    Activity context;

    public RequestedAdapter(Activity activity, List<RequestedUser> requestedUsers) {
        this.requestedUsers = requestedUsers;
        this.context = activity;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_people_you_know, parent, false);

        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {

        RequestedUser requestedUser = requestedUsers.get(position);

        int follow_status = requestedUser.getFollowedBy();
        Log.e("LLLL_Request: ", String.valueOf(follow_status));

        if (follow_status == 0) {
            holder.tv_req_follow.setText("FOLLOW");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.tv_req_follow.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.textcolor)));
            }

        } else if (follow_status == 1) {
            holder.ll_follow_req.setVisibility(View.VISIBLE);
            holder.ll_follow_accept.setVisibility(View.GONE);
            holder.ll_decline.setVisibility(View.GONE);
            Log.e("LLLL_Request: ", String.valueOf(follow_status));
            holder.tv_req_follow.setText("FOLLOWING");

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.tv_req_follow.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.darkgrey)));
            }
            holder.tv_req_follow.setText("REQUESTED");
        }

        holder.ll_follow_req.setVisibility(View.GONE);
        holder.ll_follow_accept.setVisibility(View.VISIBLE);
        holder.ll_decline.setVisibility(View.VISIBLE);

        holder.ll_follow_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = position;

                if (!Public_Function.isInternetConnected(context)) {
                    Toasty.warning(context, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    Accept_RejectRequest(requestedUsers.get(pos), "1");
                }

            }
        });

        holder.ll_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();

                if (!Public_Function.isInternetConnected(context)) {
                    Toasty.warning(context, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    Accept_RejectRequest(requestedUsers.get(pos), "0");
                }
            }
        });

        Glide.with(context)
                .load(requestedUser.getPhoto())
                .error(R.drawable.placeholder)
                .into(holder.img_user);

        String[] s1 = requestedUser.getFullName().split("\\s+");
        if (s1.length > 1) {
            if (!s1[0].isEmpty())
                holder.user_first_name.setText(s1[0]);
            if (!s1[1].isEmpty())
                holder.user_last_name.setText(s1[1]);
        } else {
            holder.user_first_name.setText(requestedUser.getFullName());
            holder.user_last_name.setText("");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(holder.itemView.getContext(), OtherUserProfile.class);
                i1.putExtra("other_id", requestedUser.getId() + "");
                i1.putExtra("other_username", "");
                holder.itemView.getContext().startActivity(i1);
                ((Activity) holder.itemView.getContext()).overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }
        });


    }

    @Override
    public int getItemCount() {
        return requestedUsers.size();
    }

    private void Accept_RejectRequest(RequestedUser friend, String status) {

        //Public_Function.Show_Progressdialog(context);


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(context) + ""));
        paramArrayList.add(new param("other_user_id", friend.getId() + ""));
        paramArrayList.add(new param("status", status + ""));


        new geturl().getdata(context, new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {
                try {
                    Public_Function.Hide_ProgressDialogue();
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("msg");
                    if (message) {
                        /*int follow_status = object.optInt("following_by_you");
                        friend.setFollowByFollowing(follow_status);*/
                        if (status.equalsIgnoreCase("0")) {
                            requestedUsers.remove(friend);
                        } else {
                            requestedUsers.remove(friend);
                        }
                        notifyDataSetChanged();
                    } else {
                        Toasty.error(context, status + "", Toast.LENGTH_LONG, true).show();
                    }
                } catch (Exception e) {
                    Toasty.error(context, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                    e.printStackTrace();
                }
            }
        }, paramArrayList, "accept_reject");
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
        @BindView(R.id.ll_follow_accept)
        LinearLayout ll_follow_accept;

        public MyClassView(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
