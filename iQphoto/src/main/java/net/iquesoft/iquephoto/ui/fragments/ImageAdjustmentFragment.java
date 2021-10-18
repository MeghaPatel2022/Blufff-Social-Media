package net.iquesoft.iquephoto.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import net.iquesoft.iquephoto.R;
import net.iquesoft.iquephoto.R2;
import net.iquesoft.iquephoto.core.ImageEditorView;
import net.iquesoft.iquephoto.core.enums.EditorTool;
import net.iquesoft.iquephoto.presentation.presenters.fragment.ImageAdjustmentPresenter;
import net.iquesoft.iquephoto.presentation.views.fragment.ImageAdjustmentView;
//import net.iquesoft.iquephoto.utils.ToolbarUtil;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ImageAdjustmentFragment extends ToolFragment implements ImageAdjustmentView {
    public static final String ARG_PARAM = "command";

    @InjectPresenter
    ImageAdjustmentPresenter mPresenter;

    @ProvidePresenter
    ImageAdjustmentPresenter provideImageAdjustmentPresenter() {
        return new ImageAdjustmentPresenter(getArguments());
    }

    @BindView(R2.id.minValueTextView)
    TextView mMinValueTextView;

    @BindView(R2.id.currentValueTextView)
    TextView mCurrentValueTextView;

    @BindView(R2.id.maxValueTextView)
    TextView mMaxValueTextView;

    @BindView(R2.id.seek_bar_adjust)
    DiscreteSeekBar mToolSeekBar;

    private Unbinder mUnbinder;

    private ImageEditorView mImageEditorView;

    public static ImageAdjustmentFragment newInstance(EditorTool editorTool) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, editorTool);


        ImageAdjustmentFragment fragment = new ImageAdjustmentFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public ImageAdjustmentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageEditorView =
                (ImageEditorView) getActivity().findViewById(R.id.image_editor_view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slider_control, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        mToolSeekBar.setMax(100);
        mToolSeekBar.setMin(-100);

        mToolSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                mPresenter.progressChanged(value);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void changeToolbarTitle(@StringRes int title) {
//        ToolbarUtil.updateTitle(title, getActivity());
    }

    @Override
    public void changeToolbarSubtitle(@StringRes int subtitle) {
//        ToolbarUtil.updateSubtitle(subtitle, getActivity());
    }

    @Override
    public void onIntensityChanged(int value) {
        Log.i("Adjustment", "Filter intensity = " + value);

        mImageEditorView.setFilterIntensity(value);
    }

    @Override
    public void onBrightnessChanged(int value) {
        mImageEditorView.setBrightnessValue(value);
    }

    @Override
    public void onContrastChanged(int value) {
        mImageEditorView.setContrastValue(value);
    }

    @Override
    public void onSaturationChanged(int value) {
        mImageEditorView.setSaturationValue(value);
    }

    @Override
    public void onWarmthChanged(int value) {
        mImageEditorView.setWarmthValue(value);
    }

    @Override
    public void onStraightenTransformChanged(int value) {
        mImageEditorView.setStraightenTransformValue(value);
    }

    @Override
    public void onVignetteChanged(int value) {
        mImageEditorView.setVignetteIntensity(value);
    }

    @Override
    public void onBlurChanged(int value) {
        mImageEditorView.setImageBitmap(blurfast(mImageEditorView.getAlteredImageBitmap(),value));
    }

    @Override
    public void setEditorTool(EditorTool tool) {
        mImageEditorView.changeTool(tool);
    }

    @Override
    public void setSeekBarValues(int minValue, int maxValue, int value) {
        mToolSeekBar.setMin(minValue);
        mMinValueTextView.setText(String.valueOf(minValue));

        mToolSeekBar.setProgress(value);
        mCurrentValueTextView.setText(String.valueOf(value));

        mToolSeekBar.setMax(maxValue);
        mMaxValueTextView.setText(String.valueOf(maxValue));
    }

    private Bitmap blurfast(Bitmap bmp, int radius) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int[] pix = new int[w * h];
        bmp.getPixels(pix, 0, w, 0, 0, w, h);

        for(int r = radius; r >= 1; r /= 2)
        {
            for(int i = r; i < h - r; i++)
            {
                for(int j = r; j < w - r; j++)
                {
                    int tl = pix[(i - r) * w + j - r];
                    int tr = pix[(i - r) * w + j + r];
                    int tc = pix[(i - r) * w + j];
                    int bl = pix[(i + r) * w + j - r];
                    int br = pix[(i + r) * w + j + r];
                    int bc = pix[(i + r) * w + j];
                    int cl = pix[i * w + j - r];
                    int cr = pix[i * w + j + r];

                    pix[(i * w) + j] = 0xFF000000 |
                            (((tl & 0xFF) + (tr & 0xFF) + (tc & 0xFF) + (bl & 0xFF) +
                                    (br & 0xFF) + (bc & 0xFF) + (cl & 0xFF) + (cr & 0xFF)) >> 3) & 0xFF |
                            (((tl & 0xFF00) + (tr & 0xFF00) + (tc & 0xFF00) + (bl & 0xFF00)
                                    +  (br & 0xFF00) + (bc & 0xFF00) + (cl & 0xFF00) + (cr & 0xFF00)) >> 3) & 0xFF00 |
                            (((tl & 0xFF0000) + (tr & 0xFF0000) + (tc & 0xFF0000) +
                                    (bl & 0xFF0000) + (br & 0xFF0000) + (bc & 0xFF0000) + (cl & 0xFF0000) +
                                    (cr & 0xFF0000)) >> 3) & 0xFF0000;
                }
            }
        }
        Bitmap blurred = Bitmap.createBitmap(w, h, bmp.getConfig());
        blurred.setPixels(pix, 0, w, 0, 0, w, h);
        return blurred;
    }

}