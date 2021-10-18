package com.TBI.Client.Bluff.Adapter.Profile.Notifications;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Model.getNewNotification.Star;
import com.TBI.Client.Bluff.R;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.MyClassView> {

    List<Star> starList;
    Activity context;

    public StarAdapter(List<Star> starList, Activity context) {
        this.starList = starList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_star, parent, false);

        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {
        Star star = starList.get(position);

        Glide
                .with(context)
                .load(star.getPhoto())
                .error(R.drawable.placeholder)
                .into(holder.img_user);

        String[] s1 = star.getFullName().split("\\s+");

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
            holder.user_first_name.setText(star.getFullName());
            holder.user_last_name.setText("");
        }

        holder.ratingBar.setRating(star.getRates());
        holder.ratingBar.setIsIndicator(true);
    }

    public void addAll(List<Star> list) {
        starList.clear();
        starList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return starList.size();
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        @BindView(R.id.img_user)
        CircleImageView img_user;
        @BindView(R.id.user_first_name)
        TextView user_first_name;
        @BindView(R.id.user_last_name)
        TextView user_last_name;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;

        public MyClassView(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
