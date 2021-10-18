package com.TBI.Client.Bluff.Adapter.Post;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.R;

import java.util.ArrayList;

public class NameSuggestAdapter extends RecyclerView.Adapter<NameSuggestAdapter.MyClassView> {

    Activity activity;
    ArrayList<String> usernameList = new ArrayList<>();

    public NameSuggestAdapter(Activity activity, ArrayList<String> usernameList) {
        this.activity = activity;
        this.usernameList = usernameList;
    }

    @NonNull
    @Override
    public NameSuggestAdapter.MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_suggest_name, parent, false);

        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NameSuggestAdapter.MyClassView holder, int position) {
        holder.tv_username.setText(usernameList.get(position));
    }

    @Override
    public int getItemCount() {
        return usernameList.size();
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        TextView tv_username;

        public MyClassView(@NonNull View itemView) {
            super(itemView);

            tv_username = itemView.findViewById(R.id.tv_username);

        }
    }
}
