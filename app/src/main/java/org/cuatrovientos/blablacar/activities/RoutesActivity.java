package org.cuatrovientos.blablacar.activities;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.fragments.FragmentDetails;
import org.cuatrovientos.blablacar.fragments.FragmentMap;
import org.cuatrovientos.blablacar.models.Route;

import java.util.ArrayList;
import java.util.List;

public class RoutesActivity extends FragmentActivity implements FragmentDetails.DataListener {
    List<Route> routesList = new ArrayList<Route>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        int idRuta = getIntent().getIntExtra("id",0);
        routesList.add(new Route(0,"70.82434579444012, -20.6598648266999774"));
        routesList.add(new Route(1,"30.82434579444012, -4.6598648266999774"));
        routesList.add(new Route(2,"-15.82434579444012, 7.6598648266999774"));
        routesList.add(new Route(3,"-70.82434579444012, 20.6598648266999774"));
        routesList.add(new Route(4,"-1.82434579444012, 89.6598648266999774"));
        routesList.add(new Route(5,"100.82434579444012, -100.6598648266999774"));
        // si creamos botones, las acciones de estos aqui
        FragmentDetails detailsFragment = (FragmentDetails) getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
        detailsFragment.renderData(idRuta);
    }

    public void sendData(int idRuta) {
        FragmentMap mapFragment = (FragmentMap) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.renderData(idRuta);
    }


}