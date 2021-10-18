package com.TBI.Client.Bluff.Activity.Post;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.TBI.Client.Bluff.Adapter.Post.TagImages_Adapter;
import com.TBI.Client.Bluff.Adapter.Post.Userlist_Adapter;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.Model.SearchUser.Datum;
import com.TBI.Client.Bluff.Model.SearchUser.SearchUser;
import com.TBI.Client.Bluff.Model.TagModel;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.MyAsyncTaskCallback;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.google.gson.Gson;
import com.harsh.instatag.InstaTag;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class TagUser extends AppCompatActivity {

    public static ArrayList<TagModel> intatagarraylist = new ArrayList<TagModel>();
    public static ArrayList<InstaTag> instaarray = new ArrayList<InstaTag>();
    @BindView(R.id.lstuserlist)
    ListView lstuserlist;
    @BindView(R.id.txtcancel)
    TextView txtcancel;
    @BindView(R.id.lnsearch)
    LinearLayout lnsearch;
    @BindView(R.id.lntag)
    LinearLayout lntag;
    @BindView(R.id.viewimages)
    ViewPager viewimages;
    @BindView(R.id.searchdiscover)
    SearchView searchdiscover;
    @BindView(R.id.txtempty)
    TextView txtempty;
    @BindView(R.id.txtdone)
    TextView txtdone;
    @BindView(R.id.recycleimages)
    RecyclerView recycleimages;
    List<File> imagesrray = new ArrayList<>();
    TagImages_Adapter tagImages_adapter;
    ConnectionDetector cd;
    boolean isInternetPresent = false;
    Userlist_Adapter userlist_adapter;
    List<Datum> userlist = new ArrayList<Datum>();
    int height, width;
    private int addTagInX, addTagInY;

    private String fileType = "";

    private final InstaTag.PhotoEvent photoEvent = new InstaTag.PhotoEvent() {
        @Override
        public void singleTapConfirmedAndRootIsInTouch(final int x, final int y) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addTagInX = x;
                    addTagInY = y;
                    lntag.setVisibility(View.GONE);
                    lnsearch.setVisibility(View.VISIBLE);
                  /*  recyclerViewUsers.setVisibility(View.VISIBLE);
                    headerUsers.setVisibility(View.GONE);
                    tapPhotoToTagUser.setVisibility(View.GONE);
                    headerSearchUsers.setVisibility(View.VISIBLE);*/
                }
            });
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (sharedpreference.getTheme(TagUser.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taguser);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(TagUser.this));

        fileType = getIntent().getStringExtra("fileType");
        imagesrray = (List<File>) getIntent().getSerializableExtra("imagarray");
        tagImages_adapter = new TagImages_Adapter(TagUser.this, imagesrray, photoEvent,fileType);

        viewimages.setAdapter(tagImages_adapter);
        // viewimages.setOffscreenPageLimit(5);

        ViewTreeObserver vto = viewimages.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                viewimages.getViewTreeObserver().removeOnPreDrawListener(this);
                int finalHeight = viewimages.getMeasuredHeight();
                int finalWidth = viewimages.getMeasuredWidth();
                Log.d("mn13height", finalHeight + "::" + finalWidth);
                return true;
            }
        });


        for (int i = 0; i < 5; i++) {
            instaarray.add(new InstaTag(TagUser.this));
        }
        txtcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lntag.setVisibility(View.VISIBLE);
                lnsearch.setVisibility(View.GONE);
            }
        });

        txtdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < intatagarraylist.size(); i++) {

                    Log.d("mn13tag", intatagarraylist.get(i).getInstaTag().getTags().toString() + "\n");
                }


                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);

            }
        });


        EditText searchEditText = searchdiscover.findViewById(androidx.appcompat.R.id.search_src_text);
        Typeface myFont = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            myFont = getResources().getFont(R.font.poppins_semibold);
            searchEditText.setTypeface(myFont);
        }
        searchEditText.setTextSize(14);
        searchEditText.setTextColor(getResources().getColor(R.color.blacklight));
        searchEditText.setHintTextColor(getResources().getColor(R.color.darkgrey));

        userlist_adapter = new Userlist_Adapter(TagUser.this);
        lstuserlist.setAdapter(userlist_adapter);

        searchdiscover.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equalsIgnoreCase("")) {
                    lstuserlist.setEmptyView(null);
                } else {

                    cd = new ConnectionDetector(TagUser.this);
                    isInternetPresent = cd.isConnectingToInternet();

                    if (!isInternetPresent) {
                        Toasty.warning(TagUser.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                    } else {
                        SearchUser(newText);
                    }
                }
                return false;
            }
        });


        lstuserlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               /* String name = userlist.get(i).getUsername();
                taglist.add(new TagsModel(viewimages.getCurrentItem(), addTagInX, addTagInY, name));
                Log.d("mn13helloworld", taglist.toString());
                tagImages_adapter.notifyDataSetChanged();*/
                Log.d("mn13xandy", addTagInX + "\n" + addTagInY);
                instaarray.get(viewimages.getCurrentItem()).setCanWeAddTags(true);
                instaarray.get(viewimages.getCurrentItem()).addTag(addTagInX, addTagInY, userlist.get(i).getUsername());
                // instaarray.get(viewimages.getCurrentItem()).set
                Log.d("mn13insta1", instaarray.get(viewimages.getCurrentItem()).getTags().toString() + "");
                tagImages_adapter.notifyDataSetChanged();


                searchEditText.setText("");
                lntag.setVisibility(View.VISIBLE);
                lnsearch.setVisibility(View.GONE);
                userlist_adapter.AddAll(new ArrayList<>());
                lstuserlist.setEmptyView(null);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        instaarray = new ArrayList<>();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    public void SearchUser(String s) {


        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(TagUser.this) + ""));
        paramArrayList.add(new param("keyword", s + ""));
        paramArrayList.add(new param("filter_by", "all"));

        new geturl().getdata(TagUser.this, new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {
                userlist = new ArrayList<>();
                try {
                    Public_Function.Hide_ProgressDialogue();
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("msg");
                    if (message) {
                        SearchUser login = new Gson().fromJson(data, SearchUser.class);
                        userlist = login.getData();

                    } else {
                        Toasty.error(TagUser.this, status + "", Toast.LENGTH_LONG, true).show();
                    }
                } catch (Exception e) {
                    Toasty.error(TagUser.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                    e.printStackTrace();
                }

                userlist_adapter.AddAll(userlist);
                lstuserlist.setEmptyView(txtempty);
            }
        }, paramArrayList, "user_listing");
    }

}
