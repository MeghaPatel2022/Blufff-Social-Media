package com.TBI.Client.Bluff.Adapter.Chat;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.TBI.Client.Bluff.Fragment.Fragment_freinds;
import com.TBI.Client.Bluff.Fragment.Group_Fragment;

public class ChatPager_Adapter extends FragmentPagerAdapter {

    Context context;
    Fragment freind = new Fragment_freinds();
    Fragment group_fragment = new Group_Fragment();

    public ChatPager_Adapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return freind;

            //ChildFragment1 at position 0
            case 1:
                return group_fragment; //ChildFragment2 at position 1
        }
        return null; //does not happen
    }


    @Override
    public int getCount() {
        return 2; //three fragments
    }

    public Fragment_freinds getfragment1() {
        return (Fragment_freinds) freind;
    }

    public Group_Fragment getfragment2() {
        return (Group_Fragment) group_fragment;
    }

}
