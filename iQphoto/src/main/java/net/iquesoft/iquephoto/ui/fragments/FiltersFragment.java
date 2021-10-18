package net.iquesoft.iquephoto.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import net.iquesoft.iquephoto.Interface.OnSwipeTouchListener;
import net.iquesoft.iquephoto.R;
import net.iquesoft.iquephoto.R2;
import net.iquesoft.iquephoto.adapters.FiltersAdapter;
import net.iquesoft.iquephoto.core.ImageEditorView;
import net.iquesoft.iquephoto.models.Filter;
import net.iquesoft.iquephoto.presentation.presenters.fragment.FiltersPresenter;
import net.iquesoft.iquephoto.presentation.views.fragment.FiltersView;
import net.iquesoft.iquephoto.ui.activities.EditorActivity;
import net.iquesoft.iquephoto.utils.Confi;
//import net.iquesoft.iquephoto.utils.ToolbarUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static net.iquesoft.iquephoto.core.enums.EditorTool.FILTERS;

public class FiltersFragment extends ToolFragment implements FiltersView {
    @InjectPresenter
    FiltersPresenter mPresenter;

    @ProvidePresenter
    FiltersPresenter provideFilterPresenter() {
        return new FiltersPresenter(
                getContext(), mImageEditorView.getAlteredImageBitmap()
        );
    }

    @BindView(R2.id.recycler_view_filters)
    RecyclerView mFiltersRecyclerView;

    private Unbinder mUnbinder;

    private ImageEditorView mImageEditorView;
    private CardView cv_main;

    public static FiltersFragment newInstance() {
        return new FiltersFragment();
    }

    public FiltersFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mImageEditorView = (ImageEditorView) getActivity().findViewById(R.id.image_editor_view);
        cv_main = (CardView) getActivity().findViewById(R.id.cv_main);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filters, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
//        ToolbarUtil.updateTitle(R.string.filters, getActivity());
        mImageEditorView.changeTool(FILTERS);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void setupFiltersAdapter(Uri uri, List<Filter> filters) {
        FiltersAdapter adapter = new FiltersAdapter(uri, filters);

        cv_main.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeLeft() {

                if (filters.size() < (Confi.pos + 2)) {
                    Log.e("LLL_pos: ", String.valueOf(Confi.pos));
                    Log.e("LLL_filtersize: ", String.valueOf(filters.size()));
                } else{
                    Confi.pos = Confi.pos + 1;
                    mPresenter.changeFilter(filters.get(Confi.pos));
//                    adapter.notifyDataSetChanged();
                    Log.e("LLL_Clik_data: ", String.valueOf(Confi.pos));
                }
            }

            @Override
            public void onSwipeRight() {
                if (Confi.pos != 0) {
                    Confi.pos = Confi.pos - 1;
                    mPresenter.changeFilter(filters.get(Confi.pos));
//                    adapter.notifyDataSetChanged();
                    Log.e("LLL_Clik_data: ", String.valueOf(Confi.pos));
                }
            }
        });

        adapter.setFiltersListener(
                new FiltersAdapter.OnFilterClickListener() {
                    @Override
                    public void onFilterClicked(Filter filter) {
                        mPresenter.changeFilter(filter);
//                        mImageEditorView.applyChanges();
                    }

                    @Override
                    public void onIntensityClicked() {
                        // TODO: Filter intensity.
                        //mPresenter.changeFilterIntensity(mImageEditorView.get);
                    }
                }
        );

        mFiltersRecyclerView.setLayoutManager(new LinearLayoutManager(null, RecyclerView.HORIZONTAL, false));
        mFiltersRecyclerView.setAdapter(adapter);
    }

    @Override
    public void filterChanged(ColorMatrix colorMatrix) {
        mImageEditorView.setFilter(colorMatrix);
    }

    @Override
    public void onChangeFilterIntensityClicked(Fragment fragment) {
        ((EditorActivity) getActivity()).setupFragment(fragment);
    }
}