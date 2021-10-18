package com.TBI.Client.Bluff.Adapter.UserPage;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;


import com.TBI.Client.Bluff.R;


public class CustomAdapter extends BaseAdapter {

    Context context;
    String[] professionName;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] professionName) {
        this.context = applicationContext;
        this.professionName = professionName;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return professionName.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.lsit_profession, null);
        TextView tv_profession = view.findViewById(R.id.tv_profession);
        tv_profession.setText(professionName[i]);
        return view;
    }
}
