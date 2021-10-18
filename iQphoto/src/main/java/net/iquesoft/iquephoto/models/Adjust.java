package net.iquesoft.iquephoto.models;


import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

public class Adjust {
    @StringRes
    private int mTitle;

    @DrawableRes
    private int mIcon;

    private Fragment mFragment;

    public Adjust(@StringRes int title, @DrawableRes int icon, @NonNull Fragment fragment) {
        mTitle = title;
        mIcon = icon;
        mFragment = fragment;
    }

    public int getTitle() {
        return mTitle;
    }

    public int getIcon() {
        return mIcon;
    }

    public Fragment getFragment() {
        return mFragment;
    }
}