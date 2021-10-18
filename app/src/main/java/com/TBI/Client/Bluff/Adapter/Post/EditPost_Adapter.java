package com.TBI.Client.Bluff.Adapter.Post;

import android.content.Context;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.TBI.Client.Bluff.Activity.Post.EditPost;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.bumptech.glide.Glide;
import com.harsh.instatag.InstaTag;

import java.util.ArrayList;
import java.util.List;

public class EditPost_Adapter extends PagerAdapter {
    public static float height, width;
    public static boolean showinstag = true;
    List<com.TBI.Client.Bluff.Model.GetFeed.Image> images = new ArrayList<>();
    float oldwidth, oldheight;
    private LayoutInflater inflater;
    private Context context;

    public EditPost_Adapter(Context context, List<com.TBI.Client.Bluff.Model.GetFeed.Image> images, float height1, float width1, float newheight, float newwidth) {
        this.context = context;
        this.images = images;
        this.oldheight = height1;
        this.oldwidth = width1;
        height = newheight;
        width = newwidth;
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
        ImageView imgview, imgtagpeople;
        InstaTag instatagview;
        View myImageLayout = inflater.inflate(R.layout.layout_viewpageritem, view, false);
        imgview = myImageLayout.findViewById(R.id.roundedImageView);
        imgtagpeople = myImageLayout.findViewById(R.id.imgtagpeople);
        instatagview = myImageLayout.findViewById(R.id.instatagview);
        instatagview.setVisibility(View.VISIBLE);
        //  instatagview.setCanWeAddTags(true);
        instatagview.setloginusername(sharedpreference.getusername(context));
        imgview.setClipToOutline(true);


        if (!images.get(position).getImage().equals("") && !images.get(position).getImage().equals(null) && !images.get(position).getImage().equals("null")) {
            Glide
                    .with(context)
                    .load(images.get(position).getImage())
                    .placeholder(R.drawable.grey_placeholder)
                    .error(R.drawable.grey_placeholder)
                    .dontAnimate()
                    .into(imgview);
        } else {
            imgview.setImageDrawable(context.getResources().getDrawable(R.drawable.grey_placeholder));
        }

        Log.d("mn13height", height + "::" + width);


        for (int i = 0; i < images.get(position).getTagPeople().size(); i++) {
            Float x = Float.valueOf(images.get(position).getTagPeople().get(i).getX());
            Float y = Float.valueOf(images.get(position).getTagPeople().get(i).getY());

            Float newx = Public_Function.convertheight(oldwidth, width, x);
            Float newy = Public_Function.convertheight(oldheight, height, y);
            Log.d("mn13height1", oldheight + "\n" + oldwidth);
            instatagview.addTag(newx, newy, images.get(position).getTagPeople().get(i).getUsername() + "");
        }

        if (images.get(position).getTagPeople().isEmpty()) {
            imgtagpeople.setVisibility(View.GONE);
        } else {
            imgtagpeople.setVisibility(View.VISIBLE);
        }

        Log.d("mn13tagshow1", instatagview.isTagShowing() + "");
        if (images.get(position).isTagshow()) {
            instatagview.showTags();
        } else {
            instatagview.hideTags();
        }


        if (showinstag) {
            instatagview.setVisibility(View.VISIBLE);
        } else {
            instatagview.setVisibility(View.INVISIBLE);
        }

        imgtagpeople.setTag(position);
        imgtagpeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("mn13click", "Click");
                int pos = (int) view.getTag();

                if (context instanceof EditPost) {
                    EditPost detailPage1 = (EditPost) context;
                    detailPage1.setmodel(images.get(pos));
                }
            }
        });
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Nullable
    @Override
    public Parcelable saveState() {
        return super.saveState();
    }

}
