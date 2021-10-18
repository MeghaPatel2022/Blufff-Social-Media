package net.iquesoft.iquephoto.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.bumptech.glide.Glide;
import com.isseiaoki.simplecropview.CropImageView;
import com.wang.avi.AVLoadingIndicatorView;

import net.iquesoft.iquephoto.R;
import net.iquesoft.iquephoto.R2;
import net.iquesoft.iquephoto.core.EditorListener;
import net.iquesoft.iquephoto.core.ImageEditorView;
import net.iquesoft.iquephoto.models.Filter;
import net.iquesoft.iquephoto.presentation.presenters.activity.EditorActivityPresenter;
import net.iquesoft.iquephoto.presentation.views.activity.EditorActivityView;
import net.iquesoft.iquephoto.presentation.views.fragment.FiltersView;
import net.iquesoft.iquephoto.tasks.ImageSaveTask;
import net.iquesoft.iquephoto.ui.dialogs.LoadingDialog;
import net.iquesoft.iquephoto.ui.fragments.AdjustFragment;
import net.iquesoft.iquephoto.ui.fragments.FiltersFragment;
import net.iquesoft.iquephoto.ui.fragments.ToolsFragment;
import net.iquesoft.iquephoto.ui.fragments.TransparencyFragment;
//import net.iquesoft.iquephoto.utils.ToolbarUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnMultiCompressListener;


public class EditorActivity extends MvpAppCompatActivity implements EditorActivityView {
    @InjectPresenter
    EditorActivityPresenter mPresenter;

    @ProvidePresenter
    EditorActivityPresenter provideEditorPresenter() {
        return new EditorActivityPresenter(this, getIntent());
    }

    @BindView(R2.id.toolbar_editor)
    Toolbar mToolbar;

//    @BindView(R2.id.button_undo)
//    Button mUndoButton;

    @BindView(R2.id.image_editor_view)
    ImageEditorView mImageEditorView;

    @BindView(R2.id.frame_layout_fragment_container)
    FrameLayout mFragmentContainer;

    @BindView(R2.id.frame_layout_fragment_container1)
    FrameLayout frame_layout_fragment_container1;

    @BindView(R2.id.imgdone)
    ImageView imgdone;

    @BindView(R2.id.img_save)
    TextView img_save;

    @BindView(R2.id.cv_main)
    CardView cv_main;

    @BindView(R2.id.button_back)
    ImageView button_back;

    @BindView(R2.id.img_profile)
    CircleImageView img_profile;

    @BindView(R2.id.tv_back)
    TextView tv_back;

    @BindView(R2.id.tv_next)
    TextView tv_next;

    @BindView(R2.id.tv_filter)
    TextView tv_filter;

    @BindView(R2.id.avi)
    AVLoadingIndicatorView avi;

    @BindView(R2.id.frame_layout_fragment_container2)
    FrameLayout frame_layout_fragment_container2;

    private MenuPopupHelper mMenuPopupHelper;

    private FragmentManager mFragmentManager;

    private LoadingDialog mLoadingDialog;

    private static final String IMAGE_DIRECTORY = "/Blufff";

    List<File> compressedImageFiles = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        ButterKnife.bind(this);

        imgdone.setOnClickListener(view -> mImageEditorView.applyChanges());
        setSupportActionBar(mToolbar);

//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            mToolbar.setNavigationIcon(R.drawable.ic_arrow_left_black_24dp);
//        }

        tv_back.setOnClickListener(view -> {
            navigateBack(true);
            frame_layout_fragment_container1.setVisibility(View.GONE);
            frame_layout_fragment_container2.setVisibility(View.GONE);
        });

