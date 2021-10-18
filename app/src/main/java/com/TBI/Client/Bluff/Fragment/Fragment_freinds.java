package com.TBI.Client.Bluff.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.TBI.Client.Bluff.Adapter.Chat.ChatFreindlist_Adapter;
import com.TBI.Client.Bluff.Model.GetFreinds.Datum;
import com.TBI.Client.Bluff.Model.GetFreinds.Getfreinds;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class Fragment_freinds extends Fragment {

    @BindView(R.id.lstfreinds)
    ListView lstfreinds;
    @BindView(R.id.txtempty)
    TextView txtempty;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    String user_id = "";

    ConnectionDetector cd;
    boolean isInternetPresent = false;

    List<Datum> freindlist = new ArrayList<>();
    ChatFreindlist_Adapter chatFreindlist;

    int limit = 20, offset = 0;
    boolean isLoading = true;

    List<Datum> dummyarray = new ArrayList<>();
    String query = "";

    public Fragment_freinds() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        ButterKnife.bind(this, view);

        user_id = sharedpreference.getUserId(getContext());
        chatFreindlist = new ChatFreindlist_Adapter(getActivity(), freindlist, "yes");
        lstfreinds.setAdapter(chatFreindlist);


        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent) {
            Toasty.warning(getActivity(), "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
        } else {
            GetFreinds("");
        }

        swiperefresh.setRefreshing(false);
        swiperefresh.setOnRefreshListener(() -> {
            offset = 0;
            isLoading = true;
            chatFreindlist.clear();
            cd = new ConnectionDetector(getActivity());
            isInternetPresent = cd.isConnectingToInternet();

            if (!isInternetPresent) {
                Toasty.warning(getActivity(), "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
            } else {
                GetFreinds("");
            }

        });


        lstfreinds.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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

        return view;
    }

    public void GetFreinds(String keyword) {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", user_id));
        paramArrayList.add(new param("keyword", keyword));
        paramArrayList.add(new param("limit", "20"));
        paramArrayList.add(new param("offset", "0"));

        new geturl().getdata(getActivity(), data -> {
            Log.d("mn13data", "in");
            swiperefresh.setRefreshing(false);
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
                    dummyarray = login.getData();
                    freindlist.addAll(dummyarray);
                    chatFreindlist.addAll(freindlist);

                    isLoading = dummyarray.isEmpty();


                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            lstfreinds.setEmptyView(txtempty);
        }, paramArrayList, "search_followers");
    }

    public void Loadmore(String keyword) {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", user_id));
        paramArrayList.add(new param("keyword", keyword));
        paramArrayList.add(new param("limit", limit + ""));
        paramArrayList.add(new param("offset", offset + ""));

        new geturl().getdata(getActivity(), data -> {
            swiperefresh.setRefreshing(false);
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

            lstfreinds.setEmptyView(txtempty);
        }, paramArrayList, "search_followers");
    }

    public void callapi(String query1) {

        query = query1;
        if (query.equalsIgnoreCase("")) {
            offset = 0;
            GetFreinds("");
        } else {
            offset = 0;
            GetFreinds(query);
        }
    }
}
