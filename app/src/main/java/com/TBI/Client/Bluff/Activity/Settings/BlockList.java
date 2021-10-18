package com.TBI.Client.Bluff.Activity.Settings;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.TBI.Client.Bluff.Activity.Profile.OtherUserProfile;
import com.TBI.Client.Bluff.Adapter.Chat.BlockUser_Adapter;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.Model.GetBlockUser.Datum;
import com.TBI.Client.Bluff.Model.GetBlockUser.GetBlockUser;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.MyAsyncTaskCallback;
import com.TBI.Client.Bluff.Utils.Public_Function;
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

public class BlockList extends AppCompatActivity {

    @BindView(R.id.tool_bar)
    Toolbar tool_bar;
    @BindView(R.id.lstblocked)
    ListView lstblocked;
    @BindView(R.id.txtempty)
    TextView txtempty;

    ConnectionDetector cd;
    boolean isInternetPresent = false;
    List<Datum> blocklist = new ArrayList<>();
    BlockUser_Adapter blockUser_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (sharedpreference.getTheme(BlockList.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocklist);

        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(BlockList.this));
        setSupportActionBar(tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (sharedpreference.getTheme(BlockList.this).equalsIgnoreCase("white")) {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        } else {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }


        cd = new ConnectionDetector(BlockList.this);
        isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent) {
            Toasty.warning(BlockList.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
        } else {
            GetBlockList();
        }


        lstblocked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent i1 = new Intent(BlockList.this, OtherUserProfile.class);
                i1.putExtra("other_id", blocklist.get(i).getId() + "");
                i1.putExtra("other_username", "");
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_out);
        return super.onOptionsItemSelected(item);
    }

    public void GetBlockList() {

        Public_Function.Show_Progressdialog(BlockList.this);

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(BlockList.this) + ""));

        new geturl().getdata(BlockList.this, new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {
                try {
                    Public_Function.Hide_ProgressDialogue();
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("msg");
                    if (message) {
                        GetBlockUser login = new Gson().fromJson(data, GetBlockUser.class);
                        blocklist = login.getData();

                        blockUser_adapter = new BlockUser_Adapter(BlockList.this, blocklist);
                        lstblocked.setAdapter(blockUser_adapter);
                    } else {
                        Toasty.error(BlockList.this, status + "", Toast.LENGTH_LONG, true).show();
                    }
                } catch (Exception e) {
                    Toasty.error(BlockList.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                    e.printStackTrace();
                }

                lstblocked.setEmptyView(txtempty);
            }
        }, paramArrayList, "get_blocked_users");
    }
}
