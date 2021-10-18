package com.TBI.Client.Bluff.Adapter.WallPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.TBI.Client.Bluff.Model.Dosearch.Location;
import com.TBI.Client.Bluff.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Searchlocation_Adapter extends BaseAdapter {

    List<Location> userarrylist = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public Searchlocation_Adapter(Context context, List<Location> userarrylist) {
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

        mViewHolder.imguser.setVisibility(View.GONE);
        mViewHolder.imglocation.setVisibility(View.VISIBLE);

        mViewHolder.txtusername.setText(userarrylist.get(position).getLocation() + "");
        /*if (sharedpreference.getTheme(context).equalsIgnoreCase("white")) {
            mViewHolder.imguser.setImageResource(RM.drawable.black_location);
            mViewHolder.imguser.setBorderColor(context.getResources().getColor(RM.color.blacklight));
            mViewHolder.imguser.setBorderWidth(2);
        } else {
            mViewHolder.imguser.setImageResource(RM.drawable.map_selected);
            mViewHolder.imguser.setBorderWidth(2);
            mViewHolder.imguser.setBorderColor(context.getResources().getColor(RM.color.blacklight));
        }*/
        // mViewHolder.txtusername.setText(userarrylist.get(position).getUsername() + "");

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.imguser)
        CircleImageView imguser;
        @BindView(R.id.txtusername)
        TextView txtusername;
        @BindView(R.id.txtfullname)
        TextView txtfullname;
        @BindView(R.id.imglocation)
        ImageView imglocation;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

}

