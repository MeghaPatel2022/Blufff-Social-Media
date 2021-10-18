package com.TBI.Client.Bluff.Activity.Mains;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.TBI.Client.Bluff.Activity.Home.OpenCamera1;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.UserPages.IntroActivity;
import com.TBI.Client.Bluff.UserPages.WelcomeActivity;
import com.TBI.Client.Bluff.Utils.sharedpreference;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(
                () -> {
                    if (sharedpreference.getInro(SplashActivity.this)) {
                        Intent intent = new Intent(SplashActivity.this, BottombarActivity.class);
                        intent.putExtra("type", "home");
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.stay);
                        finish();
                    }else {
                        Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
                        intent.putExtra("type", "home");
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.stay);
                        finish();
                    }

                }, SPLASH_TIME);
    }
}
