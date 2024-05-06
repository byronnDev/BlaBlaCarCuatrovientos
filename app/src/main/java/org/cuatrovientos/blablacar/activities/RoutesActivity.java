package org.cuatrovientos.blablacar.activities;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.fragments.FragmentDetails;
import org.cuatrovientos.blablacar.fragments.FragmentMap;
import org.cuatrovientos.blablacar.models.Route;
import org.cuatrovientos.blablacar.models.User;

import java.util.ArrayList;
import java.util.List;

public class RoutesActivity extends FragmentActivity implements FragmentDetails.DataListener {
    List<Route> routesList = new ArrayList<Route>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        String idRuta = getIntent().getStringExtra("id");
        FragmentDetails detailsFragment = (FragmentDetails) getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
        detailsFragment.renderData(idRuta);
    }

    public void sendData(int idRuta) {
        FragmentMap mapFragment = (FragmentMap) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.renderData(idRuta);
    }


}