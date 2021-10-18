package com.TBI.Client.Bluff.Adapter.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.TBI.Client.Bluff.Model.GetBlockUser.Datum;
import com.TBI.Client.Bluff.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class BlockUser_Adapter extends BaseAdapter {

    List<Datum> userarrylist = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public BlockUser_Adapter(Context context, List<Datum> userarrylist) {
        this.userarrylist = userarrylist;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return userarrylist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return 0;
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
                    .into(mViewHolder.imguser);
        }

        mViewHolder.txtfullname.setText(userarrylist.get(position).getFullName() + "");
        mViewHolder.txtusername.setText(userarrylist.get(position).getUsername() + "");

        return convertView;
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

