package com.TBI.Client.Bluff.Activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.TBI.Client.Bluff.Adapter.TokenList_adapter;
import com.TBI.Client.Bluff.Model.GetToken.GifImage;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.sharedpreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TokenList extends AppCompatActivity {

    @BindView(R.id.tool_bar)
    Toolbar tool_bar;
    @BindView(R.id.gridsavedpost)
    GridView gridsavedpost;
    @BindView(R.id.txtempty)
    TextView txtempty;
    @BindView(R.id.txttitle)
    TextView txttitle;

    ConnectionDetector cd;
    boolean isInternetPresent = false;

    String image = "";
    List<GifImage> gifarray = new ArrayList<GifImage>();

    TokenList_adapter tokenDetail_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_post);

        ButterKnife.bind(this);

        setSupportActionBar(tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (sharedpreference.getTheme(TokenList.this).equalsIgnoreCase("white")) {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        } else {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }


        gridsavedpost.setNumColumns(2);
        gifarray = (List<GifImage>) getIntent().getSerializableExtra("array");
        image = getIntent().getExtras().getString("image");
        txttitle.setText("Tokens");
        tokenDetail_adapter = new TokenList_adapter(TokenList.this, gifarray, image);
        gridsavedpost.setAdapter(tokenDetail_adapter);


        gridsavedpost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Intent i1 = new Intent(TokenList.this, TokenDetail.class);
                i1.putExtra("image", image);
                i1.putExtra("gifmodel", gifarray.get(i));
                startActivity(i1);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
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
