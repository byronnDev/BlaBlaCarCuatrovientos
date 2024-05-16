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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.activities.MainActivity;
import org.cuatrovientos.blablacar.models.LoguedUser;
import org.cuatrovientos.blablacar.models.Route;
import org.cuatrovientos.blablacar.models.User;

import io.realm.Realm;
import io.realm.RealmList;


public class FragmentDetails extends Fragment {

    private Button btnUnirse;
    private Route route;
    private TextView tvLugarInicio;
    private TextView tvLugarFin;
    private TextView tvHuecos;
    private Realm realm;

    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);


        btnUnirse = (Button) view.findViewById(R.id.btnUnirse);
        tvLugarInicio = (TextView) view.findViewById(R.id.tvLugarInicio);
        tvLugarFin = (TextView)view.findViewById(R.id.tvLugarFin);
        tvHuecos = (TextView)view.findViewById(R.id.tvHuecos);

        return view;
    }

    public void renderData(String idRuta) {
        realm = Realm.getDefaultInstance();
        route = realm.where(Route.class).equalTo("id", Integer.parseInt(idRuta)).findFirst();
        user = LoguedUser.StaticLogedUser.getUser();

        btnUnirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //realm = Realm.getDefaultInstance();
                // Define la ID de la ruta que deseas agregar al usuario
                String idRutapuntarse = idRuta; // Reemplaza con el ID real de la ruta que deseas agregar

                // Define el correo del usuario que desea unirse a la ruta
                String correoUsuario = user.getMail();

                if(route.getUsuariosApuntados().contains(correoUsuario)){
                    Toast.makeText(getContext(), "Ya estás apuntado a esta ruta", Toast.LENGTH_SHORT).show();
                    return;
                }
                RealmList<String> listaUsuariosApuntados = route.getUsuariosApuntados();
                listaUsuariosApuntados.add(correoUsuario);
                route.setUsuariosApuntados(listaUsuariosApuntados);

                RealmList<Integer> listaRutasApuntados = user.getRoutesSubscribed();
                listaRutasApuntados.add(Integer.parseInt(idRutapuntarse));
                user.setRoutesSubscribed(listaRutasApuntados);

                //Actualizar en la base de datos
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(user);
                realm.copyToRealmOrUpdate(route);
                realm.commitTransaction();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });



    }

    public interface DataListener {
        void sendData(int idRuta);

    }
}