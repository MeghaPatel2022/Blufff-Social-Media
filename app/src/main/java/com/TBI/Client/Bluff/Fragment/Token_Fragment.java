package com.TBI.Client.Bluff.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.TBI.Client.Bluff.Activity.TokenList;
import com.TBI.Client.Bluff.Adapter.Token_Adapter;
import com.TBI.Client.Bluff.Model.GetToken.Datum;
import com.TBI.Client.Bluff.Model.GetToken.GetToken;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.MyAsyncTaskCallback;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class Token_Fragment extends Fragment {

    @BindView(R.id.griddiscover)
    GridView griddiscover;


    ConnectionDetector cd;
    boolean isInternetPresent = false;

    Token_Adapter token_adapter;
    List<Datum> tokenarray = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fagment_discover, container, false);
        ButterKnife.bind(this, view);


        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent) {
            Toasty.warning(getActivity(), "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
        } else {
            Gettoken();
        }


        griddiscover.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getContext(), TokenList.class);
                intent.putExtra("array", (Serializable) tokenarray.get(i).getGifImages());
                intent.putExtra("image", tokenarray.get(i).getImage());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

            }
        });


        return view;
    }


    public void Gettoken() {


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(getActivity())));
        paramArrayList.add(new param("timezone", Public_Function.gettimezone()));
        new geturl().getdata(getActivity(), new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {

                try {
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("message");

                    if (message) {

                        Log.d("mn13status", data);
                        GetToken login = new Gson().fromJson(data, GetToken.class);
                        tokenarray = login.getData();

                        token_adapter = new Token_Adapter(getContext(), tokenarray);
                        griddiscover.setAdapter(token_adapter);

                    } else {
                        // Toasty.error(getActivity(), status + "", Toast.LENGTH_LONG, true).show();
                    }
                } catch (Exception e) {
                    // Toasty.error(Login.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                    e.printStackTrace();
                }

            }
        }, paramArrayList, "get_post_gif");
    }
}
