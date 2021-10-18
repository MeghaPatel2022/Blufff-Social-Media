package com.TBI.Client.Bluff.Activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.TBI.Client.Bluff.Activity.Post.ShowMultipleIamges;
import com.TBI.Client.Bluff.Adapter.Banner_MultiImage_Adapter;
import com.TBI.Client.Bluff.Model.Get_bannerdetail.Banner_detail;
import com.TBI.Client.Bluff.Model.Get_bannerdetail.Datum;
import com.TBI.Client.Bluff.Model.Get_bannerdetail.Photo;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.MyAsyncTaskCallback;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class BannerDetail extends AppCompatActivity {

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout app_bar_layout;
    @BindView(R.id.collapsible_toolbar)
    Toolbar collapsible_toolbar;
    @BindView(R.id.txttoolbar)
    TextView txttoolbar;
    @BindView(R.id.lnprofile)
    LinearLayout lnprofile;
    @BindView(R.id.lnbottom)
    LinearLayout lnbottom;
    @BindView(R.id.imguser)
    ImageView imguser;
    @BindView(R.id.lnnopic)
    LinearLayout lnnopic;
    @BindView(R.id.txtname)
    TextView txtname;
    @BindView(R.id.txtbio)
    TextView txtbio;
    @BindView(R.id.txtpostempty)
    TextView txtpostempty;
    @BindView(R.id.recyclefeed)
    GridView recyclefeed;
    @BindView(R.id.tabmode)
    TabLayout tabmode;
    @BindView(R.id.txtbusinessname)
    TextView txtbusinessname;
    @BindView(R.id.txtmobileno)
    TextView txtmobileno;
    @BindView(R.id.txtemail)
    TextView txtemail;
    @BindView(R.id.txtlocaion)
    TextView txtlocaion;
    @BindView(R.id.txtbtype)
    TextView txtbtype;
    @BindView(R.id.txtsubtopic)
    TextView txtsubtopic;
    @BindView(R.id.nestedscrollinfo)
    NestedScrollView nestedscrollinfo;
    @BindView(R.id.nestedscrollphoto)
    NestedScrollView nestedscrollphoto;


    String post_id = "";

    ConnectionDetector cd;
    boolean isInternetPresent = false;

    List<Datum> detailarray = new ArrayList<Datum>();
    List<Photo> photosaary = new ArrayList<Photo>();

    Banner_MultiImage_Adapter banner_multiImage_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (sharedpreference.getTheme(BannerDetail.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bannerdetail);
        ButterKnife.bind(this);

        setSupportActionBar(collapsible_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        collapsible_toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        collapsingToolbarLayout.setTitle(" ");
        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(" ");
                    lnprofile.setVisibility(View.INVISIBLE);
                    txttoolbar.setVisibility(View.VISIBLE);
                    lnbottom.setVisibility(View.INVISIBLE);
                    isShow = true;
                } else if (isShow) {
                    lnbottom.setVisibility(View.VISIBLE);
                    lnprofile.setVisibility(View.VISIBLE);
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    txttoolbar.setVisibility(View.INVISIBLE);
                    isShow = false;
                }
            }
        });

        tabmode.addTab(tabmode.newTab().setText("Details"));
        tabmode.addTab(tabmode.newTab().setText("Photos"));

        for (int i = 0; i < tabmode.getTabCount(); i++) {
            TabLayout.Tab tab = tabmode.getTabAt(i);

            TextView tv = (TextView) (((LinearLayout) ((LinearLayout) tabmode.getChildAt(0)).getChildAt(i)).getChildAt(1));
            tv.setTextAppearance(BannerDetail.this, R.style.MyTextViewStyle);
            tv.setAllCaps(false);

            if (i == 0) {
                tv.setTextSize(20);
            } else {
                tv.setTextSize(18);
            }
            tab.setTag(tv);
        }

        tabmode.getTabAt(1).select();
        tabmode.setTabIndicatorFullWidth(false);


        recyclefeed.setNestedScrollingEnabled(false);
        post_id = getIntent().getExtras().getString("post_id");


        cd = new ConnectionDetector(BannerDetail.this);
        isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent) {
            Toasty.warning(BannerDetail.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
        } else {
            GetBannerDetail();
        }

        tabmode.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    nestedscrollinfo.setVisibility(View.VISIBLE);
                    nestedscrollphoto.setVisibility(View.INVISIBLE);
                } else if (tab.getPosition() == 1) {
                    nestedscrollinfo.setVisibility(View.INVISIBLE);
                    nestedscrollphoto.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        recyclefeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Intent intent = new Intent(BannerDetail.this, ShowMultipleIamges.class);
                intent.putExtra("array", (Serializable) photosaary);
                intent.putExtra("position", i);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        return super.onOptionsItemSelected(item);
    }

    public void GetBannerDetail() {

        Public_Function.Show_Progressdialog(BannerDetail.this);

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("banner_id", post_id + ""));

        new geturl().getdata(BannerDetail.this, new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {
                try {
                    Public_Function.Hide_ProgressDialogue();
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("msg");
                    if (message) {
                        Banner_detail login = new Gson().fromJson(data, Banner_detail.class);
                        detailarray = login.getData();


                    } else {
                        Toasty.error(BannerDetail.this, status + "", Toast.LENGTH_LONG, true).show();
                    }
                } catch (Exception e) {
                    Toasty.error(BannerDetail.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                    e.printStackTrace();
                }

                if (detailarray.isEmpty()) {
                    recyclefeed.setVisibility(View.GONE);
                    txtpostempty.setVisibility(View.VISIBLE);
                } else {
                    recyclefeed.setVisibility(View.VISIBLE);
                    txtpostempty.setVisibility(View.GONE);
                    photosaary = detailarray.get(0).getPhotos();
                    banner_multiImage_adapter = new Banner_MultiImage_Adapter(BannerDetail.this, photosaary);
                    recyclefeed.setAdapter(banner_multiImage_adapter);

                    if (!detailarray.get(0).getBanner().equalsIgnoreCase("")) {

                        Glide.with(BannerDetail.this)
                                .load(detailarray.get(0).getBanner())
                                .placeholder(R.drawable.profile_placeholder1)
                                .error(R.drawable.profile_placeholder1)
                                .dontAnimate()
                                .into(imguser);

                        lnnopic.setVisibility(View.GONE);
                        imguser.setVisibility(View.VISIBLE);
                    } else {
                        lnnopic.setVisibility(View.VISIBLE);
                        imguser.setVisibility(View.GONE);
                    }


                    txttoolbar.setText(detailarray.get(0).getTitle() + " ");
                    txtname.setText(detailarray.get(0).getTitle() + " ");

                    if (detailarray.get(0).getPhotos().size() > 0) {
                        txtbio.setVisibility(View.VISIBLE);
                        txtbio.setText(detailarray.get(0).getPhotos().size() + "  items");
                    } else {
                        txtbio.setVisibility(View.GONE);
                    }

                    txtbusinessname.setText(detailarray.get(0).getBusinessName() + "");
                    txtmobileno.setText(detailarray.get(0).getBusinessMobileCode() + "" + detailarray.get(0).getBusinessNumber());
                    txtemail.setText(detailarray.get(0).getBusinessEmail() + "");
                    txtlocaion.setText(detailarray.get(0).getBusinessLocation() + "");
                    txtbtype.setText(detailarray.get(0).getBusiness_type() + "");
                    txtsubtopic.setText(detailarray.get(0).getBusinessSubTopics() + "");
                }

                recyclefeed.setNestedScrollingEnabled(true);


            }
        }, paramArrayList, "view_banner_detail");
    }

}
