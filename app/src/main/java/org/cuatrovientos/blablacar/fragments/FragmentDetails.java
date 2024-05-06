package org.cuatrovientos.blablacar.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.Route;
import org.cuatrovientos.blablacar.models.User;

public class FragmentDetails extends Fragment {
    private FirebaseFirestore db;
    private Button btnUnirse;
    private Route route;
    private TextView tvLugarInicio;
    private TextView tvLugarFin;
    private TextView tvHuecos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        db = FirebaseFirestore.getInstance();
        btnUnirse = view.findViewById(R.id.btnUnirse);
        tvLugarInicio = view.findViewById(R.id.tvLugarInicio);
        tvLugarFin = view.findViewById(R.id.tvLugarFin);
        tvHuecos = view.findViewById(R.id.tvHuecos);

        return view;
    }

    public void renderData(String idRuta) {

        db.collection("routes").document(String.valueOf(idRuta))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        route = documentSnapshot.toObject(Route.class);
                        tvLugarInicio.setText(route.getLugarInicio());
                        tvLugarFin.setText(route.getLugarFin());
                        tvHuecos.setText(route.getHuecos());
                    }
                });
        btnUnirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (route != null) {
                    // Obtén el usuario actual
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        db.collection("users").document(currentUser.getUid())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        User currentAppUser = documentSnapshot.toObject(User.class);
                                        if (currentAppUser != null) {
                                            route.apuntarUsuario(currentAppUser);
                                            currentAppUser.apuntarRuta(route);
                                            db.collection("routes").document(String.valueOf(route.getId_ruta())).set(route);
                                            db.collection("users").document(currentUser.getUid()).set(currentAppUser);
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    public interface DataListener {
        void sendData(int idRuta);

    }
}