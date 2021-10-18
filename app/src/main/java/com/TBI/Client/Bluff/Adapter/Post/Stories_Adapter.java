package com.TBI.Client.Bluff.Adapter.Post;

import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Model.GetFeed.Story;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.StoryStatus.StatusStoriesActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Stories_Adapter extends RecyclerView.Adapter<Stories_Adapter.ViewHolder> {
    public static Story selectmodel;
    public static int selectpostoin = -1;
    private final String[] resources = new String[]{
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00001.jpg?alt=media&token=460667e4-e084-4dc5-b873-eefa028cec32",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00002.jpg?alt=media&token=e8e86192-eb5d-4e99-b1a8-f00debcdc016",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00004.jpg?alt=media&token=af71cbf5-4be3-4f8a-8a2b-2994bce38377",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00005.jpg?alt=media&token=7d179938-c419-44f4-b965-1993858d6e71",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00006.jpg?alt=media&token=cdd14cf5-6ed0-4fb7-95f5-74618528a48b",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00007.jpg?alt=media&token=98524820-6d7c-4fb4-89b1-65301e1d6053",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00008.jpg?alt=media&token=7ef9ed49-3221-4d49-8fb4-2c79e5dab333",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00009.jpg?alt=media&token=00d56a11-7a92-4998-a05a-e1dd77b02fe4",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00010.jpg?alt=media&token=24f8f091-acb9-432a-ae0f-7e6227d18803",
    };
    Activity Context;
    List<Story> item = new ArrayList<>();
    boolean isCacheEnabled = true;
    boolean isImmersiveEnabled = false;
    boolean isTextEnabled = false;
    long storyDuration = 5000L;

    public Stories_Adapter(Activity Context, List<Story> subcategory) {
        this.Context = Context;
        this.item = subcategory;
        selectpostoin = -1;
        selectmodel = null;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        view = (LayoutInflater.from(parent.getContext())).inflate(R.layout.layout_stories, parent, false);
        return new ViewHolder(view, viewType);
        //    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(RM.layout.layout_wall, null, false));

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        Context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int devicewidth = (int) (displaymetrics.widthPixels / 7.3);
        viewHolder.imgstory.setClipToOutline(true);
        viewHolder.imgbackground.setClipToOutline(true);
        viewHolder.imgstory.setVisibility(View.VISIBLE);
        viewHolder.imgstory.getLayoutParams().width = devicewidth;
        viewHolder.imgstory.getLayoutParams().height = devicewidth;

        viewHolder.imgbackground.getLayoutParams().width = devicewidth;
        viewHolder.imgbackground.getLayoutParams().height = devicewidth;

        if (!item.get(position).getPhoto().equals("") && !item.get(position).getPhoto().equals(null) && !item.get(position).getPhoto().equals("null")) {
            Glide
                    .with(Context)
                    .load(item.get(position).getPhoto())
                    .placeholder(R.drawable.story_placeholder)
                    .error(R.drawable.story_placeholder)
                    .centerCrop()
                    .into(viewHolder.imgstory);

        }
        {
            // viewHolder.imgstory.setImageDrawable(Context.getResources().getDrawable(RM.drawable.round_box));
        }

        if (item.get(position).getAll_seen() == 1) {
            viewHolder.imgbackground.setColorFilter(Context.getResources().getColor(R.color.darkgrey));
        } else {
            viewHolder.imgbackground.setColorFilter(Context.getResources().getColor(R.color.textcolor));
        }
        // wall_fragment.setimagewidth(devicewidth);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgstory)
        ImageView imgstory;
        @BindView(R.id.imgbackground)
        ImageView imgbackground;

        public ViewHolder(final View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    selectmodel = item.get(getAdapterPosition());
                    selectpostoin = getAdapterPosition();
                    Intent a = new Intent(Context, StatusStoriesActivity.class);
                    a.putExtra("from", "Adapter");
                    a.putExtra(StatusStoriesActivity.STATUS_RESOURCES_KEY, item.get(getAdapterPosition()));
                    a.putExtra(StatusStoriesActivity.STATUS_DURATION_KEY, storyDuration);
                    a.putExtra(StatusStoriesActivity.IS_IMMERSIVE_KEY, isImmersiveEnabled);
                    a.putExtra(StatusStoriesActivity.IS_CACHING_ENABLED_KEY, isCacheEnabled);
                    a.putExtra(StatusStoriesActivity.IS_TEXT_PROGRESS_ENABLED_KEY, isTextEnabled);
                    Context.startActivity(a);
                }
            });

        }


    }

}




