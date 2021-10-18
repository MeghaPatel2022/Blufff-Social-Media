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
import com.TBI.Client.Bluff.Model.GetFriends.GetOtherFriends;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class OtherFriendsAdapter extends RecyclerView.Adapter<OtherFriendsAdapter.MyclasView> {

    ArrayList<GetOtherFriends> getOtherFriends;
    ArrayList<GetCloseFrend> getCloseFrends;
    Activity activity;
    String type = "";
    private CloseFriendAdapter closeFriendAdapter;
    private Handler handler;

    public OtherFriendsAdapter(ArrayList<GetOtherFriends> getOtherFriends, ArrayList<GetCloseFrend> getCloseFrends, Activity activity, CloseFriendAdapter closeFriendAdapter, String type) {
        this.getOtherFriends = getOtherFriends;
        this.getCloseFrends = getCloseFrends;
        this.activity = activity;
        this.closeFriendAdapter = closeFriendAdapter;
        this.type = type;
    }

    @NonNull
    @Override
    public MyclasView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_other_friends, parent, false);

        return new MyclasView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyclasView holder, int position) {
        GetOtherFriends getOtherFriend = getOtherFriends.get(position);

        if (getOtherFriend.getPhoto().equals("")) {
            Glide.with(activity)
                    .load(getOtherFriend.getPhoto())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.img_user);
        } else {
            Glide.with(activity)
                    .load(getOtherFriend.getPhoto())
                    .into(holder.img_user);
        }

        String[] s1 = getOtherFriend.getFullName().split("\\s+");

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
            holder.user_first_name.setText(getOtherFriend.getFullName());
            holder.user_last_name.setText("");
        }

        holder.itemView.setOnClickListener(v -> {
            Log.e("LLL_click...", String.valueOf(Public_Function.getCloseFrends.size()));
            Log.e("LLL_click11...", String.valueOf(getCloseFrends.size()));
//                Friend_Fragment.getFrendsData();
            if (getCloseFrends.size() < 5) {
                getOtherFrendsData(getOtherFriend.getUserId(), activity);
            } else {

            }
        });
    }

    private void getOtherFrendsData(int frends_id, Activity activity) {
        AndroidNetworking.post(geturl.BASE_URL + "manage_closed_friends")
                .addHeaders("Authorization", sharedpreference.getUserToken(activity))
                .addBodyParameter("user_id", sharedpreference.getUserId(activity))
                .addBodyParameter("friend_ids", String.valueOf(frends_id))
                .addBodyParameter("action", "1")
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
        return getOtherFriends.size();
    }

    public class MyclasView extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_follow_req)
        LinearLayout img_select;
        @BindView(R.id.img_user)
        CircleImageView img_user;
        @BindView(R.id.user_first_name)
        TextView user_first_name;
        @BindView(R.id.user_last_name)
        TextView user_last_name;

        public MyclasView(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }
}
