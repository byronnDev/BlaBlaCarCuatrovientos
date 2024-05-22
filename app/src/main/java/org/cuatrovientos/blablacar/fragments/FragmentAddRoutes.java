package org.cuatrovientos.blablacar.fragments;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.adapters.RecyclerDataAdapter;
import org.cuatrovientos.blablacar.models.LoguedUser;
import org.cuatrovientos.blablacar.models.Route;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class FragmentAddRoutes extends Fragment {

    List<Route> routesList = new ArrayList<>();
    RecyclerView recyclerView;
    DataListener callback;
    ImageButton btnAddRoute;
    Realm realm;
    RealmResults<Route> realmResults;
    boolean isFiltering;
    boolean isUserFilter;

    public FragmentAddRoutes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_routes, container, false);

        realm = Realm.getDefaultInstance();
        isFiltering = false;
        isUserFilter = false;
        btnAddRoute = view.findViewById(R.id.btnAddRoute);
        recyclerView = view.findViewById(R.id.recyclerRutas);

        String location = null;
        // Obtener el propietario y la fecha seleccionada del Bundle
        String propietario = getArguments() != null ? getArguments().getString("propietario") : null;
        String dateString = getArguments() != null ? getArguments().getString("selectedDate") : null;
        Date selectedDate = null;
        if (getArguments() != null) {
            location = getArguments().getString("location");
            if (selectedDate != null && location != null) {
                selectedDate = parseDate(dateString);
                Log.d(TAG, "onCreateView: " + selectedDate);
                Log.d(TAG, "onCreateView: " + location);
                isFiltering = true;
            }

            if (propietario != null) {
                isUserFilter = true;
            }
        }

        if (isFiltering && selectedDate != null && location != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedDate);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date startOfDay = calendar.getTime();

            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date endOfDay = calendar.getTime();

            realmResults = realm.where(Route.class)
                    .contains("lugarInicio", location, Case.INSENSITIVE)
                    .greaterThanOrEqualTo("horaSalida", startOfDay)
                    .lessThan("horaSalida", endOfDay)
                    .findAll();

        } else if (isUserFilter) {
            // Filtrar por usuario logueado aquí si el argumento propietario está presente
            if (propietario != null) {
                realmResults = realm.where(Route.class)
                        .equalTo("propietario", propietario, Case.INSENSITIVE)
                        .findAll();
            } else {
                realmResults = realm.where(Route.class)
                        .equalTo("propietario", LoguedUser.StaticLogedUser.getUser().getMail(), Case.INSENSITIVE)
                        .findAll();
            }
        } else {
            //Filtro con los baneos aqui
            //RealmResults<Route> realmResultsAll= realm.where(Route.class).findAll();
            realmResults = realm.where(Route.class).not().in("usuariosBaneados", new String[]{LoguedUser.StaticLogedUser.getUser().getMail().toString()}).findAll();

        }

        RecyclerDataAdapter routeAdapter = new RecyclerDataAdapter(realmResults, conten -> {
            String idRoute = conten.getId().toString();
            callback.sendData(idRoute);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(routeAdapter);

        btnAddRoute.setOnClickListener(v -> callback.addRoute());

        return view;
    }

    private Date parseDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("dd / M / yyyy");
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: " + dateString, e);
            return null;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callback = (DataListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + " should implement DataListener");
        }
    }

    public interface DataListener {
        void sendData(String idRuta);
        void addRoute();
    }


}
