package com.TBI.Client.Bluff.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Adapter.Map.CloseFriendAdapter;
import com.TBI.Client.Bluff.Adapter.Map.OtherFriendsAdapter;
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

public class Friend_Fragment extends Fragment {

    public static RecyclerView rv_close_frnd;
    public static RecyclerView rv_other_frnd;
    public static TextView tv_not_found1;
    public static TextView tv_not_found;
    public static ArrayList<GetCloseFrend> getCloseFrends = new ArrayList<>();
    public static ArrayList<GetOtherFriends> getOtherFriends = new ArrayList<>();
    public static FragmentActivity fragmentActivity;
    private static CloseFriendAdapter closeFriendAdapter;
    private static OtherFriendsAdapter otherFriendsAdapter;

    public static void getFrendsData() {
        getCloseFrends.clear();
        AndroidNetworking.post(geturl.BASE_URL + "manage_closed_friends")
                .addHeaders("Authorization", sharedpreference.getUserToken(fragmentActivity))
                .addBodyParameter("user_id", sharedpreference.getUserId(fragmentActivity))
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

                            closeFriendAdapter = new CloseFriendAdapter(getCloseFrends, fragmentActivity, otherFriendsAdapter, "fragment");
                            rv_close_frnd.setLayoutManager(new GridLayoutManager(fragmentActivity,3));
                            rv_close_frnd.setAdapter(closeFriendAdapter);

                            if (!getCloseFrends.isEmpty()) {
                                if (rv_close_frnd.getVisibility() == View.GONE) {
                                    tv_not_found.setVisibility(View.GONE);
                                    rv_close_frnd.setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (rv_close_frnd.getVisibility() == View.VISIBLE) {
                                    rv_close_frnd.setVisibility(View.GONE);
                                    tv_not_found.setVisibility(View.VISIBLE);
                                }
                            }

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
                .addHeaders("Authorization", sharedpreference.getUserToken(fragmentActivity))
                .addBodyParameter("user_id", sharedpreference.getUserId(fragmentActivity))
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

                            if (!Public_Function.getOtherFriends.isEmpty()) {
                                Public_Function.getOtherFriends.clear();
                                Public_Function.getOtherFriends.addAll(getOtherFriends);
                            } else {
                                Public_Function.getOtherFriends.addAll(getOtherFriends);
                            }

                            otherFriendsAdapter = new OtherFriendsAdapter(getOtherFriends, getCloseFrends, fragmentActivity, closeFriendAdapter, "fragment");
                            rv_other_frnd.setLayoutManager(new GridLayoutManager(fragmentActivity, 3));
                            rv_other_frnd.setAdapter(otherFriendsAdapter);

                            if (!getOtherFriends.isEmpty()) {
                                if (rv_other_frnd.getVisibility() == View.GONE) {
                                    tv_not_found1.setVisibility(View.GONE);
                                    rv_other_frnd.setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (rv_other_frnd.getVisibility() == View.VISIBLE) {
                                    rv_other_frnd.setVisibility(View.GONE);
                                    tv_not_found1.setVisibility(View.VISIBLE);
                                }
                            }

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        ButterKnife.bind(this, view);

        rv_other_frnd = view.findViewById(R.id.rv_other_frnd);
        rv_close_frnd = view.findViewById(R.id.rv_close_frnd);
        tv_not_found = view.findViewById(R.id.tv_not_found);
        tv_not_found1 = view.findViewById(R.id.tv_not_found1);

        fragmentActivity = getActivity();
        AndroidNetworking.initialize(getContext());
        getFrendsData();
        getOtherFrendsData();

        return view;
    }

}
