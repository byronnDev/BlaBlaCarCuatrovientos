package org.cuatrovientos.blablacar.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.fragments.FragmentAddRoutes;
import org.cuatrovientos.blablacar.fragments.FragmentHome;
import org.cuatrovientos.blablacar.fragments.FragmentProfile;

public class MainActivity extends AppCompatActivity implements NavigationBarView
        .OnItemSelectedListener {
    //private MapView mMapView;
    //private MapController mMapController;
    String email;
    BottomNavigationView bottomNav;
    FragmentHome homeView = new FragmentHome();
    FragmentAddRoutes addRoutesView = new FragmentAddRoutes();
    FragmentProfile profileView = new FragmentProfile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creado el menú de navegación
        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(this);
        bottomNav.setSelectedItemId(R.id.home);

        //los distintos activities
        getDataFromLogin();
    }

    private void getDataFromLogin() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        email = bundle.getString("email");
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.home) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.defaultView, homeView)
                    .commit();
            return true;
        } else if (itemId == R.id.addRoutes) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.defaultView, addRoutesView)
                    .commit();
            return true;
        } else if (itemId == R.id.profile) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.defaultView, profileView)
                    .commit();
            return true;
        }
        // Handle default case
        return false;
    }
}