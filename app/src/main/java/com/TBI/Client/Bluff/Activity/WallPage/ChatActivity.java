package com.TBI.Client.Bluff.Activity.WallPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.TBI.Client.Bluff.Activity.Chat.Hide_ArchievedChat;
import com.TBI.Client.Bluff.Activity.Home.OpenCamera1;
import com.TBI.Client.Bluff.Activity.Profile.ProfilePage;
import com.TBI.Client.Bluff.Adapter.Chat.ChatFragment_Adapter;
import com.TBI.Client.Bluff.Activity.Mains.BottombarActivity;
import com.TBI.Client.Bluff.Fragment.Talk_Fragment;
import com.TBI.Client.Bluff.R;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    //    @BindView(RM.id.imgarchieve)
//    ImageView imgarchieve;
//    @BindView(RM.id.tagchat)
//    TabLayout tagchat;
//    @BindView(RM.id.contentfragment3)
//    FrameLayout contentfragment3;
//    @BindView(RM.id.viewpagerchat)
//    ViewPager viewpagerchat;
//
//    @BindView(RM.id.imgMyLocation)
//    ImageView imgMyLocation;
//    @BindView(RM.id.imgwall)
//    ImageView imgwall;
//    @BindView(RM.id.imgcamera)
//    ImageView imgcamera;
//    @BindView(RM.id.imgchat)
//    ImageView imgchat;
//    @BindView(RM.id.imgProfile)
//    ImageView imgProfile;
    @BindView(R.id.rl_chat)
    RelativeLayout rl_chat;

//    FragmentManager fm;
//    Fragment active;
//
//    ChatFragment_Adapter chatFragment_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(ChatActivity.this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Talk_Fragment hello = new Talk_Fragment();
        fragmentTransaction.add(R.id.rl_chat, hello, "Talk");
        fragmentTransaction.commit();

//        imgarchieve.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(ChatActivity.this, Hide_ArchievedChat.class);
//                startActivity(intent);
//                overridePendingTransition(RM.anim.slide_in_up, RM.anim.stay);
//
//            }
//        });
//
//        tagchat.addTab(tagchat.newTab().setText("TALKS"));
//        tagchat.addTab(tagchat.newTab().setText("TOKENS"));
//
//        for (int i = 0; i < tagchat.getTabCount(); i++) {
//            TabLayout.Tab tab = tagchat.getTabAt(i);
//
//            TextView tv = (TextView) (((LinearLayout) ((LinearLayout) tagchat.getChildAt(0)).getChildAt(i)).getChildAt(1));
//            tv.setTextAppearance(ChatActivity.this, RM.style.tabtextstyle);
//
//            tab.setTag(tv);
//        }
//
//        tagchat.getTabAt(0).select();
//        tagchat.setTabIndicatorFullWidth(false);

//
//        chatFragment_adapter = new ChatFragment_Adapter(getSupportFragmentManager(), ChatActivity.this);
//        viewpagerchat.setAdapter(chatFragment_adapter);
//
//        tagchat.setupWithViewPager(viewpagerchat);
//
//        tagchat.getTabAt(0).setText("TALKS");
//        tagchat.getTabAt(1).setText("TOKENS");
//
//        tagchat.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
//        imgMyLocation.setOnClickListener(v -> {
//            mapclick();
//            Intent intent = new Intent(ChatActivity.this, BottombarActivity.class);
//            intent.putExtra("type", "map");
//            startActivity(intent);
//        });
//
//        imgwall.setOnClickListener(v -> {
//            lookclick();
//            Intent intent = new Intent(ChatActivity.this, BottombarActivity.class);
//            intent.putExtra("type", "home");
//            startActivity(intent);
//        });
//
//        imgProfile.setOnClickListener(view -> {
//            profileclick();
//            Intent intent = new Intent(ChatActivity.this, ProfilePage.class);
//            intent.putExtra("type", "left");
//            startActivity(intent);
//        });
//
//        imgcamera.setOnClickListener(view -> {
//            Intent intent = new Intent(ChatActivity.this, OpenCamera1.class);
//            startActivity(intent);
//            overridePendingTransition(RM.anim.slide_in_up, RM.anim.stay);
//        });
//
//        imgchat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ChatActivity.this, BottombarActivity.class);
//                intent.putExtra("type", "catch");
//                startActivity(intent);
//            }
//        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChatActivity.this, BottombarActivity.class);
        intent.putExtra("type", "home");
        startActivity(intent);
        super.onBackPressed();
    }

    //    public void mapclick() {
//        imgMyLocation.setImageDrawable(getResources().getDrawable(RM.drawable.map1_selected));
//        imgwall.setImageDrawable(getResources().getDrawable(RM.drawable.look1_unselected));
//        imgchat.setImageDrawable(getResources().getDrawable(RM.drawable.catch_unselected));
//        imgProfile.setImageDrawable(getResources().getDrawable(RM.drawable.me_unselected));
//    }
//
//    public void lookclick() {
//        imgMyLocation.setImageDrawable(getResources().getDrawable(RM.drawable.map1_unselected));
//        imgwall.setImageDrawable(getResources().getDrawable(RM.drawable.look1_selected));
//        imgchat.setImageDrawable(getResources().getDrawable(RM.drawable.catch_unselected));
//        imgProfile.setImageDrawable(getResources().getDrawable(RM.drawable.me_unselected));
//    }
//
//    public void profileclick() {
//        imgMyLocation.setImageDrawable(getResources().getDrawable(RM.drawable.map1_unselected));
//        imgwall.setImageDrawable(getResources().getDrawable(RM.drawable.look1_unselected));
//        imgchat.setImageDrawable(getResources().getDrawable(RM.drawable.catch_unselected));
//        imgProfile.setImageDrawable(getResources().getDrawable(RM.drawable.me_selected));
//    }
}
