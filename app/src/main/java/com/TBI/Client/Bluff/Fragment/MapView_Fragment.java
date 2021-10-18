package com.TBI.Client.Bluff.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.CellLocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.TBI.Client.Bluff.Activity.Home.OpenCamera1;
import com.TBI.Client.Bluff.Model.SOS.NearBy;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Activity.Map.SOSActivity;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MapView_Fragment extends Fragment implements OnMapReadyCallback, LocationListener {

    public LocationManager mLocManager;
    @BindView(R.id.mapviews)
    MapView mapView;
    @BindView(R.id.img_sos)
    ImageView img_sos;
    CircleImageView profile_image;
    GoogleMap map;
    Bitmap myBitmap;
    View mCustomMarkerView;
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);
        ButterKnife.bind(this, view);

        Log.e("LLLL", "...");
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        OpenCamera1.getFrendsData();
        img_sos.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SOSActivity.class);
            startActivity(intent);
        });

        mLocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
            }
        }
        mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                this);

        mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                0, this);

        locationUpdate();

        mCustomMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        profile_image = mCustomMarkerView.findViewById(R.id.profile_image);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(false);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        for (int i = 0; i < Public_Function.getCloseFrends.size(); i++) {
            if (!Public_Function.getCloseFrends.get(i).getLat().isEmpty()) {
                addCustomMarkerFromURL(new LatLng(Double.parseDouble(Public_Function.getCloseFrends.get(i).getLat()), Double.parseDouble(Public_Function.getCloseFrends.get(i).getLongt())), Public_Function.getCloseFrends.get(i).getPhoto());
            } else {

            }
        }
    }

    private void locationUpdate() {
        CellLocation.requestLocationUpdate();
    }

    private Bitmap getMarkerBitmapFromView(View view, Bitmap bitmap) {

        profile_image.setImageBitmap(bitmap);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;

    }

    private void addCustomMarkerFromURL(LatLng point, String ImageUrl) {

        if (map == null) {
            return;
        }
        // adding a marker with image from URL using glide image loading library
        if (!ImageUrl.equals("")) {
            Glide.with(getActivity())
                    .load(ImageUrl).asBitmap().fitCenter()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {

                            map.addMarker(new MarkerOptions().position(point)
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, bitmap))));

                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 30f));

                        }
                    });
        } else {
            map.addMarker(new MarkerOptions().position(point));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 30f));

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
            for (int i = 0; i < Public_Function.getCloseFrends.size(); i++) {
                if (!Public_Function.getCloseFrends.get(i).getLat().isEmpty()) {
                    addCustomMarkerFromURL(new LatLng(Double.parseDouble(Public_Function.getCloseFrends.get(i).getLat()), Double.parseDouble(Public_Function.getCloseFrends.get(i).getLongt())), Public_Function.getCloseFrends.get(i).getPhoto());
                }
            }
        }
        if (sharedpreference.getTheme(getActivity()).equalsIgnoreCase("white")) {
            getActivity().getTheme().applyStyle(R.style.ActivityTheme_Primary_Base_Light, true);
        } else {
            getActivity().getTheme().applyStyle(R.style.ActivityTheme_Primary_Base_Dark, true);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("LLL_current: ", location.getLatitude() + "      " + location.getLongitude());
        addCustomMarkerFromURL(new LatLng(location.getLatitude(), location.getLongitude()), "");
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
        Toast.makeText(getActivity(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }
}
