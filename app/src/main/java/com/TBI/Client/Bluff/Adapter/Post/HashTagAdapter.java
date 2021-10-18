package com.TBI.Client.Bluff.Adapter.Post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.TBI.Client.Bluff.Model.SearchHashtag.Hastag;
import com.TBI.Client.Bluff.R;
import com.hendraanggrian.appcompat.widget.SocialArrayAdapter;

import java.util.List;

public class HashTagAdapter extends SocialArrayAdapter<Hastag> {

    public HashTagAdapter(Context context) {
        super(context, R.layout.layout_hashtag, R.id.txthashtext);
    }

    @NonNull
    @Override
    public CharSequence convertToString(Hastag object) {
        return object.getHashtag().replace("#", "");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_hashtag, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtcount.setText(getItem(position).getTotal_posts() + "");
        holder.txthashtext.setText("#" + getItem(position).getHashtag() + "");
     /*   Person item = getItem(position);
        if (item != null) holder.textView.setText(item.name);*/
        return convertView;
    }


    private static class ViewHolder {
        final TextView txtcount, txthashtext;

        ViewHolder(@NonNull View view) {
            this.txtcount = view.findViewById(R.id.txtcount);
            this.txthashtext = view.findViewById(R.id.txthashtext);
        }
    }
}
