package org.cuatrovientos.blablacar.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.adapters.RecyclerDataAdapter;
import org.cuatrovientos.blablacar.models.Route;
import org.cuatrovientos.blablacar.models.User;

import java.util.ArrayList;
import java.util.List;

public class FragmentAddRoutes extends Fragment {

    List<Route> routesList = new ArrayList<Route>();
    RecyclerView recyclerView;
    DataListener callback;
    Button btnAddRoute;

    public FragmentAddRoutes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_routes, container, false);
        routesList.clear();
        routesList.add(new Route(0,"70.82434579444012, -20.6598648266999774"));
        routesList.add(new Route(1,"30.82434579444012, -4.6598648266999774"));
        routesList.add(new Route(2,"-15.82434579444012, 7.6598648266999774"));
        routesList.add(new Route(3,"-70.82434579444012, 20.6598648266999774"));
        routesList.add(new Route(4,"-1.82434579444012, 89.6598648266999774"));
        routesList.add(new Route(5,"100.82434579444012, -100.6598648266999774"));

        this.btnAddRoute = view.findViewById(R.id.btnAddRoute);

        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerRutas);
        RecyclerDataAdapter routesAdapter = new RecyclerDataAdapter(routesList, new RecyclerDataAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(Route conten) {
                int idMail = conten.getId();
                callback.sendData(idMail);
            }
        });
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        this.recyclerView.setAdapter(routesAdapter);

        this.btnAddRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //crear un objeto de tipo Ruta y construirlo, luego el resto de la logica de la clase
                callback.addRoute();
            }
        });



        return view;



    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            callback = (DataListener) context;
        }
        catch (Exception e){
            throw new ClassCastException(context.toString() + "should implement DataListener");
        }
    }


    public interface DataListener {
        void sendData(int idRuta);
        void addRoute();

    }


}