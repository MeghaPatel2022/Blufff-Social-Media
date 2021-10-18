package com.TBI.Client.Bluff.Adapter.Chat;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.TBI.Client.Bluff.Fragment.Catch_Fragment;
import com.TBI.Client.Bluff.Fragment.Look_Fragment;
import com.TBI.Client.Bluff.Fragment.Talk_Fragment;
import com.TBI.Client.Bluff.Fragment.Token_Fragment;

public class ChatFragment_Adapter extends FragmentPagerAdapter {

    Context context;

    public ChatFragment_Adapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Talk_Fragment(); //ChildFragment1 at position 0
            case 1:
                return new Token_Fragment(); //ChildFragment2 at position 1
        }
        return null; //does not happen
    }


    @Override
    public int getCount() {
        return 2; //three fragments
    }
}
