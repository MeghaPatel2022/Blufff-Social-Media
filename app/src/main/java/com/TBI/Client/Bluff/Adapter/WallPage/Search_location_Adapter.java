package com.TBI.Client.Bluff.Adapter.WallPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.TBI.Client.Bluff.Model.Search_location.Prediction;
import com.TBI.Client.Bluff.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Search_location_Adapter extends BaseAdapter {


    Context context;
    LayoutInflater inflater;
    String url = "";
    List<Prediction> item = new ArrayList<>();
    int selectedposition = 0;

    public Search_location_Adapter(Context context, List<Prediction> item) {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        this.item = item;
        this.url = url;
        selectedposition = 0;

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
            convertView = inflater.inflate(R.layout.layout_search_location, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.main_text.setText(item.get(position).getStructuredFormatting().getMainText());
        holder.sub_text.setText(item.get(position).getDescription());


        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.main_text)
        TextView main_text;
        @BindView(R.id.sub_text)
        TextView sub_text;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}