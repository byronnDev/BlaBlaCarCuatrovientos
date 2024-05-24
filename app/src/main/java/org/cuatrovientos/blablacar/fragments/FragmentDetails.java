package org.cuatrovientos.blablacar.fragments;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.activities.MainActivity;
import org.cuatrovientos.blablacar.adapters.RecyclerDataAdapter;
import org.cuatrovientos.blablacar.adapters.RecyclerDataAdapterUsersSubscribed;
import org.cuatrovientos.blablacar.models.LoguedUser;
import org.cuatrovientos.blablacar.models.Route;
import org.cuatrovientos.blablacar.models.User;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class FragmentDetails extends Fragment {
    private Button btnUnirse;
    private Route route;
    private TextView tvLugarInicio;
    private TextView tvLugarFin;
    private TextView tvHuecos;
    private TextView tvHoraSalida;
    private Realm realm;
    private User user;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        btnUnirse = view.findViewById(R.id.btnUnirse);
        tvLugarInicio = view.findViewById(R.id.tvLugarInicio);
        tvLugarFin = view.findViewById(R.id.tvLugarFin);
        tvHuecos = view.findViewById(R.id.tvHuecos);
        tvHoraSalida = view.findViewById(R.id.tvHoraSalida);
        recyclerView = view.findViewById(R.id.rvPasajeros);

        return view;
    }

    @SuppressLint("RestrictedApi")
    public void renderData(String idRuta) {
        realm = Realm.getDefaultInstance();
        route = realm.where(Route.class).equalTo("id", Integer.parseInt(idRuta)).findFirst();
        Boolean isUserPropietario = route.getPropietario().equals(LoguedUser.StaticLogedUser.getUser().getMail());
        user = LoguedUser.StaticLogedUser.getUser();

        tvLugarInicio.setText(tvLugarInicio.getText()+ route.getLugarInicio());
        tvLugarFin.setText(tvLugarFin.getText()+ route.getLugarFin());
        tvHuecos.setText(tvHuecos.getText()+ String.valueOf(route.getHuecos()));
        if (route.getHoraSalida() != null) {
            tvHoraSalida.setText(tvHoraSalida.getText() + String.valueOf(route.getHoraSalida().getHours()) + ":" + String.valueOf(route.getHoraSalida().getMinutes()));
        } else {
            // Manejar el caso en que route.getHoraSalida() es nulo
            // Por ejemplo, mostrar un mensaje de error o asignar un valor predeterminado a tvHoraSalida
            Log.e(TAG,"La hora de salida es nula");
        }
        //realm de los usuarios baneados de la ruta
        RealmList<String> usuarioApuntados = route.getUsuariosApuntados();
        RecyclerDataAdapterUsersSubscribed recyclerDataAdapterUsersSubscribed = new RecyclerDataAdapterUsersSubscribed(isUserPropietario ,usuarioApuntados, new RecyclerDataAdapterUsersSubscribed.OnItemClickListener() {
            @Override
            public void onItemClick(String user) {
                //TODO implementar baneo de usuario
                RealmList<String> listaUsuariosBaneados= route.getUsuariosBaneados();
                RealmList<String> listaUsuariosApuntados= route.getUsuariosApuntados();
                realm.beginTransaction();

                if (listaUsuariosBaneados == null) {
                    listaUsuariosBaneados = new RealmList<>();
                }
                listaUsuariosBaneados.add(user);
                listaUsuariosApuntados.remove(user);
                route.setHuecos(route.getHuecos() + 1);


                realm.commitTransaction();
                Toast.makeText(getContext(), "has baneado al usuario " + user, Toast.LENGTH_SHORT).show();
            }
        });

        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        this.recyclerView.setAdapter(recyclerDataAdapterUsersSubscribed);

        //añadirmos los usuarios apunadoas al recycler view
            btnUnirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Comprobar si el usuario ya está apuntado a la ruta
                if(route.getUsuariosApuntados().contains(user.getMail())){
                    Toast.makeText(getContext(), "Ya estás apuntado a esta ruta", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Comprobar si hay huecos disponibles en la ruta
                if(route.getHuecos() <= 0){
                    Toast.makeText(getContext(), "No hay huecos disponibles en esta ruta", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Iniciar una transacción de escritura
                realm.beginTransaction();

                RealmList<String> listaUsuariosApuntados = route.getUsuariosApuntados();
                if (listaUsuariosApuntados != null) {
                    listaUsuariosApuntados.add(user.getMail());
                } else {
                    // Manejar el caso en que la lista de usuarios apuntados sea nula
                    // Por ejemplo, mostrar un mensaje de error o asignar una lista vacía
                    Log.e(TAG, "La lista de usuarios apuntados es nula");
                    listaUsuariosApuntados = new RealmList<>();
                }

                //añadimos 100 puntos al usuario
                user.setO2Points(user.getO2Points() + 100);

                // Restar 1 al número de huecos de la ruta
                route.setHuecos(route.getHuecos() - 1);
                realm.commitTransaction();
                Toast.makeText(getContext(), "Te has apuntado a la ruta + (100 O2-Points)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface DataListener {
        void sendData(int idRuta);
    }
}