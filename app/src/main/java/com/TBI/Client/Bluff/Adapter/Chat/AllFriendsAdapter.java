package com.TBI.Client.Bluff.Adapter.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.R;

public class AllFriendsAdapter extends RecyclerView.Adapter<AllFriendsAdapter.MyClassView> {

    Context context;

    public AllFriendsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_all_friends, parent, false);
        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyClassView extends RecyclerView.ViewHolder {
        public MyClassView(@NonNull View itemView) {
            super(itemView);
        }
    }
}
