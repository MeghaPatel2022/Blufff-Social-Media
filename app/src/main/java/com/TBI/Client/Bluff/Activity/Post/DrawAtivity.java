package com.TBI.Client.Bluff.Activity.Post;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.Fragment.Look_Fragment;
import com.TBI.Client.Bluff.GIFEncoder.AnimatedGIFWriter;
import com.TBI.Client.Bluff.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.defaults.colorpicker.ColorPickerPopup;

public class DrawAtivity extends AppCompatActivity {

    private static final String TAG = DrawAtivity.class.getName();

    private static String STORE_DIRECTORY;

    @BindView(R.id.roundedImageView)
    ImageView roundedImageView;
    @BindView(R.id.imgPencil)
    ImageView imgPencil;
    @BindView(R.id.imgcolorplateer)
    ImageView imgcolorplateer;
    @BindView(R.id.ll_myView)
    RelativeLayout ll_myView;
    @BindView(R.id.rl_mainView)
    RelativeLayout rl_mainView;
    @BindView(R.id.imgundo)
    ImageView imgundo;

    String comment = "", come = "";
    int position;
    // My View Class for Draw
    MyView mv;
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();// list of file paths
    boolean isEdited = false;
    private ProgressDialog dialog1;
    private Paint mPaint;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private Handler mHandler;

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(-1);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mv = new MyView(DrawAtivity.this);
        mv.setDrawingCacheEnabled(true);

        setContentView(R.layout.activity_draw_ativity);

        ButterKnife.bind(DrawAtivity.this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(DrawAtivity.this));
        ll_myView.addView(mv);
//        mv.setDrawingCacheEnabled(false);
//        rl_mainView.removeAllViews();
//        rl_mainView.addView(roundedImageView);
//        rl_mainView.addView(ll_myView);

        comment = getIntent().getExtras().getString("comment");
        position = getIntent().getExtras().getInt("position");
        come = getIntent().getExtras().getString("come");

        mHandler = new Handler();

        dialog1 = new ProgressDialog(DrawAtivity.this);
        dialog1.setMessage("Please wait...");

        Toast.makeText(this, "Create up to 10 seconds only", Toast.LENGTH_SHORT).show();

        roundedImageView.setClipToOutline(true);
        Glide
                .with(DrawAtivity.this)
                .load(Look_Fragment.postarray.get(position).getImage())
                .placeholder(R.drawable.grey_placeholder)
                .error(R.drawable.grey_placeholder)
                .dontAnimate()
                .into(roundedImageView);

        imgcolorplateer.setOnClickListener(v -> {
            new ColorPickerPopup.Builder(this)
                    .initialColor(Color.RED) // Set initial color
                    .enableBrightness(true) // Enable brightness slider or not
                    .enableAlpha(true) // Enable alpha slider or not
                    .okTitle("Choose")
                    .cancelTitle("Cancel")
                    .showIndicator(true)
                    .showValue(true)
                    .build()
                    .show(new ColorPickerPopup.ColorPickerObserver() {
                        @Override
                        public void onColorPicked(int color) {
                            colorChanged(color);
                        }

                    });
        });

//        try {
//            URL url = new URL(Look_Fragment.postarray.get(position).getImage());
//            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            Drawable image = new BitmapDrawable(getResources(), bitmap);
//
//            mv.setBackgroundDrawable(image);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        imgundo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshot();
            }
        });

    }

    public ArrayList<String> getFromSdcard() {
        ArrayList<String> filelist = new ArrayList();
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File dir = new File(STORE_DIRECTORY);
        boolean success = true;
        if (!dir.exists()) {
            success = dir.mkdirs();
            if (!success) {
                Log.e("savePicture", dir + " not success");
                success = true;
                dir = new File(root, getResources().getString(R.string.app_name));
                if (!dir.exists()) {
                    success = dir.mkdirs();
                }
            }
        }
        if (success && dir.isDirectory()) {
            File[] listFile = dir.listFiles();

            List<File> directoryListing = new ArrayList<File>();
            directoryListing.addAll(Arrays.asList(listFile));
            Collections.sort(directoryListing);

            Arrays.sort(listFile, new SortFileName());

            for (int i = 0; i < directoryListing.size(); i++) {
                if (directoryListing.get(i).getAbsolutePath().contains(getResources().getString(R.string.app_name))) {
                    filelist.add(directoryListing.get(i).getAbsolutePath());
                    Log.e("LLLLL_files: ", filelist.get(i));
                }
            }
        }

        Log.e("size", String.valueOf(filelist.size()));
        Log.e("size", dir.getAbsolutePath());
        return filelist;
    }

    public void colorChanged(int color) {
        mPaint.setColor(color);
    }

    private void takeScreenshot() {

        try {

            String str = String.valueOf(Calendar.getInstance().getTimeInMillis());

            STORE_DIRECTORY = Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.app_name) + "/" + "Snapsots/";
            File storeDirectory = new File(STORE_DIRECTORY);
            if (!storeDirectory.exists()) {
                boolean success = storeDirectory.mkdirs();
                if (!success) {
                    Log.e(TAG, "failed to create file storage directory.");
                    return;
                }
            }

            String mPath = STORE_DIRECTORY + str + ".jpg";

            View v1 = rl_mainView;
            v1.setDrawingCacheEnabled(true);
//            Bitmap bitmap = getBitmapFromView(v1);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class MyView extends View {

        private static final float MINP = 0.25f;
        private static final float MAXP = 0.75f;
        private static final float TOUCH_TOLERANCE = 4;

        Context context;
        int i = 0;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mBitmapPaint;
        private float mX, mY;

        public MyView(Context c) {
            super(c);
            context = c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(0xFFFF0000);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.MITER);
            mPaint.setStrokeCap(Paint.Cap.BUTT);
            mPaint.setStrokeWidth(15);
            URL url = null;
            try {
                url = new URL(Look_Fragment.postarray.get(position).getImage());
                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                mCanvas.setBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mEmboss = new EmbossMaskFilter(new float[]{1, 1, 1},
                    0.4f, 6, 3.5f);
            mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaint);
        }

        private void touch_start(float x, float y) {
            //showDialog();
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;

        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
            //mPaint.setMaskFilter(null);
        }