        tv_next.setOnClickListener(view -> {
            Bitmap bitmap = mImageEditorView.getAlteredImageBitmap();
            mPresenter.save(bitmap);
            //List<Bitmap> imagarray = new ArrayList<>();
            List<File> imagarray = new ArrayList<>();


            //imagarray.add(mImageEditorView.getAlteredImageBitmap());
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs();
            }
            try {
                File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
                f.createNewFile();
                f.getAbsoluteFile();
                imagarray.add(f);
                Log.e("selectedfile", f + "");
                FileOutputStream fo = new FileOutputStream(f);

                Luban.compress(EditorActivity.this,imagarray)
                        .putGear(Luban.CUSTOM_GEAR)
                        .setMaxSize(500)
                        .setMaxHeight(1920)
                        .setMaxWidth(1080)
                        .launch(new OnMultiCompressListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(List<File> fileList) {
                                compressedImageFiles.addAll(fileList);
                                if (!fileList.isEmpty())
                                    Log.e("LLLLLLL_Filie: ",compressedImageFiles.get(0).getAbsolutePath());
//                                for (File image : imagarray) {
//                                    image.delete();
//                                    Log.e("LLLL_selectedfile", image+ "");
//                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("LLLL_selectedfile_error", e.getMessage());
                            }
                        });

                fo.write(bytes.toByteArray());
                fo.close();

                try {
                    Intent myIntent = new Intent(EditorActivity.this, Class.forName("com.TBI.Client.Bluff.Activity.Post.PostCreate"));
                    myIntent.putExtra("imagesrray", (Serializable) imagarray);
                    startActivity(myIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.stay);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        img_save.setOnClickListener(view -> {
//            mImageEditorView.applyChanges();
//            Bitmap bitmap = mImageEditorView.getAlteredImageBitmap();
//            mPresenter.save(bitmap);

            setupFragment2(new AdjustFragment());
            // showSharePopupMenu();
        });

        String imgUrl = getIntent().getStringExtra("imgUrl");

        Glide.with(EditorActivity.this)
                .load(imgUrl)
                .into(img_profile);

        mLoadingDialog = new LoadingDialog(this);

        mImageEditorView.setMvpDelegate(getMvpDelegate());

        mImageEditorView.setUndoListener(new EditorListener() {
            @Override
            public void onTransparencyHandleButtonClicked(Paint paint) {
                setupFragment(TransparencyFragment.newInstance(paint));
            }

            @Override
            public void hasChanges(int count) {
//                if (count != 0) {
//                    mUndoButton.setText(String.valueOf(count));
//                } else {
//                    mUndoButton.setText(String.valueOf(0));
//                    mUndoButton.setVisibility(View.INVISIBLE);
//                }
            }

            @Override
            public void onAppliedImageSaved(Uri uri) {
                // set the image uri from cache for share this image later
                mPresenter.setAlteredImageUri(uri);

                // ToolsFragment.class.getSimpleName();

                // return to ToolsFragment
                mFragmentManager.popBackStack();
            }
        });

//        ToolbarUtil.showTitle(false, this);

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(
                mFragmentContainer.getId(), new ToolsFragment(), ToolsFragment.class.getSimpleName()
        ).commit();

        frame_layout_fragment_container1.setVisibility(View.GONE);
        frame_layout_fragment_container2.setVisibility(View.GONE);
        Log.i("BackStack", String.valueOf(mFragmentManager.getBackStackEntryCount()));

        tv_filter.setOnClickListener(view -> setupFragment(FiltersFragment.newInstance()));
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        if (item.getItemId() == R.id.action_share) {
//
//            Bitmap bitmap = mImageEditorView.getAlteredImageBitmap();
//            mPresenter.save(bitmap);
//            //List<Bitmap> imagarray = new ArrayList<>();
//            List<File> imagarray = new ArrayList<>();
//            //imagarray.add(mImageEditorView.getAlteredImageBitmap());
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
//            if (!wallpaperDirectory.exists()) {
//                wallpaperDirectory.mkdirs();
//            }
//            try {
//                File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
//                f.createNewFile();
//                f.getAbsoluteFile();
//                imagarray.add(f);
//                Log.e("selectedfile", f + "");
//                FileOutputStream fo = new FileOutputStream(f);
//                fo.write(bytes.toByteArray());
//                // MediaScannerConnection.scanFile(ProfilePage.this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
//                fo.close();
//                //  Toasty.success(Admin_Add_Gallery.this, "Image Saved!", Toast.LENGTH_LONG).show();
//            } catch (IOException e) {
//                e.printStackTrace();
//                //    Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//            try {
//                Intent myIntent = new Intent(this, Class.forName("com.TBI.Client.Bluff.Activity.PostCreate"));
//                myIntent.putExtra("imagesrray", (Serializable) imagarray);
//                startActivity(myIntent);
//                overridePendingTransition(R.anim.fade_in, R.anim.stay);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//            // showSharePopupMenu();
//        }
//        if (item.getItemId() == R.id.action_apply) {
//            mImageEditorView.applyChanges();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /*private void showSharePopupMenu() {
        if (mMenuPopupHelper == null) {
            MenuBuilder menuBuilder = new MenuBuilder(EditorActivity.this);
            menuBuilder.setCallback(new MenuBuilder.Callback() {
                @Override
                public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                    Bitmap bitmap = mImageEditorView.getAlteredImageBitmap();

                    if (item.getItemId() == R.id.action_save) {
                        mPresenter.save(bitmap);
                    } else if (item.getItemId() == R.id.action_instagram) {
                        mPresenter.share(INSTAGRAM_PACKAGE_NAME);
                    } else if (item.getItemId() == R.id.action_facebook) {
                        mPresenter.share(FACEBOOK_PACKAGE_NAME);
                    } else if (item.getItemId() == R.id.action_more) {
                        mPresenter.share(null);
                    }
                    return false;
                }

                @Override
                public void onMenuModeChange(MenuBuilder menu) {

                }
            });

            MenuInflater menuInflater = new MenuInflater(this);
            menuInflater.inflate(R.menu.menu_share, menuBuilder);
            mMenuPopupHelper = new MenuPopupHelper(this, menuBuilder, findViewById(R.id.action_share));
            mMenuPopupHelper.setForceShowIcon(true);
            mMenuPopupHelper.show();
        } else {
            mMenuPopupHelper.show();
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() == 0) {
//            mPresenter.onBackPressed(mImageEditorView.getAlteredImageBitmap());
        } else if (mFragmentManager.getBackStackEntryCount() == 1) {
//            mToolbar.setNavigationIcon(R.drawable.ic_arrow_left_black_24dp);
//            ToolbarUtil.showTitle(false, this);
            navigateBack(true);
            frame_layout_fragment_container1.setVisibility(View.GONE);
            frame_layout_fragment_container2.setVisibility(View.GONE);
        } else if (mFragmentManager.getBackStackEntryCount() > 1) {
//            ToolbarUtil.updateSubtitle(null, this);
            navigateBack(true);
        }
    }

    @Override
    public void startEditing(Bitmap bitmap) {
        mImageEditorView.setImageBitmap(bitmap);
    }

    @Override
    public void showLoading() {
        mLoadingDialog.show();
    }

    @Override
    public void hideLoading() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        builder.setMessage(getString(R.string.on_back_alert))
                .setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> finish())
                .setNeutralButton(getString(R.string.save), ((dialogInterface1, i) ->
                        new ImageSaveTask(this, mImageEditorView.getAlteredImageBitmap()).execute())
                )
                .setNegativeButton(getString(R.string.cancel), (dialogInterface, i1) -> dialogInterface.dismiss())
                .show();
    }

    @Override
    public void showApplicationNotExistAlertDialog(@StringRes int messageBody, @NonNull String packageName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        builder.setTitle(getString(R.string.application_does_not_exist));
        builder.setMessage(getString(messageBody));
        builder.setPositiveButton(getString(R.string.install), (dialogInterface, i) -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + packageName)));
            } catch (android.content.ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
            }
        });

        builder.setNegativeButton(getString(R.string.dismiss), (dialogInterface, i1) -> dialogInterface.dismiss());
        builder.show();
    }

    public void setupFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(frame_layout_fragment_container1.getId(), fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();

//          mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
//        ToolbarUtil.showTitl/(true, this);
//        ToolbarUtil.updateSubtitle("", this);
//        mUndoButton.setVisibility(View.INVISIBLE);
        frame_layout_fragment_container1.setVisibility(View.VISIBLE);
        Log.i("BackStack", String.valueOf(mFragmentManager.getBackStackEntryCount()));
    }

    public void setupFragment2(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(frame_layout_fragment_container2.getId(), fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();

//          mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
//        ToolbarUtil.showTitl/(true, this);
//        ToolbarUtil.updateSubtitle("", this);
//        mUndoButton.setVisibility(View.INVISIBLE);
        frame_layout_fragment_container2.setVisibility(View.VISIBLE);
        Log.i("BackStack", String.valueOf(mFragmentManager.getBackStackEntryCount()));
    }


    @Override
    public void showToastMessage(int stringResource) {
        Toast.makeText(getApplicationContext(), getString(stringResource), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateBack(boolean isFragment) {
        if (isFragment) {
            if (mFragmentManager.getBackStackEntryCount() > 1)
                super.onBackPressed();
            else if (mFragmentManager.getBackStackEntryCount() == 0)
                mPresenter.onBackPressed(mImageEditorView.getAlteredImageBitmap());
            else if (mFragmentManager.getBackStackEntryCount() == 1) {
                super.onBackPressed();
            }
        } else finish();
    }

    @Override
    public void share(@NonNull Uri uri, @Nullable String packageName) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        if (packageName != null) {
            intent.setPackage(packageName);
        }

        startActivity(intent);
    }

//    @OnClick(R2.id.button_undo)
//    void onClickUndo() {
//        mImageEditorView.undo();
//    }

}