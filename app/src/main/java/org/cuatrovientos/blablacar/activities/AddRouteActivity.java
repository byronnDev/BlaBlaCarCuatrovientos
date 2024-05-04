package org.cuatrovientos.blablacar.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.Route;
import org.json.JSONArray;
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
    private String API_KEY = "5b3ce3597851110001cf6248c5bcb736abf44f18b56ed748942063de";
    private FirebaseFirestore db;
    private String[] instituteCoordinates = {"42.82437732771406", "-1.6598058201633434"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);

        db = FirebaseFirestore.getInstance();

        actvStreetName = findViewById(R.id.editTextcalle);
        tvLatitude = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.tvLongitude);
        switchTipoRuta = findViewById(R.id.switch1);
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
                            String url = "https://api.openrouteservice.org/geocode/search?api_key=" + API_KEY + "&text=" + userInput;
                            Request request = new Request.Builder().url(url).build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                        String responseData = response.body().string();
                                        try {
                                            JSONObject jsonObject = new JSONObject(responseData);
                                            JSONArray features = jsonObject.getJSONArray("features");
                                            streets.clear();
                                            coordinates.clear();
                                            for (int i = 0; i < features.length(); i++) {
                                                JSONObject feature = features.getJSONObject(i);
                                                String streetName = feature.getJSONObject("properties").getString("label");
                                                JSONArray coords = feature.getJSONObject("geometry").getJSONArray("coordinates");
                                                String[] latLng = {String.valueOf(coords.getDouble(1)), String.valueOf(coords.getDouble(0))};
                                                streets.add(streetName);
                                                coordinates.put(streetName, latLng);
                                            }
                                        } catch (Exception e) {
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

        actvStreetName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String streetName = (String) parent.getItemAtPosition(position);
                String[] coords = coordinates.get(streetName);
                if (coords != null) {
                    tvLatitude.setText(coords[0]);
                    tvLongitude.setText(coords[1]);
                }
                Route route = new Route();
                if (switchTipoRuta.isChecked()) {
                    route.setLugarInicio(streetName);
                    route.setLugarFin(instituteCoordinates[0] + ", " + instituteCoordinates[1]);
                } else {
                    route.setLugarInicio(instituteCoordinates[0] + ", " + instituteCoordinates[1]);
                    route.setLugarFin(streetName);
                }
                route.setFechaCreacion(new Date());
                Map<String, Object> data = new HashMap<>();
                data.put("lugarInicio", route.getLugarInicio());
                data.put("lugarFin", route.getLugarFin());
                data.put("fechaCreacion", route.getFechaCreacion());
                db.collection("routes").add(data);
            }
        });
    }
}