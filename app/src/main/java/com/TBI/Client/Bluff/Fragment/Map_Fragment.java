package com.TBI.Client.Bluff.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.TBI.Client.Bluff.Adapter.Map.MapViewrPagerAdapter;
import com.TBI.Client.Bluff.Activity.Mains.BottombarActivity;
import com.TBI.Client.Bluff.Model.GetFriends.GetCloseFrend;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.TBI.Client.Bluff.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Map_Fragment extends Fragment implements LocationListener {

    public static int TAB_SELECT = -1;
    static ArrayList<GetCloseFrend> getClose_Frends = new ArrayList<>();
    private static TabLayout tabmap;
    private static FragmentActivity activity;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.viewpagermap)
    ViewPager viewpagermap;
    Fragment fragment;
    private LocationManager mLocManager;
    private MapViewrPagerAdapter mapViewrPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);

        tabmap = view.findViewById(R.id.tabmap);
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();

        activity = getActivity();

        mLocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
            }
        }
        Objects.requireNonNull(mLocManager).requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                this);

        mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                0, this);

        locationUpdate();

        Log.e("LLLLLL", "...");

        mapViewrPagerAdapter = new MapViewrPagerAdapter(getActivity().getSupportFragmentManager(), getActivity());
        viewpagermap.setAdapter(mapViewrPagerAdapter);

        tabmap.setupWithViewPager(viewpagermap);

        for (int i = 0; i < tabmap.getTabCount(); i++) {
            TabLayout.Tab tab = tabmap.getTabAt(i);
            Objects.requireNonNull(tab).setCustomView(mapViewrPagerAdapter.getTabView(i));
        }

        Objects.requireNonNull(tabmap.getTabAt(0)).select();
        tabmap.setTabIndicatorFullWidth(false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && viewpagermap.getCurrentItem() == 1) {
                Objects.requireNonNull(viewpagermap.getAdapter()).notifyDataSetChanged();

                for (int i = 0; i < tabmap.getTabCount(); i++) {
                    TabLayout.Tab tab = tabmap.getTabAt(i);
                    Objects.requireNonNull(tab).setCustomView(mapViewrPagerAdapter.getTabView(i));
                }
                Objects.requireNonNull(tabmap.getTabAt(0)).select();
                tabmap.setTabIndicatorFullWidth(false);

                viewpagermap.setCurrentItem(0, true);
                return true;
            } else
                return false;

        });

        tabmap.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView tv = Objects.requireNonNull(tab.getCustomView()).findViewById(R.id.tv_tab);
                if (tab.getPosition() == 1) {
                    img_back.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getActivity()).getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                    tv.setBackground(getResources().getDrawable(R.drawable.tab_select));
                    tv.setTextColor(Color.parseColor("#000000"));
                } else {
                    img_back.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getActivity()).getApplicationContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                    tv.setBackground(getResources().getDrawable(R.drawable.map_tab_select));
                    tv.setTextColor(Color.parseColor("#ffffff"));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView tv = Objects.requireNonNull(tab.getCustomView()).findViewById(R.id.tv_tab);
                if (tab.getPosition() == 1) {
                    tv.setBackground(getResources().getDrawable(R.drawable.tab_unselect));
                    tv.setTextColor(Color.parseColor("#000000"));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        img_back.setOnClickListener(v -> {
            if (viewpagermap.getCurrentItem() == 1) {
//                viewpagermap.getAdapter().notifyDataSetChanged();

                viewpagermap.setCurrentItem(0, true);
            } else {
                Intent intent = new Intent(getActivity(), BottombarActivity.class);
                intent.putExtra("type", "home");
                startActivity(intent);
            }
        });

        if (sharedpreference.getTheme(Objects.requireNonNull(getContext())).equalsIgnoreCase("white")) {
            img_back.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        } else {
            if (viewpagermap.getCurrentItem() == 0) {
                Log.e("LLL_current_item: ", String.valueOf(viewpagermap.getCurrentItem()));
                img_back.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            } else {
                img_back.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            }
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void locationUpdate() {
        CellLocation.requestLocationUpdate();
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);

            Public_Function.current_add = add;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Public_Function.current_lat = latitude;
        Public_Function.current_long = longitude;

        getAddress(latitude, longitude);
        if (location != null) {
            mLocManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (status ==
                LocationProvider.TEMPORARILY_UNAVAILABLE) {
            Toast.makeText(getActivity(),
                    "LocationProvider.TEMPORARILY_UNAVAILABLE",
                    Toast.LENGTH_SHORT).show();
        } else if (status == LocationProvider.OUT_OF_SERVICE) {
            Toast.makeText(getActivity(),
                    "LocationProvider.OUT_OF_SERVICE", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
        Toast.makeText(getActivity(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

}
