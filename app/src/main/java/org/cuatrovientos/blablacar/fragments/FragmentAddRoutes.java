package org.cuatrovientos.blablacar.fragments;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.adapters.RecyclerDataAdapter;
import org.cuatrovientos.blablacar.models.Route;

import java.util.ArrayList;
import java.util.List;

public class FragmentAddRoutes extends Fragment {

    List<Route> routesList = new ArrayList<Route>();
    RecyclerView recyclerView;
    DataListener callback;
    ImageButton btnAddRoute;
    FirebaseFirestore db;

    public FragmentAddRoutes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_routes, container, false);
        this.btnAddRoute = (ImageButton) view.findViewById(R.id.btnAddInnerRoute);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerRutas);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        this.db = FirebaseFirestore.getInstance();

        db.collection("routes")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        routesList.clear(); // Limpiar la lista antes de agregar nuevas rutas
                        for (DocumentSnapshot document : task.getResult()) {
                            Route route = document.toObject(Route.class);
                            route.setId_ruta(document.getId());
                            if (hasHuecos(route)) routesList.add(route);
                        }
                        RecyclerDataAdapter routesAdapter = new RecyclerDataAdapter(routesList, new RecyclerDataAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Route conten) {
                                String idRoute = conten.getId_ruta();
                                callback.sendData(idRoute);
                            }
                        });
                        recyclerView.setAdapter(routesAdapter);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }

                private boolean hasHuecos(Route route) {
                    return route.getHuecos() > 0;
                }
            });

        this.btnAddRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        void sendData(String idRuta);

        void addRoute();
    }
}