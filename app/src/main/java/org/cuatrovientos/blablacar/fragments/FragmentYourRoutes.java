package org.cuatrovientos.blablacar.fragments;

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

import com.google.firebase.auth.FirebaseAuth;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.LoguedUser;
import org.cuatrovientos.blablacar.models.Route;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class FragmentYourRoutes extends Fragment {
    DataListener callback;

    public FragmentYourRoutes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_routes, container, false);
        // Crear una instancia del FragmentAddRoutes
        FragmentAddRoutes fragmentAddRoutes = new FragmentAddRoutes();

        // Pasar el propietario de la ruta como un argumento al FragmentAddRoutes
        Bundle bundle = new Bundle();
        bundle.putString("propietario", LoguedUser.StaticLogedUser.getUser().getMail());
        fragmentAddRoutes.setArguments(bundle);

        // Realizar la transacción del fragmento
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.defaultView, fragmentAddRoutes)
                .addToBackStack(null)
                .commit();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            callback = (DataListener) context;
        }
        catch (Exception e){
            throw new ClassCastException(context.toString() + "should implement DataListener");
        }
    }

    public interface DataListener {
        void sendData(String idRuta);

        void addRoute();
    }
}