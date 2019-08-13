package com.example.googlemap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.googlemap.Adapter.NearByPlaces_Adapter;
import com.example.googlemap.ApiInterfaces.Interface;
import com.example.googlemap.ApiManager.ApiClient;
import com.example.googlemap.ApiManager.Config;
import com.example.googlemap.ApiResponses.NearByPlaces_Response;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Near_By_Places extends AppCompatActivity {

    private static final int MY_LOCATION_REQUEST_CODE = 100;
    private static final String TAG ="Near_By_Places" ;
    Button restaurant, hospital, school;
    //List<NearByPlaces_Response> near_by_places = new ArrayList<>();
    RecyclerView recyclerView;
    double myLat, myLong;
    LatLng latLng;
    NearByPlaces_Response near_by_places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near__by__places);

        recyclerView = findViewById(R.id.recyclerNearBy);

        restaurant = findViewById(R.id.RestaurantsList);
        hospital = findViewById(R.id.HospitalsList);
        school = findViewById(R.id.SchoolsList);


        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRestaurants("restaurant");
            }
        });




         hospital.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 getRestaurants("hospital");
             }
         });



          school.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  getRestaurants("school");
              }
          });

        getMyLatLong();


    }

    private void getMyLatLong() {

        LocationManager   locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        myLat = location.getLatitude();
        myLong = location.getLongitude();
        Log.d(TAG, "getMyLatLong: "+myLat+","+myLong);
        latLng = new LatLng(myLat,myLong);
    }

    private void getRestaurants(String place)
    {

        final ProgressDialog progressDialog = new ProgressDialog(Near_By_Places.this);
        progressDialog.show();


        String api_key = "AIzaSyBH_TExjnT7McUcM-x39Gl0PTDPc7mSiUs";
        String location = myLat+","+myLong;
        final Interface apiInterface = ApiClient.createService(Interface.class, Config.BASE_URL);
        final Call<ResponseBody> nearByPlacesCall = apiInterface.GetNearByPlaces(location,"10000",place,api_key);

        Log.d(TAG, "getRestaurants: "+nearByPlacesCall);

         nearByPlacesCall.enqueue(new Callback<ResponseBody>() {
          @SuppressLint("WrongConstant")
          @Override
          public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
          {
              Log.d(TAG, "onResponse: "+response);

              String url=response.raw().request().url().toString();
              Log.e("Nearby places","url for nearby places: "+url);

                      if (response.isSuccessful()) {
                          String result = null;
                          try {
                              try {
                                  result = response.body().string();
                              } catch (Exception e) {
                                  Log.d(TAG, "onResponse Exception: " + e);
                              }
                              JSONObject jsonObject = new JSONObject(result);
                              JSONArray jsonArray = jsonObject.getJSONArray("results");


                              for (int i=0 ; i<jsonArray.length(); i++)
                              {

                                  near_by_places = new Gson().fromJson(jsonObject.toString(), NearByPlaces_Response.class);
                                  Log.e("NearByPlaces","results: "+near_by_places.getResults());

                                  recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                  NearByPlaces_Adapter nearByPlaces_adapter = new NearByPlaces_Adapter(getApplicationContext(), near_by_places.getResults());
                                  recyclerView.setAdapter(nearByPlaces_adapter);
                                  progressDialog.dismiss();
                              }

                          }
                            catch (Exception e)
                            {
                                Log.e(TAG, "onResponse: "+e);
                                progressDialog.dismiss();

                            }
                      }
                      else
                          {
                          Log.e(TAG, "onResponse: " + "not successfull");
                          progressDialog.dismiss();
                            }


          }
          @Override
          public void onFailure(Call<ResponseBody> call, Throwable t) {
              Log.d(TAG, "onFailure: "+t);

          }
      });
}
}
