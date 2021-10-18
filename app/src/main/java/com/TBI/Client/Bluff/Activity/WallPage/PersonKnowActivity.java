package com.TBI.Client.Bluff.Activity.WallPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.TBI.Client.Bluff.Activity.Mains.BottombarActivity;
import com.TBI.Client.Bluff.Adapter.Post.NearByPeopleAdapter;
import com.TBI.Client.Bluff.Adapter.WallPage.PeopleKnowAdapter;
import com.TBI.Client.Bluff.Adapter.WallPage.RequestedAdapter;
import com.TBI.Client.Bluff.Model.GetFeed.Image;
import com.TBI.Client.Bluff.Model.GetFeed.PersonYouKnow;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.UserPages.WelcomeActivity;
import com.TBI.Client.Bluff.Utils.Public_Function;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonKnowActivity extends AppCompatActivity {

    @BindView(R.id.rv_personknow)
    RecyclerView rv_personknow;
    @BindView(R.id.rv_near_by)
    RecyclerView rv_near_by;
    @BindView(R.id.rv_requested_user)
    RecyclerView rv_requested_user;
    @BindView(R.id.img_back)
    ImageView img_back;

    List<PersonYouKnow> personYouKnows = new ArrayList<>();

    PeopleKnowAdapter peopleKnowAdapter;
    RequestedAdapter requestedAdapter;
    NearByPeopleAdapter nearByUserAdapter;
    String activity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_know);

        ButterKnife.bind(PersonKnowActivity.this);

        activity = getIntent().getStringExtra("Activity");

        requestedAdapter =new RequestedAdapter(PersonKnowActivity.this,Public_Function.requestedUsers);
        rv_requested_user.setLayoutManager(new GridLayoutManager(PersonKnowActivity.this,3));
        rv_requested_user.setAdapter(requestedAdapter);

        peopleKnowAdapter = new PeopleKnowAdapter(PersonKnowActivity.this, Public_Function.personYouKnows);
        rv_personknow.setLayoutManager(new GridLayoutManager(PersonKnowActivity.this, 3));
        rv_personknow.setAdapter(peopleKnowAdapter);

        nearByUserAdapter = new NearByPeopleAdapter(Public_Function.nearByUsers,PersonKnowActivity.this);
        rv_near_by.setLayoutManager(new GridLayoutManager(PersonKnowActivity.this, 3));
        rv_near_by.setAdapter(nearByUserAdapter);

        if (activity.equals("Person")) {
            if (rv_personknow.getVisibility() == View.GONE)
                rv_personknow.setVisibility(View.VISIBLE);

            rv_near_by.setVisibility(View.GONE);
            rv_requested_user.setVisibility(View.GONE);
        } else if (activity.equals("Request")) {
            if (rv_requested_user.getVisibility() == View.GONE)
                rv_requested_user.setVisibility(View.VISIBLE);

            rv_personknow.setVisibility(View.GONE);
            rv_near_by.setVisibility(View.GONE);
        } else {
            if (rv_near_by.getVisibility() == View.GONE)
                rv_near_by.setVisibility(View.VISIBLE);

            rv_personknow.setVisibility(View.GONE);
            rv_requested_user.setVisibility(View.GONE);
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonKnowActivity.this, BottombarActivity.class);
                intent.putExtra("type", "home");
                startActivity(intent);
                finish();
            }
        });


    }


}
