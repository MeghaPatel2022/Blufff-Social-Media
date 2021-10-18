package com.TBI.Client.Bluff.Adapter.Profile.Notifications;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Profile.SOSReport;
import com.TBI.Client.Bluff.Model.getNewNotification.So;
import com.TBI.Client.Bluff.R;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SOSAdapter extends RecyclerView.Adapter<SOSAdapter.MyClassView> {

    List<So> soList;
    Activity context;

    public SOSAdapter(List<So> soList, Activity context) {
        this.soList = soList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sos, parent, false);

        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {

        So sos = soList.get(position);
        Glide
                .with(context)
                .load(sos.getPhoto())
                .error(R.drawable.placeholder)
                .into(holder.img_user);

        String[] s1 = sos.getFullName().split("\\s+");
        if (s1.length > 1) {
            if (!s1[0].isEmpty())
                holder.user_first_name.setText(s1[0]);
            if (!s1[1].isEmpty())
                holder.user_last_name.setText(s1[1]);
        } else {
            holder.user_first_name.setText(sos.getFullName());
            holder.user_last_name.setText("");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SOSReport.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }
        });
    }

    public void addAll(List<So> sos) {
        soList.clear();
        soList.addAll(sos);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return soList.size();
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        @BindView(R.id.img_user)
        ImageView img_user;
        @BindView(R.id.user_first_name)
        TextView user_first_name;
        @BindView(R.id.user_last_name)
        TextView user_last_name;

        public MyClassView(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
