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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.activities.Login;
import org.cuatrovientos.blablacar.models.LoguedUser;
import org.cuatrovientos.blablacar.models.User;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

public class FragmentProfile extends Fragment {
    private Button btnLogout;
    private TextView nombre;
    private TextView username;
    private TextView email;
    private TextView phone;
    private ImageView imgUsuario;
    private TextView o2Points;



    public FragmentProfile() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        fixSizeError(100); // 100MB CursorWindow size limit to avoid SQLiteBlobTooBigException


        // Initialize views
        nombre = (TextView) view.findViewById(R.id.txtNombre);
        username = (TextView) view.findViewById(R.id.username);
        email = (TextView) view.findViewById(R.id.txtMail);
        phone = (TextView) view.findViewById(R.id.txtPhone);
        imgUsuario = (ImageView) view.findViewById(R.id.imgUsuario);
        o2Points = (TextView) view.findViewById(R.id.txtO2Points);
        btnLogout = (Button) view.findViewById(R.id.btnLogOut);

        User logedUser = LoguedUser.getUser();

        nombre.setText(logedUser.getName().toString());
        username.setText(logedUser.getName().toString() + " " + logedUser.getSurname().toString());
        email.setText(logedUser.getMail().toString());
        phone.setText(logedUser.getPhone().toString());
        o2Points.setText(logedUser.getO2Points().toString());

        //recicler con las rutas del propietario



        onLogout();

        return view;
    }

    private static void fixSizeError(int mb) {
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, mb * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onLogout() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoguedUser.setUser(new User());
                returnLoginActivity();
            }
        });
    }

    private void returnLoginActivity() {
        Intent intent = new Intent(requireContext(), Login.class);
        startActivity(intent);
    }
}