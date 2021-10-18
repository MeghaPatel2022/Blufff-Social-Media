package com.TBI.Client.Bluff.Activity.Settings;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Post.PostDetailPage1;
import com.TBI.Client.Bluff.Adapter.Home.Saved_Post_Adapter;
import com.TBI.Client.Bluff.Database.DBHelper;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.Model.GetSaved_Post.Datum;
import com.TBI.Client.Bluff.Model.GetSaved_Post.GetSavedPost;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.MyAsyncTaskCallback;
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

public class Saved_Post extends AppCompatActivity {

    public static boolean deletepost = false;
    @BindView(R.id.tool_bar)
    Toolbar tool_bar;
    @BindView(R.id.gridsavedpost)
    RecyclerView gridsavedpost;
    @BindView(R.id.txtempty)
    TextView txtempty;
    @BindView(R.id.scrollload)
    NestedScrollView scrollload;
    ConnectionDetector cd;
    boolean isInternetPresent = false;
    List<Datum> getsavepost = new ArrayList<>();
    Saved_Post_Adapter saved_post_adapter;
    //public static boolean apicall;
    int limit = 20, offset = 0;
    boolean isLoading = true;
    Datum selectmodel;
    // DBHelper Initialization
    DBHelper dbHelper;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int totalMovedPixel;
    private int totalPixel;

    GridLayoutManager gridLayoutManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Log.d("mn13theme", sharedpreference.getTheme(Saved_Post.this) + "11");
        if (sharedpreference.getTheme(Saved_Post.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        deletepost = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_post);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(Saved_Post.this));
        dbHelper = new DBHelper(Saved_Post.this);

        setSupportActionBar(tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (sharedpreference.getTheme(Saved_Post.this).equalsIgnoreCase("white")) {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        } else {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }

        gridLayoutManager = new GridLayoutManager(Saved_Post.this,2);
        gridsavedpost.setLayoutManager(gridLayoutManager);

        saved_post_adapter = new Saved_Post_Adapter(Saved_Post.this, getsavepost);
        gridsavedpost.setAdapter(saved_post_adapter);

        saved_post_adapter.setClickListener(new Saved_Post_Adapter.PostClickListner() {
            @Override
            public void onItemClick(View view, int i) {
                selectmodel = getsavepost.get(i);

                Intent i1 = new Intent(Saved_Post.this, PostDetailPage1.class);
                i1.putExtra("post_id", getsavepost.get(i).getId() + "");
                i1.putExtra("comment", "no");
                i1.putExtra("showsubscribe", "no");
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }
        });

        cd = new ConnectionDetector(Saved_Post.this);
        isInternetPresent = cd.isConnectingToInternet();
        GetSavedPost();

        scrollload.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {

                        visibleItemCount = gridLayoutManager.getChildCount();
                        totalItemCount = gridLayoutManager.getItemCount();
                        pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                        if (isLoading) {
                            isLoading = false;
                            offset = offset + 20;
                            GetSavedPost();
                        }
                    }
                }
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

        if (deletepost) {
            if (selectmodel != null) {
                deletepost = false;
                getsavepost.remove(selectmodel);
                saved_post_adapter.remove(selectmodel);
                selectmodel = null;
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
        return super.onOptionsItemSelected(item);
    }


    public void GetSavedPost() {
        if (!isInternetPresent) {
            Toasty.warning(Saved_Post.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();

            if (!getsavepost.isEmpty()) {
                getsavepost.clear();
                getsavepost.addAll(getSavedPost());
                saved_post_adapter.AddAll(getSavedPost());
//                                saved_post_adapter.notifyDataSetChanged();
            } else {
                getsavepost.addAll(getSavedPost());
                saved_post_adapter.AddAll(getSavedPost());
                Log.e("LLL_Data_Length: ", getsavepost.size() + "      " + getSavedPost().size());
//                                saved_post_adapter.notifyDataSetChanged();
            }
            if (getSavedPost().isEmpty()) {
                txtempty.setVisibility(View.VISIBLE);
                gridsavedpost.setVisibility(View.GONE);
            }

        } else {
            ArrayList<param> paramArrayList = new ArrayList<>();
            paramArrayList.add(new param("user_id", sharedpreference.getUserId(Saved_Post.this)));

            new geturl().getdata(Saved_Post.this, new MyAsyncTaskCallback() {
                @Override
                public void onAsyncTaskComplete(String data) {

                    try {
                        JSONObject object = new JSONObject(data);
                        boolean message = object.optBoolean("success");
                        String status = "";
                        status = object.optString("message");

                        if (message) {
                            GetSavedPost login = new Gson().fromJson(data, GetSavedPost.class);

                            for (int i = 0; i < login.getData().size(); i++) {
                                if (!dbHelper.chceckSavedPostExits(login.getData().get(i).getId(), login.getData().get(i).getUserId())) {
                                    dbHelper.insertSavedPost(login.getData().get(i).getId(),
                                            login.getData().get(i).getUserId(),
                                            login.getData().get(i).getDescription(),
                                            login.getData().get(i).getImage(),
                                            login.getData().get(i).getMultipleImages());
                                } else {
                                    Log.e("LLL_Data: ", "....");
                                }
                            }

                            if (!getsavepost.isEmpty()) {
                                getsavepost.clear();
                                getsavepost.addAll(getSavedPost());
                                saved_post_adapter.AddAll(getSavedPost());
                            } else {
                                getsavepost.addAll(getSavedPost());
                                saved_post_adapter.AddAll(getSavedPost());
                                Log.e("LLL_Data_Length: ", getsavepost.size() + "      " + getSavedPost().size());
                            }
                            if (getSavedPost().isEmpty()) {
                                txtempty.setVisibility(View.VISIBLE);
                                gridsavedpost.setVisibility(View.GONE);
                            }
//                            if (getsavepost.isEmpty()) {
//                                isLoading = false;
//                            } else {
//                                isLoading = true;
//                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, paramArrayList, "get_bookmark_post");

        }
    }

    private ArrayList<Datum> getSavedPost() {
        Cursor cur = dbHelper.getAllSavedPost();

        ArrayList<Datum> getSavedpost = new ArrayList<>();
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                Datum datum = new Datum();
                datum.setId(cur.getInt(1));
                datum.setUserId(cur.getInt(2));
                datum.setDescription(cur.getString(3));
                datum.setImage(cur.getString(4));
                datum.setMultipleImages(cur.getInt(5));

                getSavedpost.add(datum);
            } while (cur.moveToNext());
            cur.close();
        }
        return getSavedpost;
    }

}
