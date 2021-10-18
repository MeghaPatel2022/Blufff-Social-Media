package com.TBI.Client.Bluff.Activity.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.TBI.Client.Bluff.Adapter.Chat.AllFriendsAdapter;
import com.TBI.Client.Bluff.Adapter.Chat.FrequentFriendAdapter;
import com.TBI.Client.Bluff.R;
import com.otaliastudios.cameraview.internal.utils.Op;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OptionChatActivity extends AppCompatActivity {

    @BindView(R.id.rv_frequent_friend)
    RecyclerView rv_frequent_friend;
    @BindView(R.id.rv_allFrend)
    RecyclerView rv_allFrend;

    @BindView(R.id.rl_archive_chat)
    RelativeLayout rl_archive_chat;
    @BindView(R.id.rl_settings)
    RelativeLayout rl_settings;
    @BindView(R.id.rl_new_group)
    RelativeLayout rl_new_group;
    @BindView(R.id.img_back)
    ImageView img_back;

    private FrequentFriendAdapter frequentFriendAdapter;
    private AllFriendsAdapter allFriendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_chat);

        ButterKnife.bind(OptionChatActivity.this);
        rv_frequent_friend.setLayoutManager(new GridLayoutManager(OptionChatActivity.this, 3));
        frequentFriendAdapter = new FrequentFriendAdapter(OptionChatActivity.this);
        rv_frequent_friend.setAdapter(frequentFriendAdapter);

        rv_allFrend.setLayoutManager(new GridLayoutManager(OptionChatActivity.this, 3));
        allFriendsAdapter = new AllFriendsAdapter(OptionChatActivity.this);
        rv_allFrend.setAdapter(allFriendsAdapter);

        rl_archive_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionChatActivity.this, Hide_ArchievedChat.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
            }
        });

        rl_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionChatActivity.this, ChatSettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
            }
        });

        rl_new_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionChatActivity.this, GroupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
