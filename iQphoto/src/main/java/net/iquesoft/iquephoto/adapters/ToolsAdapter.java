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
import net.iquesoft.iquephoto.models.Image;
import net.iquesoft.iquephoto.models.Tool;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToolsAdapter extends RecyclerView.Adapter<ToolsAdapter.ViewHolder> {
    private Context mContext;

    private List<Tool> mToolsList;

    private OnToolsClickListener mOnToolsClickListener;

    public interface OnToolsClickListener {
        void onClick(Tool tool);
    }

    public void setOnToolsClickListener(OnToolsClickListener onToolsClickListener) {
        mOnToolsClickListener = onToolsClickListener;
    }

    public ToolsAdapter(List<Tool> tools) {
        mToolsList = tools;
    }

    @Override
    public ToolsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.item_tool, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ToolsAdapter.ViewHolder holder, int position) {
        final Tool tool = mToolsList.get(position);

//        holder.txttitle.setText(mContext.getResources().getString(tool.getTitle()));
        holder.imgicon.setImageDrawable(mContext.getResources().getDrawable(tool.getIcon()));

        /*holder.button.setCompoundDrawablesWithIntrinsicBounds(null,
                ResourcesCompat.getDrawable(mContext.getResources(), tool.getIcon(), null),
                null, null);*/

        holder.lnclick.setOnClickListener(view -> mOnToolsClickListener.onClick(tool));
    }

    @Override
    public int getItemCount() {
        return mToolsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.lnclick)
        LinearLayout lnclick;
        @BindView(R2.id.imgicon)
        ImageView imgicon;
//        @BindView(R2.id.txttitle)
//        TextView txttitle;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}