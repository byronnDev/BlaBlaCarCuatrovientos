package org.cuatrovientos.blablacar.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import org.cuatrovientos.blablacar.R;

public class AddRouteActivity extends AppCompatActivity {
    Switch switchTipoRuta;
    //@SuppressLint("MissingInflatedId")
    EditText editTextcalle;
    EditText editTextNCalle;
    EditText editTextHuecos;
    TextView textViewTipoRuta;
    ImageButton btnAddInnerRoute;
    Button btnGoHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        switchTipoRuta =  findViewById(R.id.switch1);
        editTextcalle = findViewById(R.id.editTextcalle);
        editTextNCalle = findViewById(R.id.editTextNCalle);
        editTextHuecos = findViewById(R.id.editTextHuecos);
        textViewTipoRuta = findViewById(R.id.textoTipoRuta);
        btnAddInnerRoute = findViewById(R.id.btnAddInnerRoute);
        //cuando cambiamos el tipo de ruata el texto de la etiqueta cambia
        switchTipoRuta.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                textViewTipoRuta.setText("Ruta de ida");
            } else {
                textViewTipoRuta.setText("Ruta de vuelta");
            }
        });
        //cuando pulsamos el boton de añadir ruta se añade la ruta
        btnAddInnerRoute.setOnClickListener(v -> {
            //TODO
            //crear un objeto de tipo Ruta y construirlo, luego el resto de la logica de la clase
        });
        //cuando pulsamos el boton de ir a inicio volvemos a la pantalla de inicio
        btnGoHome = findViewById(R.id.buttonHome);
        btnGoHome.setOnClickListener(v -> {
            finish();
        });


    }
}