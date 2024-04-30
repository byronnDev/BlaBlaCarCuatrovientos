package org.cuatrovientos.blablacar.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
    private ImageView imgUsuario;
    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView o2Points;

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
        imgUsuario = view.findViewById(R.id.imgUsuario);
        o2Points = view.findViewById(R.id.txtO2Points);

        // TODO No está pillando los datos del usuario ni actualizándo la imagen
        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        user = documentSnapshot.getData();
                        if (user != null) {
                            email.setText(user.containsKey("mail") ? user.get("mail").toString() : "");
                            String nombreYApellido = (user.containsKey("name") ? user.get("name").toString() : "") + " " +
                                    (user.containsKey("surname") ? user.get("surname").toString() : "");
                            nombre.setText(nombreYApellido);
                            if (user.containsKey("picture")) {
                                String imgUrl = user.get("picture").toString();
                                if (imgUrl != null) {
                                    imgUsuario.setImageURI(Uri.parse(imgUrl));
                                }
                            }
                            o2Points.setText(user.containsKey("o2Points") ? user.get("o2Points").toString() : "");
                        }
                    }
                });
        imgUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent para abrir la galería de imágenes
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
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