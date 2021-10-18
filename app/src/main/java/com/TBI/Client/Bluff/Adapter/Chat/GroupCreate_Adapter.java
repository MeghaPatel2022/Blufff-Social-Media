package com.TBI.Client.Bluff.Adapter.Chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.TBI.Client.Bluff.Activity.Chat.CreateChat;
import com.TBI.Client.Bluff.Activity.Profile.OtherUserProfile;
import com.TBI.Client.Bluff.Activity.Profile.ProfilePage;
import com.TBI.Client.Bluff.Model.GetFreinds.Datum;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class GroupCreate_Adapter extends BaseAdapter {

    public static ArrayList<String> selestgroupid = new ArrayList<>();
    List<Datum> userarrylist = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public GroupCreate_Adapter(Context context, List<Datum> userarrylist) {
        this.userarrylist = userarrylist;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
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
    public int getCount() {
        return userarrylist.size();
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
            convertView = inflater.inflate(R.layout.layout_groupselectuser, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }


        if (!userarrylist.get(position).getPhoto().equals("") && !userarrylist.get(position).getPhoto().equals(null) && !userarrylist.get(position).getPhoto().equals("null")) {
           /* Picasso.get()
                    .load(userarrylist.get(position).getPhoto())
                    .placeholder(RM.drawable.placeholder)
                    .error(RM.drawable.placeholder)
                    .into(mViewHolder.imguser);*/
            Glide
                    .with(context)
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

        mViewHolder.lnuserclick.setTag(position);
        mViewHolder.lnuserclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = (int) view.getTag();
                if (sharedpreference.getUserId(context).equalsIgnoreCase("" + userarrylist.get(pos).getUserId())) {

                    Intent i1 = new Intent(context, ProfilePage.class);
                    i1.putExtra("type", "left");
                    context.startActivity(i1);
                    ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.stay);
                } else {
                    Intent i1 = new Intent(context, OtherUserProfile.class);
                    i1.putExtra("other_id", userarrylist.get(pos).getUserId() + "");
                    i1.putExtra("other_username", "");
                    context.startActivity(i1);
                    ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.stay);
                }


            }
        });


        if (selestgroupid.contains(userarrylist.get(position).getUserId() + "")) {
            mViewHolder.checkuser.setChecked(true);
        } else {
            mViewHolder.checkuser.setChecked(false);
        }


        mViewHolder.checkuser.setTag(position);
        mViewHolder.checkuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = (int) view.getTag();

                if (((CheckBox) view).isChecked()) {
                    selestgroupid.add(userarrylist.get(pos).getUserId() + "");
                } else {
                    selestgroupid.remove(userarrylist.get(pos).getUserId() + "");
                }

                Log.d("mn13selectgroup", selestgroupid.toString());


                if (CreateChat.txtdone != null) {
                    if (selestgroupid.isEmpty()) {
                        CreateChat.txtdone.setVisibility(View.GONE);
                    } else {
                        CreateChat.txtdone.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        return convertView;
    }

    public void clear() {
        userarrylist.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Datum> freindlist) {
        userarrylist.addAll(freindlist);
        notifyDataSetChanged();
    }

    static class ViewHolder {

        @BindView(R.id.imguser)
        CircleImageView imguser;
        @BindView(R.id.txtusername)
        TextView txtusername;
        @BindView(R.id.txtfullname)
        TextView txtfullname;
        @BindView(R.id.lnclick)
        LinearLayout lnuserclick;
        @BindView(R.id.checkuser)
        CheckBox checkuser;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);

        }

    }

}
