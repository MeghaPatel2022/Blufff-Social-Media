package com.TBI.Client.Bluff.Adapter.Post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.TBI.Client.Bluff.Model.SearchUser.Datum;
import com.TBI.Client.Bluff.R;
import com.bumptech.glide.Glide;
import com.hendraanggrian.appcompat.widget.SocialArrayAdapter;

import de.hdodenhof.circleimageview.CircleImageView;

public class MentionUserAdapter extends SocialArrayAdapter<Datum> {

    Context context1;

    public MentionUserAdapter(Context context) {
        super(context, R.layout.layout_mentionuser, R.id.txttime);
        this.context1 = context;
    }

    @NonNull
    @Override
    public CharSequence convertToString(Datum object) {
        return object.getUsername();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_mentionuser, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtusername.setText(getItem(position).getUsername() + "");
        holder.txtfullname.setText(getItem(position).getFullName() + "");

        if (!getItem(position).getPhoto().equals("") && !getItem(position).getPhoto().equals(null) && !getItem(position).getPhoto().equals("null")) {
            Glide.with(context1)
                    .load(getItem(position).getPhoto())
                    .placeholder(R.drawable.profile_placeholder1)
                    .error(R.drawable.profile_placeholder1)
                    .dontAnimate()
                    .into(holder.imguser);
        }


        return convertView;
    }


    private static class ViewHolder {
        final TextView txtusername, txtfullname;
        CircleImageView imguser;

        ViewHolder(@NonNull View view) {
            this.txtusername = view.findViewById(R.id.txtusername);
            this.txtfullname = view.findViewById(R.id.txtfullname);
            this.imguser = view.findViewById(R.id.imguser);
        }
    }
}
