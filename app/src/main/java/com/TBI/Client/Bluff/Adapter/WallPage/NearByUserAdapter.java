package com.TBI.Client.Bluff.Adapter.WallPage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Profile.OtherUserProfile;
import com.TBI.Client.Bluff.Model.SOS.NearBy;
import com.TBI.Client.Bluff.R;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class NearByUserAdapter extends RecyclerView.Adapter<NearByUserAdapter.MyClassView> {

    Context context;
    List<NearBy> nearByArrayList;

    public NearByUserAdapter(Context context, List<NearBy> nearByArrayList) {
        this.context = context;
        this.nearByArrayList = nearByArrayList;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_people_you_know, parent, false);

        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {
        NearBy nearBy = nearByArrayList.get(position);

        Glide.with(context)
                .load(nearBy.getPhoto())
                .into(holder.img_user);

        String[] s1 = nearBy.getFullName().split("\\s+");
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
            holder.user_first_name.setText(nearBy.getFullName());
            holder.user_last_name.setText("");
        }

        if (nearBy.getFollowByUser() == 0) {
            holder.tv_req_follow.setText("Follow");
        } else if (nearBy.getFollowByUser() == 1) {
            holder.tv_req_follow.setText("Following");
        } else {
            holder.tv_req_follow.setText("Requested");
        }

        holder.itemView.setOnClickListener(v -> {
            Intent i1 = new Intent(holder.itemView.getContext(), OtherUserProfile.class);
            i1.putExtra("other_id", nearByArrayList.get(position).getId() + "");
            i1.putExtra("other_username", "");
            holder.itemView.getContext().startActivity(i1);
            ((Activity) holder.itemView.getContext()).overridePendingTransition(R.anim.fade_in, R.anim.stay);
        });

    }

    @Override
    public int getItemCount() {
        return nearByArrayList.size();
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
