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
        int idRuta = getIntent().getIntExtra("id",0);
        User user1 = new User("John", "Doe", "john@example.com", "+123456789");
        User user2 = new User("Alice", "Smith", "alice@example.com", "+987654321");
        routesList.add(new Route(1, "40.7128° N, 74.0060° W", "34.0522° N, 118.2437° W", "08:00", 3, user1));
        routesList.add(new Route(2, "51.5074° N, 0.1278° W", "48.8566° N, 2.3522° E", "09:30", 2, user2));
        routesList.add(new Route(3, "35.6895° N, 139.6917° E", "37.7749° N, 122.4194° W", "10:45", 4, user1));
        routesList.add(new Route(4, "52.3667° N, 4.8945° E", "52.5200° N, 13.4050° E", "12:15", 1, user2));
        routesList.add(new Route(5, "19.4326° N, 99.1332° W", "20.5937° N, 78.9629° E", "14:00", 3, user1));
        // si creamos botones, las acciones de estos aqui
        FragmentDetails detailsFragment = (FragmentDetails) getSupportFragmentManager().findFragmentById(R.id.detailsFragment);
        detailsFragment.renderData(idRuta);
    }

    public void sendData(int idRuta) {
        FragmentMap mapFragment = (FragmentMap) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.renderData(idRuta);
    }


}