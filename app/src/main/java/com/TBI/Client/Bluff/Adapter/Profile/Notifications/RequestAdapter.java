package com.TBI.Client.Bluff.Adapter.Profile.Notifications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Profile.OtherUserProfile;
import com.TBI.Client.Bluff.Model.GetFeed.RequestedUser;
import com.TBI.Client.Bluff.Model.GetOtherUser.GetOtherUser;
import com.TBI.Client.Bluff.Model.getNewNotification.Requested;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.MyAsyncTaskCallback;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyClassView> {

    Activity activity;
    List<Requested> requestedList;

    String fullname = "";

    public RequestAdapter(Activity activity, List<Requested> requestedList) {
        this.activity = activity;
        this.requestedList = requestedList;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_request, parent, false);

        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {

        Requested requested = requestedList.get(position);

        holder.follow_accept.setImageDrawable(activity.getResources().getDrawable(R.drawable.accept));
        holder.tv_follow_accept.setText("Accept");

        Glide
                .with(activity)
                .load(requested.getPhoto())
                .error(R.drawable.placeholder)
                .into(holder.img_user);

        String[] s1 = requested.getFullName().split("\\s+");
        if (s1.length > 1) {
            if (!s1[0].isEmpty())
                holder.user_first_name.setText(s1[0]);
            if (!s1[1].isEmpty())
                holder.user_last_name.setText(s1[1]);
        } else {
            holder.user_first_name.setText(requested.getFullName());
            holder.user_last_name.setText("");
        }

        holder.ll_follow_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = position;

                if (!Public_Function.isInternetConnected(activity)) {
                    Toasty.warning(activity, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    Accept_RejectRequest(requestedList.get(pos), "1");
                }

            }
        });

        holder.ll_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();

                if (!Public_Function.isInternetConnected(activity)) {
                    Toasty.warning(activity, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    Accept_RejectRequest(requestedList.get(pos), "0");
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(holder.itemView.getContext(), OtherUserProfile.class);
                i1.putExtra("other_id", requestedList.get(position).getId() + "");
                i1.putExtra("other_username", "");
                holder.itemView.getContext().startActivity(i1);
                ((Activity) holder.itemView.getContext()).overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }
        });

    }

    public void addAll(List<Requested> requesteds) {
        requestedList.clear();
        requestedList.addAll(requesteds);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return requestedList.size();
    }

    private void Accept_RejectRequest(Requested friend, String status) {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(activity) + ""));
        paramArrayList.add(new param("other_user_id", friend.getId() + ""));
        paramArrayList.add(new param("status", status + ""));

        new geturl().getdata(activity, data -> {
            try {
                Public_Function.Hide_ProgressDialogue();
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status1 = "";
                status1 = object.optString("msg");
                if (message) {
                    if (status1.equalsIgnoreCase("0")) {
                        requestedList.remove(friend);
                    } else {
                        requestedList.remove(friend);
                    }
                    notifyDataSetChanged();
                } else {
                    Toasty.error(activity, status1 + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                Toasty.error(activity, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
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
        @BindView(R.id.img_follow_accept)
        ImageView follow_accept;
        @BindView(R.id.tv_follow_accept)
        TextView tv_follow_accept;
        @BindView(R.id.ll_follow_req)
        LinearLayout ll_follow_req;
        @BindView(R.id.ll_decline)
        LinearLayout ll_decline;


        public MyClassView(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}
