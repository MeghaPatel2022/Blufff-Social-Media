package com.TBI.Client.Bluff.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.TBI.Client.Bluff.Model.GetToken.GifImage;
import com.TBI.Client.Bluff.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TokenList_adapter extends BaseAdapter {

    List<GifImage> catcharraylist = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    String imagurl = "";

    public TokenList_adapter(Context context, List<GifImage> catcharraylist, String image) {
        this.catcharraylist = catcharraylist;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        imagurl = image;
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

        mViewHolder.imgwall.setClipToOutline(true);
        mViewHolder.imggif.setClipToOutline(true);
        mViewHolder.txtuname.setVisibility(View.GONE);
        mViewHolder.roundedImageView.setVisibility(View.VISIBLE);
        mViewHolder.imggif.setVisibility(View.VISIBLE);
        mViewHolder.imgwall.setVisibility(View.VISIBLE);
        mViewHolder.roundedImageView.setClipToOutline(true);

        if (!imagurl.equals("") && !imagurl.equals(null) && !imagurl.equals("null")) {
            Glide
                    .with(context)
                    .load(imagurl)
                    .placeholder(R.drawable.grey_placeholder)
                    .dontAnimate()
                    .into(mViewHolder.roundedImageView);
        } else {
            mViewHolder.roundedImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.grey_placeholder));
        }

        if (!catcharraylist.get(position).getImage().equals("") && !catcharraylist.get(position).getImage().equals(null) && !catcharraylist.get(position).getImage().equals("null")) {
            Glide
                    .with(context)
                    .load(catcharraylist.get(position).getImage())
                    .dontAnimate()
                    .into(mViewHolder.imggif);
            mViewHolder.imggif.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(android.R.color.transparent)));
        } else {
            mViewHolder.imggif.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(android.R.color.transparent)));
            //FmViewHolder.imgwall.settint(context.getResources().getDrawable(RM.drawable.grey_placeholder));
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.imgwall)
        ImageView imgwall;
        @BindView(R.id.roundedImageView)
        ImageView roundedImageView;
        @BindView(R.id.imggif)
        ImageView imggif;
        @BindView(R.id.txtuname)
        TextView txtuname;

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

