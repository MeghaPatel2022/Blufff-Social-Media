package com.TBI.Client.Bluff.Activity.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.TBI.Client.Bluff.Adapter.Map.CloseFriendAdapter;
import com.TBI.Client.Bluff.Adapter.Map.OtherFriendsAdapter;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.Model.GetFriends.GetCloseFrend;
import com.TBI.Client.Bluff.Model.GetFriends.GetOtherFriends;
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

import butterknife.ButterKnife;

public class FriendsActivity extends AppCompatActivity {

    public static RecyclerView rv_close_frnd;
    public static RecyclerView rv_other_frnd;
    public static ArrayList<GetCloseFrend> getCloseFrends = new ArrayList<>();
    public static ArrayList<GetOtherFriends> getOtherFriends = new ArrayList<>();
    public static Activity activity;
    private static CloseFriendAdapter closeFriendAdapter;
    private static OtherFriendsAdapter otherFriendsAdapter;
    private static String token = "";

    public static void getFrendsData() {
        getCloseFrends.clear();
        AndroidNetworking.post(geturl.BASE_URL + "manage_closed_friends")
                .addHeaders("Authorization", sharedpreference.getUserToken(activity))
                .addBodyParameter("user_id", sharedpreference.getUserId(activity))
                .addBodyParameter("friend_ids", "")
                .addBodyParameter("action", "2")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                GetCloseFrend getCloseFrend = new GetCloseFrend();
                                getCloseFrend.setUserId(jsonObject.getInt("user_id"));
                                getCloseFrend.setUsername(jsonObject.getString("username"));
                                getCloseFrend.setFullName(jsonObject.getString("full_name"));
                                getCloseFrend.setLat(jsonObject.getString("lat"));
                                getCloseFrend.setLongt(jsonObject.getString("longt"));
                                getCloseFrend.setPhoto(jsonObject.getString("photo"));

                                getCloseFrends.add(getCloseFrend);
                            }


                            if (Public_Function.getCloseFrends1.isEmpty())
                                Public_Function.getCloseFrends1.addAll(getCloseFrends);
                            else {
                                Public_Function.getCloseFrends1.clear();
                                Public_Function.getCloseFrends1.addAll(getCloseFrends);
                            }

                            closeFriendAdapter = new CloseFriendAdapter(getCloseFrends, activity, otherFriendsAdapter, "activity");
                            rv_close_frnd.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
                            rv_close_frnd.setAdapter(closeFriendAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("LLL_Error: ", anError.getMessage());
                    }
                });
    }

    public static void getOtherFrendsData() {
        getOtherFriends.clear();
        AndroidNetworking.post(geturl.BASE_URL + "manage_closed_friends")
                .addHeaders("Authorization", sharedpreference.getUserToken(activity))
                .addBodyParameter("user_id", sharedpreference.getUserId(activity))
                .addBodyParameter("friend_ids", "")
                .addBodyParameter("action", "2")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray1 = response.getJSONArray("followers");

                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject jsonObject = jsonArray1.getJSONObject(i);
                                GetOtherFriends getOtherFriend = new GetOtherFriends();
                                getOtherFriend.setUserId(jsonObject.getInt("user_id"));
                                getOtherFriend.setUsername(jsonObject.getString("username"));
                                getOtherFriend.setFullName(jsonObject.getString("full_name"));
                                getOtherFriend.setLat(jsonObject.getString("lat"));
                                getOtherFriend.setLongt(jsonObject.getString("longt"));
                                getOtherFriend.setPhoto(jsonObject.getString("photo"));

                                getOtherFriends.add(getOtherFriend);
                            }

                            otherFriendsAdapter = new OtherFriendsAdapter(getOtherFriends, getCloseFrends, activity, closeFriendAdapter, "activity");
                            rv_other_frnd.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
                            rv_other_frnd.setAdapter(otherFriendsAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLL_Error: ", anError.getMessage());
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(FriendsActivity.this));

        rv_other_frnd = findViewById(R.id.rv_other_frnd);
        rv_close_frnd = findViewById(R.id.rv_close_frnd);

        activity = FriendsActivity.this;
        AndroidNetworking.initialize(FriendsActivity.this);
        token = sharedpreference.getUserToken(FriendsActivity.this);
        getFrendsData();
        getOtherFrendsData();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FriendsActivity.this, SOSActivity.class);
        startActivity(intent);
        finish();
    }
}
