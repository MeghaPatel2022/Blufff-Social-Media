package com.TBI.Client.Bluff.UserPages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.TBI.Client.Bluff.R;

public class CustomPagerAdapter extends PagerAdapter {
    private Context context;
    public CustomPagerAdapter(Context context) {
        this.context = context;
    }
    /*
    This callback is responsible for creating a page. We inflate the layout and set the drawable
    to the ImageView based on the position. In the end we add the inflated layout to the parent
    container .This method returns an object key to identify the page view, but in this example page view
    itself acts as the object key
    */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.intro_1, null);
        ImageView imageView = view.findViewById(R.id.img1);
        imageView.setImageDrawable(context.getResources().getDrawable(getImageAt(position)));
        container.addView(view);
        return view;
    }
    /*
    This callback is responsible for destroying a page. Since we are using view only as the
    object key we just directly remove the view from parent container
    */
    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }
    /*
    Returns the count of the total pages
    */
    @Override
    public int getCount() {
        return 12;
    }
    /*
    Used to determine whether the page view is associated with object key returned by instantiateItem.
    Since here view only is the key we return view==object
    */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }
    private int getImageAt(int position) {
        switch (position) {
            case 0:
                return R.drawable.intro_1;
            case 1:
                return R.drawable.intro_2;
            case 2:
                return R.drawable.intro_3;
            case 3:
                return R.drawable.intro_4;
            case 4:
                return R.drawable.intro_5;
            case 5:
                return R.drawable.intro_6;
            case 6:
                return R.drawable.intro_7;
            case 7:
                return R.drawable.intro_8;
            case 8:
                return R.drawable.intro_9;
            case 9:
                return R.drawable.intro_10;
            case 10:
                return R.drawable.intro_11;
            case 11:
                return R.drawable.intro_12;
            default:
                return R.drawable.intro_1;
        }
    }


}
