package org.cuatrovientos.blablacar.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.activities.Login;
import org.cuatrovientos.blablacar.activities.MainActivity;

import java.util.Objects;

public class FragmentProfile extends Fragment {
    Button btnLogout;
    public FragmentProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
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