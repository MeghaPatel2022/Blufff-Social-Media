package com.TBI.Client.Bluff.Adapter.Post;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Model.GetProfile.Post;
import com.TBI.Client.Bluff.Model.View_Connection.Following;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.bumptech.glide.Glide;
import com.hendraanggrian.appcompat.widget.SocialTextView;

import net.iquesoft.iquephoto.models.Image;
import net.iquesoft.iquephoto.models.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FollowFrendAdapter extends RecyclerView.Adapter<FollowFrendAdapter.MyClassView> implements Filterable {

    Context context;
    List<Following> followingarray = new ArrayList<>();

    public FollowFrendAdapter(Context context, List<Following> followingarray) {
        this.context = context;
        this.followingarray = followingarray;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_send_user_list, parent, false);
        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {
        Following following = followingarray.get(position);

        Log.e("LLLL_following:/ ", following.getUsername());
        if (following.getPhoto().isEmpty()) {
            Glide
                    .with(context)
                    .load(following.getPhoto())
                    .placeholder(context.getResources().getDrawable(R.drawable.profile1))
                    .into(holder.img_user);
        } else {
            Glide
                    .with(context)
                    .load(following.getPhoto())
                    .into(holder.img_user);
        }

        holder.tv_fullname.setText(following.getFullName());
        holder.tv_username.setText("@" + following.getUsername());

        holder.img_chek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.img_chek.setVisibility(View.GONE);
                holder.img_unchek.setVisibility(View.VISIBLE);
                if (!Public_Function.shareUserList.isEmpty()){
                    for (int i = 0; i < Public_Function.shareUserList.size(); i++) {
                        if (!following.getId().equals(Public_Function.shareUserList.get(i))){
                            Public_Function.shareUserList.add(following.getId());
                        }
                    }
                } else {
                    Public_Function.shareUserList.add(following.getId());
                }

            }
        });

        holder.img_unchek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.img_unchek.setVisibility(View.GONE);
                holder.img_chek.setVisibility(View.VISIBLE);

                if (!Public_Function.shareUserList.isEmpty()){
                    for (int i = 0; i < Public_Function.shareUserList.size(); i++) {
                        if (following.getId().equals(Public_Function.shareUserList.get(i))){
                            Public_Function.shareUserList.remove(following.getId());
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return followingarray.size();
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        @BindView(R.id.img_user)
        CircleImageView img_user;
        @BindView(R.id.tv_fullname)
        TextView tv_fullname;
        @BindView(R.id.tv_username)
        SocialTextView tv_username;
        @BindView(R.id.img_chek)
        ImageView img_chek;
        @BindView(R.id.img_unchek)
        ImageView img_unchek;

        public MyClassView(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Following> filteredList = new ArrayList<>();
            Log.e("LLLLLL_con: ", String.valueOf(constraint));
            if (constraint == null || constraint.length() == 0 || constraint.equals("")) {
                filteredList.addAll(Public_Function.followingarray1);
            } else {
                filteredList.clear();
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (int i = 0; i <  Public_Function.followingarray1.size(); i++) {
                    Following item =  Public_Function.followingarray1.get(i);
                    if (item.getUsername().toLowerCase().contains(filterPattern) ||
                            item.getFullName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            followingarray.clear();
            followingarray.addAll(filteredList);
            notifyDataSetChanged();

            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        }
    };


}
