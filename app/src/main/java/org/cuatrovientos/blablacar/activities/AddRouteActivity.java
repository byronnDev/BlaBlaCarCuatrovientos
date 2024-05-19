package org.cuatrovientos.blablacar.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.app.MyApplication;
import org.cuatrovientos.blablacar.models.LoguedUser;
import org.cuatrovientos.blablacar.models.Route;
import org.cuatrovientos.blablacar.models.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddRouteActivity extends AppCompatActivity {

    private AutoCompleteTextView actvStreetName;
    private TextView tvLatitude;
    private TextView tvLongitude;
    private Switch switchTipoRuta;
    private List<String> streets;
    private Map<String, String[]> coordinates;
    private ArrayAdapter<String> adapter;
    private String API_KEY = "5b3ce3597851110001cf6248972084980f0c4a449993375528b81e72";

    private String[] instituteCoordinates = {"42.82437732771406", "-1.6598058201633434"};
    private ImageButton btnSave;
    private EditText etStreetNumber;
    private EditText etHuecos;
    private EditText etHoraSalida;

    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);




        actvStreetName = findViewById(R.id.editTextcalle);
        tvLatitude = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.tvLongitude);
        switchTipoRuta = findViewById(R.id.switch1);
        btnSave = findViewById(R.id.btnAddRoute);
        etStreetNumber = findViewById(R.id.editTextNCalle);
        etHuecos = findViewById(R.id.editTextHuecos);
        etHoraSalida = findViewById(R.id.editTextHoraSalida);
        streets = new ArrayList<>();
        coordinates = new HashMap<>();


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, streets) {
            public Filter getFilter() {
                return new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults filterResults = new FilterResults();
                        if (constraint != null) {
                            String userInput = constraint.toString();
                            OkHttpClient client = new OkHttpClient();
                            String url = "https://api.openrouteservice.org/geocode/search?api_key=" + API_KEY + "&text=" + userInput + "&boundary.country=ES";
                            Request request = new Request.Builder().url(url).build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                    // Handle exceptions in case of API call failure
                                    filterResults.values = new ArrayList<String>(); // Empty list to avoid errors
                                    filterResults.count = 0;
                                    notifyAdapter(filterResults);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                        String myResponse = response.body().string();
                                        try {
                                            JSONObject json = new JSONObject(myResponse);
                                            JSONArray features = json.getJSONArray("features");
                                            streets.clear();
                                            coordinates.clear();
                                            for (int i = 0; i < features.length(); i++) {
                                                JSONObject feature = features.getJSONObject(i);
                                                String streetName = feature.getJSONObject("properties").getString("label");
                                                streets.add(streetName);
                                                JSONArray coords = feature.getJSONObject("geometry").getJSONArray("coordinates");
                                                coordinates.put(streetName, new String[]{coords.getString(1), coords.getString(0)});
                                            }
                                            filterResults.values = streets;
                                            filterResults.count = streets.size();
                                            notifyAdapter(filterResults);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            // Handle exceptions in case of JSON processing error
                                            filterResults.values = new ArrayList<String>(); // Empty list to avoid errors
                                            filterResults.count = 0;
                                            notifyAdapter(filterResults);
                                        }
                                    }
                                }
                            });
                        }
                        return filterResults;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        // No need to implement this method here, as we're notifying the adapter from onResponse and onFailure methods
                    }

                    // Method to notify the adapter on the main thread
                    private void notifyAdapter(FilterResults filterResults) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (filterResults != null && filterResults.count > 0) {
                                    notifyDataSetChanged();
                                } else {
                                    notifyDataSetInvalidated();
                                }
                            }
                        });
                    }
                };
            }
        };

        actvStreetName.setAdapter(adapter);


       btnSave.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        // Crear una nueva instancia de Route
        final Route route = new Route();
        route.setId(MyApplication.rutaID.incrementAndGet()); // Asigna un ID único a la ruta

        // Verificar el estado del Switch
        if (switchTipoRuta.isChecked()) {
            // Si el Switch está activado, la ruta es de Cuatrovientos a la calle seleccionada
            route.setLugarInicio("Cuatrovientos");
            route.setLugarFin(actvStreetName.getText().toString());
        } else {
            // Si el Switch está desactivado, la ruta es de la calle seleccionada a Cuatrovientos
            route.setLugarInicio(actvStreetName.getText().toString());
            route.setLugarFin("Cuatrovientos");
        }

        route.setHoraSalida(new Date()); // Usa la fecha y hora actual
        route.setHuecos(Integer.parseInt(etHuecos.getText().toString())); // Configura el número de huecos
        route.setPropietario(LoguedUser.StaticLogedUser.getUser().getMail()); // Configura el propietario de la ruta como el usuario logueado

        // Ejecutar la operación de escritura en un hilo en segundo plano
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Obtener una instancia de Realm en este hilo
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealm(route);
                    }
                });
                realm.close();
            }
        }).start();

        // Mostrar un mensaje de éxito
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AddRouteActivity.this, "Ruta guardada con éxito", Toast.LENGTH_SHORT).show();
            }
        });
    }
});

    }
}