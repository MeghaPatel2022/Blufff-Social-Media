package com.TBI.Client.Bluff.Adapter.Profile.FriendsFollowers;

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
import com.TBI.Client.Bluff.Model.A2Z.A;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.TBI.Client.Bluff.Utils.geturl.BASE_URL;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.MyClassView> {

    private List<A> aList = new ArrayList<>();
    private Activity context;

    public FriendsAdapter(List<A> aList, Activity context) {
        this.aList = aList;
        this.context = context;
        AndroidNetworking.initialize(context);
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_follow_followers,parent,false);

        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {

        A a = aList.get(position);

        String[] s1 = a.getFullName().split("\\s+");

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
            holder.user_first_name.setText(a.getFullName());
            holder.user_last_name.setText("");
        }

        Glide
                .with(context)
                .load(a.getPhoto())
                .error(R.drawable.placeholder)
                .into(holder.img_user);

        int follow_status = a.getFollowByUser();
        Log.e("LLLL_Request: ", String.valueOf(follow_status));

        if (follow_status == 0) {
            holder.tv_follow_accept.setText("FOLLOW");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.ll_follow_req.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.textcolor)));
            }

        } else if (follow_status == 1) {
            Log.e("LLLL_Request: ", String.valueOf(follow_status));
            holder.tv_follow_accept.setText("FOLLOWING");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.ll_follow_req.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.following_color)));
            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.ll_follow_req.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.darkgrey)));
            }
            holder.tv_follow_accept.setText("REQUESTED");

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(context, OtherUserProfile.class);
                i1.putExtra("other_id", a.getId() + "");
                i1.putExtra("other_username", "");
                context.startActivity(i1);
                context. overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }
        });
        
        holder.ll_follow_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Public_Function.isInternetConnected(context)) {
                    Toasty.warning(context, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {

                    Public_Function.Show_Progressdialog(context);

                    setFollowStatus(holder,a);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return aList.size();
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        @BindView(R.id.img_user)
        CircleImageView img_user;
        @BindView(R.id.user_first_name)
        TextView user_first_name;
        @BindView(R.id.user_last_name)
        TextView user_last_name;
        @BindView(R.id.tv_follow_accept)
        TextView tv_follow_accept;
        @BindView(R.id.ll_follow_req)
        LinearLayout ll_follow_req;

        public MyClassView(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }

    private void setFollowStatus(MyClassView holder,A a){
        AndroidNetworking.post(BASE_URL+"follow_user")
                .addHeaders("Authorization", sharedpreference.getUserToken(context))
                .addBodyParameter("user_id", String.valueOf(a.getId()))
                .addBodyParameter("followed_by",sharedpreference.getUserId(context))
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
                                holder.ll_follow_req.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.textcolor)));
                            }

                        } else if (follow_status == 1) {
                            Log.e("LLLL_Request: ", String.valueOf(follow_status));
                            holder.tv_follow_accept.setText("FOLLOWING");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                holder.ll_follow_req.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.following_color)));
                            }
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                holder.ll_follow_req.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.darkgrey)));
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
