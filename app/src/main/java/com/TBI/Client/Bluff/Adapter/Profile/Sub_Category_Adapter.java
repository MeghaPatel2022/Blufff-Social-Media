package com.TBI.Client.Bluff.Adapter.Profile;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class Sub_Category_Adapter extends RecyclerView.Adapter<Sub_Category_Adapter.ViewHolder> {
    Activity Context;
    List<String> item = new ArrayList<>();

    public Sub_Category_Adapter(Activity Context, List<String> subcategory) {
        this.Context = Context;
        this.item = subcategory;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        if (viewType == 1) {
            view = (LayoutInflater.from(parent.getContext())).inflate(R.layout.layout_high, parent, false);
        } else {
            view = (LayoutInflater.from(parent.getContext())).inflate(R.layout.layout_low, parent, false);
        }


        return new ViewHolder(view, viewType);
        //    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(RM.layout.layout_wall, null, false));

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {


    }

    @Override
    public int getItemViewType(int position) {
        return (position % 3 == 0) ? 2 : 1;
    }


    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(final View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


    }

}




