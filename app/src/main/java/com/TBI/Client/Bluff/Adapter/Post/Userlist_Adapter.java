package com.TBI.Client.Bluff.Adapter.Post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.TBI.Client.Bluff.Model.SearchUser.Datum;
import com.TBI.Client.Bluff.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Userlist_Adapter extends BaseAdapter {

    List<Datum> userarrylist = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public Userlist_Adapter(Context context) {
        this.userarrylist = new ArrayList<>();
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
            convertView = inflater.inflate(R.layout.layout_searchuser, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }


        if (!userarrylist.get(position).getPhoto().equals("") && !userarrylist.get(position).getPhoto().equals(null) && !userarrylist.get(position).getPhoto().equals("null")) {
            Glide.with(context)
                    .load(userarrylist.get(position).getPhoto())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .dontAnimate()
                    .into(mViewHolder.imguser);
        } else {
            mViewHolder.imguser.setImageDrawable(context.getResources().getDrawable(R.drawable.placeholder));
        }

        mViewHolder.txtfullname.setText(userarrylist.get(position).getFullName() + "");
        mViewHolder.txtusername.setText(userarrylist.get(position).getUsername() + "");

        return convertView;
    }

    public void AddAll(List<Datum> userlist) {
        userarrylist = new ArrayList<Datum>();
        userarrylist.addAll(userlist);
        notifyDataSetChanged();
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

        @BindView(R.id.imguser)
        CircleImageView imguser;
        @BindView(R.id.txtusername)
        TextView txtusername;
        @BindView(R.id.txtfullname)
        TextView txtfullname;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }


}

