package org.cuatrovientos.blablacar.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;

import org.cuatrovientos.blablacar.R;


public class FragmentDetails extends Fragment {
    private GoogleMap map;
    private Button btnCalculate;

    private String start = "";
    private  String end = "";

    private Polyline poly = null;
    RecyclerView recyclerRutas;
    private TextView txtid;
    public FragmentDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        btnCalculate = getView().findViewById(R.id.btnCalculateRoute);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        //TODO
        //vincular los items del xml a variables

        //poner toda la informacion en los textos del xml





        return view;
    }

    public interface DataListener {
        void sendData(int idRuta);
    }
}