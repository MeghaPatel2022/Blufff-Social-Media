package com.TBI.Client.Bluff.Adapter.WallPage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Model.SOS.NearBy;
import com.TBI.Client.Bluff.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NearByAdapter extends RecyclerView.Adapter<NearByAdapter.MyClassView> {

    ArrayList<NearBy> nearByArrayList;
    Activity activity;

    public NearByAdapter(ArrayList<NearBy> nearByArrayList, Activity activity) {
        this.nearByArrayList = nearByArrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sos_near_by, parent, false);

        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {
        NearBy nearBy = nearByArrayList.get(position);

        Glide.with(activity)
                .load(nearBy.getPhoto())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.img_user);
    }

    @Override
    public int getItemCount() {
        return nearByArrayList.size();
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        CircleImageView img_user;

        public MyClassView(@NonNull View itemView) {
            super(itemView);

            img_user = itemView.findViewById(R.id.img_user);
        }
    }
}
