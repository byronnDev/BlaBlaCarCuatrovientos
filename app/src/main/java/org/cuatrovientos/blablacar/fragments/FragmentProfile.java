package org.cuatrovientos.blablacar.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.activities.Login;
import org.cuatrovientos.blablacar.utils.dbQuery;

import java.util.HashMap;
import java.util.Map;

public class FragmentProfile extends Fragment {
    Button btnLogout;
    private TextView nombre;
    private TextView email;
    FirebaseFirestore db;
    Map<String, Object> user = new HashMap<>();
    public FragmentProfile() {
        // Required empty public constructor
        db = FirebaseFirestore.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        //el nopmbre del usuario se obtiene del mail por un substring
        nombre = view.findViewById(R.id.txtNombre);
        email = view.findViewById(R.id.txtMail);

        // Se obtiene el usuario de la base de datos
        dbQuery dbQuery = new dbQuery(requireContext());
        String dbMail = dbQuery.getCurrentUserEmail();

        email.setText(dbMail);
        db.collection("users").document(dbMail)
                .get().addOnSuccessListener(v -> {
                    if (v.exists()) {
                        user = v.getData();
                        nombre.setText(user.get("name").toString());
                    }
                });


        btnLogout = (Button) view.findViewById(R.id.btnLogOut);
        onLogout();
        return view;
    }

    private void onLogout() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                returnLoginActivity();
            }
        });
    }

    private void returnLoginActivity() {
        Intent intent = new Intent(requireContext(), Login.class);
        startActivity(intent);
    }

    public void renderData() {
        //TODO
        //crear un objeto de tipo Ruta y construirlo, luego el resto de la logica de la clase


    }
}