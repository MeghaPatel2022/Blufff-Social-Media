package com.TBI.Client.Bluff.Adapter.Map;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Map.FriendsActivity;
import com.TBI.Client.Bluff.Fragment.Friend_Fragment;
import com.TBI.Client.Bluff.Model.GetFriends.GetCloseFrend;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.applozic.mobicomkit.Applozic;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CloseFriendAdapter extends RecyclerView.Adapter<CloseFriendAdapter.MyClassView> {

    ArrayList<GetCloseFrend> getCloseFrends;
    Activity activity;
    String type = "";
    private OtherFriendsAdapter otherFriendsAdapter;
    private Handler handler;

    public CloseFriendAdapter(ArrayList<GetCloseFrend> getCloseFrends, Activity activity, OtherFriendsAdapter otherFriendsAdapter, String type) {
        this.getCloseFrends = getCloseFrends;
        this.activity = activity;
        this.otherFriendsAdapter = otherFriendsAdapter;
        this.type = type;
        Applozic.init(activity.getApplicationContext(), activity.getString(R.string.application_key));
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_close_friend, parent, false);
        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {
        GetCloseFrend getCloseFrend = getCloseFrends.get(position);

        if (getCloseFrend.getPhoto().equals("")) {
            Glide.with(activity)
                    .load(getCloseFrend.getPhoto())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.img_user);
        } else {
            Glide
                    .with(activity)
                    .load(getCloseFrend.getPhoto())
                    .into(holder.img_user);
        }
        holder.img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcloseFrendsData(getCloseFrend.getUserId());
            }
        });

        String[] s1 = getCloseFrend.getFullName().split("\\s+");

        if (s1.length > 1) {
            if (s1.length <=2) {
                if (!s1[0].isEmpty()) {
                    holder.user_first_name.setText(s1[0]);
                }
                if (!s1[1].isEmpty()) {
                    holder.user_last_name.setText(s1[1]);
                }
            } else {
                if (!s1[0].isEmpty()) {
                    holder.user_first_name.setText(s1[0] + " "+ s1[1]);
                }
                if (!s1[1].isEmpty()) {
                    holder.user_last_name.setText(s1[2]);
                }
            }
        } else {
            holder.user_first_name.setText(getCloseFrend.getFullName());
            holder.user_last_name.setText("");
        }
    }

    private void getcloseFrendsData(int frends_id) {
        AndroidNetworking.post(geturl.BASE_URL + "manage_closed_friends")
                .addHeaders("Authorization", sharedpreference.getUserToken(activity))
                .addBodyParameter("user_id", sharedpreference.getUserId(activity))
                .addBodyParameter("friend_ids", String.valueOf(frends_id))
                .addBodyParameter("action", "0")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getString("success").equals("true")) {
                                handler = new Handler();
                                final Runnable r = () -> {
                                    if (!type.equals("fragment")) {
                                        FriendsActivity friendsActivity = new FriendsActivity();
                                        FriendsActivity.getFrendsData();
                                        FriendsActivity.getOtherFrendsData();
                                    } else {
                                        Friend_Fragment.getFrendsData();
                                        Friend_Fragment.getOtherFrendsData();
                                    }
                                };
                                handler.postDelayed(r, 200);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLL_Error: ", anError.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return getCloseFrends.size();
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_follow_req)
        LinearLayout img_cancel;
        @BindView(R.id.img_user)
        CircleImageView img_user;
        @BindView(R.id.user_first_name)
        TextView user_first_name;
        @BindView(R.id.user_last_name)
        TextView user_last_name;

        public MyClassView(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }
}
