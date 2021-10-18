package com.TBI.Client.Bluff.Adapter.Post;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Model.Like.LikeData;
import com.TBI.Client.Bluff.Model.View_Connection.Following;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.MyClassView> implements Filterable {

    List<LikeData> likeData = new ArrayList<>();
    Context context;

    public LikeAdapter(List<LikeData> likeData, Context context) {
        this.likeData = likeData;
        this.context = context;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_follower,parent,false);

        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {
        holder.lnright.setVisibility(View.GONE);

        LikeData likeData1 = likeData.get(position);

        Glide.with(holder.itemView.getContext())
                .load(likeData1.getPhoto())
                .error(R.drawable.placeholder)
                .into(holder.imgavtar);

        holder.txtname.setText(likeData1.getUsername());
        holder.txtuname.setText(likeData1.getFullName());
    }

    @Override
    public int getItemCount() {
        return likeData.size();
    }
    public void addAll(List<LikeData> likeDataList){
        likeData.clear();
        Public_Function.likeDataArrayList.clear();
        likeData.addAll(likeDataList);
        Public_Function.likeDataArrayList.addAll(likeDataList);
        notifyDataSetChanged();
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        @BindView(R.id.imgavtar)
        CircleImageView imgavtar;
        @BindView(R.id.txtuname)
        TextView txtuname;
        @BindView(R.id.txtname)
        TextView txtname;
        @BindView(R.id.lnright)
        LinearLayout lnright;


        public MyClassView(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<LikeData> filteredList = new ArrayList<>();
            Log.e("LLLLLL_con: ", String.valueOf(constraint));
            if (constraint == null || constraint.length() == 0 || constraint.equals("")) {
                filteredList.addAll(Public_Function.likeDataArrayList);
            } else {
                filteredList.clear();
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (int i = 0; i <  Public_Function.likeDataArrayList.size(); i++) {
                    LikeData item =  Public_Function.likeDataArrayList.get(i);
                    if (item.getUsername().toLowerCase().contains(filterPattern) ||
                            item.getFullName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            likeData.clear();
            likeData.addAll(filteredList);
            notifyDataSetChanged();

            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        }
    };


}
