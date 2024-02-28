package org.cuatrovientos.blablacar.activities;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.fragments.FragmentDetails;
import org.cuatrovientos.blablacar.fragments.FragmentMap;

public class RoutesActivity extends FragmentActivity implements FragmentDetails.DataListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        // si creamos botones, las acciones de estos aqui
    }

    public void sendData(int idRuta) {
        FragmentMap mapFragment = (FragmentMap) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.renderData(idRuta);
    }


}