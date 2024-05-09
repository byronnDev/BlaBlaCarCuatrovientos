package org.cuatrovientos.blablacar.fragments;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
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
    private org.cuatrovientos.blablacar.utils.dbQuery dbQuery;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        db = FirebaseFirestore.getInstance();
        dbQuery = new dbQuery(getContext());
        btnUnirse = view.findViewById(R.id.btnUnirse);
        tvLugarInicio = view.findViewById(R.id.tvLugarInicio);
        tvLugarFin = view.findViewById(R.id.tvLugarFin);
        tvHuecos = view.findViewById(R.id.tvHuecos);

        return view;
    }

    public void renderData(String idRuta) {
        dbQuery.getUserDataFromFirestore(dbQuery.getCurrentUserEmail(), new org.cuatrovientos.blablacar.utils.dbQuery.UserDataSuccessListener() {
            @Override
            public void onUserDataReceived(User userData) {
                user = userData;
            }

            @Override
            public void onUserDataError(String errorMessage) {
                // Manejar el error aquí
            }
        });

        db.collection("routes").document(String.valueOf(idRuta))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        route = documentSnapshot.toObject(Route.class);
                        tvLugarInicio.setText(route.getLugarInicio());
                        tvLugarFin.setText(route.getLugarFin());
                        tvHuecos.setText(String.valueOf(route.getHuecos()));
                    }
                });

        btnUnirse.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        // Obtén una instancia de FirebaseFirestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Define la ID de la ruta que deseas agregar al usuario
        String idRutapuntarse = idRuta; // Reemplaza con el ID real de la ruta que deseas agregar

        // Define el correo del usuario que desea unirse a la ruta
        String correoUsuario = user.getMail();

        // Obtén una referencia al documento del usuario con el correo especificado
        DocumentReference usuarioRef = db.collection("users").document(correoUsuario);

        // Obtén una referencia al documento de la ruta con la ID especificada
        DocumentReference rutaRef = db.collection("routes").document(idRutapuntarse);

        // Actualiza el array routesSubscribed del documento del usuario con el ID de la nueva ruta
        usuarioRef.update("routesSubscribed", FieldValue.arrayUnion(idRutapuntarse))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onSuccess(Void aVoid) {
                        // La ruta se agregó exitosamente al array routesSubscribed del usuario
                        Log.d(TAG, "ID de ruta agregada al array routesSubscribed del usuario correctamente");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Maneja cualquier error que ocurra al agregar el ID de la ruta al array routesSubscribed del usuario
                        Log.w(TAG, "Error al agregar el ID de ruta al array routesSubscribed del usuario", e);
                    }
                });

        // Actualiza el array usuariosApuntados del documento de la ruta con el correo del nuevo usuario
        rutaRef.update("usuariosApuntados", FieldValue.arrayUnion(correoUsuario))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onSuccess(Void aVoid) {

                        // El correo del usuario se agregó exitosamente al array usuariosApuntados de la ruta
                        Log.d(TAG, "Correo de usuario agregado al array usuariosApuntados de la ruta correctamente");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("RestrictedApi")

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Maneja cualquier error que ocurra al agregar el correo del usuario al array usuariosApuntados de la ruta
                        Log.w(TAG, "Error al agregar el correo de usuario al array usuariosApuntados de la ruta", e);
                    }
                });
    }
});
    }

    public interface DataListener {
        void sendData(int idRuta);

    }
}