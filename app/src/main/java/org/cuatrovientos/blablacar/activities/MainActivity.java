package org.cuatrovientos.blablacar.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import org.cuatrovientos.blablacar.R;

public class MainActivity extends AppCompatActivity {
    //private MapView mMapView;
    //private MapController mMapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //los distintos activities
        getDataFromLogin();
    }

    private void getDataFromLogin() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String email = bundle.getString("email");
    }
}