package com.example.googlemap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.googlemap.Adapter.Info_Window_Adapter;
import com.example.googlemap.Model.DataParser;

import com.example.googlemap.Model.GetDataFromUrl;
import com.example.googlemap.Model.MapData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AutoFragmentSearch extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int MY_LOCATION_REQUEST_CODE = 100;
    private static final String TAG = "AutoFragmentSearch" ;
    private GoogleMap googleMap;
    GoogleApiClient googleApiClient;
    ArrayList<LatLng> points = null;
    ArrayList<String> permissionToRequest;
    ArrayList<String> permissionRejected = new ArrayList<>();
    ArrayList<String> permissions = new ArrayList<>();
    private static final int ALL_PERMISSION_RESULT= 1011;
    private Location location;
    Marker mCurrLocationMarker;
    double originLatitude,originLongitude;
    LatLng  sourceLatLong;
    LatLng destiLatLong;
    AutocompleteSupportFragment autocomplete_fragment_origin,autocomplete_fragment_destination;
    ImageView swap_locationClick;
    String source,desti,destinationName;
    SupportMapFragment supportMapFragment;
    List<Address> addresses;
    Geocoder geocoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_fragment_search);

        getPermission();

        swap_locationClick = findViewById(R.id.swap_location);

         supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);


        points = new ArrayList<LatLng>();
        if (!Places.isInitialized()) {
            Places.initialize(AutoFragmentSearch.this, getString(R.string.API_KEY), Locale.US);
        }
        ////////////////////origin
        autocomplete_fragment_origin = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_origin);
        autocomplete_fragment_origin.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));
        autocomplete_fragment_origin.setCountry("IN");
        autocomplete_fragment_origin.setHint("source");
        autocomplete_fragment_origin.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place_origin)
            {

                Log.d(TAG, "onPlaceSelected: " + place_origin.getName());
                Log.d(TAG, "onPlaceSelected: " + place_origin.getId());
                Log.d(TAG, "onPlaceSelected: " + place_origin.getAddress());
                Log.d(TAG, "onPlaceSelected: " + place_origin.getLatLng());
                sourceLatLong = place_origin.getLatLng();
                originLatitude = place_origin.getLatLng().latitude;
                originLongitude = place_origin.getLatLng().longitude;
            }
            @Override
            public void onError(@NonNull Status status) {
                Log.d(TAG, "onError: "+status);
            }
        });

        //////////////////destination
        autocomplete_fragment_destination = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_destination);
        autocomplete_fragment_destination.setHint("Destination");
        autocomplete_fragment_destination.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));
        autocomplete_fragment_destination.setCountry("IN");
        autocomplete_fragment_destination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place placeDestination) {

                points.clear();
                googleMap.clear();

                Log.d(TAG, "onPlaceSelected: " + placeDestination.getName());
                Log.d(TAG, "onPlaceSelected: " + placeDestination.getId());
                Log.d(TAG, "onPlaceSelected: " + placeDestination.getAddress());
                Log.d(TAG, "onPlaceSelected: " + placeDestination.getLatLng());

                destiLatLong = placeDestination.getLatLng();
                desti = placeDestination.getAddress();
                destinationName = placeDestination.getName();
                SearchLocationMethod(placeDestination.getLatLng(), placeDestination.getName());

                double currentLat;
                double currentLong;
                double destiLat;
                double destiLong;

                currentLat = sourceLatLong.latitude;
                currentLong = sourceLatLong.longitude;
                destiLat = destiLatLong.latitude;
                destiLong = destiLatLong.longitude;

                String uri ="http://maps.google.com/maps?saddr="+currentLat+","+currentLong+ "&daddr="+destiLat+","+destiLong;
                Intent googleMap  = new Intent(Intent.ACTION_VIEW,Uri.parse(uri));
                googleMap.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(googleMap);



                //CalculationByDistance(sourceLatLong,destiLatLong);
            }
            @Override
            public void onError(@NonNull Status status) {
                Log.d(TAG, "onError: " + status);
            }
        });
        swap_locationClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapLocationValue();
            }
        });
    }

    @SuppressLint("ResourceType")
    private void swapLocationValue()
    {

        Log.d(TAG, "swapLocationValue: "+source);
        Log.d(TAG, "swapLocationValue: "+desti);


        String temp = source;

        source = desti;
        desti = temp;


        Log.d(TAG, "swapLocationValue source: "+source);
        Log.d(TAG, "swapLocationValue destination: "+desti);


        View originView,destiView;

        originView = findViewById(R.id.originView);
        destiView = findViewById(R.id.destinationView);

        utils utils = new utils();
        autocomplete_fragment_origin.setText(source);

        utils.SlideUP(originView,this);
        utils.SlideDown(destiView,this);

        autocomplete_fragment_destination.setText(desti);


        SearchLocationMethod(destiLatLong, destinationName);
    }

    private void SearchLocationMethod(LatLng latLng, String name)
    {
        try
            {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(11).build();


            MarkerOptions markerOptions = new MarkerOptions();

            LatLng origin = new LatLng(originLatitude,originLongitude);
            googleMap.addMarker(markerOptions.position(origin).title("my Place"));


                if (points.size() == 1)
                {
                    markerOptions.getIcon();
                }
                else
                {
//                                  markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.second));
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                }



                googleMap.addMarker(markerOptions.position(latLng).title(name));
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                LatLng destination =  latLng;


            String url= GetDataFromUrl.getDirectionsUrl(origin,destination,getString(R.string.API_KEY));
            Log.d(TAG, "SearchLocationMethod: "+url);

            FetchUrl fetchUrl = new FetchUrl();
            fetchUrl.execute(url);

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            }
        catch (Exception e)
        {
            Log.d(TAG, "SearchLocationMethod exception: "+e);
        }
    }
    private void getPermission() {
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionToRequest = permissionToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (permissionToRequest.size() > 0)
            {
                requestPermissions(permissionToRequest.toArray(new String[permissionToRequest.size()]), ALL_PERMISSION_RESULT);
            }
        }

        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addConnectionCallbacks(this).build();


    }
    private ArrayList<String> permissionToRequest(ArrayList<String> requestedpermissions)
    {
        ArrayList<String> result = new ArrayList<>();

        for (String permission : requestedpermissions)
        {
                if (!hasermission(permission))
                {
                    result.add(permission);
                }
        }
        return permissions;
    }
    private boolean hasermission(String permission)
    {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    return  checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
                }

        return true;
    }
    @Override
    public void onLocationChanged(Location location) { }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }
    @Override
    public void onProviderEnabled(String provider) { }
    @Override
    public void onProviderDisabled(String provider) { }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&  ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return ;
        }



        // Permissions ok, we get last location
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            Log.e("Latitude : ", + location.getLatitude() + "\nLongitude : " + location.getLongitude());
            originLatitude = location.getLatitude();
            originLongitude = location.getLongitude();
            Log.d(TAG, "onConnected: "+originLongitude+originLongitude);

            //String latlong = location.getLatitude()+""+location.getLongitude();



            LatLng  latLng = new LatLng(originLatitude,originLongitude);
            sourceLatLong = latLng;



            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(originLatitude, originLongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            } catch (IOException e) {
                e.printStackTrace();
            }


            Log.d(TAG, "onConnected: "+addresses.get(0).getAddressLine(0));
            String myAddress = addresses.get(0).getAddressLine(0);
            source = myAddress;
            autocomplete_fragment_origin.setText(myAddress);




        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                }

                googleMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
            }
        }
        switch (requestCode) {
            case ALL_PERMISSION_RESULT:
                for (String perm : permissionToRequest) {
                    if (!hasermission(perm)) {
                        permissionRejected.add(perm);
                    }
                }

                if (permissionRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionRejected.get(0))) {
                            new AlertDialog.Builder(AutoFragmentSearch.this).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionRejected.
                                                        toArray(new String[permissionRejected.size()]), ALL_PERMISSION_RESULT);
                                            }
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }

                } else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                }

                break;
        }
    }
    @Override
    public void onConnectionSuspended(int i) { }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }
    @Override
    public void onMapReady(final GoogleMap googleMaps) {
        googleMap = googleMaps;
        googleMaps.setPadding(20,1100,20,20);



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            googleApiClient.connect();
            googleMap.setMyLocationEnabled(true);





            googleMaps.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    googleMap.clear();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);


                    double lat,longitu;

                    lat = latLng.latitude;
                    longitu = latLng.longitude;

                    geocoder = new Geocoder(AutoFragmentSearch.this,Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(lat,longitu,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    MapData mapData = new MapData();
                    mapData.setName(addresses.get(0).getAddressLine(0));
                    mapData.setPlace(addresses.get(0).getLocality());
                    mapData.setType(addresses.get(0).getPhone());
                    mapData.setCountry(addresses.get(0).getCountryName());


                    googleMaps.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mCurrLocationMarker = googleMaps.addMarker(markerOptions);
                    Info_Window_Adapter info_window_adapter = new Info_Window_Adapter(getApplicationContext(),mapData);
                    googleMaps.setInfoWindowAdapter(info_window_adapter);

                    mCurrLocationMarker.showInfoWindow();





                }
            });








        }
        else
            {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
            }

    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    point.clear();
                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(4);
                lineOptions.color(Color.BLUE);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                googleMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        googleApiClient.connect();
    }
    public double CalculationByDistance(LatLng StartP, LatLng EndP)
    {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            newFormat = new DecimalFormat("####");
        }
        int kmInDec = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            kmInDec = Integer.valueOf(newFormat.format(km));
        }
        double  meter = valueResult % 1000;
        int meterInDec = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            meterInDec = Integer.valueOf(newFormat.format(meter));
        }
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec + "." + meterInDec);
        Toast.makeText(this, "KM"+kmInDec+"Meter"+meterInDec, Toast.LENGTH_SHORT).show();








        return Radius * c;
    }

    GoogleMap.OnMyLocationChangeListener onMyLocationChangeListener = new GoogleMap.OnMyLocationChangeListener()
    {
        @Override
        public void onMyLocationChange(Location location)
        {

            LatLng myLocation = new LatLng(location.getLatitude(),location.getLongitude());
            mCurrLocationMarker = googleMap.addMarker(new MarkerOptions().position(myLocation));
            if (googleMap!=null)
            {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,16.0f));
                googleMap.setOnMyLocationChangeListener(onMyLocationChangeListener);

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(AutoFragmentSearch.this, Locale.getDefault());

                try
                {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String  LocationAddress = addresses.get(0).getAddressLine(0);
                    Log.d(TAG, "onMyLocationChange: "+LocationAddress);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    };
}
