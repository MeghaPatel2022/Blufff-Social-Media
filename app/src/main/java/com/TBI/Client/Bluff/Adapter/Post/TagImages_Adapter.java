package com.TBI.Client.Bluff.Adapter.Post;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.TBI.Client.Bluff.Activity.Post.PostCreate;
import com.TBI.Client.Bluff.Activity.Post.TagUser;
import com.TBI.Client.Bluff.R;
import com.harsh.instatag.InstaTag;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.TBI.Client.Bluff.Activity.Post.TagUser.instaarray;

public class TagImages_Adapter extends PagerAdapter {
    List<File> images = new ArrayList<>();
    InstaTag.PhotoEvent photoEvent;
    private LayoutInflater inflater;
    private Context context;
    private String fileType = "";


    public TagImages_Adapter(Context context, List<File> images, InstaTag.PhotoEvent photoEvent,String fileType) {
        this.context = context;
        this.images = images;
        this.fileType = fileType;
        inflater = LayoutInflater.from(context);
        this.photoEvent = photoEvent;
        Log.d("size", images.size() + "");
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

        LinearLayout lnadd;
        View myImageLayout = inflater.inflate(R.layout.layout_tagimages, view, false);
        lnadd = myImageLayout.findViewById(R.id.lnadd);
        lnadd.setClipToOutline(true);
        InstaTag insta_tag = myImageLayout.findViewById(R.id.insta_tag);
        VideoView videoView = myImageLayout.findViewById(R.id.videoView);

        if (fileType.equalsIgnoreCase("video")){
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.fromFile(images.get(0)));
            videoView.setOnPreparedListener(mp -> {
                mp.setLooping(true);
                if (!videoView.isPlaying()) {
                    videoView.start();
                } else
                    videoView.pause();
            });

        } else if (fileType.equalsIgnoreCase("image")){
            videoView.setVisibility(View.GONE);
            Bitmap myBitmap = BitmapFactory.decodeFile(images.get(0).getAbsolutePath());
            insta_tag.getTagImageView().setImageBitmap(myBitmap);
            insta_tag.setTaggedPhotoEvent(photoEvent);
        }

        insta_tag.setTag(R.id.insta_tag, position);
        ViewTreeObserver vto = insta_tag.getTagImageView().getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                insta_tag.getTagImageView().getViewTreeObserver().removeOnPreDrawListener(this);
                int finalHeight = insta_tag.getTagImageView().getMeasuredHeight();
                int finalWidth = insta_tag.getTagImageView().getMeasuredWidth();
                PostCreate.height = finalHeight;
                PostCreate.width = finalWidth;
                Log.d("mn13height", finalHeight + "::" + finalWidth);
                return true;
            }
        });

        if (TagUser.instaarray.get(position).getParent() != null) {
            ((ViewGroup) TagUser.instaarray.get(position).getParent()).removeView(instaarray.get(position)); // <- fix
        }
        for (int i = 0; i < TagUser.instaarray.get(position).getTags().size(); i++) {
            insta_tag.addTag(TagUser.instaarray.get(position).getTags().get(i).getX_co_ord(), TagUser.instaarray.get(position).getTags().get(i).getY_co_ord(), TagUser.instaarray.get(position).getTags().get(i).getUnique_tag_id());
        }

        Log.d("mn13hi1", position + "\n" + TagUser.instaarray.get(position).getTags().toString() + "");
        TagUser.instaarray.set(position, insta_tag);
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Nullable
    @Override
    public Parcelable saveState() {
        return super.saveState();
    }


}
