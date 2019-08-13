package com.example.googlemap.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.googlemap.Model.MapData;
import com.example.googlemap.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class Info_Window_Adapter implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private Activity activity;
    MapData mapData;

    public Info_Window_Adapter(Context context, MapData mapData) {
        this.context = context;
        this.mapData = mapData;
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {


        final View view = View.inflate(context, R.layout.custom_info_window, null);



        TextView name,country,address;



       name = view.findViewById(R.id.nameInfo);
       country = view.findViewById(R.id.countryInfo);
       address = view.findViewById(R.id.addressInfo);


        name.setText(mapData.getName());
        address.setText(mapData.getPlace());
        country.setText(mapData.getCountry());
    


        return view;
    }
}
