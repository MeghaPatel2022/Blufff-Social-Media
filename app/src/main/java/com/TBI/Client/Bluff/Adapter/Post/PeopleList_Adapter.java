package com.TBI.Client.Bluff.Adapter.Post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.TBI.Client.Bluff.Activity.Profile.OtherUserProfile;
import com.TBI.Client.Bluff.Activity.Profile.ProfilePage;
import com.TBI.Client.Bluff.Model.GlobalSearch.Persons;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.MyAsyncTaskCallback;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class PeopleList_Adapter extends BaseAdapter {

    List<Persons> recentlyaddedlist = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    ConnectionDetector cd;
    boolean isInternetPresent = false;
    String type = "";


    public PeopleList_Adapter(Context context, List<Persons> recentlyaddedlist, String type) {
        this.recentlyaddedlist = recentlyaddedlist;
        this.context = context;
        this.type = type;
        inflater = LayoutInflater.from(this.context);
        Log.d("mn13array", recentlyaddedlist.toString());
    }

    @Override
    public int getCount() {
        return recentlyaddedlist.size();
    }

    @Override
    public Persons getItem(int position) {
        return recentlyaddedlist.get(position);
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

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_follower, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.txtname.setText(recentlyaddedlist.get(position).getFullName() + "");
        mViewHolder.txtuname.setText(recentlyaddedlist.get(position).getUsername() + "");

        if (!recentlyaddedlist.get(position).getPhoto().equals("") && !recentlyaddedlist.get(position).getPhoto().equals(null) && !recentlyaddedlist.get(position).getPhoto().equals("null")) {
            Glide.with(context)
                    .load(recentlyaddedlist.get(position).getPhoto())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .dontAnimate()
                    .into(mViewHolder.imgavtar);
        } else {
            mViewHolder.imgavtar.setImageDrawable(context.getResources().getDrawable(R.drawable.placeholder));
        }

        if (type.equalsIgnoreCase("Yes")) {
            mViewHolder.lnright.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.lnright.setVisibility(View.GONE);
        }
        mViewHolder.txtfolllow.setTag(position);

        if (recentlyaddedlist.get(position).getId() != Integer.parseInt(sharedpreference.getUserId(context))) {
            mViewHolder.txtfolllow.setVisibility(View.VISIBLE);
            if (recentlyaddedlist.get(position).getFollowByUser() == 1) {

                mViewHolder.txtfolllow.setText("FOLLOWING");
                if (sharedpreference.getTheme(context).equalsIgnoreCase("white")) {
                    mViewHolder.txtfolllow.setTextColor(context.getResources().getColor(R.color.black));
                    mViewHolder.txtfolllow.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.blacklight)));
                } else {
                    mViewHolder.txtfolllow.setTextColor(context.getResources().getColor(R.color.white));
                    mViewHolder.txtfolllow.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
                }
                mViewHolder.txtfolllow.setBackground(context.getResources().getDrawable(R.drawable.follow_box));

            } else if (recentlyaddedlist.get(position).getFollowByUser() == 0) {

                mViewHolder.txtfolllow.setText("FOLLOW");
                mViewHolder.txtfolllow.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.textcolor)));
                mViewHolder.txtfolllow.setBackground(context.getResources().getDrawable(R.drawable.edit_profile));


            } else if (recentlyaddedlist.get(position).getFollowByUser() == 2) {

                mViewHolder.txtfolllow.setText("REQUESTED");
                mViewHolder.txtfolllow.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.darkgrey)));
            }
        } else {
            mViewHolder.txtfolllow.setVisibility(View.GONE);
        }
        mViewHolder.txtfolllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = (int) view.getTag();

                cd = new ConnectionDetector(context);
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    Toasty.warning(context, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    Send_Request(recentlyaddedlist.get(pos));
                }

            }
        });

        mViewHolder.llnclick.setTag(position);
        mViewHolder.llnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = (int) view.getTag();
                if (sharedpreference.getUserId(context).equalsIgnoreCase("" + recentlyaddedlist.get(pos).getId())) {

                    Intent i1 = new Intent(context, ProfilePage.class);
                    i1.putExtra("type", "left");
                    context.startActivity(i1);
                    ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.stay);
                } else {
                    Intent i1 = new Intent(context, OtherUserProfile.class);
                    i1.putExtra("other_id", recentlyaddedlist.get(pos).getId() + "");
                    i1.putExtra("other_username", "");
                    context.startActivity(i1);
                    ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.stay);
                }

            }
        });
        return convertView;
    }

    public void Send_Request(Persons friend) {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", friend.getId() + ""));
        paramArrayList.add(new param("followed_by", sharedpreference.getUserId(context) + ""));


        new geturl().getdata(context, new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {
                try {
                    Public_Function.Hide_ProgressDialogue();
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("msg");
                    if (message) {
                        int follow_status = object.optInt("following_by_you");
                        friend.setFollowByUser(follow_status);
                        notifyDataSetChanged();
                    } else {
                        Toasty.error(context, status + "", Toast.LENGTH_LONG, true).show();
                    }
                } catch (Exception e) {
                    Toasty.error(context, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                    e.printStackTrace();
                }
            }
        }, paramArrayList, "follow_user");
    }

    static class ViewHolder {
        @BindView(R.id.imgavtar)
        CircleImageView imgavtar;
        @BindView(R.id.txtuname)
        TextView txtuname;
        @BindView(R.id.txtname)
        TextView txtname;
        @BindView(R.id.txtfolllow)
        TextView txtfolllow;
        @BindView(R.id.llnclick)
        LinearLayout llnclick;
        @BindView(R.id.lnright)
        LinearLayout lnright;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
