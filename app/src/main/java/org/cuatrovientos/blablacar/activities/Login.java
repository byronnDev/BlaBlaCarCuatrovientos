package org.cuatrovientos.blablacar.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import org.cuatrovientos.blablacar.R;

public class Login extends AppCompatActivity {
    private ActivityResultLauncher<Intent> launcher;
    Button login;
    Button register;
    EditText txtUser;
    EditText txtPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.LoginBtnLogin);
        register = (Button) findViewById(R.id.btnRegister);
        txtUser = (EditText) findViewById(R.id.txtMail);
        txtPass = (EditText) findViewById(R.id.txtPass);

        setupSignInButtons(); // Configura los botones de inicio de sesión

        /* Añadir un usuario a la base de datos
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("name", null);
        usuario.put("username", null);
        usuario.put("bio", null);
        usuario.put("image", null);
        usuario.put("mail", null);
        usuario.put("gender", null);
        usuario.put("age", null);
        usuario.put("phone", null);
        usuario.put("birthDate", new Date());

        // Primero crea la tabla usuarios si no existe, sino mete los datos usando mail como clave
        db.collection("users").document("mail").set(usuario);

        */

        /*
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                }
            }
        });*/
        //borrar mas tarde

    }

    private void setupSignInButtons() {
        // Botones inicio sesión
        setRegisterFunction();
        setLoginFunction();
    }

    private void setLoginFunction() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areValidFields()) {
                    authUserWithDatabase(txtUser.getText().toString(), txtPass.getText().toString());
                }
            }
        });
    }

    private void setRegisterFunction() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registrar.class);
                startActivity(intent);
                finish();
//                if (areValidFields()){
//                    authUserAndAddToDatabase(txtUser.getText().toString(), txtPass.getText().toString());
//                }
            }
        });
    }

    private void authUserWithDatabase(String user, String pass) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(user, pass).addOnCompleteListener(command -> {
            if (command.isSuccessful()) {
                goHome(txtUser.getText().toString());
            } else {
                showAlert();
            }
        });
    }

    private void authUserAndAddToDatabase(String user, String pass) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(user, pass).addOnCompleteListener(command -> {
            if (command.isSuccessful()) {
                goHome(txtUser.getText().toString());
            } else {
                showAlert();
            }
        });
    }

    private boolean areValidFields() {
        return !txtUser.getText().toString().isEmpty() && !txtPass.getText().toString().isEmpty();
    }

    private void goHome(String email) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        // Start the new activity
        startActivity(intent);
        // Finish the current activity
        finish();
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