package org.cuatrovientos.blablacar.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.fragments.FragmentAddRoutes;
import org.cuatrovientos.blablacar.fragments.FragmentHome;
import org.cuatrovientos.blablacar.fragments.FragmentProfile;
import org.cuatrovientos.blablacar.fragments.FragmentYourRoutes;

public class MainActivity extends AppCompatActivity implements NavigationBarView
        .OnItemSelectedListener, FragmentAddRoutes.DataListener, FragmentYourRoutes.DataListener {
    BottomNavigationView bottomNav;
    FragmentHome homeView = new FragmentHome();
    FragmentAddRoutes addRoutesView = new FragmentAddRoutes();
    FragmentYourRoutes yourRoutesView = new FragmentYourRoutes();
    FragmentProfile profileView = new FragmentProfile();
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance(); // Inicializar Firestore

        setDefaultHomeSelectedInNav();
    }

    private void setDefaultHomeSelectedInNav() {
        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(this);
        bottomNav.setSelectedItemId(R.id.home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (isHome(itemId)) {
            setHomeView();
        } else if (isAddRoutes(itemId)) {
            setAddRoutesView();
        } else if (isYourRoutesView(itemId)) {
            setYourRoutesView();
        } else if (isProfile(itemId)) {
            setProfileView();
        }
        return true;
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

    private void setYourRoutesView() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.defaultView, yourRoutesView)
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

    private static boolean isYourRoutesView(int itemId) {
        return itemId == R.id.routesHistory;
    }

    private static boolean isHome(int itemId) {
        return itemId == R.id.home;
    }

    @Override
    public void sendData(String idRuta) {
        Intent intent = new Intent(MainActivity.this,RoutesActivity.class);
        intent.putExtra("id",idRuta);
        startActivity(intent);
    }

    @Override
    public void addRoute() {
        Intent intent = new Intent(MainActivity.this,AddRouteActivity.class);
        startActivity(intent);
    }
}