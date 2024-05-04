package org.cuatrovientos.blablacar.fragments;

import android.content.Intent;
import android.database.CursorWindow;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

public class FragmentProfile extends Fragment {
    private Button btnLogout;
    private TextView nombre;
    private TextView email;
    private TextView phone;
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
        setNewSizeImage(100); // 100MB


        // Initialize views
        nombre = view.findViewById(R.id.txtNombre);
        email = view.findViewById(R.id.txtMail);
        phone = view.findViewById(R.id.txtPhone);
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

            // Añadir ActivityResultLauncher para seleccionar imagen de perfil
            ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        // Handle the returned Uri
                        if (uri != null) {
                            try {
                                Bitmap bitmap = BitmapFactory.decodeStream(requireActivity().getContentResolver().openInputStream(uri));
                                imgUsuario.setImageBitmap(bitmap);
                                guardarImagenUsuario(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
        });

        return view;
    }

    private static void setNewSizeImage(int mb) {
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, mb * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void guardarImagenUsuario(Bitmap bitmap) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            currentUser.updateProfile(new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                    .setPhotoUri(currentUser.getPhotoUrl())
                    .build()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mostrarMensajeError("Imagen de perfil actualizada correctamente");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mostrarMensajeError("Error al actualizar la imagen de perfil");
                }
            });
        } else {
            mostrarMensajeError("No hay una sesión activa");
        }
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

        phone.setText(getStringFromMap(userData, "phone"));

        String o2PointsText = getStringFromMap(userData, "O2Points");
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