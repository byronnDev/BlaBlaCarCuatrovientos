package org.cuatrovientos.blablacar.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import org.cuatrovientos.blablacar.R;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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
    private List<String> streets;
    private ArrayAdapter<String> adapter;
    private String API_KEY = "5b3ce3597851110001cf6248c5bcb736abf44f18b56ed748942063de";
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);

        db = FirebaseFirestore.getInstance();

        actvStreetName = findViewById(R.id.editTextcalle);
        streets = new ArrayList<>();

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
                                            JSONObject jsonObj = new JSONObject(responseData);
                                            JSONArray features = jsonObj.getJSONArray("features");
                                            streets.clear();
                                            for (int i = 0; i < features.length(); i++) {
                                                JSONObject feature = features.getJSONObject(i);
                                                String streetName = feature.getJSONObject("properties").getString("label");
                                                streets.add(streetName);
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
                // Aquí debes buscar las coordenadas correspondientes a la calle seleccionada y guardarlas en Firebase
                Map<String, Object> data = new HashMap<>();
                data.put("streetName", streetName);
                // Aquí debes agregar las coordenadas correspondientes
                data.put("coordinates", "coordinates");
                db.collection("streets").add(data);
            }
        });
    }
}