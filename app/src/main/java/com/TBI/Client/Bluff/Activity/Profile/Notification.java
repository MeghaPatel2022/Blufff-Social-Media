package com.TBI.Client.Bluff.Activity.Profile;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Adapter.Profile.Notifications.AllNotificationAdapter;
import com.TBI.Client.Bluff.Adapter.Profile.Notifications.RequestAdapter;
import com.TBI.Client.Bluff.Adapter.Profile.Notifications.SOSAdapter;
import com.TBI.Client.Bluff.Adapter.Profile.Notifications.StarAdapter;
import com.TBI.Client.Bluff.Adapter.Profile.Notifications.SuggestedAdapter;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.Model.Get_Notification.Datum;
import com.TBI.Client.Bluff.Model.Get_Notification.GetNotification;
import com.TBI.Client.Bluff.Model.getNewNotification.AllNotification;
import com.TBI.Client.Bluff.Model.getNewNotification.DataReq;
import com.TBI.Client.Bluff.Model.getNewNotification.DataSos;
import com.TBI.Client.Bluff.Model.getNewNotification.DataStar;
import com.TBI.Client.Bluff.Model.getNewNotification.DataSuggest;
import com.TBI.Client.Bluff.Model.getNewNotification.NotificationData;
import com.TBI.Client.Bluff.Model.getNewNotification.Notify;
import com.TBI.Client.Bluff.Model.getNewNotification.Requested;
import com.TBI.Client.Bluff.Model.getNewNotification.So;
import com.TBI.Client.Bluff.Model.getNewNotification.Star;
import com.TBI.Client.Bluff.Model.getNewNotification.Suggested;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class Notification extends AppCompatActivity {

    @BindView(R.id.rl_req)
    RelativeLayout rl_req;
    @BindView(R.id.tv_no_found)
    TextView tv_no_found;
    @BindView(R.id.ll_text)
    RelativeLayout ll_text;
    @BindView(R.id.imgsearch)
    ImageView imgsearch;
    @BindView(R.id.txtempty)
    TextView txtempty;
    @BindView(R.id.tool_bar)
    Toolbar tool_bar;
    @BindView(R.id.searchnotification)
    SearchView searchnotification;
    @BindView(R.id.reltitle)
    RelativeLayout reltitle;
    @BindView(R.id.tabsuggest)
    TabLayout tabsuggest;
    @BindView(R.id.ll_all_notification)
    LinearLayout ll_all_notification;
    @BindView(R.id.rv_today_req)
    RecyclerView rv_today_req;
    @BindView(R.id.rv_today_suggest)
    RecyclerView rv_today_suggest;
    @BindView(R.id.rv_today)
    RecyclerView rv_today;
    @BindView(R.id.rv_request)
    RecyclerView rv_request;
    @BindView(R.id.rv_suggested)
    RecyclerView rv_suggested;
    @BindView(R.id.rv_star)
    RecyclerView rv_star;
    @BindView(R.id.rv_sos)
    RecyclerView rv_sos;

    List<Datum> notificatiolist = new ArrayList<Datum>();
    ConnectionDetector cd;
    boolean isInternetPresent = false;
    AllNotificationAdapter allNotificationAdapter;
    RequestAdapter requestAdapter;
    SuggestedAdapter suggestedAdapter;
    StarAdapter starAdapter;
    SOSAdapter sosAdapter;
    String type = "All";
    private List<AllNotification> allNotifications = new ArrayList<>();
    private List<Requested> requesteds = new ArrayList<>();
    private List<So> soList = new ArrayList<>();
    private List<Star> starList = new ArrayList<>();
    private List<Suggested> suggestedList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Log.d("mn13theme", sharedpreference.getTheme(Notification.this) + "11");
        if (sharedpreference.getTheme(Notification.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifiaction);

        ButterKnife.bind(this);
        AndroidNetworking.initialize(Notification.this);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(Notification.this));
        setSupportActionBar(tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (sharedpreference.getTheme(Notification.this).equalsIgnoreCase("white")) {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        } else {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }

        tool_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        EditText searchEditText = searchnotification.findViewById(androidx.appcompat.R.id.search_src_text);
        Typeface myFont = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            myFont = getResources().getFont(R.font.poppins_semibold);
            searchEditText.setTypeface(myFont);
            searchEditText.setTextSize(14);
            searchEditText.setTextColor(Notification.this.getResources().getColor(R.color.blacklight));
            searchEditText.setHintTextColor(Notification.this.getResources().getColor(R.color.darkgrey));
            //searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(RM.dimen.fab_margin));
        }

        ImageView closeButton = searchnotification.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            tool_bar.setVisibility(View.VISIBLE);
            searchnotification.setVisibility(View.GONE);
        });

        if (Public_Function.isInternetConnected(Notification.this)) {
            Public_Function.Show_Progressdialog(Notification.this);
            getAllNotification();
        }

        imgsearch.setOnClickListener(view -> {

            tool_bar.setVisibility(View.GONE);
            searchnotification.setVisibility(View.VISIBLE);

        });

        searchnotification.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        cd = new ConnectionDetector(Notification.this);
        isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent) {
            Toasty.warning(Notification.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
        } else {
            GetNotification();
        }

        rv_today.setLayoutManager(new LinearLayoutManager(Notification.this, RecyclerView.VERTICAL, false));
        rv_today_req.setLayoutManager(new LinearLayoutManager(Notification.this, RecyclerView.HORIZONTAL, false));
        rv_today_suggest.setLayoutManager(new LinearLayoutManager(Notification.this, RecyclerView.HORIZONTAL, false));
        rv_request.setLayoutManager(new GridLayoutManager(Notification.this, 3));
        rv_suggested.setLayoutManager(new GridLayoutManager(Notification.this, 3));
        rv_star.setLayoutManager(new GridLayoutManager(Notification.this, 3));
        rv_sos.setLayoutManager(new GridLayoutManager(Notification.this, 3));

        allNotificationAdapter = new AllNotificationAdapter(allNotifications, Notification.this);
        rv_today.setAdapter(allNotificationAdapter);
        requestAdapter = new RequestAdapter(Notification.this, requesteds);
        rv_today_req.setAdapter(requestAdapter);
        rv_request.setAdapter(requestAdapter);
        suggestedAdapter = new SuggestedAdapter(Notification.this, suggestedList);
        rv_today_suggest.setAdapter(suggestedAdapter);
        rv_suggested.setAdapter(suggestedAdapter);

        sosAdapter = new SOSAdapter(soList, Notification.this);
        rv_sos.setAdapter(sosAdapter);

        starAdapter = new StarAdapter(starList, Notification.this);
        rv_star.setAdapter(starAdapter);

        tabsuggest.addTab(tabsuggest.newTab().setText("All"));
        tabsuggest.addTab(tabsuggest.newTab().setText("Requests"));
        tabsuggest.addTab(tabsuggest.newTab().setText("Suggested"));
        tabsuggest.addTab(tabsuggest.newTab().setText("SOS"));
        tabsuggest.addTab(tabsuggest.newTab().setText("Star"));

        tabsuggest.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    rv_request.setVisibility(View.GONE);
                    rv_suggested.setVisibility(View.GONE);
                    rv_sos.setVisibility(View.GONE);
                    rv_star.setVisibility(View.GONE);
                    ll_all_notification.setVisibility(View.VISIBLE);
                    Public_Function.Hide_ProgressDialogue();
                    if (Public_Function.isInternetConnected(Notification.this)) {
                        Public_Function.Show_Progressdialog(Notification.this);
                        getAllNotification();
                    }
                }
                if (tab.getText().toString().equalsIgnoreCase("Requests")) {
                    ll_all_notification.setVisibility(View.GONE);
                    rv_suggested.setVisibility(View.GONE);
                    rv_sos.setVisibility(View.GONE);
                    rv_star.setVisibility(View.GONE);
                    rv_request.setVisibility(View.VISIBLE);
                    Public_Function.Show_Progressdialog(Notification.this);
                    getRequested();
                }
                if (tab.getText().toString().equalsIgnoreCase("Suggested")) {
                    ll_all_notification.setVisibility(View.GONE);
                    rv_request.setVisibility(View.GONE);
                    rv_sos.setVisibility(View.GONE);
                    rv_star.setVisibility(View.GONE);
                    rv_suggested.setVisibility(View.VISIBLE);
                    Public_Function.Show_Progressdialog(Notification.this);
                    getSuggested();
                }
                if (tab.getText().toString().equalsIgnoreCase("SOS")) {
                    ll_all_notification.setVisibility(View.GONE);
                    rv_suggested.setVisibility(View.GONE);
                    rv_star.setVisibility(View.GONE);
                    rv_request.setVisibility(View.GONE);
                    rv_sos.setVisibility(View.VISIBLE);
                    Public_Function.Show_Progressdialog(Notification.this);
                    getSOS();
                }
                if (tab.getText().toString().equalsIgnoreCase("Star")) {
                    ll_all_notification.setVisibility(View.GONE);
                    rv_suggested.setVisibility(View.GONE);
                    rv_request.setVisibility(View.GONE);
                    rv_sos.setVisibility(View.GONE);
                    rv_star.setVisibility(View.VISIBLE);
                    Public_Function.Show_Progressdialog(Notification.this);
                    getStar();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        overridePendingTransition(R.anim.stay, R.anim.fade_out);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.fade_out);
    }

    public void GetNotification() {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(Notification.this)));

        new geturl().getdata(Notification.this, data -> {

            try {
                JSONObject object = new JSONObject(data);
                boolean message = object.optBoolean("success");
                Log.e("LLL_Notification_data: ", data);
                if (message) {
                    GetNotification login = new Gson().fromJson(data, GetNotification.class);
                    notificatiolist = login.getData();
//                    notification_adapter = new Notification_Adapter(Notification.this, notificatiolist);
//                    lstnotificationT.setAdapter(notification_adapter);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }, paramArrayList, "get_notification");
    }

    private void getAllNotification() {
        AndroidNetworking.post(geturl.BASE_URL + "get_notification_new")
                .addHeaders("Authorization", sharedpreference.getUserToken(Notification.this))
                .addBodyParameter("user_id", sharedpreference.getUserId(Notification.this))
                .addBodyParameter("type", "1")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Public_Function.Hide_ProgressDialogue();
                        try {
                            if (response.getBoolean("success")) {

                                Notify notify = new Gson().fromJson(response.toString(), Notify.class);

                                NotificationData notificationData = notify.getData();

                                if (notificationData.getAll() != null)
                                    allNotifications.addAll(notificationData.getAll());
                                if (notificationData.getRequested() != null)
                                    requesteds.addAll(notificationData.getRequested());
                                if (notificationData.getSuggested() != null)
                                    suggestedList.addAll(notificationData.getSuggested());
                                if (notificationData.getSos() != null)
                                    soList.addAll(notificationData.getSos());
                                if (notificationData.getStars() != null)
                                    starList.addAll(notificationData.getStars());

                                allNotificationAdapter.addAll(notificationData.getAll());

                                if (notificationData.getAll().isEmpty()) {
                                    tv_no_found.setVisibility(View.VISIBLE);
                                    rv_today.setVisibility(View.GONE);
                                } else {
                                    tv_no_found.setVisibility(View.GONE);
                                    rv_today.setVisibility(View.VISIBLE);
                                }

                                if (notificationData.getRequested().isEmpty()) {
                                    rl_req.setVisibility(View.GONE);
                                    rv_today_req.setVisibility(View.GONE);
                                } else {
                                    rl_req.setVisibility(View.VISIBLE);
                                    rv_today_req.setVisibility(View.VISIBLE);
                                }

                                if (suggestedList.size() == 0) {
                                    ll_text.setVisibility(View.GONE);
                                    rv_today_suggest.setVisibility(View.GONE);
                                } else {
                                    ll_text.setVisibility(View.VISIBLE);
                                    suggestedAdapter.addAll(notificationData.getSuggested());
                                    rv_today_suggest.setVisibility(View.VISIBLE);
                                }

                                if (notificationData.getAll().isEmpty() &&
                                        notificationData.getSuggested().isEmpty() &&
                                        notificationData.getRequested().isEmpty()) {
                                    tv_no_found.setVisibility(View.GONE);
                                    txtempty.setVisibility(View.VISIBLE);
                                    rv_today_req.setVisibility(View.GONE);
                                    rv_today.setVisibility(View.GONE);
                                    rv_today_suggest.setVisibility(View.GONE);
                                }

                                Log.e("LLLLLL_Notify_Data: ", notify.toString());
                            }
                        } catch (JSONException e) {
                            Public_Function.Hide_ProgressDialogue();
                            Log.e("LLLLLL_Notify_Error: ", "1: " + e.getLocalizedMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Public_Function.Hide_ProgressDialogue();
                        Log.e("LLLLLL_Notify_Error: ", "2: " + anError.getLocalizedMessage());
                    }
                });
    }

    private void getSuggested() {
        if (!Public_Function.isInternetConnected(Notification.this)) {
            Public_Function.Hide_ProgressDialogue();
            rv_suggested.setVisibility(View.GONE);
            txtempty.setVisibility(View.VISIBLE);
            Toasty.error(Notification.this, "Please Check Your Internet Connection...");
        } else {
            AndroidNetworking.post(geturl.BASE_URL + "get_notification_new")
                    .addHeaders("Authorization", sharedpreference.getUserToken(Notification.this))
                    .addBodyParameter("user_id", sharedpreference.getUserId(Notification.this))
                    .addBodyParameter("type", "3")
                    .setPriority(Priority.IMMEDIATE)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Public_Function.Hide_ProgressDialogue();
                            try {
                                if (response.getBoolean("success")) {

                                    DataSuggest dataSuggest = new Gson().fromJson(response.toString(), DataSuggest.class);

                                    suggestedList.addAll(dataSuggest.getData());

                                    if (dataSuggest.getData().isEmpty()) {
                                        rv_suggested.setVisibility(View.GONE);
                                        txtempty.setVisibility(View.VISIBLE);
                                    } else {
                                        rv_suggested.setVisibility(View.VISIBLE);
                                        txtempty.setVisibility(View.GONE);
                                    }

                                    suggestedAdapter.addAll(dataSuggest.getData());

                                    Log.e("LLLLLL_NotifyS_Data: ", dataSuggest.getData().toString());
                                }
                            } catch (JSONException e) {
                                Public_Function.Hide_ProgressDialogue();
                                Log.e("LLLLLL_Notify_Error: ", "1: " + e.getLocalizedMessage());
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Public_Function.Hide_ProgressDialogue();
                            Log.e("LLLLLL_Notify_Error: ", "2: " + anError.getLocalizedMessage());
                        }
                    });
        }

    }

    private void getRequested() {
        if (!Public_Function.isInternetConnected(Notification.this)) {
            Public_Function.Hide_ProgressDialogue();
            rv_request.setVisibility(View.GONE);
            txtempty.setVisibility(View.VISIBLE);
            Toasty.error(Notification.this, "Please Check Your Internet Connection...");
        } else {
            AndroidNetworking.post(geturl.BASE_URL + "get_notification_new")
                    .addHeaders("Authorization", sharedpreference.getUserToken(Notification.this))
                    .addBodyParameter("user_id", sharedpreference.getUserId(Notification.this))
                    .addBodyParameter("type", "2")
                    .setPriority(Priority.IMMEDIATE)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Public_Function.Hide_ProgressDialogue();
                            try {
                                if (response.getBoolean("success")) {

                                    DataReq dataSuggest = new Gson().fromJson(response.toString(), DataReq.class);

                                    requesteds.addAll(dataSuggest.getData());

                                    if (dataSuggest.getData().isEmpty()) {
                                        rv_request.setVisibility(View.GONE);
                                        txtempty.setVisibility(View.VISIBLE);
                                    } else {
                                        rv_request.setVisibility(View.VISIBLE);
                                        txtempty.setVisibility(View.GONE);
                                    }

                                    requestAdapter.addAll(dataSuggest.getData());

                                    Log.e("LLLLLL_NotifyR_Data: ", dataSuggest.getData().toString());
                                }
                            } catch (JSONException e) {
                                Public_Function.Hide_ProgressDialogue();
                                Log.e("LLLLLL_Notify_Error: ", "1: " + e.getLocalizedMessage());
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Public_Function.Hide_ProgressDialogue();
                            Log.e("LLLLLL_Notify_Error: ", "2: " + anError.getLocalizedMessage());
                        }
                    });
        }

    }

    private void getSOS() {
        if (!Public_Function.isInternetConnected(Notification.this)) {
            Public_Function.Hide_ProgressDialogue();
            rv_sos.setVisibility(View.GONE);
            txtempty.setVisibility(View.VISIBLE);
            Toasty.error(Notification.this, "Please Check Your Internet Connection...");
        } else {
            AndroidNetworking.post(geturl.BASE_URL + "get_notification_new")
                    .addHeaders("Authorization", sharedpreference.getUserToken(Notification.this))
                    .addBodyParameter("user_id", sharedpreference.getUserId(Notification.this))
                    .addBodyParameter("type", "4")
                    .setPriority(Priority.IMMEDIATE)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Public_Function.Hide_ProgressDialogue();
                            try {
                                if (response.getBoolean("success")) {

                                    DataSos dataSos = new Gson().fromJson(response.toString(), DataSos.class);

                                    soList.addAll(dataSos.getData());
                                    Public_Function.sosList.addAll(dataSos.getData());

                                    if (dataSos.getData().isEmpty()) {
                                        rv_sos.setVisibility(View.GONE);
                                        txtempty.setVisibility(View.VISIBLE);
                                    } else {
                                        rv_sos.setVisibility(View.VISIBLE);
                                        txtempty.setVisibility(View.GONE);
                                    }

                                    sosAdapter.addAll(dataSos.getData());

//                                    Log.e("LLLLLL_NotifySS_Data: ", dataSos.getData().get(0).getPhoto());
                                }
                            } catch (JSONException e) {
                                Public_Function.Hide_ProgressDialogue();
                                Log.e("LLLLLL_Notify_Error: ", "1: " + e.getLocalizedMessage());
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Public_Function.Hide_ProgressDialogue();
                            Log.e("LLLLLL_Notify_Error: ", "2: " + anError.getLocalizedMessage());
                        }
                    });
        }

    }

    private void getStar() {
        if (!Public_Function.isInternetConnected(Notification.this)) {
            Public_Function.Hide_ProgressDialogue();
            rv_star.setVisibility(View.GONE);
            txtempty.setVisibility(View.VISIBLE);
            Toasty.error(Notification.this, "Please Check Your Internet Connection...");
        } else {
            AndroidNetworking.post(geturl.BASE_URL + "get_notification_new")
                    .addHeaders("Authorization", sharedpreference.getUserToken(Notification.this))
                    .addBodyParameter("user_id", sharedpreference.getUserId(Notification.this))
                    .addBodyParameter("type", "5")
                    .setPriority(Priority.IMMEDIATE)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Public_Function.Hide_ProgressDialogue();
                            try {
                                if (response.getBoolean("success")) {

                                    DataStar dataStar = new Gson().fromJson(response.toString(), DataStar.class);

                                    starList.addAll(dataStar.getData());

                                    if (dataStar.getData().isEmpty()) {
                                        rv_star.setVisibility(View.GONE);
                                        txtempty.setVisibility(View.VISIBLE);
                                    } else {
                                        rv_star.setVisibility(View.VISIBLE);
                                        txtempty.setVisibility(View.GONE);
                                    }

                                    starAdapter.addAll(dataStar.getData());

                                }
                            } catch (JSONException e) {
                                Public_Function.Hide_ProgressDialogue();
                                Log.e("LLLLLL_Notify_Error: ", "1: " + e.getLocalizedMessage());
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Public_Function.Hide_ProgressDialogue();
                            Log.e("LLLLLL_Notify_Error: ", "2: " + anError.getLocalizedMessage());
                        }
                    });
        }

    }
}
