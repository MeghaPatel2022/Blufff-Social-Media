package net.iquesoft.iquephoto.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.StringRes;

import com.arellomobile.mvp.presenter.InjectPresenter;

import net.iquesoft.iquephoto.R;
import net.iquesoft.iquephoto.R2;
import net.iquesoft.iquephoto.core.ImageEditorView;
import net.iquesoft.iquephoto.models.EditorColor;
import net.iquesoft.iquephoto.models.Text;
import net.iquesoft.iquephoto.presentation.presenters.fragment.AddTextPresenter;
import net.iquesoft.iquephoto.presentation.views.fragment.AddTextView;
import net.iquesoft.iquephoto.ui.dialogs.ColorPickerDialog;
import net.iquesoft.iquephoto.ui.dialogs.FontPickerDialog;
//import net.iquesoft.iquephoto.utils.ToolbarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static net.iquesoft.iquephoto.core.enums.EditorTool.TEXT;

public class TextFragment extends ToolFragment implements AddTextView, ColorPickerDialog.OnColorClickListener, FontPickerDialog.OnFontClickListener {
    @InjectPresenter
    AddTextPresenter mPresenter;

    @BindView(R2.id.edit_text)
    EditText mEditText;

    private Context mContext;

    private int mColor;

    private Typeface mTypeface = Typeface.DEFAULT;

    private Unbinder mUnbinder;

    private FontPickerDialog mFontPickerDialog;
    private ColorPickerDialog colorPickerDialog;
    private ImageEditorView mImageEditorView;

    public static TextFragment newInstance() {
        return new TextFragment();
    }

    private ColorPickerDialog.OnColorClickListener mOnColorClickListener;
    private FontPickerDialog.OnFontClickListener mFontclikListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageEditorView = (ImageEditorView) getActivity().findViewById(R.id.image_editor_view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        mColor = new EditorColor(R.color.white).getColor();
        //getContext().getResources().getColor(R.color.black);
        mContext = view.getContext();
        mFontPickerDialog = new FontPickerDialog(mContext);
        colorPickerDialog = new ColorPickerDialog(mContext);
        mOnColorClickListener = new ColorPickerDialog.OnColorClickListener() {
            @Override
            public void onClick(EditorColor editorColor) {

                Log.d("mn31", editorColor.toString());

                mColor = editorColor.getColor();
                mEditText.setTextColor(getResources().getColor(editorColor.getColor()));


            }
        };

        mFontclikListener = new FontPickerDialog.OnFontClickListener() {
            @Override
            public void onClick(Typeface typeface) {

                Log.d("mn13typeface,", typeface + "12");
                mTypeface = typeface;
                mEditText.setTypeface(typeface);

            }
        };
        colorPickerDialog.setOnColorClickListener(mOnColorClickListener);
        mFontPickerDialog.setOnFontClickListener(mFontclikListener);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageEditorView.changeTool(TEXT);
//        ToolbarUtil.updateTitle(R.string.text, getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void addText(Text text) {
        mImageEditorView.addText(text, getActivity());
    }

    @Override
    public void onTextColorChanged(@ColorInt int color) {

    }

    @Override
    public void showToastMessage(@StringRes int messageText) {
        Toast.makeText(mContext, getResources().getString(messageText), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R2.id.addTextButton)
    void onClickAddText() {

        Log.d("mn13color", mColor + "");
        mPresenter.addText(mEditText.getText().toString(), mTypeface, mColor);
        mEditText.setText("");

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        if (view == null) {
            view = new View(getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @OnClick(R2.id.selectTextColorButton)
    void onClickTextColorButton() {
        colorPickerDialog.show();
        //((EditorActivity) getActivity()).setupFragment(ColorsFragment.newInstance());
    }

    @OnClick(R2.id.selectFontButton)
    void onClickTextButton() {
        mFontPickerDialog.show();
    }

    @Override
    public void onClick(EditorColor editorColor) {
        mColor = editorColor.getColor();
        mEditText.setTextColor(editorColor.getColor());
        //mPresenter.changeTextColor(getContext(), editorColor);
    }

    @Override
    public void onClick(Typeface typeface) {
        Log.d("mn13typeface,", typeface + "12");
        mTypeface = typeface;
        mEditText.setTypeface(typeface);
    }
}