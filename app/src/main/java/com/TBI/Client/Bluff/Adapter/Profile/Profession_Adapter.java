package com.TBI.Client.Bluff.Adapter.Profile;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.TBI.Client.Bluff.Model.GetProfession.DatumProfession;
import com.TBI.Client.Bluff.R;

import java.util.ArrayList;
import java.util.List;

public class Profession_Adapter extends BaseAdapter {


    Activity context;
    LayoutInflater inflater;
    String url = "";
    List<DatumProfession> item = new ArrayList<>();

    public Profession_Adapter(Activity context, List<DatumProfession> item) {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        this.item = item;
        this.url = url;

    }


    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_profession, parent, false);
            holder.txtspinneritem = convertView.findViewById(R.id.txtspinneritem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtspinneritem.setText(item.get(position).getName());

        return convertView;
    }

    class ViewHolder {
        TextView txtspinneritem;

    }
}
