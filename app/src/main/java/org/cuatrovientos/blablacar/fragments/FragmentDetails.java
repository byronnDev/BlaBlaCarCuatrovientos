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
import android.widget.TextView;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.Route;

import java.util.ArrayList;
import java.util.List;


public class FragmentDetails extends Fragment {
    RecyclerView recyclerRutas;
    TextView test;
    List<Route> routesList = new ArrayList<Route>();
    public FragmentDetails() {
        // Required empty public constructor
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
        routesList.add(new Route(0,"70.82434579444012, -20.6598648266999774"));
        routesList.add(new Route(1,"30.82434579444012, -4.6598648266999774"));
        routesList.add(new Route(2,"-15.82434579444012, 7.6598648266999774"));
        routesList.add(new Route(3,"-70.82434579444012, 20.6598648266999774"));
        routesList.add(new Route(4,"-1.82434579444012, 89.6598648266999774"));
        routesList.add(new Route(5,"100.82434579444012, -100.6598648266999774"));


        //poner toda la informacion en los textos del xml





        return view;
    }

    public interface DataListener {
        void sendData(int idRuta);
    }

    public void renderData(int idRuta) {
        //TODO
        //crear un objeto de tipo Ruta y construirlo, luego el resto de la logica de la clase

        test.setText(String.valueOf(routesList.get(idRuta).getId()));



    }
}