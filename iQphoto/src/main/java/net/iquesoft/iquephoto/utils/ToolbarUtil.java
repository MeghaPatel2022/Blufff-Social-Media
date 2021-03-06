package net.iquesoft.iquephoto.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentActivity;

import net.iquesoft.iquephoto.ui.activities.EditorActivity;

public class ToolbarUtil {
    public static void showTitle(boolean showTitle, @NonNull FragmentActivity fragmentActivity) {
        EditorActivity editorActivity = (EditorActivity) fragmentActivity;
        if (editorActivity.getSupportActionBar() != null) {
            editorActivity.getSupportActionBar().setDisplayShowTitleEnabled(showTitle);
        }
    }

    public static void updateTitle(@StringRes int title,
                                   @NonNull FragmentActivity fragmentActivity) {
        EditorActivity editorActivity = (EditorActivity) fragmentActivity;
        if (editorActivity.getSupportActionBar() != null) {
            editorActivity.getSupportActionBar().setTitle(title);
        }
    }

    public static void updateSubtitle(@StringRes int subtitle,
                                      @NonNull FragmentActivity fragmentActivity) {
        EditorActivity editorActivity = (EditorActivity) fragmentActivity;
        if (editorActivity.getSupportActionBar() != null) {
            editorActivity.getSupportActionBar().setSubtitle(subtitle);
        }
    }

    public static void updateSubtitle(@Nullable String subtitle,
                                      @NonNull FragmentActivity fragmentActivity) {
        EditorActivity editorActivity = (EditorActivity) fragmentActivity;
        if (editorActivity.getSupportActionBar() != null) {
            editorActivity.getSupportActionBar().setSubtitle(subtitle);
        }
    }
}