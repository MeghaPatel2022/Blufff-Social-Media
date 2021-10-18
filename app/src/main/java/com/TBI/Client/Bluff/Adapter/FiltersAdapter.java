package com.TBI.Client.Bluff.Adapter;

import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Post.CreateStory;
import com.TBI.Client.Bluff.Activity.Home.OpenCamera1;

import net.iquesoft.iquephoto.R;
import net.iquesoft.iquephoto.R2;
import net.iquesoft.iquephoto.models.Filter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.ViewHolder> {
    OpenCamera1 camera1;
    CreateStory createStory;
    private int mCurrentPosition = 0;
    private Context mContext;
    private List<Filter> mFiltersList;

    public FiltersAdapter(Context openCamera1, List<Filter> abc, OpenCamera1 camera1) {
        mContext = openCamera1;
        mFiltersList = abc;
        this.camera1 = camera1;
    }

    public FiltersAdapter(CreateStory openCamera1, List<Filter> abc, CreateStory createStory) {
        mContext = openCamera1;
        mFiltersList = abc;
        this.createStory = createStory;
    }


    @Override
    public FiltersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_filter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FiltersAdapter.ViewHolder holder, int position) {
        final Filter filter = mFiltersList.get(position);

        holder.filterTitle.setText(filter.getTitle());
        if (position == 0) {
            holder.filterImageView.setColorFilter(null);
        } else {
            holder.filterImageView.setColorFilter(new ColorMatrixColorFilter(filter.getColorMatrix()));
        }


        if (mCurrentPosition == position) {
            holder.filterChecked.setVisibility(View.VISIBLE);
        } else
            holder.filterChecked.setVisibility(View.GONE);


        holder.filterImageView.setTag(position);
        holder.filterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = (int) view.getTag();
                mCurrentPosition = pos;
                notifyDataSetChanged();
                if (camera1 != null) {
                    camera1.changeCurrentFilter(pos);
                } else {
                    createStory.changeCurrentFilter(pos);
                }
            }
        });

        /*holder.filterImageView.setOnClickListener(view -> {
            notifyItemChanged(mCurrentPosition);
            if (mCurrentPosition != position) {
                mCurrentPosition = position;
                notifyItemChanged(position);
            } else {
                // mOnFilterClickListener.onIntensityClicked();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mFiltersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.text_view_filter_title)
        TextView filterTitle;

        @BindView(R2.id.image_view_filter)
        ImageView filterImageView;

        @BindView(R2.id.image_view_filter_checked)
        ImageView filterChecked;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}