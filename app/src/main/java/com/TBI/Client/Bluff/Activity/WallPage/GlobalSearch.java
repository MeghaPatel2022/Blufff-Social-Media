package com.TBI.Client.Bluff.Activity.WallPage;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Adapter.Post.PeopleList_Adapter;
import com.TBI.Client.Bluff.Adapter.Post.UserFeed_Adapter;
import com.TBI.Client.Bluff.Adapter.Profile.FriendList_Adapter;
import com.TBI.Client.Bluff.Adapter.WallPage.Searchhashtag_Adapter;
import com.TBI.Client.Bluff.Adapter.WallPage.Searchlocation_Adapter;
import com.TBI.Client.Bluff.Model.Dosearch.Hastag;
import com.TBI.Client.Bluff.Model.Dosearch.Location;
import com.TBI.Client.Bluff.Model.GetProfile.Friend;
import com.TBI.Client.Bluff.Model.GetProfile.Post;
import com.TBI.Client.Bluff.Model.GlobalSearch.GetGlobalSearch;
import com.TBI.Client.Bluff.Model.GlobalSearch.Persons;
import com.TBI.Client.Bluff.Model.NearBy.GetNearby;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.KeyboardUtils;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class GlobalSearch extends AppCompatActivity {

    @BindView(R.id.searchdiscover)
    SearchView searchdiscover;
    @BindView(R.id.tabsuggest)
    TabLayout tabsuggest;
    @BindView(R.id.gridpost)
    RecyclerView gridpost;
    @BindView(R.id.txtempty)
    TextView txtempty;
    @BindView(R.id.listsearch)
    ListView listsearch;
    @BindView(R.id.listpeople)
    ListView listpeople;
    @BindView(R.id.lnnearby)
    LinearLayout lnnearby;
    @BindView(R.id.listnearby)
    ListView listnearby;
    @BindView(R.id.listhashtag)
    ListView listhashtag;
    @BindView(R.id.listlocation)
    ListView listlocation;
    @BindView(R.id.img_nearBy)
    ImageView img_nearBy;

    ConnectionDetector cd;
    boolean isInternetPresent = false;

    String type = "Friends";

    List<Friend> friendarray = new ArrayList<>();
    List<Persons> peoplearray = new ArrayList<Persons>();
    List<Post> postarray = new ArrayList<Post>();
    List<Hastag> hashtagarray = new ArrayList<Hastag>();
    List<Location> locationarray = new ArrayList<Location>();


    FriendList_Adapter friendList_adapter;
    PeopleList_Adapter peopleList_adapter, nearby_Adapter;
    Searchhashtag_Adapter searchhashtag_adapter;
    Searchlocation_Adapter searchlocation_adapter;

    UserFeed_Adapter userFeed_adapter;
    int random;
    boolean isLoadData = true;
    boolean aBoolean = false;
    List<Persons> nearbyarray = new ArrayList<Persons>();
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (sharedpreference.getTheme(GlobalSearch.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_globalsearch);
        ButterKnife.bind(this);

        if (sharedpreference.getTheme(GlobalSearch.this).equalsIgnoreCase("white")) {
            img_nearBy.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        } else {
            img_nearBy.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        }


        EditText searchEditText = searchdiscover.findViewById(androidx.appcompat.R.id.search_src_text);
        Typeface myFont = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            myFont = getResources().getFont(R.font.poppins_regular);
            searchEditText.setTypeface(myFont);
        }
        searchEditText.setTextSize(14);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.darkgrey));

        img_nearBy.setOnClickListener(v -> {
            Intent intent = new Intent(GlobalSearch.this, NearByUserActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
        });

        cd = new ConnectionDetector(GlobalSearch.this);
        isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent) {
            Toasty.warning(GlobalSearch.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
        } else {
            //GetSavedPost();
        }


        searchdiscover.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.equals("")) {
                    tabsuggest.setVisibility(View.GONE);
                    listsearch.setVisibility(View.GONE);
                    listpeople.setVisibility(View.GONE);
                    gridpost.setVisibility(View.GONE);
                    txtempty.setVisibility(View.GONE);
                    if (!nearbyarray.isEmpty()) {
                        lnnearby.setVisibility(View.VISIBLE);
                    }
                } else {
                    lnnearby.setVisibility(View.GONE);
                    tabsuggest.setVisibility(View.VISIBLE);
                    cd = new ConnectionDetector(GlobalSearch.this);
                    isInternetPresent = cd.isConnectingToInternet();

                    if (!isInternetPresent) {
                        Toasty.warning(GlobalSearch.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                    } else {
//                        Public_Function.Show_Progressdialog(GlobalSearch.this);
                        Dosearch(newText);
                    }
                }
                return false;
            }
        });

        tabsuggest.addTab(tabsuggest.newTab().setText("Friends"));
        tabsuggest.addTab(tabsuggest.newTab().setText("People"));
