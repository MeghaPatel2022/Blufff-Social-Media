package com.TBI.Client.Bluff.Adapter.Post;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.TBI.Client.Bluff.R;

import java.io.File;
import java.util.List;

public class ShowImages_Adapter extends PagerAdapter {
    List<File> images;
    private LayoutInflater inflater;
    private Context context;


    public ShowImages_Adapter(Context context, List<File> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
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


   /* @Override
    public float getPageWidth(int position) {
        if (pics.length > 1) {
            return 0.9f;
        } else {
            return 1.0f;
        }
    }*/

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        ImageView imgview;
        View myImageLayout = inflater.inflate(R.layout.layout_viewpageritem, view, false);
        imgview = myImageLayout.findViewById(R.id.roundedImageView);
        Bitmap myBitmap = BitmapFactory.decodeFile(images.get(position).getAbsolutePath());

        imgview.setImageBitmap(myBitmap);
        imgview.setClipToOutline(true);
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }
}
