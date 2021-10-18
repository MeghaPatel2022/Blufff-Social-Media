package com.TBI.Client.Bluff.Activity.Profile;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.TBI.Client.Bluff.Adapter.Profile.FollowAdapter;
import com.TBI.Client.Bluff.Adapter.Profile.FriendsFollowers.AllUserAdapter;
import com.TBI.Client.Bluff.Adapter.Profile.FriendsFollowers.FollowingAAdapter;
import com.TBI.Client.Bluff.Adapter.Profile.RecentlyAdapter;
import com.TBI.Client.Bluff.Model.A2Z.MainDetum.AllUsers;
import com.TBI.Client.Bluff.Model.A2Z.MainDetum.Following;
import com.TBI.Client.Bluff.Model.A2Z.MainDetum.Friends;
import com.TBI.Client.Bluff.Model.A2Z.MainDetum.RecentUsers;
import com.TBI.Client.Bluff.Model.GetProfile.GetProfile;
import com.TBI.Client.Bluff.Model.View_Connection.Request;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class FriendList extends AppCompatActivity {

    @BindView(R.id.lstfollowing)
    RecyclerView lstfollowing;

    @BindView(R.id.tabfreind)
    TabLayout tabfreind;
    @BindView(R.id.searchrequested)
    SearchView searchrequested;
    @BindView(R.id.search_following)
    SearchView search_following;
    @BindView(R.id.search_follower)
    SearchView search_follower;
    @BindView(R.id.search_all)
    SearchView search_all;
    @BindView(R.id.img_back)
    ImageView img_back;

    @BindView(R.id.txtempty)
    TextView txtempty;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeToRefresh;


    String type = "", otherid = "", othrname = "";
    ConnectionDetector cd;
    boolean isInternetPresent = false;
    int accountprivacy;

    private FollowAdapter followAdapter;
    private FollowingAAdapter followingAAdapter;
    private AllUserAdapter allUserAdapter;
    private RecentlyAdapter recentlyAdapter;

    List<Friends> friendsList = new ArrayList<>();
    List<Following> followingArrayList = new ArrayList<>();
    List<RecentUsers> recentUsersList = new ArrayList<>();
    List<AllUsers> allUsersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (sharedpreference.getTheme(FriendList.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);
        AndroidNetworking.initialize(FriendList.this);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lstfollowing.setNestedScrollingEnabled(false);

        type = Objects.requireNonNull(getIntent().getExtras()).getString("type");
        otherid = getIntent().getExtras().getString("otherid");
        othrname = getIntent().getExtras().getString("othername");
        accountprivacy = getIntent().getExtras().getInt("accountprivacy");

        if (Public_Function.isInternetConnected(FriendList.this)){
            Public_Function.Show_Progressdialog(FriendList.this);
            getData();
        } else {
            Toasty.warning(FriendList.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
        }

//        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(FriendList.this));

        EditText searchEditText1 = search_follower.findViewById(androidx.appcompat.R.id.search_src_text);
        EditText searchEditText2 = search_following.findViewById(androidx.appcompat.R.id.search_src_text);
        EditText searchEditText3 = searchrequested.findViewById(androidx.appcompat.R.id.search_src_text);

        Typeface myFont = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            myFont = getResources().getFont(R.font.poppins_semibold);
            searchEditText1.setTypeface(myFont);
            searchEditText2.setTypeface(myFont);
            searchEditText3.setTypeface(myFont);

            //searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(RM.dimen.fab_margin));
        }
        searchEditText1.setTextSize(14);
        searchEditText1.setTextColor(getResources().getColor(R.color.blacklight));
        searchEditText1.setHintTextColor(getResources().getColor(R.color.blacklight));

        searchEditText2.setTextSize(14);
        searchEditText2.setTextColor(getResources().getColor(R.color.blacklight));
        searchEditText2.setHintTextColor(getResources().getColor(R.color.blacklight));

        searchEditText3.setTextSize(14);
        searchEditText3.setTextColor(getResources().getColor(R.color.blacklight));
        searchEditText3.setHintTextColor(getResources().getColor(R.color.blacklight));

        tabfreind.addTab(tabfreind.newTab().setText("All"));
        tabfreind.addTab(tabfreind.newTab().setText("Followers"));
        tabfreind.addTab(tabfreind.newTab().setText("Following"));
        tabfreind.addTab(tabfreind.newTab().setText("Recently"));


        for (int i = 0; i < tabfreind.getTabCount(); i++) {
            TabLayout.Tab tab = tabfreind.getTabAt(i);

            TextView tv = (TextView) (((LinearLayout) ((LinearLayout) tabfreind.getChildAt(0)).getChildAt(i)).getChildAt(1));
            tv.setTextAppearance(FriendList.this, R.style.MyTextViewStyle);
            tv.setAllCaps(false);

            if (i == 0) {
                tv.setTextSize(20);
            } else {
                tv.setTextSize(18);
            }
            tab.setTag(tv);
        }

        tabfreind.setTabIndicatorFullWidth(false);

        if (type.equalsIgnoreCase("all")){
            tabfreind.getTabAt(0).select();
            search_all.setVisibility(View.VISIBLE);
            search_follower.setVisibility(View.GONE);
            search_following.setVisibility(View.GONE);
            searchrequested.setVisibility(View.GONE);
        } else if (type.equalsIgnoreCase("follower")) {
            tabfreind.getTabAt(1).select();
            search_follower.setVisibility(View.VISIBLE);
            search_all.setVisibility(View.GONE);
            search_following.setVisibility(View.GONE);
            searchrequested.setVisibility(View.GONE);

        } else if (type.equalsIgnoreCase("following")) {
            tabfreind.getTabAt(2).select();
            search_follower.setVisibility(View.GONE);
            search_following.setVisibility(View.VISIBLE);
            search_all.setVisibility(View.VISIBLE);
            searchrequested.setVisibility(View.GONE);
        }

        search_all.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                cd = new ConnectionDetector(FriendList.this);
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    Toasty.warning(FriendList.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    if (newText.equalsIgnoreCase("")) {
                        if (Public_Function.isInternetConnected(FriendList.this)) {
                            Public_Function.Show_Progressdialog(FriendList.this);
                            getData();
                        } else {
                            Toast.makeText(FriendList.this, "Please check your nternet connection...", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        SearchUser(newText, "all");
                    }
                }

                return true;
            }
        });

        search_following.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                cd = new ConnectionDetector(FriendList.this);
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    Toasty.warning(FriendList.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    if (newText.equalsIgnoreCase("")) {
                        if (Public_Function.isInternetConnected(FriendList.this)) {
                            Public_Function.Show_Progressdialog(FriendList.this);
                            getData();
                        } else {
                            Toast.makeText(FriendList.this, "Please check your nternet connection...", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        SearchUser(newText, "following");
                    }
                }

                return true;
            }
        });

        search_follower.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                cd = new ConnectionDetector(FriendList.this);
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    Toasty.warning(FriendList.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    if (newText.equalsIgnoreCase("")) {
                        if (Public_Function.isInternetConnected(FriendList.this)) {
                            Public_Function.Show_Progressdialog(FriendList.this);
                            getData();
                        } else {
                            Toast.makeText(FriendList.this, "Please check your nternet connection...", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        SearchUser(newText, "followers");
                    }
                }

                return true;
            }
        });

        searchrequested.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                cd = new ConnectionDetector(FriendList.this);
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    Toasty.warning(FriendList.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    if (newText.equalsIgnoreCase("")) {
                        if (Public_Function.isInternetConnected(FriendList.this)) {
                            Public_Function.Show_Progressdialog(FriendList.this);
                            getData();
                        } else {
                            Toast.makeText(FriendList.this, "Please check your nternet connection...", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        SearchUser(newText, "recent");
                    }
                }

                return true;
            }
        });

        tabfreind.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {

                    if (!type.equalsIgnoreCase("all")) {
                        type = "all";
                        if (Public_Function.isInternetConnected(FriendList.this)) {
                            Public_Function.Show_Progressdialog(FriendList.this);
                            getData();
                        } else {
                            Toast.makeText(FriendList.this, "Please check your nternet connection...", Toast.LENGTH_SHORT).show();
                        }
                        search_all.setVisibility(View.VISIBLE);
                        search_follower.setVisibility(View.GONE);
                        search_following.setVisibility(View.GONE);
                        searchrequested.setVisibility(View.GONE);
                    }

                }else if (tab.getPosition() == 1) {

                    if (!type.equalsIgnoreCase("follower")) {
                        type = "follower";
                        if (Public_Function.isInternetConnected(FriendList.this)) {
                            Public_Function.Show_Progressdialog(FriendList.this);
                            getData();
                        } else {
                            Toast.makeText(FriendList.this, "Please check your nternet connection...", Toast.LENGTH_SHORT).show();
                        }
                        search_follower.setVisibility(View.VISIBLE);
                        search_following.setVisibility(View.GONE);
                        searchrequested.setVisibility(View.GONE);
                        search_all.setVisibility(View.GONE);
                    }

                } else if (tab.getPosition() == 2) {

                    if (!type.equalsIgnoreCase("following")) {
                        type = "following";
                        if (Public_Function.isInternetConnected(FriendList.this)) {
                            Public_Function.Show_Progressdialog(FriendList.this);
                            getData();
                        } else {
                            Toast.makeText(FriendList.this, "Please check your nternet connection...", Toast.LENGTH_SHORT).show();
                        }
                        search_follower.setVisibility(View.GONE);
                        search_following.setVisibility(View.VISIBLE);
                        search_all.setVisibility(View.GONE);
                        searchrequested.setVisibility(View.GONE);
                    }

                } else if (tab.getPosition()==3){

                    if (!type.equalsIgnoreCase("recent")) {
                        type = "recent";
                        if (Public_Function.isInternetConnected(FriendList.this)) {
                            Public_Function.Show_Progressdialog(FriendList.this);
                            getData();
                        } else {
                            Toast.makeText(FriendList.this, "Please check your nternet connection...", Toast.LENGTH_SHORT).show();
                        }
                        search_follower.setVisibility(View.GONE);
                        search_following.setVisibility(View.GONE);
                        search_all.setVisibility(View.GONE);
                        searchrequested.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        cd = new ConnectionDetector(FriendList.this);
        isInternetPresent = cd.isConnectingToInternet();


        swipeToRefresh.setOnRefreshListener(() -> {
            if (type.equalsIgnoreCase("all")) {

                if (search_all.getQuery().toString().equalsIgnoreCase("")) {

                    cd = new ConnectionDetector(FriendList.this);
                    isInternetPresent = cd.isConnectingToInternet();

                    if (!isInternetPresent) {
                        Toasty.warning(FriendList.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                    } else {
                        Public_Function.Show_Progressdialog(FriendList.this);
                        getData();
                    }

                } else {

                    cd = new ConnectionDetector(FriendList.this);
                    isInternetPresent = cd.isConnectingToInternet();

                    if (!isInternetPresent) {
                        Toasty.warning(FriendList.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                    } else {
                        SearchUser(search_all.getQuery().toString(), "followers");
                    }

                }
            } else if (type.equalsIgnoreCase("follower")) {

                if (search_follower.getQuery().toString().equalsIgnoreCase("")) {

                    cd = new ConnectionDetector(FriendList.this);
                    isInternetPresent = cd.isConnectingToInternet();

                    if (!isInternetPresent) {
                        Toasty.warning(FriendList.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                    } else {
                        Public_Function.Show_Progressdialog(FriendList.this);
                        getData();
                    }

                } else {

                    cd = new ConnectionDetector(FriendList.this);
                    isInternetPresent = cd.isConnectingToInternet();

                    if (!isInternetPresent) {
                        Toasty.warning(FriendList.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                    } else {
                        SearchUser(search_follower.getQuery().toString(), "followers");
                    }

                }
            } else if (type.equalsIgnoreCase("following")) {

                if (search_following.getQuery().toString().equalsIgnoreCase("")) {

                    cd = new ConnectionDetector(FriendList.this);
                    isInternetPresent = cd.isConnectingToInternet();

                    if (!isInternetPresent) {
                        Toasty.warning(FriendList.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                    } else {
                        Public_Function.Show_Progressdialog(FriendList.this);
                        getData();
                    }

                } else {
                    cd = new ConnectionDetector(FriendList.this);
                    isInternetPresent = cd.isConnectingToInternet();

                    if (!isInternetPresent) {
                        Toasty.warning(FriendList.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                    } else {
                        SearchUser(search_following.getQuery().toString(), "following");
                    }

                }
            } else if (type.equalsIgnoreCase("recent")) {

                if (searchrequested.getQuery().toString().equalsIgnoreCase("")) {

                    cd = new ConnectionDetector(FriendList.this);
                    isInternetPresent = cd.isConnectingToInternet();

                    if (!isInternetPresent) {
                        Toasty.warning(FriendList.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                    } else {
                        Public_Function.Show_Progressdialog(FriendList.this);
                        getData();
                    }

                } else {
                    cd = new ConnectionDetector(FriendList.this);
                    isInternetPresent = cd.isConnectingToInternet();

                    if (!isInternetPresent) {
                        Toasty.warning(FriendList.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                    } else {
                        SearchUser(searchrequested.getQuery().toString(), "following");
                    }

                }
            } else {
                if (searchrequested.getQuery().toString().equalsIgnoreCase("")) {

                    cd = new ConnectionDetector(FriendList.this);
                    isInternetPresent = cd.isConnectingToInternet();

                    if (!isInternetPresent) {
                        Toasty.warning(FriendList.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                    } else {
                        Public_Function.Show_Progressdialog(FriendList.this);
                        getData();
                    }
                } else {
                    cd = new ConnectionDetector(FriendList.this);
                    isInternetPresent = cd.isConnectingToInternet();

                    if (!isInternetPresent) {
                        Toasty.warning(FriendList.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                    } else {
                        SearchUser(searchrequested.getQuery().toString(), "requested");
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_out);
        return super.onOptionsItemSelected(item);
    }

    public void addfollower(Request friend) {
        if (otherid.equalsIgnoreCase("")) {
        }
    }


    public void add_following(Request friend) {
        if (otherid.equalsIgnoreCase("")) {
        }
    }

    public void SearchUser(String newText, String following) {

//
//        ArrayList<param> paramArrayList = new ArrayList<>();
//        paramArrayList.add(new param("user_id", sharedpreference.getUserId(FriendList.this)));
//        paramArrayList.add(new param("filter_by", following));
//        paramArrayList.add(new param("keyword", newText));
//        paramArrayList.add(new param("other_user_id", otherid));
//
//        new geturl().getdata(FriendList.this, new MyAsyncTaskCallback() {
//            @Override
//            public void onAsyncTaskComplete(String data) {
//                swipeToRefresh.setRefreshing(false);
//                followingarray = new ArrayList<>();
//                try {
//                    JSONObject object = new JSONObject(data);
//                    boolean message = object.optBoolean("success");
//                    String status = "";
//                    status = object.optString("message");
//
//                    if (message) {
//                        SearchUser login = new Gson().fromJson(data, SearchUser.class);
//
//                        if (type.equalsIgnoreCase("follower")) {
//                            followerlist = new ArrayList<>();
//                            followerlist = login.getFollowers();
//                            follower_adapter.addAll(followerlist);
////                            lstfollowing.setAdapter(follower_adapter);
//                            txtempty.setText("No Followers found");
//                        } else if (type.equalsIgnoreCase("following")) {
//                            followingarray = new ArrayList<>();
////                            followingarray = login.getFollowing();
////                            following_adapter.addAll(followingarray);
////                            lstfollowing.setAdapter(following_adapter);
//                            txtempty.setText("No Following found");
//                        } else {
//                            requestarray = new ArrayList<>();
//                            requestarray = login.getRequested();
////                            lstfollowing.setAdapter(requested_adapter);
//                            requested_adapter.addAll(requestarray);
//                            txtempty.setText("No Request found");
//                        }
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
////                lstfollowing.setEmptyView(txtempty);
//            }
//        }, paramArrayList, "user_listing");
    }

    public void getData(){
        if (!otherid.equals("")){
            AndroidNetworking.post(geturl.BASE_URL + "get_profile")
                    .addHeaders("Authorization", sharedpreference.getUserToken(FriendList.this))
                    .addBodyParameter("user_id", otherid)
                    .addBodyParameter("timezone", Public_Function.gettimezone())
                    .setPriority(Priority.IMMEDIATE)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("success")) {

                                    GetProfile login = new Gson().fromJson(response.toString(), GetProfile.class);
                                    Public_Function.Hide_ProgressDialogue();
                                    Friends friends = login.getFriends();
                                    AllUsers allUsers = login.getAllUsers();
                                    Following following = login.getFollowing();
                                    RecentUsers recentUsers = login.getRecentUsers();

                                    friendsList.clear();
                                    allUsersList.clear();
                                    followingArrayList.clear();
                                    recentUsersList.clear();

                                    friendsList.add(friends);
                                    allUsersList.add(allUsers);
                                    followingArrayList.add(following);
                                    recentUsersList.add(recentUsers);

                                    lstfollowing.setLayoutManager(new LinearLayoutManager(FriendList.this, RecyclerView.VERTICAL, false));
                                    followAdapter = new FollowAdapter(friendsList, FriendList.this);
                                    followingAAdapter = new FollowingAAdapter(followingArrayList, FriendList.this);
                                    allUserAdapter = new AllUserAdapter(allUsersList, FriendList.this);
                                    recentlyAdapter = new RecentlyAdapter(recentUsersList, FriendList.this);

                                    if (type.equalsIgnoreCase("follower")) {
                                        lstfollowing.setAdapter(followAdapter);
                                        Log.e("LLLLLLL_FriendList1: ", String.valueOf(followingArrayList.size()));
                                        if (friendsList.isEmpty())
                                            txtempty.setText("No Followers yet");
                                    }
                                    if (type.equalsIgnoreCase("following")) {
                                        lstfollowing.setAdapter(followingAAdapter);
                                        Log.e("LLLLLLL_FriendList: ", String.valueOf(followingArrayList.size()));
                                        if (followingArrayList.isEmpty())
                                            txtempty.setText("No Following yet");
                                    }
                                    if (type.equalsIgnoreCase("all")) {
                                        lstfollowing.setAdapter(allUserAdapter);
                                        Log.e("LLLLLLL_FriendList: ", String.valueOf(allUsersList.size()));
                                        if (allUsersList.isEmpty())
                                            txtempty.setText("No Users yet");
                                    }
                                    if (type.equalsIgnoreCase("recent")) {
                                        lstfollowing.setAdapter(recentlyAdapter);
                                        Log.e("LLLLLLL_FriendList: ", String.valueOf(allUsersList.size()));
                                        if (recentUsersList.isEmpty()) {
                                            txtempty.setVisibility(View.VISIBLE);
                                            txtempty.setText("No Recent users yet");
                                        }
                                    }


//                                else {
//                                    txtempty.setText("No Request yet");
//                                    lstfollowing.setLayoutManager(new LinearLayoutManager(FriendList.this,RecyclerView.VERTICAL,false));
//                                    followAdapter = new FollowAdapter(friendsList,FriendList.this);
//                                    lstfollowing.setAdapter(followAdapter);
//                                }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        } else {
            AndroidNetworking.post(geturl.BASE_URL + "get_profile")
                    .addHeaders("Authorization", sharedpreference.getUserToken(FriendList.this))
                    .addBodyParameter("user_id", sharedpreference.getUserId(FriendList.this))
                    .addBodyParameter("timezone", Public_Function.gettimezone())
                    .setPriority(Priority.IMMEDIATE)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("success")) {
                                    GetProfile login = new Gson().fromJson(response.toString(), GetProfile.class);
                                    Public_Function.Hide_ProgressDialogue();
                                    Friends friends = login.getFriends();
                                    AllUsers allUsers = login.getAllUsers();
                                    Following following = login.getFollowing();
                                    RecentUsers recentUsers = login.getRecentUsers();

                                    Public_Function.Hide_ProgressDialogue();

                                    friendsList.clear();
                                    allUsersList.clear();
                                    followingArrayList.clear();
                                    recentUsersList.clear();

                                    friendsList.add(friends);
                                    allUsersList.add(allUsers);
                                    followingArrayList.add(following);
                                    recentUsersList.add(recentUsers);

                                    lstfollowing.setLayoutManager(new LinearLayoutManager(FriendList.this, RecyclerView.VERTICAL, false));
                                    followAdapter = new FollowAdapter(friendsList, FriendList.this);
                                    followingAAdapter = new FollowingAAdapter(followingArrayList, FriendList.this);
                                    allUserAdapter = new AllUserAdapter(allUsersList, FriendList.this);
                                    recentlyAdapter = new RecentlyAdapter(recentUsersList, FriendList.this);

                                    if (type.equalsIgnoreCase("follower")) {
                                        lstfollowing.setAdapter(followAdapter);
                                        Log.e("LLLLLLL_FriendList1: ", String.valueOf(followingArrayList.size()));
                                        if (friendsList.isEmpty())
                                            txtempty.setText("No Followers yet");
                                        else
                                            lstfollowing.setAdapter(followAdapter);
                                    }
                                    if (type.equalsIgnoreCase("following")) {
                                        lstfollowing.setAdapter(followingAAdapter);
                                        Log.e("LLLLLLL_FriendList: ", String.valueOf(followingArrayList.size()));
                                        if (followingArrayList.isEmpty())
                                            txtempty.setText("No Following yet");
                                        else
                                            lstfollowing.setAdapter(followingAAdapter);
                                    }
                                    if (type.equalsIgnoreCase("all")) {
                                        lstfollowing.setAdapter(allUserAdapter);
                                        Log.e("LLLLLLL_FriendList: ", String.valueOf(allUsersList.size()));
                                        if (allUsersList.isEmpty())
                                            txtempty.setText("No Users yet");
                                    }
                                    if (type.equalsIgnoreCase("recent")) {
                                        Log.e("LLLLLLL_FriendList: ", String.valueOf(allUsersList.size()));
                                        if (recentUsersList.isEmpty()) {
                                            txtempty.setVisibility(View.VISIBLE);
                                            txtempty.setText("No Recent users yet");
                                        } else {
                                            lstfollowing.setAdapter(recentlyAdapter);
                                        }
                                    }
//                                else {
//                                    txtempty.setText("No Request yet");
//                                    lstfollowing.setLayoutManager(new LinearLayoutManager(FriendList.this,RecyclerView.VERTICAL,false));
//                                    followAdapter = new FollowAdapter(friendsList,FriendList.this);
//                                    lstfollowing.setAdapter(followAdapter);
//                                }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.fade_out);
    }

    //    private void checkArray(Friends friends){
//        friendsList.clear();
//        if (!friends.getA().isEmpty()){
//            friendsList.add(friends);
//        }
//        if (!friends.getB().isEmpty()){
//            friendsList.add(friends);
//        }
//    }

}
