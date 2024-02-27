package org.cuatrovientos.blablacar.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cuatrovientos.blablacar.R;


public class details_fragment extends Fragment {

    RecyclerView recyclerRutas;

    public details_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_routes_fragment, container, false);

        //TODO
        //vincular los items del xml a variables

        //poner toda la informacion en los textos del xml





        return view;
    }

    public interface DataListener {
        void sendData(int idRuta);
    }
}