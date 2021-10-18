package com.TBI.Client.Bluff.Adapter.Home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Model.GetSaved_Post.Datum;
import com.TBI.Client.Bluff.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Saved_Post_Adapter extends RecyclerView.Adapter<Saved_Post_Adapter.MyClassView> {

    private PostClickListner clickListener;
    List<com.TBI.Client.Bluff.Model.GetSaved_Post.Datum> userarrylist = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public interface PostClickListner {
        void onItemClick(View view, int i);
    }

    public Saved_Post_Adapter(Context context, List<com.TBI.Client.Bluff.Model.GetSaved_Post.Datum> getsavepost) {
        this.userarrylist = new ArrayList<>();
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getItemCount() {
        return userarrylist.size();
    }

    public void setClickListener(PostClickListner itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public void AddAll(List<Datum> userlist) {
        userarrylist.clear();
        userarrylist.addAll(userlist);
        notifyDataSetChanged();
    }

    public void remove(Datum selectmodel) {
        userarrylist.remove(selectmodel);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_savedpost,parent,false);
        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView mViewHolder, int position) {
        mViewHolder.imgsaved.setClipToOutline(true);

        if (!userarrylist.get(position).getImage().equals("") && userarrylist.get(position).getImage()!=null && !userarrylist.get(position).getImage().equals("null")) {

            Glide
                    .with(context)
                    .load(userarrylist.get(position).getImage())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Log.e("LLLLL_ErrorLOadImage: ", e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            mViewHolder.simmar_id.stopShimmer();
                            mViewHolder.simmar_id.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .centerCrop()
                    .into(mViewHolder.imgsaved);

        } else {
            mViewHolder.simmar_id.stopShimmer();
            mViewHolder.simmar_id.setVisibility(View.GONE);
            Glide
                    .with(context)
                    .load(R.drawable.grey_placeholder)
                    .centerCrop()
                    .into(mViewHolder.imgsaved);
        }
    }

    public class MyClassView extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imgsaved)
        ImageView imgsaved;
        @BindView(R.id.simmar_id)
        ShimmerFrameLayout simmar_id;


        public MyClassView(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (Saved_Post_Adapter.this.clickListener != null) {
                Saved_Post_Adapter.this.clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}

