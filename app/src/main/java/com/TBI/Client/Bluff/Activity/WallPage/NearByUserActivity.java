package com.TBI.Client.Bluff.Activity.WallPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.TBI.Client.Bluff.Activity.Mains.BottombarActivity;
import com.TBI.Client.Bluff.Adapter.WallPage.NearByUserAdapter;
import com.TBI.Client.Bluff.Model.SOS.NearBy;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NearByUserActivity extends AppCompatActivity {

    @BindView(R.id.rv_nearby_user)
    RecyclerView rv_nearby_user;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.tv_not_found)
    TextView tv_not_found;

    private NearByUserAdapter nearByUserAdapter;
    private ArrayList<NearBy> nearByArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_user);

        ButterKnife.bind(NearByUserActivity.this);
        rv_nearby_user.setLayoutManager(new GridLayoutManager(NearByUserActivity.this, 3));
        nearByUserAdapter = new NearByUserAdapter(NearByUserActivity.this, nearByArrayList);
        rv_nearby_user.setAdapter(nearByUserAdapter);

        getNearbyUsers();
        img_back.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NearByUserActivity.this, BottombarActivity.class);
        intent.putExtra("type", "home");
        startActivity(intent);
        super.onBackPressed();
    }

    public void getNearbyUsers() {
        if (!Public_Function.isInternetConnected(NearByUserActivity.this)) {
            if (rv_nearby_user.getVisibility() == View.VISIBLE)
                rv_nearby_user.setVisibility(View.GONE);
            tv_not_found.setText("Please Chek your Internet connection....");
            tv_not_found.setVisibility(View.VISIBLE);
        } else {
            nearByArrayList.clear();
            AndroidNetworking.post(geturl.BASE_URL + "getNearbyAndClosedUser")
                    .addHeaders("Authorization", sharedpreference.getUserToken(NearByUserActivity.this))
                    .addBodyParameter("user_id", sharedpreference.getUserId(NearByUserActivity.this))
                    .setPriority(Priority.IMMEDIATE)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("near_users");

                                if (jsonArray.length() == 0) {
                                    if (rv_nearby_user.getVisibility() == View.VISIBLE)
                                        rv_nearby_user.setVisibility(View.GONE);
                                    tv_not_found.setVisibility(View.VISIBLE);
                                } else {

                                    if (tv_not_found.getVisibility() == View.VISIBLE) {
                                        tv_not_found.setVisibility(View.GONE);
                                        rv_nearby_user.setVisibility(View.VISIBLE);
                                    }

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        NearBy nearBy = new NearBy();
                                        nearBy.setId(jsonObject.getInt("id"));
                                        nearBy.setUsername(jsonObject.getString("username"));
                                        nearBy.setFullName(jsonObject.getString("full_name"));
                                        nearBy.setProfessionId(jsonObject.getInt("profession_id"));
                                        nearBy.setPhoto(jsonObject.getString("photo"));
                                        nearBy.setFollowByUser(jsonObject.getInt("follow_by_user"));
                                        nearBy.setDistanceInKm(jsonObject.getDouble("distance_in_km"));

                                        nearByArrayList.add(nearBy);
                                        nearByUserAdapter.notifyDataSetChanged();
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        }
    }
}