//        void startRepeatingTask() {
//            runnable.run();
//        }
//
//        void stopRepeatingTask() {
//            mHandler.removeCallbacks(runnable);
//        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean onTouchEvent(MotionEvent event) {

            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    if (!isEdited) {
//                        new Thread() {
//                            public void run() {
//                                while (i++ < 10000) {
//                                    try {
//                                        runOnUiThread(new Runnable() {
//
//                                            @Override
//                                            public void run() {
//                                               takeScreenshot();
//                                            }
//                                        });
//                                        Thread.sleep(300);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }.start();
//
//                        if (i>=10000){
//
//                        }
//
//                        long startTime = System.currentTimeMillis();
//                        final Handler handler = new Handler();
//
//                        Runnable runnable = new Runnable() {
//                            @Override
//                            public void run() {
//                                takeScreenshot();
//                                if (System.currentTimeMillis() - startTime < 10000){
//                                    handler.postDelayed(this,400);
//                                } else {
//                                    //TODO start second activity
//                                }
//                            }
//                        };
//
//                        handler.postDelayed(() -> {
//                            handler.removeCallbacks(runnable); //stop next runnable execution
//                            new LongOperation().execute();
//                            //TODO start second activity
//                        }, 30000);
                        new CountDownTimer(10000, 400) {

                            @Override
                            public void onFinish() {
//                            stopRepeatingTask();
                                Log.e("LLLLLL_finish: ", "finish...");

                                new LongOperation().execute();
                                Toast.makeText(DrawAtivity.this, "over 10sec", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onTick(long millisUntilFinished) {
                                takeScreenshot();
                            }

                        }.start();
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }

    //sorts based on the files name
    public class SortFileName implements Comparator<File> {
        @Override
        public int compare(File f1, File f2) {
            return f1.getName().compareTo(f2.getName());
        }

        @Override
        public Comparator<File> reversed() {
            return null;
        }
    }

    private class LongOperation extends AsyncTask<Void, Void, String> {

        public LongOperation() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog1.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            runOnUiThread(() -> {
                isEdited = true;
                ArrayList<String> filelist = new ArrayList<>();
                filelist.addAll(getFromSdcard());
                bitmapArrayList.clear();
                Log.e("LLL_Size ", String.valueOf(filelist.size()));

                for (int j = 0; j < filelist.size(); j++) {
                    bitmapArrayList.add(BitmapFactory.decodeFile(filelist.get(j)));
                }

                Log.e("LLL_Size ", String.valueOf(bitmapArrayList.size()));

                AnimatedGIFWriter writer = new AnimatedGIFWriter(true);
                OutputStream os = null;
                try {
                    os = new FileOutputStream(STORE_DIRECTORY + "example.gif");
                    writer.prepareForWrite(os, -1, -1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < bitmapArrayList.size(); i++) {
                    //True for dither. Will need more memory and CPU
                    try {
                        writer.writeFrame(os, bitmapArrayList.get(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    writer.finishWrite(os);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.e("LLLLL", "executed..");

            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            if (dialog1.isShowing())
                dialog1.dismiss();
            // You might want to change "executed" for the returned string
            // passed into onPostExecute(), but that is up to you
        }
    }
}
