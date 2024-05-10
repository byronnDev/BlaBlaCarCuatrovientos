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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.cuatrovientos.blablacar.R;
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
        btnSave = findViewById(R.id.btnAddInnerRoute);
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
                                    // Manejo de excepciones en caso de fallo en la llamada a la API
                                    filterResults.values = new ArrayList<String>(); // Lista vacía para evitar errores
                                    filterResults.count = 0;
                                    synchronized (filterResults) {
                                        filterResults.notifyAll();
                                    }
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
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            // Manejo de excepciones en caso de error al procesar la respuesta JSON
                                            filterResults.values = new ArrayList<String>(); // Lista vacía para evitar errores
                                            filterResults.count = 0;
                                            synchronized (filterResults) {
                                                filterResults.notifyAll();
                                            }
                                        }
                                    }
                                }
                            });
                            // Esperar hasta que se haya completado la llamada a la API y se haya procesado la respuesta
                            synchronized (filterResults) {
                                try {
                                    filterResults.wait(3000); // Tiempo máximo de espera en milisegundos
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            filterResults.values = streets;
                            filterResults.count = streets.size();
                        }
                        return filterResults;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        if (results != null && results.count > 0) {
                            notifyDataSetChanged();
                        } else {
                            notifyDataSetInvalidated();
                        }
                    }
                };
            }
        };

        actvStreetName.setAdapter(adapter);


        btnSave.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


    }
});

    }
}