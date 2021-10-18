package com.TBI.Client.Bluff.Adapter.Profile;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Profile.OtherUserProfile;
import com.TBI.Client.Bluff.Model.View_Connection.Follower;
import com.TBI.Client.Bluff.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    Activity Context;
    List<Follower> item = new ArrayList<>();
    OtherUserProfile otherUserProfile;

    public FriendAdapter(Activity Context, List<Follower> item, OtherUserProfile otherUserProfile) {
        this.Context = Context;
        this.item = item;
        this.otherUserProfile = otherUserProfile;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_otherprofriendlist, null, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.txtname.setText(item.get(position).getFullName());
        Glide.with(Context)
                .load(item.get(position).getPhoto())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .dontAnimate()
                .into(holder.imgfriend);


    }


    private int getColor(int color) {
        return ContextCompat.getColor(Context, color);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgfriend;
        TextView txtname;


        public ViewHolder(final View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.txtname);
            imgfriend = itemView.findViewById(R.id.imgfriend);

        }
    }

}

