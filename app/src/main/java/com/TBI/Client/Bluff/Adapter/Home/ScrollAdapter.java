package com.TBI.Client.Bluff.Adapter.Home;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Model.GetFeed.Rating;
import com.TBI.Client.Bluff.R;

import java.util.ArrayList;
import java.util.List;

public class ScrollAdapter /*extends RecyclerView.Adapter<ScrollAdapter.ViewHolder> {
    Activity Context;
    List<Stock> item = new ArrayList<>();

  *//*  public ScrollAdapter(Activity Context, List<Stock> subcategory) {
        this.Context = Context;
        this.item = subcategory;
    }*//*


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(RM.layout.scroll_child, null, false));

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        //viewHolder.txtPercent.setText(item.get(i).getValue());

    }


    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(RM.id.imgscroll)
        ImageView imgscroll;
        @BindView(RM.id.txtPercent)
        TextView txtPercent;


        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


    }

    public void addAdll(List<Stock> stockarray) {

        item.clear();
        item.addAll(stockarray);
        notifyDataSetChanged();
    }

}*/ extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    Context context;

    List<Rating> ratings = new ArrayList<>();

    public ScrollAdapter(Context context, List<Rating> ratings) {
        this.context = context;
        this.ratings = ratings;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scroll_child, parent, false);
            viewHolder = new ViewHolderItem(view);
        } else if (viewType == TYPE_HEADER || viewType == TYPE_FOOTER) {
            //inflate your layout and pass it to view holder
            //View view = LayoutInflater.from(parent.getContext()).inflate(RM.layout.header_footer, parent, false);
            View view = new View(parent.getContext());
            DisplayMetrics metrics = parent.getContext().getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            view.setLayoutParams(new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT));
            viewHolder = new ViewHolderHeaderOrFooter(view);

        }
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_ITEM) {

            ViewHolderItem viewHolder0 = (ViewHolderItem) holder;

            viewHolder0.txtname.setText(ratings.get(position - 1).getName() + "");
//            if (ratings.get(position - 1).getStock().equalsIgnoreCase("down")) {
//                viewHolder0.txtPercent.setText(stocklist.get(position - 1).getValue() + " -");
//                viewHolder0.txtPercent.setTextColor(Color.RED);
//            } else {
//                viewHolder0.txtPercent.setText(stocklist.get(position - 1).getValue() + " +");
//                viewHolder0.imgscroll.setRotation(180);
//                viewHolder0.txtPercent.setTextColor(Color.GREEN);
//            }

            viewHolder0.txtPercent.setText(String.valueOf(ratings.get(position - 1).getValue()));
            viewHolder0.imgscroll.setRotation(180);
            viewHolder0.txtPercent.setTextColor(Color.YELLOW);

//            if (ratings.get(position - 1).getActivity().equalsIgnoreCase("followers")) {
//                viewHolder0.imgtype.setImageDrawable(context.getResources().getDrawable(RM.drawable.followers));
//            } else {
//
//            }

        }
    }

    @Override
    public int getItemCount() {
        // 1 for header and 1 for footer
        return ratings.size() + 1 + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        else if (isPositionFooter(position))
            return TYPE_FOOTER;
        return TYPE_ITEM;
    }


    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private boolean isPositionFooter(int position) {
        return position == getItemCount() - 1;
    }

    /*public void addAll(List<Stock> stockarray, Context context) {
        stocklist.clear();
        stocklist.addAll(stockarray);
        this.context = context;
        notifyDataSetChanged();

    }*/


    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        TextView txtPercent, txtname;
        ImageView imgscroll, imgtype;

        public ViewHolderItem(View v) {
            super(v);
            mView = v;
            txtPercent = v.findViewById(R.id.txtPercent);
            txtname = v.findViewById(R.id.txtname);
            imgscroll = v.findViewById(R.id.imgscroll);
//            imgtype = (ImageView) v.findViewById(RM.id.imgtype);
        }
    }

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public static class ViewHolderHeaderOrFooter extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;

        public ViewHolderHeaderOrFooter(View v) {
            super(v);
            mView = v;
        }
    }
}
