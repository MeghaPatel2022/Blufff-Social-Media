package com.applozic.mobicomkit.uiwidgets.conversation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.applozic.mobicomkit.uiwidgets.R;

public class ChatMediaAdapter extends RecyclerView.Adapter<ChatMediaAdapter.MyClassView> {

    Context context;

    public ChatMediaAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_media,parent,false);

        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public class MyClassView extends RecyclerView.ViewHolder {
        public MyClassView(@NonNull View itemView) {
            super(itemView);
        }
    }
}
