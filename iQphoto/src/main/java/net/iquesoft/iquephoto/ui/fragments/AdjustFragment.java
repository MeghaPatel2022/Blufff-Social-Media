package net.iquesoft.iquephoto.ui.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.isseiaoki.simplecropview.CropImageView;
import com.wang.avi.AVLoadingIndicatorView;

import net.iquesoft.iquephoto.BlurrFragment;
import net.iquesoft.iquephoto.CropActivity;
import net.iquesoft.iquephoto.R;
import net.iquesoft.iquephoto.R2;
import net.iquesoft.iquephoto.adapters.AdjustAdapter;
import net.iquesoft.iquephoto.core.ImageEditorView;
import net.iquesoft.iquephoto.models.Adjust;
import net.iquesoft.iquephoto.presentation.presenters.fragment.AdjustPresenter;
import net.iquesoft.iquephoto.presentation.views.fragment.AdjustView;
import net.iquesoft.iquephoto.ui.activities.EditorActivity;
//import net.iquesoft.iquephoto.utils.ToolbarUtil;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static net.iquesoft.iquephoto.core.enums.EditorTool.NONE;

public class AdjustFragment extends MvpAppCompatFragment implements AdjustView {
    @InjectPresenter
    AdjustPresenter mPresenter;

    @BindView(R2.id.recycler_view_adjust)
    RecyclerView mRecyclerView;

    private Unbinder mUnbinder;
    ImageEditorView mImageEditorView;
    ImageView imgdone;
    AVLoadingIndicatorView avi;
    public static Bitmap finalBitmap = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mImageEditorView = (ImageEditorView) getActivity().findViewById(R.id.image_editor_view);
        imgdone = (ImageView) getActivity().findViewById(R.id.imgdone);
        avi = (AVLoadingIndicatorView) getActivity().findViewById(R.id.avi);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adjust, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ImageEditorView) getActivity().findViewById(R.id.image_editor_view))
                .changeTool(NONE);
//        ToolbarUtil.updateTitle(R.string.adjust, getActivity());
//        ToolbarUtil.updateSubtitle(null, getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        menu.removeItem(R.id.action_share);
    }

    @Override
    public void setupAdapter(List<Adjust> adjusts) {
        AdjustAdapter adapter = new AdjustAdapter(adjusts);
        adapter.setOnAdjustClickListener(new AdjustAdapter.OnAdjustClickListener() {
            @Override
            public void onAdjustClick(Adjust adjust) {

                if (adjust.getTitle() == R.string.crop){
//                   new AsyncTaskExample().execute();
                    Bitmap bitmap = mImageEditorView.getAlteredImageBitmap();
                    if (bitmap!=null) {
                        finalBitmap = bitmap;
                        Intent intent = new Intent(getActivity(), CropActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }

                else if (adjust.getTitle() == R.string.blur){
                    ((EditorActivity) getActivity())
                            .setupFragment(new BlurrFragment());
                }

                else if (adjust.getTitle() == R.string.stickers){
                    ((EditorActivity) getActivity())
                            .setupFragment(new StickersSetFragment());
                }

                else if (adjust.getTitle() == R.string.rotate) {

//                    ToolbarUtil.updateSubtitle(R.string.rotate, getActivity());
                    /*if (imgdone != null) {
                        imgdone.setVisibility(View.VISIBLE);
                    }*/
                    Log.d("mn13rotation", mImageEditorView.getRotation() + "");
                    if (mImageEditorView.getRotation() == 90) {
                        mImageEditorView.setRotation(180);
                    } else if (mImageEditorView.getRotation() == 180) {
                        mImageEditorView.setRotation(270);
                    } else if (mImageEditorView.getRotation() == 270) {
                        mImageEditorView.setRotation(0);
                    } else {
                        mImageEditorView.setRotation(90);
                        //  mImageEditorView.setRotation(-90);
                    }
                } else {
                    if (imgdone != null) {
                        imgdone.setVisibility(View.VISIBLE);
                    }
                    mPresenter.changeAdjust(adjust);
                }

            }
        });
        // adapter.setOnAdjustClickListener(adjust -> mPresenter.changeAdjust(adjust));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(null, LinearLayout.HORIZONTAL, false));
        mRecyclerView.setAdapter(adapter);
    }

    private class AsyncTaskExample extends AsyncTask<String, String, ByteArrayOutputStream> {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startAnim();
        }
        @Override
        protected ByteArrayOutputStream doInBackground(String... strings) {
            getActivity().runOnUiThread(() -> {
                Bitmap bitmap = mImageEditorView.getAlteredImageBitmap();
                if (bitmap!=null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                }
            });

            return stream;
        }
        @Override
        protected void onPostExecute(ByteArrayOutputStream stream1) {
            super.onPostExecute(stream1);
            stopAnim();
            Intent intent = new Intent(getActivity(), CropActivity.class);
            intent.putExtra("picture", stream.toByteArray());
            startActivity(intent);
        }
    }

    void startAnim(){
        avi.smoothToShow();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        avi.smoothToHide();
        // or avi.smoothToHide();
    }

    @Override
    public void adjustChanged(Fragment fragment) {
        ((EditorActivity) getActivity())
                .setupFragment(fragment);
    }
}