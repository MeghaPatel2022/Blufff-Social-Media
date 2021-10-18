package com.TBI.Client.Bluff.Adapter.WallPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.TBI.Client.Bluff.Model.Dosearch.Hastag;
import com.TBI.Client.Bluff.Model.Dosearch.Location;
import com.TBI.Client.Bluff.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Searchhashtag_Adapter extends BaseAdapter {

    List<Hastag> userarrylist = new ArrayList<Hastag>();
    LayoutInflater inflater;
    Context context;

    public Searchhashtag_Adapter(Context context, List<Hastag> userarrylist) {
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
        mViewHolder.framehash.setVisibility(View.VISIBLE);

        mViewHolder.txtusername.setText(userarrylist.get(position).getHastags() + "");
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
        @BindView(R.id.framehash)
        FrameLayout framehash;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

}

