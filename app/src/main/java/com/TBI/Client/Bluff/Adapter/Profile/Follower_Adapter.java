package com.TBI.Client.Bluff.Adapter.Profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.TBI.Client.Bluff.Activity.Profile.FriendList;
import com.TBI.Client.Bluff.Activity.Profile.OtherUserProfile;
import com.TBI.Client.Bluff.Activity.Profile.ProfilePage;
import com.TBI.Client.Bluff.Model.View_Connection.Follower;
import com.TBI.Client.Bluff.Model.View_Connection.Request;
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

public class Follower_Adapter extends BaseAdapter {

    List<Follower> followerlist = new ArrayList<>();
    // List<Follower> originalData = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    ConnectionDetector cd;
    boolean isInternetPresent = false;
    FriendList friendList;

    //   private ItemFilter mFilter = new ItemFilter();


    public Follower_Adapter(Context context, List<Follower> followerlist, FriendList friendList) {
        this.followerlist = followerlist;
        //   this.originalData = followerlist;
        this.context = context;
        this.friendList = friendList;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return followerlist.size();
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
            convertView = inflater.inflate(R.layout.layout_follower, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        if (!followerlist.get(position).getPhoto().equals("") && !followerlist.get(position).getPhoto().equals(null) && !followerlist.get(position).getPhoto().equals("null")) {
            Glide.with(context)
                    .load(followerlist.get(position).getPhoto())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(mViewHolder.imgavtar);
        }

        mViewHolder.txtname.setText(followerlist.get(position).getFullName() + "");
        mViewHolder.txtuname.setText(followerlist.get(position).getUsername() + "");


        if (followerlist.get(position).getId() != Integer.parseInt(sharedpreference.getUserId(context))) {
            mViewHolder.txtfolllow.setVisibility(View.VISIBLE);
            if (followerlist.get(position).getFollowByFollowing() == 1) {
                mViewHolder.txtfolllow.setText("FOLLOWING");
                if (sharedpreference.getTheme(context).equalsIgnoreCase("white")) {
                    mViewHolder.txtfolllow.setTextColor(context.getResources().getColor(R.color.black));
                    mViewHolder.txtfolllow.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.blacklight)));
                } else {
                    mViewHolder.txtfolllow.setTextColor(context.getResources().getColor(R.color.white));
                    mViewHolder.txtfolllow.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
                }
                mViewHolder.txtfolllow.setBackground(context.getResources().getDrawable(R.drawable.follow_box));
            } else if (followerlist.get(position).getFollowByFollowing() == 0) {
                mViewHolder.txtfolllow.setText("FOLLOW");
                mViewHolder.txtfolllow.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.textcolor)));
                mViewHolder.txtfolllow.setBackground(context.getResources().getDrawable(R.drawable.edit_profile));
            } else if (followerlist.get(position).getFollowByFollowing() == 2) {
                mViewHolder.txtfolllow.setText("REQUESTED");
                mViewHolder.txtfolllow.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.darkgrey)));
            }
        } else {
            mViewHolder.txtfolllow.setVisibility(View.GONE);
        }

        mViewHolder.txtfolllow.setTag(position);
        mViewHolder.txtfolllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = (int) view.getTag();

                cd = new ConnectionDetector(context);
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    Toasty.warning(context, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    Send_Request(followerlist.get(pos));
                }

            }
        });

        mViewHolder.llnclick.setTag(position);
        mViewHolder.llnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = (int) view.getTag();

                if (sharedpreference.getUserId(context).equalsIgnoreCase("" + followerlist.get(pos).getId())) {

                    Intent i1 = new Intent(context, ProfilePage.class);
                    i1.putExtra("type", "left");
                    context.startActivity(i1);
                    ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.stay);
                } else {
                    Intent i1 = new Intent(context, OtherUserProfile.class);
                    i1.putExtra("other_id", followerlist.get(pos).getId() + "");
                    i1.putExtra("other_username", "");
                    context.startActivity(i1);
                    ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.stay);
                }

            }
        });

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

    public void addAll(List<Follower> Followlist) {
        followerlist.clear();
        followerlist.addAll(Followlist);
        notifyDataSetChanged();

    }

/*
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Follower> list = originalData;

            int count = list.size();
            final ArrayList<Follower> nlist = new ArrayList<Follower>(count);

            for (int i = 0; i < count; i++) {
                if (list.get(i).getFullName().contains(filterString) || list.get(i).getFullName().contains(filterString)) {
                    nlist.add(list.get(i));
                }
                *//*filterableString = list.get(i).getFullName();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }*//*
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            followerlist = (List<Follower>) results.values;
            notifyDataSetChanged();
        }

    }

    public Filter getfilter() {
        return mFilter;
    }*/

    public void add(Follower Followlist) {
        followerlist.add(Followlist);
        notifyDataSetChanged();

    }

    public void Send_Request(Follower friend) {

        //Public_Function.Show_Progressdialog(context);


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
                        friend.setFollowByFollowing(follow_status);
                        notifyDataSetChanged();

                        if (follow_status == 1) {
                            friendList.add_following(new Request(friend.getId(), friend.getUsername(), friend.getFullName(), friend.getPhoto(), friend.getFollowedBy(), friend.getFollowByFollowing()));
                        }
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

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
