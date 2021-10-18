package com.TBI.Client.Bluff.Adapter.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.TBI.Client.Bluff.R;

import java.util.ArrayList;

public class DrawerAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<Integer> images = new ArrayList<>();
    LayoutInflater layoutInflater;
    private int lastAdded;

    public DrawerAdapter(Context context, ArrayList<String> arrayList, ArrayList<Integer> images) {
        this.context = context;
        this.arrayList = arrayList;
        this.images = images;
        layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_setting, parent, false);
            holder = new ViewHolder();

            holder.txt = convertView.findViewById(R.id.txt);
            holder.img = convertView.findViewById(R.id.img);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt.setText(arrayList.get(position));


        lastAdded = arrayList.size() - 1;

        holder.img.setImageResource(images.get(position));
        if (position == lastAdded) {
            holder.txt.setTextColor(context.getResources().getColor(R.color.brown));
        } else {
            holder.txt.setTextColor(context.getResources().getColor(R.color.white));
        }

        return convertView;
    }

    class ViewHolder {
        TextView txt;
        ImageView img;
    }
}


