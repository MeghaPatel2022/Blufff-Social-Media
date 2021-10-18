package com.TBI.Client.Bluff.Activity.Post;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.TBI.Client.Bluff.Adapter.Post.Multipleimage_Adapter;
import com.TBI.Client.Bluff.Model.Get_bannerdetail.Photo;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.sharedpreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowMultipleIamges extends AppCompatActivity {

    @BindView(R.id.tool_bar)
    Toolbar tool_bar;
    @BindView(R.id.viewmultipleimage)
    ViewPager viewmultipleimage;

    Integer position;

    List<Photo> photosaary = new ArrayList<Photo>();
    Multipleimage_Adapter multipleimage_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (sharedpreference.getTheme(ShowMultipleIamges.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showmultipleimage);
        ButterKnife.bind(this);

        setSupportActionBar(tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (sharedpreference.getTheme(ShowMultipleIamges.this).equalsIgnoreCase("white")) {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        } else {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }

        position = getIntent().getExtras().getInt("position");
        photosaary = (List<Photo>) getIntent().getSerializableExtra("array");


        multipleimage_adapter = new Multipleimage_Adapter(ShowMultipleIamges.this, photosaary);
        viewmultipleimage.setAdapter(multipleimage_adapter);
        viewmultipleimage.setCurrentItem(position);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
        return super.onOptionsItemSelected(item);
    }

}
