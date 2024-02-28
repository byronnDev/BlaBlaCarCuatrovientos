package org.cuatrovientos.blablacar.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.LogedUser;
import org.cuatrovientos.blablacar.models.User;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;

public class Login extends AppCompatActivity {
    Button login;
    Button register;
    EditText txtUser;
    EditText txtPass;
    List<User> tempUserList = new ArrayList<User>();

    LogedUser logedUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.LoginBtnLogin);
        register = (Button) findViewById(R.id.LoginBtnRegister);
        txtUser = (EditText) findViewById(R.id.LoginTxtUsuario);
        txtPass = (EditText) findViewById(R.id.LoginTxtContrasena);
        //borrar mas tarde
        tempUserList.add(new User("usuario1@example.com"));
        tempUserList.add(new User("usuario2@example.com"));
        tempUserList.add(new User("usuario3@example.com"));
        //borrar mas tarde

        setup();
    }

    private void setup() {
        // Botones inicio sesión
        setRegisterFunction();
        setLoginFunction();

        // Otras opciones de Inicio de sesión
        SignInWithGoogle();
    }

    private void SignInWithGoogle() {
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
                if (areValidFields()){
                    authUserAndAddToDatabase(txtUser.getText().toString(), txtPass.getText().toString());
                }
            }
        });
    }

    private void authUserWithDatabase(String user, String pass) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(user, pass).addOnCompleteListener(command -> {
            if (command.isSuccessful()) {
                logedUser.setLogedUser(new User(user));
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

        intent.putExtra("email", email);
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