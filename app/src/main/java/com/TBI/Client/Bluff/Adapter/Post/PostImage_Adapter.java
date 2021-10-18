package com.TBI.Client.Bluff.Adapter.Post;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Post.PostCreate;
import com.TBI.Client.Bluff.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostImage_Adapter extends RecyclerView.Adapter<PostImage_Adapter.ViewHolder> {
    Activity Context;
    List<File> item = new ArrayList<>();
    PostCreate postCreate;

    public PostImage_Adapter(Activity Context, List<File> subcategory, PostCreate postCreate) {
        this.Context = Context;
        this.item = subcategory;
        this.postCreate = postCreate;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        view = (LayoutInflater.from(parent.getContext())).inflate(R.layout.layout_postimage, parent, false);

        return new ViewHolder(view, viewType);
        //    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(RM.layout.layout_wall, null, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // holder.roundimage.setImageBitmap(item.get(position));
        Bitmap myBitmap = BitmapFactory.decodeFile(item.get(position).getAbsolutePath());
        holder.roundimage.setImageBitmap(myBitmap);
        holder.roundimage.setTag(position);
        holder.roundimage.setClipToOutline(true);

        holder.roundimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = (int) view.getTag();

                postCreate.ShowImages(pos);
            }
        });
    }


    @Override
    public int getItemCount() {
        return item.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.roundedImageView)
        ImageView roundimage;

        public ViewHolder(final View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


    }

}




