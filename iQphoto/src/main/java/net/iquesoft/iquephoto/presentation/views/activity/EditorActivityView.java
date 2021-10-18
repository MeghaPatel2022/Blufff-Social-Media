package net.iquesoft.iquephoto.presentation.views.activity;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.arellomobile.mvp.MvpView;

public interface EditorActivityView extends MvpView {

    void startEditing(Bitmap bitmap);

    void showLoading();

    void hideLoading();

    void showAlertDialog();
    
    void showApplicationNotExistAlertDialog(@StringRes int messageBody, @NonNull String packageName);

    void showToastMessage(int stringResource);

    void navigateBack(boolean isFragment);

    void share(@NonNull Uri uri, @Nullable String packageName);
}