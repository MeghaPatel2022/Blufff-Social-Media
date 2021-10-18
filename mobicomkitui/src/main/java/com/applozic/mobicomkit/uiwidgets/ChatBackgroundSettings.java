package com.applozic.mobicomkit.uiwidgets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.applozic.mobicomkit.uiwidgets.Pref.SharedPref;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ChannelCreateActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import top.defaults.colorpicker.ColorObserver;
import top.defaults.colorpicker.ColorPickerPopup;

public class ChatBackgroundSettings extends AppCompatActivity implements ColorObserver {

    private RelativeLayout rl_chat_background;
    private RelativeLayout rl_solid_color;
    private RelativeLayout rl_reset_wallpaper;
    private ImageView img_back;

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_background_settings);

        rl_chat_background = findViewById(R.id.rl_chat_background);
        rl_solid_color = findViewById(R.id.rl_solid_color);
        rl_reset_wallpaper = findViewById(R.id.rl_reset_wallpaper);
        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rl_chat_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        rl_solid_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorPickerPopup.Builder(ChatBackgroundSettings.this)
                        .initialColor(Color.RED) // Set initial color
                        .enableBrightness(true) // Enable brightness slider or not
                        .enableAlpha(true) // Enable alpha slider or not
                        .okTitle("Choose")
                        .cancelTitle("Cancel")
                        .showIndicator(true)
                        .onlyUpdateOnTouchEventUp(true)
                        .showValue(true)
                        .build()
                        .show(v, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                SharedPref.setWallpaperColor(ChatBackgroundSettings.this,color);
                                SharedPref.setWallpaper(ChatBackgroundSettings.this,"");
                            }

                        });
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {

            if (resultCode == RESULT_OK && data != null) {
                if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {

                    Uri imageUri = data.getData();
                    try {
                        Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        if (bitmap!=null)
                            createDirectoryAndSaveFile(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void createDirectoryAndSaveFile(Bitmap imageToSave) {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
        File createDir = new File(root+"Blufff/Blufff Wallpaper"+File.separator);
        if(!createDir.exists()) {
            createDir.mkdir();
        }

        String fileName = "Blufff_"+Calendar.getInstance().getTimeInMillis();
        Log.e("LLLL_Current_time: ",fileName);

        File file = new File(createDir, fileName);
        if (file.exists()) {
            file.delete();
        }
        Log.e("LLL_filepath: ",file.getAbsolutePath());
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            SharedPref.setWallpaper(ChatBackgroundSettings.this,file.getAbsolutePath());
            SharedPref.setWallpaperColor(ChatBackgroundSettings.this,0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onColor(int color, boolean fromUser, boolean shouldPropagate) {

        SharedPref.setWallpaperColor(ChatBackgroundSettings.this,color);
        SharedPref.setWallpaper(ChatBackgroundSettings.this,"");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
