package net.iquesoft.iquephoto.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import net.iquesoft.iquephoto.R;
import net.iquesoft.iquephoto.R2;
import net.iquesoft.iquephoto.models.Adjust;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdjustAdapter extends RecyclerView.Adapter<AdjustAdapter.ViewHolder> {
    private Context mContext;
    
    private List<Adjust> mAdjustList;

    private OnAdjustClickListener mOnAdjustClickListener;

    public interface OnAdjustClickListener {
        void onAdjustClick(Adjust adjust);
    }

    public void setOnAdjustClickListener(OnAdjustClickListener onAdjustClickListener) {
        mOnAdjustClickListener = onAdjustClickListener;
    }

    public AdjustAdapter(List<Adjust> adjustList) {
        mAdjustList = adjustList;
    }

    @Override
    public AdjustAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tool, parent, false);

        return new AdjustAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdjustAdapter.ViewHolder holder, int position) {
        final Adjust adjust = mAdjustList.get(position);


        holder.txttitle.setText(mContext.getResources().getString(adjust.getTitle()));
        holder.imgicon.setImageDrawable(mContext.getResources().getDrawable(adjust.getIcon()));
       /* holder.button.setCompoundDrawablesWithIntrinsicBounds(null,
                ResourcesCompat.getDrawable(mContext.getResources(), adjust.getIcon(), null),
                null, null);*/

        holder.lnclick.setOnClickListener(view -> mOnAdjustClickListener.onAdjustClick(adjust));
    }

    @Override
    public int getItemCount() {
        return mAdjustList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.lnclick)
        LinearLayout lnclick;
        @BindView(R2.id.imgicon)
        ImageView imgicon;
        @BindView(R2.id.txttitle)
        TextView txttitle;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}