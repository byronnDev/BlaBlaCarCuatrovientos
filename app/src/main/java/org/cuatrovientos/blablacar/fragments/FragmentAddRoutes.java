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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.adapters.RecyclerDataAdapter;
import org.cuatrovientos.blablacar.models.Route;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class FragmentAddRoutes extends Fragment {

    List<Route> routesList = new ArrayList<Route>();
    RecyclerView recyclerView;
    DataListener callback;
    ImageButton btnAddRoute;
    Realm realm;
    public RealmResults<Route> realmResults;


    public FragmentAddRoutes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_routes, container, false);
        realm = Realm.getDefaultInstance();
        this.btnAddRoute = (ImageButton) view.findViewById(R.id.btnAddInnerRoute);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerRutas);



        realmResults = realm.where(Route.class).findAll();
        RecyclerDataAdapter routeAdapter =new RecyclerDataAdapter(realmResults, new RecyclerDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Route conten) {
                String idRoute = conten.getId().toString();
                callback.sendData(idRoute);
            }
        });

        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        this.recyclerView.setAdapter(routeAdapter);



        this.btnAddRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.addRoute();
            }
        });

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