//        tabsuggest.addTab(tabsuggest.newTab().setText("Post"));
        tabsuggest.addTab(tabsuggest.newTab().setText("Tags"));
        tabsuggest.addTab(tabsuggest.newTab().setText("Location"));

        GetNearby();

        userFeed_adapter = new UserFeed_Adapter(GlobalSearch.this, postarray, "globalsearch");
        userFeed_adapter.randome(random);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(GlobalSearch.this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {


                if (random == 3) {
                    if (position % 3 == 0) {
                        return 2;
                    } else {
                        return 1;
                    }
                } else {
                    switch (position % 5) {
                        // first two items span 3 columns each
                        case 0:
                        case 1:
                            return 1;
                        // next 3 items span 2 columns each
                        case 2:
                        case 3:
                        case 4:
                            return 2;
                    }
                }

                throw new IllegalStateException("internal error");
            }
        });

        gridpost.setLayoutManager(gridLayoutManager);
        gridpost.setAdapter(userFeed_adapter);


        tabsuggest.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getText().toString().equalsIgnoreCase("Friends")) {
                    if (!type.equalsIgnoreCase("Friends")) {
                        type = "Friends";
                        listsearch.setVisibility(View.VISIBLE);
                        txtempty.setText("No Friend found");
                        listsearch.setEmptyView(txtempty);
                        gridpost.setVisibility(View.GONE);
                        listpeople.setVisibility(View.GONE);
                        listlocation.setVisibility(View.GONE);
                        listhashtag.setVisibility(View.GONE);
                    }
                } else if (tab.getText().toString().equalsIgnoreCase("People")) {
                    if (!type.equalsIgnoreCase("People")) {
                        type = "People";
                        listpeople.setVisibility(View.VISIBLE);
                        txtempty.setText("No Account found");
                        listpeople.setEmptyView(txtempty);
                        listsearch.setVisibility(View.GONE);
                        gridpost.setVisibility(View.GONE);
                        listlocation.setVisibility(View.GONE);
                        listhashtag.setVisibility(View.GONE);

                    }
                } else if (tab.getText().toString().equalsIgnoreCase("Post")) {
                    if (!type.equalsIgnoreCase("Post")) {
                        type = "Post";
                        if (postarray.isEmpty()) {
                            txtempty.setVisibility(View.VISIBLE);
                        } else {
                            txtempty.setVisibility(View.GONE);
                            gridpost.setVisibility(View.VISIBLE);
                        }
                        // gridpost.setVisibility(View.VISIBLE);
                        txtempty.setText("No Post found");
                        listsearch.setVisibility(View.GONE);
                        //gridpost.setEmptyView(txtempty);
                        listpeople.setVisibility(View.GONE);
                        listlocation.setVisibility(View.GONE);
                        listhashtag.setVisibility(View.GONE);
                    }
                } else if (tab.getText().toString().equalsIgnoreCase("Tags")) {
                    if (!type.equalsIgnoreCase("Tags")) {
                        txtempty.setText("No Tags found");
                        type = "Tags";
                        listsearch.setVisibility(View.GONE);
                        listpeople.setVisibility(View.GONE);
                        listlocation.setVisibility(View.GONE);
                        gridpost.setVisibility(View.GONE);
                        listhashtag.setVisibility(View.VISIBLE);

                        listhashtag.setEmptyView(txtempty);
                        searchhashtag_adapter = new Searchhashtag_Adapter(GlobalSearch.this, hashtagarray);
                        listhashtag.setAdapter(searchhashtag_adapter);
                    }
                } else if (tab.getText().toString().equalsIgnoreCase("Location")) {
                    if (!type.equalsIgnoreCase("Places")) {
                        txtempty.setText("No Location found");
                        type = "Places";

                        listlocation.setEmptyView(txtempty);
                        listsearch.setVisibility(View.GONE);
                        listpeople.setVisibility(View.GONE);
                        listlocation.setVisibility(View.VISIBLE);
                        gridpost.setVisibility(View.GONE);
                        listhashtag.setVisibility(View.GONE);

                        searchlocation_adapter = new Searchlocation_Adapter(GlobalSearch.this, locationarray);
                        listlocation.setAdapter(searchlocation_adapter);
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


        /*gridpost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ActivityOptions options = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(GlobalSearch.this, view, getString(RM.string.picture_transition_name));
                    Intent i1 = new Intent(GlobalSearch.this, PostDetailPage1.class);
                    //i1.putExtra("post_id", postarray.get(i).getId() + "");
                    i1.putExtra("post_id", postarray.get(i).getId() + "");
                    i1.putExtra("comment", "no");
                    startActivity(i1, options.toBundle());
                }


            }
        });*/

        KeyboardUtils.addKeyboardToggleListener(this, isVisible -> {

            if (isVisible) {
                aBoolean = true;
            } else {
                aBoolean = false;
                if (searchdiscover.getQuery().toString().equalsIgnoreCase("")) {
                    if (!nearbyarray.isEmpty()) {
                        lnnearby.setVisibility(View.VISIBLE);
                    }
                } else {
                    lnnearby.setVisibility(View.GONE);

                }
            }
            Log.d("keyboard", "keyboard visible: " + isVisible);
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (aBoolean) {
            Public_Function.hideKeyboard(GlobalSearch.this);
        }
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (aBoolean) {
            Public_Function.hideKeyboard(GlobalSearch.this);
        }
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
        super.onBackPressed();
    }

    public void Dosearch(String newText) {


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("keyword", newText + ""));
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(GlobalSearch.this)));
        paramArrayList.add(new param("timezone", Public_Function.gettimezone()));

        new geturl().getdata(GlobalSearch.this, data -> {

            friendarray.clear();
            friendarray.clear();
            postarray.clear();

            try {
//                Public_Function.Hide_ProgressDialogue();

                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("message");

                if (message) {
                    GetGlobalSearch login = new Gson().fromJson(data, GetGlobalSearch.class);
                    friendarray = login.getFriends();
                    peoplearray = login.getPeoples();
                    postarray = login.getPosts();
                    hashtagarray = login.getHastags();
                    locationarray = login.getLocation();

                } else {
                    Toasty.error(GlobalSearch.this, status + "", Toast.LENGTH_LONG, true).show();
                }
            } catch (Exception e) {
                Toasty.error(GlobalSearch.this, e.getMessage() + "", Toast.LENGTH_LONG, true).show();
//                Toasty.error(GlobalSearch.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                e.printStackTrace();
            }

            friendList_adapter = new FriendList_Adapter(GlobalSearch.this, friendarray);
            listsearch.setAdapter(friendList_adapter);

            peopleList_adapter = new PeopleList_Adapter(GlobalSearch.this, peoplearray, "Yes");
            listpeople.setAdapter(peopleList_adapter);

            userFeed_adapter.removeAll();
            userFeed_adapter.addAll(postarray);

            txtempty.setVisibility(View.GONE);

            if (type.equalsIgnoreCase("Friends")) {
                txtempty.setText("No Friend found");
                listsearch.setVisibility(View.VISIBLE);
                listsearch.setEmptyView(txtempty);
                gridpost.setVisibility(View.GONE);
            } else if (type.equalsIgnoreCase("Post")) {
                txtempty.setText("No Post found");
                listsearch.setVisibility(View.GONE);
                listpeople.setVisibility(View.GONE);

                if (postarray.isEmpty()) {
                    txtempty.setVisibility(View.VISIBLE);
                } else {
                    gridpost.setVisibility(View.VISIBLE);
                }

            } else if (type.equalsIgnoreCase("People")) {
                txtempty.setText("No People found");
                listpeople.setVisibility(View.VISIBLE);
                listpeople.setEmptyView(txtempty);
                listsearch.setVisibility(View.GONE);
                gridpost.setVisibility(View.GONE);


            } else if (type.equalsIgnoreCase("Tags")) {

                listsearch.setVisibility(View.GONE);
                listpeople.setVisibility(View.GONE);
                listlocation.setVisibility(View.GONE);
                gridpost.setVisibility(View.GONE);
                listhashtag.setVisibility(View.VISIBLE);
                listhashtag.setEmptyView(txtempty);
                searchhashtag_adapter = new Searchhashtag_Adapter(GlobalSearch.this, hashtagarray);
                listhashtag.setAdapter(searchhashtag_adapter);
            } else if (type.equalsIgnoreCase("Places")) {

                listsearch.setVisibility(View.GONE);
                listpeople.setVisibility(View.GONE);
                listlocation.setVisibility(View.VISIBLE);
                gridpost.setVisibility(View.GONE);
                listhashtag.setVisibility(View.GONE);
                listlocation.setEmptyView(txtempty);
                searchlocation_adapter = new Searchlocation_Adapter(GlobalSearch.this, locationarray);
                listlocation.setAdapter(searchlocation_adapter);
            }

            //
        }, paramArrayList, "global_search");
    }

    public void GetNearby() {

        nearbyarray = new ArrayList<>();
        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(GlobalSearch.this)));

        new geturl().getdata(GlobalSearch.this, data -> {

            try {
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                String status = "";
                status = object.optString("message");

                if (message) {
                    GetNearby login = new Gson().fromJson(data, GetNearby.class);
                    nearbyarray = login.getData();

                    nearby_Adapter = new PeopleList_Adapter(GlobalSearch.this, nearbyarray, "No");
                    listnearby.setAdapter(nearby_Adapter);


                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (nearbyarray.isEmpty()) {
                lnnearby.setVisibility(View.GONE);
            } else {
                lnnearby.setVisibility(View.VISIBLE);
            }
        }, paramArrayList, "get_nearby_user");
    }

}
