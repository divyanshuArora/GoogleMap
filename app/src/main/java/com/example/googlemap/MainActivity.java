package com.example.googlemap;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.googlemap.Model.DirectionsJsonParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "main";
    private GoogleMap map;
    Marker CurrentLocationarker;
    GoogleApiClient googleApiClient;
    int  MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 1;
    EditText searchEdit;
    ImageView searchBtn;
    ArrayList markerPoint= new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        searchEdit = findViewById(R.id.searchEdit);
        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLocation();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

 @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: ");

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CurrentLocationarker = map.addMarker(new MarkerOptions().position(latLng));

            if (map!=null)
            {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16.0f));
            }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged: ");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled: ");

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled: ");

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Log.d(TAG, "onConnectionFailed: ");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: ");

        map = googleMap;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
               try {
                   if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                       buildGoogleApiClient();
                       map.setMyLocationEnabled(true);
                       map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {

                                if (markerPoint.size() > 1)
                                {
                                    markerPoint.clear();
                                    map.clear();
                                }

                                markerPoint.add(latLng);
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                markerOptions.title(latLng.latitude+":"+latLng.latitude);



                                if (markerPoint.size() == 1)
                                {
                                    markerOptions.getIcon();
                                }
                                else
                                {
//                                  markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.second));
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                                }

                                map.addMarker(markerOptions);

                                if (markerPoint.size() >= 2 )
                                {
                                    LatLng origion = (LatLng) markerPoint.get(0);
                                    LatLng destination = (LatLng) markerPoint.get(1);

                                    String url = getDirectionUrl(origion,destination);
                                    DownloadTask downloadTask = new DownloadTask();
                                    downloadTask.execute(url);
                                }
                            }
                        });
                   }
                   else
                   {
                       ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
                   }
               }
               catch (Exception e)
               {
                   Log.d(TAG, "onMapReadyException: ");
               }
           }
           else
               {
               buildGoogleApiClient();
               map.setMyLocationEnabled(true);
               }
    }
    protected synchronized void buildGoogleApiClient()
    {
         googleApiClient = new GoogleApiClient.Builder(this).
              addConnectionCallbacks(this)
              .addOnConnectionFailedListener(this)
              .addApi(LocationServices.API).build();
               googleApiClient.connect();
    }

    public   class  DownloadTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... url) {

            String data = " ";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d(TAG, "doInBackground: " + e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }

    }

    private String getDirectionUrl(LatLng origin,LatLng destination)
    {
        String str_origion = "origin=" + origin.latitude + "," + origin.longitude;
        String str_destination = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor =  "sensor=false";
        String mode = "mode=driving";
        String parameter =  str_origion + "&" + str_destination + "&" + sensor + "&" + mode;
        String output = "json";
        String api_key  = "AIzaSyBH_TExjnT7McUcM-x39Gl0PTDPc7mSiUs";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output+ "?" +parameter+ "&key=" + api_key;


        return url;

       }

       private String downloadUrl(Object StrUrl) throws IOException
      {

          String strUrl  = StrUrl.toString();
          String data  = "";

          InputStream inputStream = null;
          HttpURLConnection httpURLConnection  =  null;

          try
          {
              URL url = new URL(strUrl);

              httpURLConnection = (HttpURLConnection) url.openConnection();

              httpURLConnection.connect();


              httpURLConnection.connect();
              inputStream  = httpURLConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer =  new StringBuffer();

            String line = "";
            while ((line = br.readLine())!=null)
            {
                stringBuffer.append(line);
            }

            data = stringBuffer.toString();
            br.close();
            

          }
          catch (Exception e)
          {
              Log.d(TAG, "downloadUrl Exception: "+e.toString());
          }
          finally {
              inputStream.close();
              httpURLConnection.disconnect();
          }


            return  data;
      }

      private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJsonParser parser = new DirectionsJsonParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }


        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
                PolylineOptions lineOptions = null;
                MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap point = path.get(j);

                    double lat = Double.parseDouble(String.valueOf(point.get("lat")));
                    double lng = Double.parseDouble(String.valueOf(point.get("lng")));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(7);
                lineOptions.color(Color.BLUE);
                lineOptions.geodesic(true);

            }

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }



    }

////////////////////////////////////////////////search location
    public void searchLocation()
    {
        map.clear();
        markerPoint.clear();
        String location = searchEdit.getText().toString();
        Log.d(TAG, "searchLocation: "+location);
        List<Address> addressList = null;

        if (location!=null || !location.equals(""))
        {
            map.clear();
            markerPoint.clear();
            Geocoder geocoder  = new Geocoder(this);
            try
            {
                addressList = geocoder.getFromLocationName(location,1);
                Log.d(TAG, "searchLocation Try: "+location);
            }
            catch (Exception e)
            {
                Log.d(TAG, "searchLocation: "+e);
            }

            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
            Log.d(TAG, "searchLocation latLong: "+latLng);
            Log.d(TAG, "searchLocation latLong: "+address.getLatitude()+" "+address.getLongitude());
            map.addMarker(new MarkerOptions().position(latLng).title(location));
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.zoomTo(10));


        }
        else
        {
            searchEdit.setError("Enter Location");
        }
    }





}