package com.TBI.Client.Bluff.Activity.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.TBI.Client.Bluff.Fragment.Group_Fragment;
import com.TBI.Client.Bluff.Fragment.Talk_Fragment;
import com.TBI.Client.Bluff.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupActivity extends AppCompatActivity {

    @BindView(R.id.rl_chat)
    RelativeLayout rl_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        ButterKnife.bind(GroupActivity.this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Group_Fragment hello = new Group_Fragment();
        fragmentTransaction.add(R.id.rl_chat, hello, "Group");
        fragmentTransaction.commit();
    }
}
