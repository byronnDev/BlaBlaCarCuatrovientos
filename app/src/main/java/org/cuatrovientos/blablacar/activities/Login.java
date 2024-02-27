package org.cuatrovientos.blablacar.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.cuatrovientos.blablacar.R;

public class Login extends AppCompatActivity {
    Button LogOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LogOn = (Button) findViewById(R.id.LoginBtnLogin);

        LogOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });
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

}