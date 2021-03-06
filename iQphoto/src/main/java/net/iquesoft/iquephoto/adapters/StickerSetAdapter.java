package net.iquesoft.iquephoto.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import net.iquesoft.iquephoto.R;
import net.iquesoft.iquephoto.R2;
import net.iquesoft.iquephoto.models.StickersSet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StickerSetAdapter extends RecyclerView.Adapter<StickerSetAdapter.ViewHolder> {
    private Context mContext;

    private List<StickersSet> mStickersSets;

    private OnStickerSetClickListener mOnStickersSetClickListener;

    public interface OnStickerSetClickListener {
        void onClick(StickersSet stickersSet);
    }

    public void setStickerSetClickListener(OnStickerSetClickListener onStickerSetClickListener) {
        mOnStickersSetClickListener = onStickerSetClickListener;
    }

    public StickerSetAdapter(List<StickersSet> stickersSets) {
        mStickersSets = stickersSets;
    }

    @Override
    public StickerSetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_sticker_set, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StickerSetAdapter.ViewHolder holder, int position) {
        final StickersSet stickersSet = mStickersSets.get(position);

        holder.title.setText(stickersSet.getTitle());

        Picasso.get()
                .load(stickersSet.getIcon())
                .fit()
                .centerCrop()
                .noPlaceholder()
                .into(holder.image);

        holder.itemView.setOnClickListener(view -> mOnStickersSetClickListener.onClick(stickersSet));
    }
    
    @Override
    public int getItemCount() {
        return mStickersSets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.text_view_stickers_sets_title)
        TextView title;

        @BindView(R2.id.image_view_stickers_sets_image)
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}