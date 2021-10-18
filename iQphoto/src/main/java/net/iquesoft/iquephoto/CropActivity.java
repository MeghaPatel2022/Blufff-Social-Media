package net.iquesoft.iquephoto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.isseiaoki.simplecropview.CropImageView;

import net.iquesoft.iquephoto.core.ImageEditorView;
import net.iquesoft.iquephoto.ui.activities.EditorActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static net.iquesoft.iquephoto.ui.fragments.AdjustFragment.finalBitmap;

public class CropActivity extends MvpAppCompatActivity {

    private static final String IMAGE_DIRECTORY = "/Blufff/temp";

    @BindView(R2.id.cropImageView)
    CropImageView ivImage;
    @BindView(R2.id.free_size)
    Button free_size;
    @BindView(R2.id.square)
    Button square;
    @BindView(R2.id.third_fourth)
    Button third_fourth;
    @BindView(R2.id.foure_third)
    Button foure_third;
    @BindView(R2.id.nine_sixteen)
    Button nine_sixteen;
    @BindView(R2.id.sixteen_nine)
    Button sixteen_nine;
    @BindView(R2.id.rotate_left)
    ImageView rotate_left;
    @BindView(R2.id.rotate_right)
    ImageView rotate_right;
    @BindView(R2.id.done)
    ImageView done;

    Bitmap finalbitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        ButterKnife.bind(CropActivity.this);

//        Bitmap bmp = (Bitmap) getIntent().getParcelableExtra("picture");

        ivImage.setImageBitmap(finalBitmap);

        free_size.setOnClickListener(v -> ivImage.setCropMode(CropImageView.CropMode.FREE));

        square.setOnClickListener(v -> ivImage.setCropMode(CropImageView.CropMode.SQUARE));

        third_fourth.setOnClickListener(v -> ivImage.setCropMode(CropImageView.CropMode.RATIO_3_4));

        foure_third.setOnClickListener(v -> ivImage.setCropMode(CropImageView.CropMode.RATIO_4_3));

        nine_sixteen.setOnClickListener(v -> ivImage.setCropMode(CropImageView.CropMode.RATIO_9_16));

        sixteen_nine.setOnClickListener(v -> ivImage.setCropMode(CropImageView.CropMode.RATIO_16_9));

        rotate_left.setOnClickListener(v -> ivImage.rotateImage(CropImageView.RotateDegrees.ROTATE_90D));

        rotate_right.setOnClickListener(v -> ivImage.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D));

        done.setOnClickListener(v -> {
            finalbitmap = ivImage.getCroppedBitmap();

            SaveImage(finalbitmap);
        });

    }

    private void SaveImage(Bitmap finalBitmap) {

        String filePath = "";
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        File file = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");

        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            filePath = file.getAbsolutePath();
            Intent intent = new Intent(CropActivity.this, EditorActivity.class);
            intent.setData(Uri.fromFile(new File(filePath)));
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
