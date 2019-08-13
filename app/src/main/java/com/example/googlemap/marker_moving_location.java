package com.example.googlemap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.getDefault;

public class marker_moving_location extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{
    private static final int MY_LOCATION_REQUEST_CODE = 100;
    private static final String TAG = "marker_moving_location";
    EditText search;
    ImageView marker;
    SupportMapFragment supportMapFragment;
    private GoogleMap map;
    private Location location;
    private LocationManager locationManager;
    GoogleApiClient googleApiClient;
    Double originLatitude, originLongitude;
    LatLng sourceLatLong;
    String source;
    LocationListener locationListener;
    List<Address> addresses;
    Geocoder geocoder;



    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_moving_location);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_location);
        search = findViewById(R.id.searchEdit_location);

        marker = findViewById(R.id.marker_location);
        supportMapFragment.getMapAsync(this);

        Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.ACCURACY_FINE);
        String best;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        best = locationManager.getBestProvider(crit, false);
        locationManager.requestLocationUpdates(best,0,1,this);

        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addConnectionCallbacks(this).build();






    }






    @Override
    public void onLocationChanged(Location location)
    {
//        Log.d(TAG, "onLocationChanged: ");
//
//        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
//        Log.d(TAG, "onLocationChanged: "+latLng.latitude+""+latLng.longitude);
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,11);
//        map.animateCamera(cameraUpdate);
//        locationManager.removeUpdates(this);
    }







    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        // Permissions ok, we get last location
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            Log.e("Latitude : ", +location.getLatitude() + "\nLongitude : " + location.getLongitude());
            originLatitude = location.getLatitude();
            originLongitude = location.getLongitude();
            Log.d(TAG, "onConnected: " + location.getLatitude() + "\nLongitude : " + location.getLongitude());

            //String latlong = location.getLatitude()+""+location.getLongitude();
            LatLng latLng = new LatLng(originLatitude, originLongitude);
            sourceLatLong = latLng;

            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, getDefault());

            try {
                addresses = geocoder.getFromLocation(originLatitude, originLongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            } catch (IOException e) {
                e.printStackTrace();
            }


            Log.d(TAG, "onConnected: " + addresses.get(0).getAddressLine(0));
            String myAddress = addresses.get(0).getAddressLine(0);
            source = myAddress;
            search.setText(myAddress);


        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(final GoogleMap googleMap)
    {
        map = googleMap;



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            googleApiClient.connect();
            googleMap.setMyLocationEnabled(true);
            googleMap.animateCamera(CameraUpdateFactory.zoomBy(15));

        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
        }

//        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener()
//        {
//            @Override
//            public void onCameraIdle() {
//                map.clear();
//                marker.clearColorFilter();
//                LatLng centerMap = map.getCameraPosition().target;
//                Log.d(TAG, "onLocationChanged: "+centerMap);
//                List<Address>addresses;
//                Geocoder geocoder = new Geocoder(marker_moving_location.this, Locale.getDefault());
//
//                try {
//
//                    addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),10);
//                    String address = addresses.get(0).getAddressLine(0);
//                    Log.d(TAG, "onCameraIdle: "+address);
//                    search.setText(address);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });


        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                search.setText(" ");

                double latIdle = googleMap.getCameraPosition().target.latitude;
                double longitudeIdle = googleMap.getCameraPosition().target.longitude;

                Log.d(TAG, "onCameraIdle: "+latIdle+","+longitudeIdle);
                //LatLng latLng = new LatLng(latIdle,longitudeIdle);
                geocoder = new Geocoder(marker_moving_location.this,Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latIdle,longitudeIdle,1);
                    }
                    catch (IOException e)
                    {
                    e.printStackTrace();
                    }

                Log.d(TAG, "onCameraIdle: "+addresses.get(0).getAddressLine(0));
                Log.d(TAG, "onCameraIdle: "+addresses.get(0).getAddressLine(0));
                String address = addresses.get(0).getAddressLine(0);
                search.setText(address);


            }
        });







    }


}
