package org.cuatrovientos.blablacar.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.LogedUser;
import org.cuatrovientos.blablacar.models.User;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    public final int GOOGLE_SIGN_IN = 100;
    private ActivityResultLauncher<Intent> launcher;
    private GoogleSignInClient googleClient;
    Button login;
    Button register;
    EditText txtUser;
    EditText txtPass;
    List<User> tempUserList = new ArrayList<User>();

    LogedUser logedUser;
    ImageButton googleButton;
    ImageButton appleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.LoginBtnLogin);
        register = (Button) findViewById(R.id.LoginBtnRegister);
        txtUser = (EditText) findViewById(R.id.LoginTxtUsuario);
        txtPass = (EditText) findViewById(R.id.LoginTxtContrasena);
        googleButton = (ImageButton) findViewById(R.id.googleLogo);
        appleButton = (ImageButton) findViewById(R.id.appleLogo);

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
        onSignInWithGoogle();
    }

    private void onSignInWithGoogle() {
        // Inicializa GoogleSignInClient
        GoogleSignInOptions googleConfiguration = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleClient = GoogleSignIn.getClient(getApplicationContext(), googleConfiguration);

        // Configura el botón de Google Sign-In
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        // Registra el launcher para manejar el resultado de la actividad de Google Sign-In
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        handleSignInResult(data);
                    }
                }
        );
    }

    private void signInWithGoogle() {
        // Inicia el proceso de Google Sign-In
        Intent signInIntent = googleClient.getSignInIntent();
        launcher.launch(signInIntent);
    }

    private void handleSignInResult(Intent data) {
        try {
            // Obtiene la cuenta de Google
            GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);

            if (account != null) {
                // Obtiene el token de ID y autentica al usuario
                AuthCredential credentials = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                FirebaseAuth.getInstance().signInWithCredential(credentials).addOnCompleteListener(command -> {
                    if (command.isSuccessful()) {
                        // El inicio de sesión fue exitoso
                        goHome(getEmailFromGoogle());
                    } else {
                        // Mostrar alerta en caso de error
                        showAlert();
                    }
                });
            }
        } catch (ApiException e) {
            showAlert();
        }
    }

    @Nullable
    private String getEmailFromGoogle() {
        return GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getEmail();
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

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Se ha producido un error autenticando el usuario");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}