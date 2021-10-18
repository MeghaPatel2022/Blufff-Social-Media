package com.TBI.Client.Bluff.Activity.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Adapter.WallPage.NearByAdapter;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.Model.GetFriends.GetCloseFrend;
import com.TBI.Client.Bluff.Model.SOS.NearBy;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.api.conversation.MobiComConversationService;
import com.bumptech.glide.Glide;
import com.ebanx.swipebtn.SwipeButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SOSActivity extends AppCompatActivity {


    private static RecyclerView rv_near_by;
    private static TextView tv_no_near_by;
    private static Activity activity;

    private static NearByAdapter nearByAdapter;
    private static ArrayList<NearBy> nearByArrayList = new ArrayList<>();

    @BindView(R.id.img_user1)
    CircleImageView img_user1;

    @BindView(R.id.img_user2)
    CircleImageView img_user2;

    @BindView(R.id.img_user3)
    CircleImageView img_user3;

    @BindView(R.id.img_user4)
    CircleImageView img_user4;

    @BindView(R.id.img_user5)
    CircleImageView img_user5;

    @BindView(R.id.img_premium_user1)
    CircleImageView img_premium_user1;

    @BindView(R.id.img_premium_user2)
    CircleImageView img_premium_user2;

    @BindView(R.id.img_premium_user3)
    CircleImageView img_premium_user3;

    @BindView(R.id.img_premium_user4)
    CircleImageView img_premium_user4;

    @BindView(R.id.img_premium_user5)
    CircleImageView img_premium_user5;

    @BindView(R.id.active_sos_btn)
    SwipeButton active_sos_btn;

    @BindView(R.id.img_back)
    ImageView img_back;

    int[] imageUser = {R.id.img_user1,
            R.id.img_user2, R.id.img_user3, R.id.img_user4, R.id.img_user5};

    public static void getNearbyUsers() {
        nearByArrayList.clear();
        AndroidNetworking.post(geturl.BASE_URL + "getNearbyAndClosedUser")
                .addHeaders("Authorization", sharedpreference.getUserToken(activity))
                .addBodyParameter("user_id", sharedpreference.getUserId(activity))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("near_users");

                            if (jsonArray.length() == 0) {
                                if (rv_near_by.getVisibility() == View.VISIBLE)
                                    rv_near_by.setVisibility(View.GONE);
                                tv_no_near_by.setVisibility(View.VISIBLE);
                            } else {

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
                                }

                                if (tv_no_near_by.getVisibility() == View.VISIBLE)
                                    tv_no_near_by.setVisibility(View.GONE);
                                rv_near_by.setVisibility(View.VISIBLE);
                                nearByAdapter = new NearByAdapter(nearByArrayList, activity);
                                rv_near_by.setLayoutManager(new GridLayoutManager(activity, 5));
                                rv_near_by.setAdapter(nearByAdapter);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(SOSActivity.this));

        activity = SOSActivity.this;
        rv_near_by = findViewById(R.id.rv_near_by);
        tv_no_near_by = findViewById(R.id.tv_no_near_by);
        ButterKnife.bind(SOSActivity.this);
        AndroidNetworking.initialize(SOSActivity.this);

        getNearbyUsers();

        active_sos_btn.setOnActiveListener(() -> SOSActive());

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        img_user1.setOnClickListener(v -> {
            Intent intent = new Intent(SOSActivity.this, FriendsActivity.class);
            startActivity(intent);
            finish();
        });

        img_user2.setOnClickListener(v -> {

            Intent intent = new Intent(SOSActivity.this, FriendsActivity.class);
            startActivity(intent);
            finish();
        });

        img_user3.setOnClickListener(v -> {

            Intent intent = new Intent(SOSActivity.this, FriendsActivity.class);
            startActivity(intent);
            finish();
        });

        img_user4.setOnClickListener(v -> {

            Intent intent = new Intent(SOSActivity.this, FriendsActivity.class);
            startActivity(intent);
            finish();
        });

        img_user5.setOnClickListener(v -> {

            Intent intent = new Intent(SOSActivity.this, FriendsActivity.class);
            startActivity(intent);
            finish();
        });

        if (Public_Function.getCloseFrends1.size() == 1) {
            img_user1.setEnabled(false);
        } else if (Public_Function.getCloseFrends1.size() == 2) {
            img_user1.setEnabled(false);
            img_user2.setEnabled(false);
        } else if (Public_Function.getCloseFrends1.size() == 3) {
            img_user1.setEnabled(false);
            img_user2.setEnabled(false);
            img_user3.setEnabled(false);
        } else if (Public_Function.getCloseFrends1.size() == 4) {
            img_user1.setEnabled(false);
            img_user2.setEnabled(false);
            img_user3.setEnabled(false);
            img_user4.setEnabled(false);
        } else if (Public_Function.getCloseFrends1.size() == 5) {
            img_user1.setEnabled(false);
            img_user2.setEnabled(false);
            img_user3.setEnabled(false);
            img_user4.setEnabled(false);
            img_user5.setEnabled(false);
        }

        for (int i = 0; i < Public_Function.getCloseFrends1.size(); i++) {
            GetCloseFrend getCloseFrend = Public_Function.getCloseFrends1.get(i);
            CircleImageView userId = findViewById(imageUser[i]);

            Glide
                    .with(SOSActivity.this)
                    .load(getCloseFrend.getPhoto())
                    .error(R.drawable.story_placeholder)
                    .into(userId);
        }

        img_premium_user1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SOSActivity.this, "This feature is premium", Toast.LENGTH_SHORT).show();
            }
        });

        img_premium_user2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SOSActivity.this, "This feature is premium", Toast.LENGTH_SHORT).show();
            }
        });

        img_premium_user3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SOSActivity.this, "This feature is premium", Toast.LENGTH_SHORT).show();
            }
        });

        img_premium_user4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SOSActivity.this, "This feature is premium", Toast.LENGTH_SHORT).show();
            }
        });

        img_premium_user5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SOSActivity.this, "This feature is premium", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SOSActive() {

        for (int i = 0; i < Public_Function.getCloseFrends1.size(); i++) {

            MobiComUserPreference userPreferences = MobiComUserPreference.getInstance(SOSActivity.this);
            Message messageToSend = new Message();
            messageToSend.setTo(String.valueOf(Public_Function.getCloseFrends1.get(i).getUserId()));
            messageToSend.setContactIds(String.valueOf(Public_Function.getCloseFrends1.get(i).getUserId()));
            messageToSend.setRead(Boolean.TRUE);
            messageToSend.setStoreOnDevice(Boolean.TRUE);
            messageToSend.setSendToDevice(Boolean.FALSE);
            messageToSend.setType(Message.MessageType.MT_OUTBOX.getValue());
            messageToSend.setMessage("I need your help!");
            messageToSend.setDeviceKeyString(userPreferences.getDeviceKeyString());
            messageToSend.setSource(Message.Source.MT_MOBILE_APP.getValue());

            new MobiComConversationService(SOSActivity.this).sendMessage(messageToSend);
        }

        AndroidNetworking.post(geturl.BASE_URL + "sos")
                .addHeaders("Authorization", sharedpreference.getUserToken(SOSActivity.this))
                .addBodyParameter("user_id", sharedpreference.getUserId(SOSActivity.this))
                .addBodyParameter("lat", String.valueOf(Public_Function.current_lat))
                .addBodyParameter("lang", String.valueOf(Public_Function.current_long))
                .addBodyParameter("address", Public_Function.current_add)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getString("success").equalsIgnoreCase("true")) {
                                Toast.makeText(SOSActivity.this, "Active!", Toast.LENGTH_SHORT).show();
                                finish();
//                                Log.e("LLL_Parameter: ", sharedpreference.getUserId(SOSActivity.this) + "  lat: " + String.valueOf(Public_Function.current_lat)
//                                        + "  lang: "+String.valueOf(Public_Function.current_long) + "   address: " + Public_Function.current_add);
                                Log.e("LLL_Res: ", response.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLL_erro: ", anError.getMessage());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
