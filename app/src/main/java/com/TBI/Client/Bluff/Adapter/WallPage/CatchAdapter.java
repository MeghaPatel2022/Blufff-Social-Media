package com.TBI.Client.Bluff.Adapter.WallPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.TBI.Client.Bluff.Model.GetDiscover.Post;
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

public class CatchAdapter extends BaseAdapter {

    List<Post> catcharraylist = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public CatchAdapter(Context context, List<Post> catcharraylist) {
        this.catcharraylist = catcharraylist;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return catcharraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_wall, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.simmar_id.startShimmer();
        mViewHolder.imgwall.setClipToOutline(true);
        mViewHolder.txtuname.setClipToOutline(true);
        mViewHolder.roundedImageView.setClipToOutline(true);
//        if (position == 0) {
//            mViewHolder.txtuname.setVisibility(View.GONE);
//            mViewHolder.roundedImageView.setVisibility(View.VISIBLE);
//            mViewHolder.imgwall.setVisibility(View.GONE);
//        } else {
            mViewHolder.txtuname.setVisibility(View.VISIBLE);
            mViewHolder.imgwall.setVisibility(View.VISIBLE);
            mViewHolder.roundedImageView.setVisibility(View.GONE);
            if (catcharraylist.get(position).getImage() != null) {
                if (!catcharraylist.get(position).getImage().equals("") && !catcharraylist.get(position).getImage().equals("null")) {
                    Glide
                            .with(context)
                            .load(catcharraylist.get(position).getImage())
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    mViewHolder.simmar_id.stopShimmer();
                                    mViewHolder.simmar_id.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .dontAnimate()
                            .into(mViewHolder.imgwall);


                } else {
                    mViewHolder.imgwall.setImageDrawable(context.getResources().getDrawable(R.drawable.grey_placeholder));
                }
                if (catcharraylist.get(position).getType().equalsIgnoreCase("post")) {
                    mViewHolder.txtuname.setText(catcharraylist.get(position).getUsername() + "");
                } else {
                    mViewHolder.txtuname.setText(catcharraylist.get(position).getFullName() + "");
                }
            }

//        }


        return convertView;
    }

    public void addAll(List<Post> discoverarray) {
        catcharraylist.addAll(discoverarray);
        notifyDataSetChanged();
    }

    public void updatenotifity(Post post, int i) {
        catcharraylist.set(i, post);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.imgwall)
        ImageView imgwall;
        @BindView(R.id.roundedImageView)
        ImageView roundedImageView;
        @BindView(R.id.txtuname)
        TextView txtuname;
        @BindView(R.id.simmar_id)
        ShimmerFrameLayout simmar_id;

        /*@BindView(RM.id.imgonline)
        ImageView imgonline;
        @BindView(RM.id.txtname)
        TextView txtname;
        @BindView(RM.id.txtlastmsg)
        TextView txtlastmsg;
        @BindView(RM.id.txttime)
        TextView txttime;
        @BindView(RM.id.imgunread)
        TextView imgunread;
*/
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}

