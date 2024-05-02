package org.cuatrovientos.blablacar.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.activities.Login;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class FragmentProfile extends Fragment {
    private Button btnLogout;
    private TextView nombre;
    private TextView email;
    private ImageView imgUsuario;
    private TextView o2Points;

    private FirebaseFirestore db;

    public FragmentProfile() {
        // Required empty public constructor
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        nombre = view.findViewById(R.id.txtNombre);
        email = view.findViewById(R.id.txtMail);
        imgUsuario = view.findViewById(R.id.imgUsuario);
        o2Points = view.findViewById(R.id.txtO2Points);
        btnLogout = view.findViewById(R.id.btnLogOut);

        // Load user data
        cargarDatosUsuario();

        // Handle logout button click
        onLogout();

        // Handle image click to select a new image
        imgUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirSelectorImagen();
            }
            private void abrirSelectorImagen() {
                imagePickerLauncher.launch("image/*");
            }

            // Declare an ActivityResultLauncher for image selection
            ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            // Handle the selected image URI here
                            imgUsuario.setImageURI(uri);
                            imgUsuario.setClipToOutline(true);
                            imgUsuario.setBackgroundResource(R.drawable.rounded_corner);
                            // Ajustar la escala de la imagen para que ocupe todo el espacio dentro del ImageView
                            imgUsuario.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            // Guardamos la url de la imagen en la bbdd en el usuario actual
                            db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).update("picture", uri);
                        }
                    }
            );
        });

        return view;
    }

    private void cargarDatosUsuario() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String mail = currentUser.getEmail();
            obtenerDatosUsuario(mail);
        } else {
            mostrarMensajeError("No hay una sesión activa");
        }
    }

    private void obtenerDatosUsuario(String mail) {
        CollectionReference usersRef = db.collection("users");
        usersRef.document(mail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> userData = documentSnapshot.getData();
                    actualizarInterfazUsuario(userData);
                } else {
                    mostrarMensajeError("No se encontraron datos del usuario");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mostrarMensajeError("Error al obtener datos del usuario");
            }
        });
    }

    private void actualizarInterfazUsuario(Map<String, Object> userData) {
        // Update user interface with user data
        email.setText(getStringFromMap(userData, "mail"));
        String nombreYApellido = getStringFromMap(userData, "name") + " " + getStringFromMap(userData, "surname");
        nombre.setText(nombreYApellido);

        String imgUrl = getStringFromMap(userData, "picture");
        Bitmap bitmap = getBitmapFromURL(imgUrl);
        if (bitmap != null) {
            imgUsuario.setImageBitmap(bitmap);
        }

        String o2PointsText = getStringFromMap(userData, "o2Points");
        if (!o2PointsText.isEmpty()) {
            o2Points.setText(o2PointsText);
        }
    }

    private String getStringFromMap(Map<String, Object> map, String key) {
        return map.containsKey(key) ? map.get(key).toString() : "";
    }

    private void mostrarMensajeError(String mensaje) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Bitmap getBitmapFromURL(String url) {
        try {
            URL imageUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            return null;
        }
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
}