package org.cuatrovientos.blablacar.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.cuatrovientos.blablacar.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class Registrar extends AppCompatActivity {
    private EditText txtName;
    private EditText txtSurname;
    private EditText txtMail;
    private EditText txtPass;
    private EditText txtPhone;
    private Button btnRegister;
    Realm realm;
    RealmResults<User> realmList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        realm = Realm.getDefaultInstance();
        setup();
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
    private void registerUser() {
        String name = txtName.getText().toString();
        String surname = txtSurname.getText().toString();
        String mail = txtMail.getText().toString();
        String pass = txtPass.getText().toString();
        String phone = txtPhone.getText().toString();
        User user = new User(name,surname,mail,pass,phone);
        realm.beginTransaction();
        realm.copyToRealm(user);
        realm.commitTransaction();
        realm.close();
        goHome();
    }

    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isInputValid() {
        return !TextUtils.isEmpty(txtName.getText().toString())
                && !TextUtils.isEmpty(txtSurname.getText().toString())
                && isEmailValid(txtMail.getText().toString())
                && isPasswordValid(txtPass.getText().toString())
                && isPhoneValid(txtPhone.getText().toString());
    }

    private boolean isPhoneValid(String phone) {
        if (phone.isEmpty()) {
            showErrorToast("Por favor, introduce un número de teléfono.");
            return false;
        }

        if (phone.length() < 9) {
            showErrorToast("Por favor, introduce un número de teléfono válido.");
            return false;
        }

        return true;
    }

    private boolean isEmailValid(String email) {
        User user = realm.where(User.class).equalTo("mail", email).findFirst();
        if (user != null){
            return true;
        }
        return false;
    }

    private boolean isPasswordValid(String password) {
        if (password.isEmpty()) {
            showErrorToast("Por favor, introduce una contraseña.");
            return false;
        }

        if (password.length() < 6) {
            showErrorToast("La contraseña debe tener al menos 6 caracteres.");
            return false;
        }

        // Caracter especial, número y mayúscula
        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$")) {
            showErrorToast("La contraseña debe tener al menos un número, una mayúscula y un carácter especial.");
            return false;
        }

        if (password.contains(" ")){
            showErrorToast("La contraseña no puede neten espacios en blanco.");
            return false;
        }

        return true;
    }


    private void setup() {
        txtName = (EditText) findViewById(R.id.txtName);
        txtSurname = (EditText) findViewById(R.id.txtSurname);
        txtMail = (EditText) findViewById(R.id.txtMail);
        txtPass = (EditText) findViewById(R.id.txtPass);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        btnRegister = (Button) findViewById(R.id.btnRegister);
    }

    private void showErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
