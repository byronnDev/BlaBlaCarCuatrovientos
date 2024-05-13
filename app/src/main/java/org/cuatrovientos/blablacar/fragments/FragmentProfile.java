package org.cuatrovientos.blablacar.fragments;

import android.content.Context;
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
import androidx.fragment.app.Fragment;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.activities.Login;
import org.cuatrovientos.blablacar.models.User;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import io.realm.Realm;
import io.realm.RealmResults;

public class FragmentProfile extends Fragment {
    private Button btnLogout;
    private TextView nombre;
    private TextView username;
    private TextView email;
    private TextView phone;
    private ImageView imgUsuario;
    private TextView o2Points;
    private User loggedUser;
    private Realm realm;
    ActivityResultLauncher<String> imagePickerLauncher;

    public FragmentProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        fixSizeError(100); // 100MB CursorWindow size limit to avoid SQLiteBlobTooBigException

        setup(view);
        cargarDatosUsuario();
        onLogout();
        imageSetFunction();

        return view;
    }

    private void setup(View view) {
        nombre = view.findViewById(R.id.txtNombre);
        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.txtMail);
        phone = view.findViewById(R.id.txtPhone);
        imgUsuario = view.findViewById(R.id.imgUsuario);
        o2Points = view.findViewById(R.id.txtO2Points);
        btnLogout = view.findViewById(R.id.btnLogOut);
        realm = Realm.getDefaultInstance();

        // Initialize the image picker launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(requireActivity().getContentResolver().openInputStream(uri));
                            imgUsuario.setImageBitmap(bitmap);
                            guardarImagenUsuario(bitmap); // Save the user's image locally
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    private void onLogout() {
        btnLogout.setOnClickListener(v -> {
            realm.executeTransaction(r -> {
                // Clear the logged-in user's data
                r.where(User.class).findAll().deleteAllFromRealm();
            });
            returnLoginActivity();
        });
    }

    private void returnLoginActivity() {
        Intent intent = new Intent(requireContext(), Login.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void cargarDatosUsuario() {
        // Load the user from Realm; replace this with your logic to identify the logged-in user
        loggedUser = realm.where(User.class).findFirst();

        if (loggedUser != null) {
            nombre.setText(loggedUser.getName());
            username.setText(loggedUser.getName() + " " + loggedUser.getSurname());
            email.setText(loggedUser.getMail());
            phone.setText(loggedUser.getPhone());
            if (loggedUser.getO2Points() != null) {
                o2Points.setText(String.valueOf(loggedUser.getO2Points()));
            } else {
                o2Points.setText("N/A");
            }

            cargarImagenDesdeAlmacenamientoLocal(); // Load the image from local storage
        } else {
            mostrarMensajeError("No hay una sesión activa");
        }
    }

    private void guardarImagenUsuario(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageData = baos.toByteArray();

        try {
            FileOutputStream fos = requireContext().openFileOutput("profile_image.png", Context.MODE_PRIVATE);
            fos.write(imageData);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap cargarImagenLocal() {
        Bitmap bitmap = null;
        try {
            FileInputStream fis = requireContext().openFileInput("profile_image.png");
            bitmap = BitmapFactory.decodeStream(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensajeError("Error al cargar la imagen");
        }
        return bitmap;
    }

    private void cargarImagenDesdeAlmacenamientoLocal() {
        Bitmap bitmap = cargarImagenLocal();
        if (bitmap != null) {
            imgUsuario.setImageBitmap(bitmap);
        }
    }

    private void imageSetFunction() {
        // Handle image click to select a new image
        imgUsuario.setOnClickListener(v -> abrirSelectorImagen());
    }

    private void abrirSelectorImagen() {
        // Launch the image picker
        imagePickerLauncher.launch("image/*");
    }

    private static void fixSizeError(int mb) {
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, mb * 1024 * 1024); // the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarMensajeError(String mensaje) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show()
        );
    }
}