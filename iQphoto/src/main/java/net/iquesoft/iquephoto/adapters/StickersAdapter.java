package net.iquesoft.iquephoto.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import net.iquesoft.iquephoto.R;
import net.iquesoft.iquephoto.R2;
import net.iquesoft.iquephoto.models.Sticker;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StickersAdapter extends RecyclerView.Adapter<StickersAdapter.ViewHolder> {
    private Context mContext;

    private List<Sticker> mStickersList;

    private OnStickerClickListener mOnStickerClickListener;

    public interface OnStickerClickListener {
        void onClick(Sticker sticker);
    }

    public void setOnStickerClickListener(OnStickerClickListener onStickerClickListener) {
        mOnStickerClickListener = onStickerClickListener;
    }

    public StickersAdapter(List<Sticker> stickers) {
        mStickersList = stickers;
    }

    @Override
    public StickersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_sticker, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StickersAdapter.ViewHolder holder, int position) {
        final Sticker sticker = mStickersList.get(position);

        Picasso.get()
                .load(sticker.getImage())
                .fit()
                .centerCrop()
                .noPlaceholder()
                .into(holder.sticker);

        holder.sticker.setOnClickListener(view -> mOnStickerClickListener.onClick(sticker));
    }

    @Override
    public int getItemCount() {
        return mStickersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.image_view_sticker)
        ImageView sticker;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}