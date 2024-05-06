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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.adapters.RecyclerDataAdapter;
import org.cuatrovientos.blablacar.models.Route;
import org.cuatrovientos.blablacar.utils.dbQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentYourRoutes extends Fragment {
    RecyclerView recyclerView;
    List<Route> routesList = new ArrayList<Route>();
    DataListener callback;
    FirebaseFirestore db;

    public FragmentYourRoutes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_routes, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.tusRutas);
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
                                // Por alguna razón no pilla el propietario por defecto. Así que se lo asigno de prueba
                                route.setPropietoario(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
                                route.setId_ruta(document.getId());
                                if (hasHuecos(route) && isCurrentUserRoute(route)) routesList.add(route);
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

        return view;
    }
    private boolean isCurrentUserRoute(Route route) {
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        return currentUserEmail != null && currentUserEmail.equals(route.getPropietoario());
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