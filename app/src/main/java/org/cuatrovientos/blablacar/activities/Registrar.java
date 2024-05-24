package org.cuatrovientos.blablacar.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.User;

import io.realm.Realm;

public class Registrar extends AppCompatActivity {
    private EditText txtName;
    private EditText txtSurname;
    private EditText txtMail;
    private EditText txtPass;
    private EditText txtPhone;
    private Button btnRegister;
    private Realm realm;

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
                if (isInputValid()) {
                    registerUser();
                }
            }
        });
    }

    private void registerUser() {
        String name = txtName.getText().toString().trim();
        String surname = txtSurname.getText().toString().trim();
        String mail = txtMail.getText().toString().trim();
        String pass = txtPass.getText().toString().trim();
        String phone = txtPhone.getText().toString().trim();

        if (isEmailExist(mail)) {
            showErrorToast("Este email ya está registrado. Por favor, usa otro.");
            return;
        }

        realm.executeTransactionAsync(r -> {
            // Create a new User object
            User user = r.createObject(User.class, mail);  // Ensure your User class has a primary key
            user.setName(name);
            user.setSurname(surname);
            user.setPass(User.hashPassword(pass));
            user.setPhone(phone);
            user.setO2Points(0);
        }, () -> {
            // Transaction was successful
            Toast.makeText(this, "Usuario registrado con éxito!", Toast.LENGTH_SHORT).show();
            goHome();
        }, error -> {
            // Transaction failed
            Log.e(TAG, "Error registering user: " + error.getMessage(), error);
            Toast.makeText(this, "Error al registrar el usuario. Por favor, inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
        });
    }

    private void goHome() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    private boolean isInputValid() {
        String name = txtName.getText().toString().trim();
        String surname = txtSurname.getText().toString().trim();
        String mail = txtMail.getText().toString().trim();
        String pass = txtPass.getText().toString();
        String phone = txtPhone.getText().toString().trim();

        if (name.isEmpty()) {
            showErrorToast("Por favor, introduce un nombre.");
            return false;
        }
        if (surname.isEmpty()) {
            showErrorToast("Por favor, introduce un apellido.");
            return false;
        }
        if (mail.isEmpty()) {
            showErrorToast("Por favor, introduce un email.");
            return false;
        }
        if (!isPasswordValid(pass)) {
            // showErrorToast called within isPasswordValid
            return false;
        }
        if (!isPhoneValid(phone)) {
            // showErrorToast called within isPhoneValid
            return false;
        }
        return true;
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

    private boolean isEmailExist(String email) {
        User user = realm.where(User.class).equalTo("mail", email).findFirst();
        return user != null;
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

        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%.^&+=!])(?=\\S+$).{6,}$")) {
            showErrorToast("La contraseña debe tener al menos un número, una mayúscula y un carácter especial.");
            return false;
        }

        if (password.contains(" ")) {
            showErrorToast("La contraseña no puede contener espacios en blanco.");
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
