package com.TBI.Client.Bluff.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.TBI.Client.Bluff.Model.GetDiscover.Post;
import com.TBI.Client.Bluff.Model.GetToken.Datum;
import com.TBI.Client.Bluff.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Token_Adapter extends BaseAdapter {

    List<Datum> catcharraylist = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public Token_Adapter(Context context, List<Datum> catcharraylist) {
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

        mViewHolder.imgwall.setClipToOutline(true);
        mViewHolder.txtuname.setClipToOutline(true);
        mViewHolder.txtuname.setVisibility(View.GONE);
        mViewHolder.roundedImageView.setVisibility(View.GONE);
        mViewHolder.roundedImageView.setClipToOutline(true);

        if (!catcharraylist.get(position).getImage().equals("") && !catcharraylist.get(position).getImage().equals(null) && !catcharraylist.get(position).getImage().equals("null")) {
            Glide
                    .with(context)
                    .load(catcharraylist.get(position).getImage())
                    .placeholder(R.drawable.grey_placeholder)
                    .dontAnimate()
                    .into(mViewHolder.imgwall);
        } else {
            mViewHolder.imgwall.setImageDrawable(context.getResources().getDrawable(R.drawable.grey_placeholder));
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.imgwall)
        ImageView imgwall;
        @BindView(R.id.roundedImageView)
        ImageView roundedImageView;
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

