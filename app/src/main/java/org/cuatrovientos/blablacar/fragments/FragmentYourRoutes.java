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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.adapters.RecyclerDataAdapter;
import org.cuatrovientos.blablacar.models.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentYourRoutes extends Fragment {
    RecyclerView recyclerView;
    List<Route> routesList = new ArrayList<Route>();
    DataListener callback;


    public FragmentYourRoutes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_routes, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.tusRutas);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));




        return view;
    }
    private boolean isCurrentUserRoute(Route route) {
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        return currentUserEmail != null && currentUserEmail.equals(route.getPropietoario());
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