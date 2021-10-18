package com.TBI.Client.Bluff.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.TBI.Client.Bluff.Model.GetBusinessType.Datum;
import com.TBI.Client.Bluff.R;

import java.util.ArrayList;
import java.util.List;

public class BusinessType_Adapter extends BaseAdapter implements Filterable {


    String deliverytype = "";
    String cid = "";
    ArrayList<Datum> GetsearchList = new ArrayList<>();
    private Context mContext;
    private List<Datum> resultList = new ArrayList<>();


    public BusinessType_Adapter(Context context, String cid, String deliverytype) {
        mContext = context;
        this.cid = cid;
        this.deliverytype = deliverytype;
        //DogetRestro();
    }

    public BusinessType_Adapter(Context businessForm, List<Datum> typearray) {
        mContext = businessForm;
        resultList = typearray;
    }


    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Datum getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_text, parent, false);
        }

        TextView search_text = convertView.findViewById(R.id.txttype);
        search_text.setText(getItem(position).getName());

        convertView.setTag(cid);
        return convertView;
    }


    /**
     * Returns a search result for the given book title.
     */
   /* private void findBooks(String bookTitle) {
        // GoogleBooksProtocol is a wrapper for the Google Books API
        searchtext = bookTitle;
        GetsearchList = new ArrayList<>();


    }*/
    /*public void DogetRestro() {
        ArrayList<param> paramArrayList = new ArrayList<>();

        new geturl().getdata(mContext, new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {

                GetsearchList.clear();
                try {
                    restaurantSerach = new Gson().fromJson(data, RestaurantSerach.class);
                    GetsearchList = (ArrayList<DatumProfession>) restaurantSerach.getData();

                    if (!restaurantSerach.getError()) {
                        resultList.clear();
                        resultList.addAll(GetsearchList);
                        notifyDataSetChanged();
                    } else {
                        new AwesomeSuccessDialog(mContext)
                                .setTitle(RM.string.app_name)
                                .setMessage("Oops something went wrong.!")
                                .setColoredCircle(RM.color.red1)
                                .setDialogIconOnly(RM.mipmap.yummy_blackicon)
                                .setCancelable(false)
                                .setPositiveButtonText("OK")
                                .setPositiveButtonbackgroundColor(RM.color.red1)
                                .setPositiveButtonTextColor(RM.color.white)
                                .setPositiveButtonClick(new Closure() {
                                    @Override
                                    public void exec() {

                                        checkservice();
                                        //click
                                    }
                                })
                                .show();
                        //  Toast.makeText(mContext, "Opps something went wrong.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, paramArrayList, "get_all_restaurant_from_city/" + cid + "/" + deliverytype);

    }
    */
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    //  findBooks(constraint.toString());
                    // Assign the data to the FilterResults

                    ArrayList<Datum> GetsearchList1 = new ArrayList<>();
                   /* for (int i = 0; i < GetsearchList.size(); i++) {
                        if (GetsearchList.get(i).getBusinessName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            GetsearchList1.add(GetsearchList.get(i));
                        }
                    }*/

                    filterResults.values = GetsearchList1;
                    filterResults.count = GetsearchList1.size();
/*                    filterResults.values = GetsearchList;
                    filterResults.count = GetsearchList.size();*/
                    // Log.d("values_filter",searchlist.get(0).getPg_name());
                }

                if (constraint == null) {
                    filterResults.values = GetsearchList;
                    filterResults.count = GetsearchList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
//                Log.d("values_publish",searchlist.get(0).getPg_name());
                if (results != null && results.count > 0) {
                    resultList = (List<Datum>) results.values;
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}

/*extends RecyclerView.Adapter<BusinessType_Adapter.ViewHolder> {

    List<DatumProfession> typearray = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    public static int selecpositon = 0;

    public BusinessType_Adapter(Context context, List<DatumProfession> typearray) {
        this.typearray = typearray;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        selecpositon = 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return typearray.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(RM.layout.layout_businesstype, null, false));

    }

    @Override
    public void onBindViewHolder(ViewHolder mViewHolder, int position) {

        if (selecpositon == position) {

            if (sharedpreference.getTheme(context).equalsIgnoreCase("white")) {
                mViewHolder.txtbtype.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(RM.color.black)));
                mViewHolder.txtbtype.setTextColor(context.getResources().getColor(RM.color.white));
            } else {
                mViewHolder.txtbtype.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(RM.color.white)));
                mViewHolder.txtbtype.setTextColor(context.getResources().getColor(RM.color.black));
            }

        } else {

            if (sharedpreference.getTheme(context).equalsIgnoreCase("white")) {
                mViewHolder.txtbtype.setTextColor(context.getResources().getColor(RM.color.black));
            } else {
                mViewHolder.txtbtype.setTextColor(context.getResources().getColor(RM.color.white));
            }

            mViewHolder.txtbtype.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(RM.color.blacklight)));

        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(RM.id.txtbtype)
        TextView txtbtype;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    selecpositon = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }

}*/
