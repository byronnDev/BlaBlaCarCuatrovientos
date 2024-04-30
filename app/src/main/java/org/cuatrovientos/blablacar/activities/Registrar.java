package org.cuatrovientos.blablacar.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Registrar extends AppCompatActivity {
    private FirebaseFirestore db;
    private TextView txtName;
    private TextView txtSurname;
    private TextView txtMail;
    private TextView txtPass;
    private TextView txtPhone;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        setup();

        db = FirebaseFirestore.getInstance(); // Inicializar Firestore

        onRegister();
    }

    private void onRegister() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputValid()) registerUser();
            }
        });
    }

    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isInputValid() {
        return !TextUtils.isEmpty(txtName.getText())
                && !TextUtils.isEmpty(txtSurname.getText())
                && isEmailValid(txtMail.getText())
                && isPasswordValid(txtPass.getText())
                && isPhoneValid(txtPhone.getText());
    }

    private boolean isPhoneValid(CharSequence phone) {
        if (phone.toString().isEmpty()) {
            showErrorToast("Por favor, introduce un número de teléfono.");
            return false;
        }

        if (phone.length() < 9) {
            showErrorToast("Por favor, introduce un número de teléfono válido.");
            return false;
        }

        return true;
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(CharSequence password) {
        if (password.toString().isEmpty()) {
            showErrorToast("Por favor, introduce una contraseña.");
            return false;
        }

        if (password.length() < 6) {
            showErrorToast("La contraseña debe tener al menos 6 caracteres.");
            return false;
        }

        // Caracter especial, número y mayúscula
        if (!password.toString().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$")) {
            showErrorToast("La contraseña debe tener al menos un número, una mayúscula y un carácter especial.");
            return false;
        }

        return true;
    }

    private void registerUser() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(txtMail.getText().toString(), txtPass.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (currentUser != null) {
                            addUserToDatabase();
                            goHome();
                        } else {
                            showErrorToast("Error al registrar el usuario.");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showErrorToast("Error al crear la cuenta: " + e.getMessage());
                    }
                });
    }

    private void addUserToDatabase() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", txtName.getText().toString());
        user.put("surname", txtSurname.getText().toString());
        user.put("mail", txtMail.getText().toString());
        user.put("phone", txtPhone.getText().toString());
        // Start with 0 O2 points and an empty list of routes
        user.put("O2Points", 0);
        user.put("Routes", new ArrayList<Route>());

        db.collection("users").document(txtMail.getText().toString()).set(user)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showErrorToast("Error al guardar los datos del usuario en la base de datos: " + e.getMessage());
                    }
                });
    }

    private void setup() {
        txtName = findViewById(R.id.txtName);
        txtSurname = findViewById(R.id.txtSurname);
        txtMail = findViewById(R.id.txtMail);
        txtPass = findViewById(R.id.txtPass);
        txtPhone = findViewById(R.id.txtPhone);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void showErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
