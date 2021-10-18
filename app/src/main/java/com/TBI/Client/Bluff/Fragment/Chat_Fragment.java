package com.TBI.Client.Bluff.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.TBI.Client.Bluff.Activity.Chat.Hide_ArchievedChat;
import com.TBI.Client.Bluff.Adapter.Chat.ChatFragment_Adapter;
import com.TBI.Client.Bluff.R;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Chat_Fragment extends Fragment {

    @BindView(R.id.imgarchieve)
    ImageView imgarchieve;
    @BindView(R.id.tagchat)
    TabLayout tagchat;
    @BindView(R.id.contentfragment3)
    FrameLayout contentfragment3;
    @BindView(R.id.viewpagerchat)
    ViewPager viewpagerchat;

    FragmentManager fm;
    Fragment active;

    ChatFragment_Adapter chatFragment_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatbottom, container, false);
        ButterKnife.bind(this, view);


        imgarchieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), Hide_ArchievedChat.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

            }
        });

        tagchat.addTab(tagchat.newTab().setText("TALKS"));
        tagchat.addTab(tagchat.newTab().setText("TOKENS"));

        //tabsuggest.setupWithViewPager(viewpagerlook);

        for (int i = 0; i < tagchat.getTabCount(); i++) {
            TabLayout.Tab tab = tagchat.getTabAt(i);

            TextView tv = (TextView) (((LinearLayout) ((LinearLayout) tagchat.getChildAt(0)).getChildAt(i)).getChildAt(1));
            tv.setTextAppearance(getActivity(), R.style.tabtextstyle);

            tab.setTag(tv);
        }

        tagchat.getTabAt(0).select();
        tagchat.setTabIndicatorFullWidth(false);


        chatFragment_adapter = new ChatFragment_Adapter(getActivity().getSupportFragmentManager(), getActivity());
        viewpagerchat.setAdapter(chatFragment_adapter);

        tagchat.setupWithViewPager(viewpagerchat);

        tagchat.getTabAt(0).setText("TALKS");
        tagchat.getTabAt(1).setText("TOKENS");

        /*fragment1 = new Look_Fragment();
        fragment2 = new Catch_Fragment();
        fm = getActivity().getSupportFragmentManager();
        active = fragment1;
        fm.beginTransaction().add(RM.id.contentframe2, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(RM.id.contentframe2, fragment1, "1").commit();*/

        tagchat.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

               /* if (tab.getPosition() == 0) {
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                } else {
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                }*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && viewpagerchat.getCurrentItem() == 1) {
                    viewpagerchat.setCurrentItem(0, true);
                    return true;
                } else
                    return false;
            }
        });

        return view;
    }

}
