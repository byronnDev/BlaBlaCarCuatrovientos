package org.cuatrovientos.blablacar.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

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

        getDataFromLogin();
        setDefaultHomeSelectedInNav();
    }

    private void setDefaultHomeSelectedInNav() {
        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(this);
        bottomNav.setSelectedItemId(R.id.home);
    }

    private void getDataFromLogin() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        email = bundle.getString("email");

        SharedPreferences.Editor preferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit();
        preferences.putString("email", email);
        preferences.apply();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (isHome(itemId)) {
            setHomeView();
            return true; // Seleccionar el botón
        } else if (isAddRoutes(itemId)) {
            setAddRoutesView();
            return true; // Seleccionar el botón
        } else if (isProfile(itemId)) {
            setProfileView();
            return true; // Seleccionar el botón
        }
        return false; // No seleccionar nada
    }

    private void setProfileView() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.defaultView, profileView)
                .commit();
    }

    private void setAddRoutesView() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.defaultView, addRoutesView)
                .commit();
    }

    private void setHomeView() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.defaultView, homeView)
                .commit();
    }

    private static boolean isProfile(int itemId) {
        return itemId == R.id.profile;
    }

    private static boolean isAddRoutes(int itemId) {
        return itemId == R.id.addRoutes;
    }

    private static boolean isHome(int itemId) {
        return itemId == R.id.home;
    }
}