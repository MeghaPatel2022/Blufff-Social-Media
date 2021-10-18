package com.TBI.Client.Bluff.Adapter.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.TBI.Client.Bluff.Fragment.Friend_Fragment;
import com.TBI.Client.Bluff.Fragment.MapView_Fragment;
import com.TBI.Client.Bluff.R;

import static com.TBI.Client.Bluff.Fragment.Map_Fragment.TAB_SELECT;


public class MapViewrPagerAdapter extends FragmentPagerAdapter {

    Context context;
    private String[] tabTitles = new String[]{"Map", "Friends"};

    public MapViewrPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = v.findViewById(R.id.tv_tab);
        tv.setText(tabTitles[position]);

        if (position == 1) {
            tv.setBackground(context.getResources().getDrawable(R.drawable.tab_unselect));
            tv.setTextColor(Color.parseColor("#000000"));
        }

        TAB_SELECT = -1;

        return v;
    }


    public View addView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = v.findViewById(R.id.tv_tab);
        tv.setText(tabTitles[position]);
        v.setBackground(context.getResources().getDrawable(R.drawable.tab_select));
        tv.setTextColor(Color.parseColor("#000000"));

        return v;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new MapView_Fragment(); //ChildFragment1 at position 0
            case 1:
                return new Friend_Fragment(); //ChildFragment2 at position 1
        }

        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
