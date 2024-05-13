package org.cuatrovientos.blablacar.activities;

import static org.cuatrovientos.blablacar.utils.Utils.getDummyData;

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
import org.cuatrovientos.blablacar.models.LoguedUser;
import org.cuatrovientos.blablacar.models.User;

import io.realm.Realm;
import io.realm.RealmResults;

public class Login extends AppCompatActivity {
    private ActivityResultLauncher<Intent> launcher;
    Button login;
    Button register;
    EditText txtUser;
    EditText txtPass;
    Realm realm;
    RealmResults<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.LoginBtnLogin);
        register = (Button) findViewById(R.id.btnRegister);
        txtUser = (EditText) findViewById(R.id.txtMail);
        txtPass = (EditText) findViewById(R.id.txtPass);

        realm = Realm.getDefaultInstance();

//        purgeData(); // TODO: Eliminar esta línea en producción
        chargeDummy();

        setupSignInButtons(); // Configura los botones de inicio de sesión
    }
    private void chargeDummy() {
        userList = realm.where(User.class).findAll();
        if (userList.size() == 0) {
            realm.beginTransaction();
            realm.copyToRealm(getDummyData());
            realm.commitTransaction();
        }
        realm.close();
    }

    private void purgeData() {
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
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
            }
        });
    }

    private void authUserWithDatabase(String userEmail, String password) {
        // Abrir una instancia de Realm
        Realm realm = Realm.getDefaultInstance();
        try {
            // Buscar el usuario por email
            User user = realm.where(User.class).equalTo("mail", userEmail).findFirst();
            if (user != null && user.getPass().equals(User.hashPassword(password))) {
                // Guardar el usuario logueado
                LoguedUser.setUser(user);
                // Si el usuario existe y la contraseña es correcta
                goHome();
            } else {
                // Mostrar alerta si el usuario no existe o la contraseña es incorrecta
                showAlert("Error", "Usuario o contraseña incorrectos.");
            }
        } finally {
            // Asegúrate de cerrar Realm en el hilo actual
            realm.close();
        }
    }

    private boolean areValidFields() {
        return !txtUser.getText().toString().isEmpty() && !txtPass.getText().toString().isEmpty();
    }

    private void goHome() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}