package org.cuatrovientos.blablacar.activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.Route;
import org.cuatrovientos.blablacar.models.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.cuatrovientos.blablacar.utils.dbQuery;


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
    private FirebaseFirestore db;
    private String[] instituteCoordinates = {"42.82437732771406", "-1.6598058201633434"};
    private ImageButton btnSave;
    private EditText etStreetNumber;
    private EditText etHuecos;
    private EditText etHoraSalida;
    private dbQuery dbQuery;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);

        db = FirebaseFirestore.getInstance();
        dbQuery = new dbQuery(this);

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
            @Override
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
                            }
                        }
                    }
                });
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

        dbQuery.getUserDataFromFirestore(dbQuery.getCurrentUserEmail(), new org.cuatrovientos.blablacar.utils.dbQuery.UserDataSuccessListener() {
            @Override
            public void onUserDataReceived(User userData) {
                user = userData;
            }

            @Override
            public void onUserDataError(String errorMessage) {
                // Manejar el error aquí
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        db.collection("counters").document("routes")
            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Long count = documentSnapshot.getLong("count");
                    if (count == null) {
                        count = 0L;
                    }
                    String streetName = actvStreetName.getText().toString();
                    int huecos = 0;
                    try {
                        huecos = Integer.parseInt(etHuecos.getText().toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        // Mostrar un mensaje al usuario indicando que el número de huecos no es válido
                        return;
                    }
                    Route route = new Route();
                    if (switchTipoRuta.isChecked()) {
                        route.setLugarInicio(streetName);
                        route.setLugarFin("Instituto");
                    } else {
                        route.setLugarInicio("Instituto");
                        route.setLugarFin(streetName);
                    }
                    String horaSalida = etHoraSalida.getText().toString();

//                    User currentAppUser = documentSnapshot.toObject(User.class);
                    route.setHuecos(huecos);
                    route.setFechaCreacion(new Date());
                    route.setHoraSalida(horaSalida);


                    Map<String, Object> data = new HashMap<>();
                    data.put("id", route.getId_ruta());
                    data.put("lugarInicio", route.getLugarInicio());
                    data.put("lugarFin", route.getLugarFin());
                    data.put("horaSalida", route.getHoraSalida());
                    data.put("huecos", route.getHuecos());
                    data.put("fechaCreacion", route.getFechaCreacion());
                    data.put("usuariosApuntados", new ArrayList<>());
                    data.put("usuariosBaneados", new ArrayList<>());
                    data.put("propietario", user);
                    Long finalCount = count;
                    db.collection("routes").add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // Incrementa el contador solo después de que se ha creado con éxito el nuevo documento
                                db.collection("counters").document("routes").update("count", finalCount + 1);
                            }
                        });
                }
            });
    }
});

    }
}