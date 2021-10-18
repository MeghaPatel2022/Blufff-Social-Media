package com.TBI.Client.Bluff.Adapter.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FrequentFriendAdapter extends RecyclerView.Adapter<FrequentFriendAdapter.MyClassView> {

    Context context;

    public FrequentFriendAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_frend_chat, parent, false);


        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView mHolder, int position) {
        mHolder.img_1.setVisibility(View.GONE);
        mHolder.img_2.setVisibility(View.GONE);
        mHolder.img_3.setVisibility(View.GONE);
        mHolder.img_4.setVisibility(View.GONE);
        mHolder.img_5.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        @BindView(R.id.imguser)
        ImageView imguser;
        @BindView(R.id.txtusername)
        TextView txtusername;
        @BindView(R.id.alphabeticImage)
        TextView alphabeticImage;
        @BindView(R.id.img_1)
        CircleImageView img_1;
        @BindView(R.id.img_2)
        CircleImageView img_2;
        @BindView(R.id.img_3)
        CircleImageView img_3;
        @BindView(R.id.img_4)
        CircleImageView img_4;
        @BindView(R.id.img_5)
        CircleImageView img_5;


        public MyClassView(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
