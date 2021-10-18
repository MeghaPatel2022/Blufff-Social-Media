package com.TBI.Client.Bluff.Adapter.Post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.TBI.Client.Bluff.Activity.Profile.FriendList;
import com.TBI.Client.Bluff.Activity.Profile.OtherUserProfile;
import com.TBI.Client.Bluff.Activity.Profile.ProfilePage;
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

public class Requested_Adapter extends BaseAdapter {

    List<Request> requestedarray = new ArrayList<>();
    List<Request> originalData = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    FriendList friendList;

    ConnectionDetector cd;
    boolean isInternetPresent = false;

    public Requested_Adapter(Context context, List<Request> requestedarray, FriendList friendList) {
        this.requestedarray = requestedarray;
        this.context = context;
        this.friendList = friendList;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return requestedarray.size();
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
            convertView = inflater.inflate(R.layout.layout_follower, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        if (!requestedarray.get(position).getPhoto().equals("") && !requestedarray.get(position).getPhoto().equals(null) && !requestedarray.get(position).getPhoto().equals("null")) {
            Glide.with(context)
                    .load(requestedarray.get(position).getPhoto())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .dontAnimate()
                    .into(mViewHolder.imgavtar);
        }

        mViewHolder.txtname.setText(requestedarray.get(position).getFullName() + "");
        mViewHolder.txtuname.setText(requestedarray.get(position).getUsername() + "");
        mViewHolder.imgcancel.setVisibility(View.VISIBLE);
        mViewHolder.imgdone.setVisibility(View.VISIBLE);
        mViewHolder.txtfolllow.setVisibility(View.GONE);

        mViewHolder.imgdone.setTag(position);
        mViewHolder.imgdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = (int) view.getTag();

                cd = new ConnectionDetector(context);
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    Toasty.warning(context, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    Accept_RejectRequest(requestedarray.get(pos), "1");
                }

            }
        });

        mViewHolder.imgcancel.setTag(position);
        mViewHolder.imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = (int) view.getTag();
                cd = new ConnectionDetector(context);
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    Toasty.warning(context, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    Accept_RejectRequest(requestedarray.get(pos), "0");
                }
            }
        });

        mViewHolder.llnclick.setTag(position);
        mViewHolder.llnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = (int) view.getTag();

                if (sharedpreference.getUserId(context).equalsIgnoreCase("" + requestedarray.get(pos).getId())) {

                    Intent i1 = new Intent(context, ProfilePage.class);
                    i1.putExtra("type", "left");
                    context.startActivity(i1);
                    ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.stay);
                } else {
                    Intent i1 = new Intent(context, OtherUserProfile.class);
                    i1.putExtra("other_id", requestedarray.get(pos).getId() + "");
                    i1.putExtra("other_username", "");
                    context.startActivity(i1);
                    ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.stay);
                }

            }
        });

        return convertView;
    }

    public void addAll(List<Request> Followlist) {
        requestedarray.clear();
        originalData.clear();
        requestedarray.addAll(Followlist);
        originalData.addAll(Followlist);
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

    public void Accept_RejectRequest(Request friend, String status) {

        //Public_Function.Show_Progressdialog(context);


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(context) + ""));
        paramArrayList.add(new param("other_user_id", friend.getId() + ""));
        paramArrayList.add(new param("status", status + ""));


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
                        /*int follow_status = object.optInt("following_by_you");
                        friend.setFollowByFollowing(follow_status);*/
                        if (status.equalsIgnoreCase("0")) {
                            requestedarray.remove(friend);
                        } else {
                            friendList.addfollower(friend);
                            requestedarray.remove(friend);
                        }
                        notifyDataSetChanged();
                    } else {
                        Toasty.error(context, status + "", Toast.LENGTH_LONG, true).show();
                    }
                } catch (Exception e) {
                    Toasty.error(context, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                    e.printStackTrace();
                }
            }
        }, paramArrayList, "accept_reject");
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
        @BindView(R.id.imgdone)
        ImageView imgdone;
        @BindView(R.id.imgcancel)
        ImageView imgcancel;
        @BindView(R.id.llnclick)
        LinearLayout llnclick;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
