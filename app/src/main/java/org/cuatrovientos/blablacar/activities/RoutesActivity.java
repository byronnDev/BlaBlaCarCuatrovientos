package org.cuatrovientos.blablacar.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.fragments.details_fragment;
import org.cuatrovientos.blablacar.fragments.map_fragment;

public class RoutesActivity extends FragmentActivity implements details_fragment.DataListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        // si creamos botones, las acciones de estos aqui
    }

    public void sendData(int idRuta) {
        map_fragment mapFragment = (map_fragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.renderData(idRuta);
    }


}