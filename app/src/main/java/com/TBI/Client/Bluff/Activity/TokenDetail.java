package com.TBI.Client.Bluff.Activity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.TBI.Client.Bluff.Model.GetToken.GifImage;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class TokenDetail extends AppCompatActivity {


    @BindView(R.id.collapsible_toolbar)
    Toolbar toolbar;
    @BindView(R.id.imgprofilepic)
    CircleImageView imgprofilepic;
    @BindView(R.id.txttoolbar)
    TextView txttoolbar;
    @BindView(R.id.txttime)
    TextView txttime;
    @BindView(R.id.imggif)
    ImageView imggif;
    @BindView(R.id.imgpic)
    ImageView imgpic;

    String image = "";
    GifImage gifmodel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tokendetail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (sharedpreference.getTheme(TokenDetail.this).equalsIgnoreCase("white")) {
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        } else {
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }


        image = getIntent().getExtras().getString("image");
        gifmodel = (GifImage) getIntent().getSerializableExtra("gifmodel");

        imgpic.setClipToOutline(true);
        imggif.setClipToOutline(true);

        if (!image.equals("") && !image.equals(null) && !image.equals("null")) {
            Glide
                    .with(TokenDetail.this)
                    .load(image)
                    .placeholder(R.drawable.grey_placeholder)
                    .dontAnimate()
                    .into(imgpic);
        }

        if (gifmodel != null) {

            txttoolbar.setText(gifmodel.getUsername() + "");
            txttime.setText(gifmodel.getDuration() + "");

            if (!gifmodel.getPhoto().equals("") && !gifmodel.getPhoto().equals(null) && !gifmodel.getPhoto().equals("null")) {
                Glide
                        .with(TokenDetail.this)
                        .load(gifmodel.getPhoto())
                        .placeholder(R.drawable.grey_placeholder)
                        .dontAnimate()
                        .into(imgprofilepic);
            }

            if (!gifmodel.getImage().equals("") && !gifmodel.getImage().equals(null) && !gifmodel.getImage().equals("null")) {
                Glide
                        .with(TokenDetail.this)
                        .load(gifmodel.getImage())
                        .placeholder(R.drawable.grey_placeholder)
                        .dontAnimate()
                        .into(imggif);
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_out);
        return super.onOptionsItemSelected(item);
    }


}
