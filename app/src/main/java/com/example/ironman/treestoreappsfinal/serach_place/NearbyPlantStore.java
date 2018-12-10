package com.example.ironman.treestoreappsfinal.serach_place;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ironman.treestoreappsfinal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NearbyPlantStore extends AppCompatActivity {


 //   String url1 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+"51.503186,"+"-0.126446"+"&radius=1000&types=hospital&key=";
    String url1 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";

    String url2="&radius=1000&types=nursery&key=";

    String api= "AIzaSyCkCbrTuxQaXnhG-9-UcOnWrm5sU7CbWkk";
    int PERMISSIONS_REQUEST_CODE=12190;
    LocationManager locationManager;
    LocationListener locationListener;
    ProgressDialog pd;
    ArrayList<PlaceValueHolder> placeValueHolderArrayList;
    RecyclerView placeRyc;
    RecyclerView.LayoutManager linearLayout;
    PlaceAdapter placeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_plant_store);

        linearLayout=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        placeValueHolderArrayList=new ArrayList<>();
        placeAdapter=new PlaceAdapter(this,placeValueHolderArrayList);
        placeRyc =findViewById(R.id.place_ryc);
        placeRyc.setLayoutManager(linearLayout);
        placeRyc.setNestedScrollingEnabled(false);
        placeRyc.setAdapter(placeAdapter);

        pd = new ProgressDialog(this);
        pd.setMessage("Please wait");
        pd.setCancelable(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!checkInternet()){
            Toast.makeText(this,"Internet is not connected",Toast.LENGTH_LONG).show();
        }else {
           startSearching();
        }

    }


    boolean checkInternet(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        return connected;
    }

    public void getLocation(View view) {
        placeValueHolderArrayList.clear();
        placeAdapter.notifyDataSetChanged();
        startSearching();

    }

    void startSearching(){
        checkPermission();
        if(!checkInternet()){
            Toast.makeText(this,"Internet is not connected",Toast.LENGTH_LONG).show();
        }else {
            if(checkGps()){
                startLocationUpdate();
            }else {
                Toast.makeText(this,"turn on location service",Toast.LENGTH_LONG).show();
            }
//            if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
//                startLocationUpdate();
//            }
//            else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//                startLocationUpdate();
//            }
//            else {
//                Toast.makeText(this,"turn on location service",Toast.LENGTH_LONG).show();
//            }

        }
    }

    boolean checkGps(){
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {

            return  false;
        }
        return true;
    }

    public void startLocationUpdate() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                getLatLon(location.getLatitude(),location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        List<String> allProviders= locationManager.getAllProviders();
        for(String  name: allProviders){
            locationManager.requestLocationUpdates(name, 1000, 0, locationListener);
        }
    }

    void getLatLon(double lat, double lon){
    //    Toast.makeText(this,lat+" : "+lon,Toast.LENGTH_LONG).show();
        locationManager.removeUpdates(locationListener);

        String mainUrl=url1+lat+","+lon+url2+api;
        new JsonTask().execute(mainUrl);

    }

    void getAllData(String jsonString) throws JSONException {
        JSONObject jsonObject=new JSONObject(jsonString);
        JSONArray jsonArray =jsonObject.getJSONArray("results");

        for (int i=0;i<jsonArray.length();i++){
            JSONObject place=jsonArray.getJSONObject(i);
            JSONObject location=place.getJSONObject("geometry");
            location=location.getJSONObject("location");

            double lat=location.getDouble("lat"),lon=location.getDouble("lng");

            String name=place.getString("name");
            String adress=place.getString("vicinity");

            placeValueHolderArrayList.add(new PlaceValueHolder(lat, lon, name, adress));
        }

        placeAdapter.notifyDataSetChanged();
    }


    void checkPermission(){
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();



        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION)){
            permissionsNeeded.add("android.permission.ACCESS_FINE_LOCATION");}
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION)){
            permissionsNeeded.add("android.permission.ACCESS_COARSE_LOCATION");}
        if(!permissionsList.isEmpty()){
            ActivityCompat.requestPermissions(this,
                    permissionsList.toArray(new String[permissionsList.size()]),
                    PERMISSIONS_REQUEST_CODE);
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requestCode == PERMISSIONS_REQUEST_CODE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "please add permission to get nearby plants store", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }




    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            try { getAllData(result);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }


    }

}




