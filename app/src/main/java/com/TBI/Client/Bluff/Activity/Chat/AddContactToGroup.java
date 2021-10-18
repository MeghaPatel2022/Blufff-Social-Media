package com.TBI.Client.Bluff.Activity.Chat;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.TBI.Client.Bluff.Adapter.Chat.ChatFreindlist_Adapter;
import com.TBI.Client.Bluff.Model.GetFreinds.Datum;
import com.TBI.Client.Bluff.Model.GetFreinds.Getfreinds;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.MyAsyncTaskCallback;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.applozic.mobicomkit.contact.AppContactService;
import com.applozic.mobicommons.people.channel.ChannelUserMapper;
import com.applozic.mobicommons.people.contact.Contact;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class AddContactToGroup extends AppCompatActivity {

    public List<ChannelUserMapper> channelUserMapperList = new ArrayList<>();
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.searchchat)
    SearchView searchchat;
    @BindView(R.id.lstcontactlist)
    ListView lstcontactlist;
    @BindView(R.id.txtempty)
    TextView txtempty;
    int limit = 20, offset = 0;
    boolean isLoading = true;

    List<Datum> freindlist = new ArrayList<>();
    ChatFreindlist_Adapter chatFreindlist;


    List<Datum> dummyarray = new ArrayList<>();
    String query = "";

    ConnectionDetector cd;
    boolean isInternetPresent = false;
    List<String> contactis = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (sharedpreference.getTheme(AddContactToGroup.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontacttogroup);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (sharedpreference.getTheme(AddContactToGroup.this).equalsIgnoreCase("white")) {
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        } else {
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }


        EditText searchEditText = searchchat.findViewById(androidx.appcompat.R.id.search_src_text);

        Typeface myFont = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            myFont = getResources().getFont(R.font.poppins_semibold);
            searchEditText.setTypeface(myFont);

            //searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(RM.dimen.fab_margin));
        }
        searchEditText.setTextSize(14);
        searchEditText.setTextColor(getResources().getColor(R.color.blacklight));
        searchEditText.setHintTextColor(getResources().getColor(R.color.blacklight));


        searchchat.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                GetFreinds(newText);
                return false;
            }
        });


        chatFreindlist = new ChatFreindlist_Adapter(AddContactToGroup.this, freindlist, "no");
        lstcontactlist.setAdapter(chatFreindlist);

        channelUserMapperList = (List<ChannelUserMapper>) getIntent().getSerializableExtra("channel");

        if (channelUserMapperList != null) {


            if (!channelUserMapperList.isEmpty()) {

                AppContactService baseContactService = new AppContactService(getApplicationContext());
                for (int i = 0; i < channelUserMapperList.size(); i++) {

                    ChannelUserMapper channelUserMapper = channelUserMapperList.get(i);
                    Contact contact = baseContactService.getContactById(channelUserMapper.getUserKey());

                    Log.d("mn13contact:", contact.toString());
                    if (!contact.getUserId().equalsIgnoreCase(sharedpreference.getUserId(AddContactToGroup.this))) {
                        contactis.add(contact.getUserId());
                    }

                }
            }

        }


        cd = new ConnectionDetector(AddContactToGroup.this);
        isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent) {
            Toasty.warning(AddContactToGroup.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
        } else {
            GetFreinds("");
        }


        lstcontactlist.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (isLoading == false) {
                        offset = offset + 20;
                        isLoading = true;
                        if (dummyarray.size() == 20) {
                            Loadmore(query);
                        }

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


    public void GetFreinds(String keyword) {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(AddContactToGroup.this)));
        paramArrayList.add(new param("keyword", keyword));
        paramArrayList.add(new param("limit", "20"));
        paramArrayList.add(new param("offset", "0"));

        new geturl().getdata(AddContactToGroup.this, new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {

                Log.d("mn13data", "indata");
                dummyarray = new ArrayList<>();
                freindlist = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("message");

                    if (message) {
                        Getfreinds login = new Gson().fromJson(data, Getfreinds.class);
                        chatFreindlist.clear();
                        List<Datum> randomarray = login.getData();
                        for (int i = 0; i < randomarray.size(); i++) {

                            if (!contactis.contains(randomarray.get(i).getUserId() + "")) {
                                dummyarray.add(randomarray.get(i));
                            }
                        }
                        freindlist.addAll(dummyarray);
                        chatFreindlist.addAll(freindlist);

                        isLoading = dummyarray.isEmpty();


                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                lstcontactlist.setEmptyView(txtempty);
            }
        }, paramArrayList, "search_followers");
    }

    public void Loadmore(String keyword) {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(AddContactToGroup.this)));
        paramArrayList.add(new param("keyword", keyword));
        paramArrayList.add(new param("limit", limit + ""));
        paramArrayList.add(new param("offset", offset + ""));

        new geturl().getdata(AddContactToGroup.this, new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {
                dummyarray = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("message");

                    if (message) {
                        Getfreinds login = new Gson().fromJson(data, Getfreinds.class);

                        dummyarray = login.getData();
                        freindlist.addAll(dummyarray);
                        chatFreindlist.addAll(freindlist);

                        isLoading = dummyarray.isEmpty();


                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                lstcontactlist.setEmptyView(txtempty);
            }
        }, paramArrayList, "search_followers");
    }


}
