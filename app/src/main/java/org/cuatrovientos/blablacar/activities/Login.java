package org.cuatrovientos.blablacar.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

import org.cuatrovientos.blablacar.R;

public class Login extends AppCompatActivity {
    Button login;
    Button register;
    EditText txtUser;
    EditText txtPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.LoginBtnLogin);
        register = (Button) findViewById(R.id.LoginBtnRegister);
        txtUser = (EditText) findViewById(R.id.LoginTxtUsuario);
        txtPass = (EditText) findViewById(R.id.LoginTxtContrasena);

        setup();
    }

    private void setup() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areValidFields()){
                    authUserAndAddToDatabase(txtUser.getText().toString(), txtPass.getText().toString());
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areValidFields()) {
                    authUserWithDatabase(txtUser.getText().toString(), txtPass.getText().toString());
                }
            }
        });
    }

    private void authUserWithDatabase(String user, String pass) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(user, pass).addOnCompleteListener(command -> {
            if (command.isSuccessful()) {
                goHome();
            } else {
                showAlert();
            }
        });
    }

    private void authUserAndAddToDatabase(String user, String pass) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(user, pass).addOnCompleteListener(command -> {
            if (command.isSuccessful()) {
                goHome();
            } else {
                showAlert();
            }
        });
    }

    private boolean areValidFields() {
        return !txtUser.getText().toString().isEmpty() && !txtPass.getText().toString().isEmpty();
    }

    private void goHome() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        // Set the new task and clear flags
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Start the new activity
        startActivity(intent);
        // Finish the current activity
        finish();
    }

    private class GoogleProvider {
        public GoogleProvider() {

        }
    }

    private class AppleProvider {
        public AppleProvider(){
        }
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Se ha producido un error autenticando el usuario");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}