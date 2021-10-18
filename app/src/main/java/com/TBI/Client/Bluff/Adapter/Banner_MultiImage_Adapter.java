package com.TBI.Client.Bluff.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.TBI.Client.Bluff.Model.GetSaved_Post.Datum;
import com.TBI.Client.Bluff.Model.Get_bannerdetail.Photo;
import com.TBI.Client.Bluff.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Banner_MultiImage_Adapter extends BaseAdapter {

    List<Photo> userarrylist = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public Banner_MultiImage_Adapter(Context context, List<Photo> getsavepost) {
        Log.d("mn13size", getsavepost.size() + "");
        this.userarrylist = getsavepost;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return userarrylist.size();
    }

    @Override
    public Object getItem(int position) {
        return userarrylist.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_savedpost, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }


        mViewHolder.imgsaved.setClipToOutline(true);

        if (!userarrylist.get(position).getBannerImg().equals("") && !userarrylist.get(position).getBannerImg().equals(null) && !userarrylist.get(position).getBannerImg().equals("null")) {
            Glide
                    .with(context)
                    .load(userarrylist.get(position).getBannerImg())
                    .placeholder(R.drawable.grey_placeholder)
                    .error(R.drawable.grey_placeholder)
                    .centerCrop()
                    .into(mViewHolder.imgsaved);

        } else {
            mViewHolder.imgsaved.setImageDrawable(context.getResources().getDrawable(R.drawable.grey_placeholder));
        }

        //    mViewHolder.imgsaved.getLayoutParams().height = mViewHolder.imgsaved.getLayoutParams().width / 2;

        // mViewHolder.imgsaved.getLayoutParams().height = mViewHolder.imgsaved.getLayoutParams().width;

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        if (getCount() > 0) {
            return getCount();
        } else {
            return super.getViewTypeCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolder {

        @BindView(R.id.imgsaved)
        ImageView imgsaved;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }


}

