package com.TBI.Client.Bluff.Activity.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.TBI.Client.Bluff.Activity.Mains.BottombarActivity;
import com.TBI.Client.Bluff.Activity.Post.Locationsearch;
import com.TBI.Client.Bluff.Adapter.Profile.Notifications.SOSAdapter;
import com.TBI.Client.Bluff.Model.SOS.SOSDetail;
import com.TBI.Client.Bluff.Model.SOS.SOSEx;
import com.TBI.Client.Bluff.Model.SOS.SOSList;
import com.TBI.Client.Bluff.Model.SearchHashtag.SearchHashtag;
import com.TBI.Client.Bluff.Model.getNewNotification.So;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class SOSReport extends AppCompatActivity implements OnMapReadyCallback {

    public LocationManager mLocManager;
    @BindView(R.id.imgprofile1)
    CircleImageView imgprofile1;
    @BindView(R.id.txtname)
    TextView txtname;
    @BindView(R.id.mapviews)
    MapView mapView;
    @BindView(R.id.textlocation)
    TextView textlocation;
    @BindView(R.id.rv_sosDetails)
    RecyclerView rv_sosDetails;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.img_send)
    ImageView img_send;
    @BindView(R.id.tv_open)
    TextView tv_open;

    CircleImageView profile_image;
    GoogleMap map;
    So so1;
    private int position = 0;
    ArrayList<SOSDetail> sosDetails = new ArrayList<>();
    ArrayList<SOSList> sosExes = new ArrayList<>();

    private SOSDetailsAdapter sosDetailsAdapter;
    private String getAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_o_s_report);
        ButterKnife.bind(SOSReport.this);

        AndroidNetworking.initialize(SOSReport.this);
        position = getIntent().getIntExtra("position", 0);

        so1 = Public_Function.sosList.get(position);

        Log.e("LLLLLL_USerID: ", String.valueOf(so1.getUserId()));

        img_back.setOnClickListener(v -> onBackPressed());

        rv_sosDetails.setLayoutManager(new LinearLayoutManager(SOSReport.this,RecyclerView.VERTICAL,false));
        sosDetailsAdapter = new SOSDetailsAdapter(sosExes);
        rv_sosDetails.setAdapter(sosDetailsAdapter);

        if (Public_Function.isInternetConnected(SOSReport.this)){
            Public_Function.Show_Progressdialog(SOSReport.this);
            getSOSDetails();
        }

        img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Public_Function.isInternetConnected(SOSReport.this)){
                    sendSOSReport();
                } else {
                    Toasty.error(SOSReport.this,"Please check your internet connection..").show();
                }
            }
        });

        Glide
                .with(SOSReport.this)
                .load(so1.getPhoto())
                .error(R.drawable.placeholder)
                .into(imgprofile1);

        txtname.setText(so1.getFullName());

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        getAddress(Double.parseDouble(so1.getLat()), Double.parseDouble(so1.getLongt()));
        getAddress1(Double.parseDouble(so1.getLat()), Double.parseDouble(so1.getLongt()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
            }
        }

        tv_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BottombarActivity.class);
                intent.putExtra("type", "map");
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(false);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(so1.getLat()), Double.parseDouble(so1.getLongt()))));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(so1.getLat()), Double.parseDouble(so1.getLongt())), 90f));

    }

    @Override
    protected void onPause() {
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
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
            Log.e("LLLLL_MArker: ", so1.getLat());
            Log.e("LLLLL_MArker: ", so1.getLongt());
        }
        if (sharedpreference.getTheme(SOSReport.this).equalsIgnoreCase("white")) {
            getTheme().applyStyle(R.style.ActivityTheme_Primary_Base_Light, true);
        } else {
            getTheme().applyStyle(R.style.ActivityTheme_Primary_Base_Dark, true);
        }
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(SOSReport.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            textlocation.setText(obj.getSubAdminArea() + ", " + obj.getAdminArea());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(SOSReport.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String getAddress1(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(SOSReport.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getAddressLine(0));
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        getAddress = result.toString();
        Log.d("mn13location:", result.toString());

        return result.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.fade_out);
    }

    static class SOSDetailsAdapter extends RecyclerView.Adapter<SOSDetailsAdapter.MyClassView>{

        List<SOSList> sosList1;

        public SOSDetailsAdapter(List<SOSList> sosList1) {
            this.sosList1 = sosList1;
        }

        @NonNull
        @Override
        public SOSDetailsAdapter.MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sos_report,parent,false);
            return new MyClassView(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SOSDetailsAdapter.MyClassView holder, int position) {
            SOSList sosList = sosList1.get(position);

            holder.tv_name.setText(sosList.getFullName());
            holder.tv_date.setText(sosList.getDate());
            holder.tv_time.setText(sosList.getTime());
        }

        @Override
        public int getItemCount() {
            Log.e("LLLL_Size: ", String.valueOf(sosList1.size()));
            return sosList1.size();
        }

        public void addAll(List<SOSList> sosList2){
            sosList1.clear();
            sosList1.addAll(sosList2);
            notifyDataSetChanged();
        }

        public class MyClassView extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_name)
            TextView tv_name;
            @BindView(R.id.tv_date)
            TextView tv_date;
            @BindView(R.id.tv_time)
            TextView tv_time;

            public MyClassView(@NonNull View itemView) {
                super(itemView);

                ButterKnife.bind(this,itemView);
            }
        }
    }

    private void getSOSDetails(){
        AndroidNetworking.post(geturl.BASE_URL + "get_notification_sos_detail")
                .addHeaders("Authorization", sharedpreference.getUserToken(SOSReport.this))
                .addBodyParameter("sos_id", String.valueOf(so1.getId()))
                .addBodyParameter("other_user_id", String.valueOf(so1.getUserId()))
                .addBodyParameter("user_id", sharedpreference.getUserId(SOSReport.this))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Public_Function.Hide_ProgressDialogue();
                        try {
                            if (response.getBoolean("success")){
                               SOSEx sosEx = new Gson().fromJson(response.toString(), SOSEx.class);
                               sosDetails.addAll(sosEx.getData());
                               sosExes.addAll(sosEx.getList());
                               sosDetailsAdapter.addAll(sosEx.getList());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLLL_SOS__REPORT: ",anError.getMessage());
                    }
                });
    }

    private void sendSOSReport(){
        AndroidNetworking.post(geturl.BASE_URL + "send_notification_sos")
                .addHeaders("Authorization", sharedpreference.getUserToken(SOSReport.this))
                .addBodyParameter("user_id", sharedpreference.getUserId(SOSReport.this))
                .addBodyParameter("other_user_id", String.valueOf(so1.getUserId()))
                .addBodyParameter("lat", so1.getLat())
                .addBodyParameter("long", so1.getLongt())
                .addBodyParameter("address", getAddress)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")){
                                onBackPressed();
                                Toasty.info(SOSReport.this,response.getString("message")).show();
                            }
                        } catch (JSONException e) {
                            Log.e("LLLLL_SendReport_Ex: ",e.getMessage());
                            Toasty.info(SOSReport.this,"Oops something went wrong...").show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLLLL_SendReport_Ex: ",anError.getMessage());
                        Toasty.info(SOSReport.this,"Oops something went wrong...").show();
                    }
                });
    }
}
