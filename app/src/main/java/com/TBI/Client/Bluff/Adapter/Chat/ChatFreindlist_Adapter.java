package com.TBI.Client.Bluff.Adapter.Chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.TBI.Client.Bluff.Model.GetFreinds.Datum;
import com.TBI.Client.Bluff.R;
import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ChannelInfoActivity;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFreindlist_Adapter extends BaseAdapter {

    List<Datum> userarrylist = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    String type = "";

    public ChatFreindlist_Adapter(Context context, List<Datum> userarrylist, String type) {
        this.userarrylist = userarrylist;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        this.type = type;
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

            Glide
                    .with(context)
                    .load(userarrylist.get(position).getPhoto())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .dontAnimate()
                    .into(mViewHolder.imguser);
            /*Picasso.get()
                    .load(userarrylist.get(position).getPhoto())
                    .placeholder(RM.drawable.placeholder)
                    .error(RM.drawable.placeholder)
                    .into(mViewHolder.imguser);*/
        }

        mViewHolder.txtfullname.setText(userarrylist.get(position).getFullName() + "");
        mViewHolder.txtusername.setText(userarrylist.get(position).getUsername() + "");

        mViewHolder.lnuserclick.setTag(position);
        mViewHolder.lnuserclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = (int) view.getTag();

                Applozic.init(context, context.getString(R.string.application_key));

                if (TextUtils.isEmpty(userarrylist.get(pos).getUserId() + "")) {
                    return;
                }

                if (type.equalsIgnoreCase("yes")) {
                    Intent intent = new Intent(context, ConversationActivity.class);
                    intent.putExtra(ConversationUIService.USER_ID, userarrylist.get(pos).getUserId() + "");
                    context.startActivity(intent);
                } else if (type.equalsIgnoreCase("no")) {

                    Intent intent = new Intent();
                    intent.putExtra(ChannelInfoActivity.USERID, userarrylist.get(pos).getUserId() + "");
                    ((Activity) context).setResult(Activity.RESULT_OK, intent);
                    ((Activity) context).finish();

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

        public ViewHolder(View view) {

            ButterKnife.bind(this, view);
        }

    }

}
