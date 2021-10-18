package com.TBI.Client.Bluff.Activity.Chat;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.TBI.Client.Bluff.Adapter.Chat.ChatPager_Adapter;
import com.TBI.Client.Bluff.Adapter.Chat.GroupCreate_Adapter;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateChat extends AppCompatActivity {

    public static TextView txtdone;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.viewpagercreate)
    ViewPager viewpagercreate;
    @BindView(R.id.tabcreate)
    TabLayout tabcreate;
    @BindView(R.id.searchchat)
    SearchView searchchat;
    ChatPager_Adapter chatPager_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (sharedpreference.getTheme(CreateChat.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        GroupCreate_Adapter.selestgroupid = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createchat);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(CreateChat.this));
        txtdone = findViewById(R.id.txtdone);

        txtdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i1 = new Intent(CreateChat.this, GroupSetNameImage.class);
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);

            }
        });

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

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (sharedpreference.getTheme(CreateChat.this).equalsIgnoreCase("white")) {
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        } else {
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }


        tabcreate.addTab(tabcreate.newTab().setText("FRIENDS"));
        tabcreate.addTab(tabcreate.newTab().setText("GROUP"));


        //tabsuggest.setupWithViewPager(viewpagerlook);

        for (int i = 0; i < tabcreate.getTabCount(); i++) {
            TabLayout.Tab tab = tabcreate.getTabAt(i);

            TextView tv = (TextView) (((LinearLayout) ((LinearLayout) tabcreate.getChildAt(0)).getChildAt(i)).getChildAt(1));
            tv.setTextAppearance(CreateChat.this, R.style.tabtextstyle);
            tab.setTag(tv);
            tv.setTextSize(16);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Typeface myFont1 = getResources().getFont(R.font.poppins_semibold);
                tv.setTypeface(myFont1);
            }
        }


        searchchat.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (chatPager_adapter != null) {
                    chatPager_adapter.getfragment1().callapi(newText);
                    chatPager_adapter.getfragment2().callapi(newText);
                }

                return false;
            }
        });
        tabcreate.getTabAt(0).select();
        tabcreate.setTabIndicatorFullWidth(false);

//        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
//
//        fragmentArrayList.add(new Look_Fragment());
//        fragmentArrayList.add(new Chat_Fragment());

        chatPager_adapter = new ChatPager_Adapter(getSupportFragmentManager(), CreateChat.this);
        viewpagercreate.setAdapter(chatPager_adapter);

        tabcreate.setupWithViewPager(viewpagercreate);

        tabcreate.getTabAt(0).setText("FRIENDS");
        tabcreate.getTabAt(1).setText("GROUP");


        viewpagercreate.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    txtdone.setVisibility(View.GONE);
                } else {
                    if (GroupCreate_Adapter.selestgroupid.isEmpty()) {
                        txtdone.setVisibility(View.GONE);
                    } else {
                        txtdone.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
        return super.onOptionsItemSelected(item);
    }


}
