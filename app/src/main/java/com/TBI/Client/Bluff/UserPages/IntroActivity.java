package com.TBI.Client.Bluff.UserPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.TBI.Client.Bluff.Activity.Mains.BottombarActivity;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_skip)
    TextView tv_skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ButterKnife.bind(IntroActivity.this);
        viewpager.setAdapter(new CustomPagerAdapter(this));

        sharedpreference.setInro(IntroActivity.this,true);

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler().postDelayed(() -> {
                    if (MobiComUserPreference.getInstance(IntroActivity.this).isLoggedIn()) {
                        if (sharedpreference.getUserId(IntroActivity.this).equals("")) {
                            Intent i = new Intent(IntroActivity.this, WelcomeActivity.class);
                            startActivity(i);
                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.stay);
                        } else {
                            ApplozicClient.getInstance(IntroActivity.this).setContextBasedChat(true).setHandleDial(true).setIPCallEnabled(true);
                        }
                    } else {
                        Intent i = new Intent(IntroActivity.this, WelcomeActivity.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.stay);
                    }
                }, 500);
                
                sharedpreference.setInro(IntroActivity.this,true);
                Intent intent = new Intent(IntroActivity.this, BottombarActivity.class);
                intent.putExtra("type", "home");
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.stay);
            }
        });

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position)
            {

                new Handler().postDelayed(() -> {
                    if (MobiComUserPreference.getInstance(IntroActivity.this).isLoggedIn()) {
                        if (sharedpreference.getUserId(IntroActivity.this).equals("")) {
                            Intent i = new Intent(IntroActivity.this, WelcomeActivity.class);
                            startActivity(i);
                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.stay);
                        } else {
                            ApplozicClient.getInstance(IntroActivity.this).setContextBasedChat(true).setHandleDial(true).setIPCallEnabled(true);
                        }
                    } else {
                        Intent i = new Intent(IntroActivity.this, WelcomeActivity.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.stay);
                    }
                }, 500);

                if(position==viewpager.getAdapter().getCount()-1){
                    sharedpreference.setInro(IntroActivity.this,true);
                    Intent intent = new Intent(IntroActivity.this, BottombarActivity.class);
                    intent.putExtra("type", "home");
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.stay);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
