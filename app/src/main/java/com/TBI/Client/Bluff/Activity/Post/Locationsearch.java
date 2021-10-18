package com.TBI.Client.Bluff.Activity.Post;

import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.TBI.Client.Bluff.Adapter.WallPage.Search_location_Adapter;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.Model.GetLatLong.GetLatlng;
import com.TBI.Client.Bluff.Model.Search_location.Prediction;
import com.TBI.Client.Bluff.Model.Search_location.SearchLocation;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Locationsearch extends AppCompatActivity {

    @BindView(R.id.lstlocation)
    ListView lstlocation;
    @BindView(R.id.txtempty)
    TextView txtempty;
    @BindView(R.id.txtcancel)
    TextView txtcancel;
    @BindView(R.id.searchlocatoin)
    SearchView searchlocatoin;

    ConnectionDetector cd;
    boolean isInternetPresent = false;
    PlacesTask placesTask;
    List<Prediction> getlocation = new ArrayList<>();
    Search_location_Adapter search_location_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (sharedpreference.getTheme(Locationsearch.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationsearch);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(Locationsearch.this));

        EditText searchEditText = searchlocatoin.findViewById(androidx.appcompat.R.id.search_src_text);
        Typeface myFont = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            myFont = getResources().getFont(R.font.poppins_semibold);
            searchEditText.setTypeface(myFont);
            searchEditText.setTextSize(14);
            searchEditText.setTextColor(Locationsearch.this.getResources().getColor(R.color.blacklight));
            searchEditText.setHintTextColor(Locationsearch.this.getResources().getColor(R.color.darkgrey));
            //searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(RM.dimen.fab_margin));
        }

        txtcancel.setOnClickListener(view -> {
            finish();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_out);
        });

        searchlocatoin.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                cd = new ConnectionDetector(Locationsearch.this);
                isInternetPresent = cd.isConnectingToInternet();
                if (!newText.equals("")) {
                    if (!isInternetPresent) {
                    } else {
                        placesTask = new PlacesTask();
                        placesTask.execute(newText);
                    }
                    lstlocation.setEmptyView(txtempty);
                } else {

                    lstlocation.setEmptyView(null);
                }

                return true;
            }
        });

        lstlocation.setOnItemClickListener((parent, view, position, id) -> {

            Placelatlng placelatlng = new Placelatlng();
            placelatlng.execute(getlocation.get(position).getPlaceId());

        });

    }

    public String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(Locationsearch.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getAddressLine(0));
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        Log.d("mn13location:", result.toString());

        return result.toString();
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception whil", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class Placelatlng extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Public_Function.Show_Progressdialog(Locationsearch.this);
        }

        @Override
        protected String doInBackground(String... place) {

            String data = "";
            String key = "key=AIzaSyDPBxL0kNeHML55lcJRmagAjW22coqJPkw";
            String input = "";
            try {
                input = "placeid=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            // input="placeid=ChIJD98cx4rJWTkRO62Tvs8V3XY";
            String parameters = input + "&" + key;
            String output = "json";
            String url = "https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;

            try {
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Public_Function.Hide_ProgressDialogue();

            try {
                JSONObject object = new JSONObject(result);
                String status = object.optString("status");
                getlocation.clear();
                String location = "";
                if (status.equals("OK")) {
                    GetLatlng searchLocation = new Gson().fromJson(result, GetLatlng.class);
                    location = searchLocation.getResult().getFormattedAddress();
                    PostCreate.location = getAddress(searchLocation.getResult().getGeometry().getLocation().getLat(), searchLocation.getResult().getGeometry().getLocation().getLng());
                    PostCreate.islocation = true;
                    PostCreate.latitude = searchLocation.getResult().getGeometry().getLocation().getLat() + "";
                    PostCreate.longitude = searchLocation.getResult().getGeometry().getLocation().getLng() + "";

                    Public_Function.hideKeyboard(Locationsearch.this);

                    finish();
                    overridePendingTransition(R.anim.slide_enter, R.anim.slide_out);

                    /*lat = searchLocation.getResult().getGeometry().getLocation().getLat().toString();
                    lng = searchLocation.getResult().getGeometry().getLocation().getLng().toString();
                    Log.d("mn13location", lat + "   " + lng);
                    getAddress(searchLocation.getResult().getGeometry().getLocation().getLat(), searchLocation.getResult().getGeometry().getLocation().getLng());
                    sharedpreference.setLatitude(LocationSearch.this, lat);
                    sharedpreference.setLongitude(LocationSearch.this, lng);

                    locationflag = true;
                    Location_Model model = new Location_Model(lat, lng, location);
                    abc.clear();
                    if (sharedpreference.getseracharray(LocationSearch.this) != null) {
                        abc.addAll(sharedpreference.getseracharray(LocationSearch.this));
                    }*/

                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... place) {

            String data = "";
            String key = "key=AIzaSyDPBxL0kNeHML55lcJRmagAjW22coqJPkw";
            String input = "";
            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            String types = "types=geocode";
            String sensor = "sensor=false";
            String parameters = input + "&" + types + "&" + sensor + "&" + key;
            String output = "json";
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

            try {
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject object = new JSONObject(result);
                String status = object.optString("status");
                getlocation.clear();
                if (status.equals("OK")) {
                    SearchLocation searchLocation = new Gson().fromJson(result, SearchLocation.class);
                    getlocation = searchLocation.getPredictions();
                    search_location_adapter = new Search_location_Adapter(Locationsearch.this, getlocation);
                    lstlocation.setAdapter(search_location_adapter);
                } else {
                    search_location_adapter = new Search_location_Adapter(Locationsearch.this, getlocation);
                    lstlocation.setAdapter(search_location_adapter);

                }

                lstlocation.setEmptyView(txtempty);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}
