package net.iquesoft.iquephoto.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.iquesoft.iquephoto.R;
import net.iquesoft.iquephoto.R2;
import net.iquesoft.iquephoto.adapters.ColorAdapter;
import net.iquesoft.iquephoto.models.EditorColor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ColorPickerDialog extends Dialog {
    private EditorColor mColor;
    private Context mContext;

    //private RGBColorPickerDialog mRGBColorPickerDialog;

    private OnColorClickListener mOnColorClickListener;

    public interface OnColorClickListener {
        void onClick(EditorColor editorColor);
    }

    private ColorAdapter mAdapter;

    @BindView(R2.id.colorRecyclerView)
    RecyclerView recyclerView;

    public ColorPickerDialog(Context context) {
        super(context);
        mContext = context;

        //mRGBColorPickerDialog = new RGBColorPickerDialog(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_color_picker);

        ButterKnife.bind(this);

        mAdapter = new ColorAdapter(EditorColor.getColorsList());
        mAdapter.setOnColorClickListener(editorColor ->
                mColor = editorColor
        );

        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerView.setAdapter(mAdapter);
    }

    @OnClick(R2.id.applyColor)
    void onClickApply() {
        Log.d("mn13selectedcolor", mColor.toString());
        mOnColorClickListener.onClick(mColor);
        dismiss();
    }

    @OnClick(R2.id.cancelColor)
    void onClickCancel() {
        dismiss();
    }

    public void setOnColorClickListener(OnColorClickListener onColorClickListener) {
        mOnColorClickListener = onColorClickListener;

        //mRGBColorPickerDialog.setOnColorClickListener(onColorClickListener);
    }

    @OnClick(R2.id.rgbButton)
    void onClickRGB() {
        dismiss();
        // mRGBColorPickerDialog.show();
    }

    @OnClick(R2.id.argbButton)
    void onClikcARGB() {
        dismiss();
    }

}
