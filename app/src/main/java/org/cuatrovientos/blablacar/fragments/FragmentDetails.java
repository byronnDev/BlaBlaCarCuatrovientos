package org.cuatrovientos.blablacar.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.firestore.FirebaseFirestore;

import android.widget.TextView;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.Route;
import org.cuatrovientos.blablacar.models.User;

import java.util.ArrayList;
import java.util.List;


public class FragmentDetails extends Fragment {
    RecyclerView recyclerRutas;
    TextView test;
    List<Route> routesList = new ArrayList<Route>();
    FirebaseFirestore db;
    public FragmentDetails() {
        // Required empty public constructor
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        //TODO
        //vincular los items del xml a variables
        test = (TextView) view.findViewById(R.id.textView2);
        routesList.clear();
        User user1 = new User("John", "Doe", "john@example.com", "+123456789");
        User user2 = new User("Alice", "Smith", "alice@example.com", "+987654321");
        routesList.add(new Route(1, "40.7128° N, 74.0060° W", "34.0522° N, 118.2437° W", "08:00", 3, user1));
        routesList.add(new Route(2, "51.5074° N, 0.1278° W", "48.8566° N, 2.3522° E", "09:30", 2, user2));
        routesList.add(new Route(3, "35.6895° N, 139.6917° E", "37.7749° N, 122.4194° W", "10:45", 4, user1));
        routesList.add(new Route(4, "52.3667° N, 4.8945° E", "52.5200° N, 13.4050° E", "12:15", 1, user2));
        routesList.add(new Route(5, "19.4326° N, 99.1332° W", "20.5937° N, 78.9629° E", "14:00", 3, user1));


        //poner toda la informacion en los textos del xml





        return view;
    }

    public interface DataListener {
        void sendData(int idRuta);
    }

    public void renderData(int idRuta) {
        //TODO
        //crear un objeto de tipo Ruta y construirlo, luego el resto de la logica de la clase

        test.setText(String.valueOf(routesList.get(idRuta).getId_ruta()));



    }
}