package com.TBI.Client.Bluff.Adapter.Post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.TBI.Client.Bluff.Model.Get_bannerdetail.Photo;
import com.TBI.Client.Bluff.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Multipleimage_Adapter extends PagerAdapter {
    List<Photo> images = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    public Multipleimage_Adapter(Context context, List<Photo> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        ImageView roundedImageView;
        View myImageLayout = inflater.inflate(R.layout.layout_multipleimage, view, false);
        roundedImageView = myImageLayout.findViewById(R.id.imgmulti);

        if (!images.get(position).getBannerImg().equals("") && !images.get(position).getBannerImg().equals(null) && !images.get(position).getBannerImg().equals("null")) {
            Glide
                    .with(context)
                    .load(images.get(position).getBannerImg())
                    .placeholder(R.drawable.grey_placeholder)
                    .error(R.drawable.grey_placeholder)
                    .into(roundedImageView);
        } else {
            roundedImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.grey_placeholder));
        }


        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    public void addtaglist() {

    }

    /*public void showtag(boolean showtag) {
        this.showtag = showtag;
        notifyDataSetChanged();
    }*/
}
