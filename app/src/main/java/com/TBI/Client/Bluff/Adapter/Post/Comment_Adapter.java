package com.TBI.Client.Bluff.Adapter.Post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.TBI.Client.Bluff.Activity.Profile.OtherUserProfile;
import com.TBI.Client.Bluff.Activity.Profile.ProfilePage;
import com.TBI.Client.Bluff.Model.PostDetail.Comment;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.hendraanggrian.appcompat.widget.SocialTextView;
import com.hendraanggrian.appcompat.widget.SocialView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Comment_Adapter extends BaseAdapter {

    List<Comment> recentlyaddedlist = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public Comment_Adapter(Context context, List<Comment> recentlyaddedlist) {
        this.recentlyaddedlist = recentlyaddedlist;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }


    @Override
    public int getCount() {
        return recentlyaddedlist.size();
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
            convertView = inflater.inflate(R.layout.layout_comment, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }


      /*  LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mViewHolder.lnback.getLayoutParams();
        params.setMargins(random(), 20, 0, 0);
        mViewHolder.lnback.setLayoutParams(params);

*/

        Log.e("LLLLL_Comment_Data: ", String.valueOf(recentlyaddedlist.get(position)));

        mViewHolder.txtctime.setText(recentlyaddedlist.get(position).getComment_duration());
        mViewHolder.txtcomment.setText(recentlyaddedlist.get(position).getComment());
        mViewHolder.txtuname.setText(recentlyaddedlist.get(position).getUsername());

        if (recentlyaddedlist.get(position).getLiked() == 0) {
            mViewHolder.imglike.setImageResource(R.drawable.like_comment);
        } else {
            mViewHolder.imglike.setImageResource(R.drawable.like_comment_color);
        }

        mViewHolder.imglike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = "1";
                if (recentlyaddedlist.get(position).getLiked() == 0) {
                    status = "1";
                } else
                    status = "0";

                AndroidNetworking.post(geturl.BASE_URL + "like_unlike_comment")
                        .addHeaders("Authorization", sharedpreference.getUserToken(context))
                        .addBodyParameter("user_id", sharedpreference.getUserId(context))
                        .addBodyParameter("comment_id", String.valueOf(recentlyaddedlist.get(position).getId()))
                        .addBodyParameter("status", status)
                        .setPriority(Priority.IMMEDIATE)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getBoolean("success")) {
                                        if (response.getString("liked").equals("0")) {
                                            mViewHolder.imglike.setImageResource(R.drawable.like_comment);
                                        } else {
                                            mViewHolder.imglike.setImageResource(R.drawable.like_comment_color);
                                        }
                                    }
                                } catch (JSONException e) {
                                    Log.e("LLLL_Like_Comment: ", e.getMessage());
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.e("LLLL_Like_Comment_1: ", anError.getMessage());
                            }
                        });
            }
        });

        if (!recentlyaddedlist.get(position).getPhoto().equals("") && !recentlyaddedlist.get(position).getPhoto().equals(null) && !recentlyaddedlist.get(position).getPhoto().equals("null")) {
            Glide.with(context)
                    .load(recentlyaddedlist.get(position).getPhoto())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .dontAnimate()
                    .into(mViewHolder.imguser);
        } else {
            mViewHolder.imguser.setImageDrawable(context.getResources().getDrawable(R.drawable.placeholder));
        }

        mViewHolder.txtcomment.setTag(position);
        mViewHolder.txtcomment.setOnMentionClickListener(new SocialView.OnClickListener() {
            @Override
            public void onClick(@NonNull SocialView view, @NonNull CharSequence text) {

                if (sharedpreference.getusername(context).equalsIgnoreCase("" + text)) {

                    Intent i1 = new Intent(context, ProfilePage.class);
                    i1.putExtra("type", "left");
                    context.startActivity(i1);
                    ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.stay);
                } else {
                    Intent i1 = new Intent(context, OtherUserProfile.class);
                    i1.putExtra("other_id", "");
                    i1.putExtra("other_username", text.toString());
                    context.startActivity(i1);
                    ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.stay);
                }

            }
        });

        return convertView;
    }

    public void Addall(List<Comment> commentarraylist) {
        recentlyaddedlist.clear();
        recentlyaddedlist.addAll(commentarraylist);
        notifyDataSetChanged();

    }

    public void AppendAll(List<Comment> commentarraylist) {
        recentlyaddedlist.addAll(commentarraylist);
        notifyDataSetChanged();

    }

    public void add(Comment comment) {
        recentlyaddedlist.add(comment);
        notifyDataSetChanged();
    }

    public int random() {
        int min = 50;
        int max = 150;
        int random = new Random().nextInt((max - min) + 1) + min;
        return random;
    }

    private void setLike(int position, String status) {

    }

    static class ViewHolder {
        @BindView(R.id.lnback)
        LinearLayout lnback;
        @BindView(R.id.txtcomment)
        SocialTextView txtcomment;
        @BindView(R.id.txtuname)
        TextView txtuname;
        @BindView(R.id.imguser)
        CircleImageView imguser;
        @BindView(R.id.txtctime)
        TextView txtctime;
        @BindView(R.id.imglike)
        ImageView imglike;

        /*  @BindView(RM.id.imgavtar)
          RoundedImageView imgavtar;
          @BindView(RM.id.imgonline)
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

