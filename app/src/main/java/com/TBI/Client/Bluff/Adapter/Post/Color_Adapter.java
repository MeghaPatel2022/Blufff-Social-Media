package com.TBI.Client.Bluff.Adapter.Post;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Post.CreateStory;
import com.TBI.Client.Bluff.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Color_Adapter extends RecyclerView.Adapter<Color_Adapter.ViewHolder> {
    CreateStory Context;
    List<Integer> item = new ArrayList<>();
    int selectposition = -1;

    public Color_Adapter(CreateStory Context, List<Integer> subcategory) {
        this.Context = Context;
        this.item = subcategory;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        view = (LayoutInflater.from(parent.getContext())).inflate(R.layout.layout_color, parent, false);


        return new ViewHolder(view, viewType);
        //    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(RM.layout.layout_wall, null, false));

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.imgcolorback.setColorFilter(Context.getResources().getColor(item.get(i)));

    }


    @Override
    public int getItemCount() {
        return item.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgcolorback)
        ImageView imgcolorback;

        public ViewHolder(final View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();

                    selectposition = position;
                    notifyDataSetChanged();
                    Context.setcolorback(item.get(position));
                }
            });

        }


    }

}




