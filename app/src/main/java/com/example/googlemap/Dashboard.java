package com.example.googlemap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Dashboard extends AppCompatActivity {

    CardView  auto_search,search,direction_navigation,near_by;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        auto_search = findViewById(R.id.auto_search);
        search = findViewById(R.id.search);
        direction_navigation = findViewById(R.id.direction_navigation);
        near_by = findViewById(R.id.nearBy);



        auto_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),AutoFragmentSearch.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });


        direction_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),marker_moving_location.class);
                startActivity(intent);
            }
        });

        near_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),Near_By_Places.class);
                startActivity(intent);
            }
        });


    }
}
