package net.iquesoft.iquephoto.models;


import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

public class Tool {
    @StringRes
    private int mTitle;

    @DrawableRes
    private int mIcon;

    private Fragment mFragment;

    public Tool(@StringRes int title, @DrawableRes int icon, @NonNull Fragment fragment) {
        mTitle = title;
        mIcon = icon;
        mFragment = fragment;
    }

    public int getIcon() {
        return mIcon;
    }

    public int getTitle() {
        return mTitle;
    }

    public Fragment getFragment() {
        return mFragment;
    }
}