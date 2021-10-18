package com.TBI.Client.Bluff.Activity.Post;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.TBI.Client.Bluff.Activity.Home.OpenCamera1;
import com.TBI.Client.Bluff.Gravityview.GravityView2;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.bumptech.glide.Glide;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Post3dActivity extends AppCompatActivity {

    @BindView(R.id.imgView)
    ImageView imgView;
    @BindView(R.id.img_profile)
    CircleImageView img_profile;
    @BindView(R.id.tv_back)
    TextView tv_back;

    Uri mUri;
    Bitmap mBitmap;

    GravityView2 gravityView;
    private boolean etSupported = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post3d);

        ButterKnife.bind(Post3dActivity.this);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Glide.with(Post3dActivity.this)
                .load(sharedpreference.getphoto(Post3dActivity.this))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(img_profile);

        try {

            mUri = getIntent().getData();

            if (!mUri.equals(null)) {
                mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
            }

        } catch (IOException e) {
            Log.e("LLLLL_Error_Uri:", e.getMessage());
            e.printStackTrace();
        }

        gravityView = GravityView2.getInstance(Post3dActivity.this);
        etSupported = gravityView.deviceSupported();

        if (etSupported) {
            gravityView.setImage(imgView, mBitmap).center();
        } else {
            Glide
                    .with(getApplicationContext())
                    .load(mBitmap)
                    .asBitmap()
                    .into(imgView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        gravityView.registerListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gravityView.unRegisterListener();
    }
}
