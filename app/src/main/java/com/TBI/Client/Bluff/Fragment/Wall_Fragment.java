package com.TBI.Client.Bluff.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.TBI.Client.Bluff.Activity.WallPage.ChatActivity;
import com.TBI.Client.Bluff.Activity.WallPage.GlobalSearch;
import com.TBI.Client.Bluff.Activity.WallPage.NearByUserActivity;
import com.TBI.Client.Bluff.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Wall_Fragment extends Fragment {

    @BindView(R.id.imglogo)
    ImageView imglogo;
    @BindView(R.id.img_chat)
    ImageView img_chat;
    @BindView(R.id.rl_look)
    RelativeLayout rl_look;
    @BindView(R.id.img_nearBy)
    ImageView img_nearBy;
    @BindView(R.id.tv_search)
    TextView tv_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wall, container, false);

        ButterKnife.bind(this, view);

        setFragment();

        img_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
            }
        });

        tv_search.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), GlobalSearch.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
        });

        img_nearBy.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), NearByUserActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
        });

        return view;
    }


    private void setFragment() {
        // set News Fragment
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Look_Fragment look_fragment = new Look_Fragment();
        fragmentTransaction.add(R.id.rl_look, look_fragment, "look");
        fragmentTransaction.commit();
    }

}
