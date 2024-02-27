package org.cuatrovientos.blablacar.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.cuatrovientos.blablacar.R;


public class map_fragment extends Fragment {



    public map_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_fragment, container, false);

        //TODO
        //vincular los items del xml a variables

        return view;
    }

    public void renderData(int idRuta) {
        //TODO
        //crear un objeto de tipo Ruta y construirlo, luego el resto de la logica de la clase





    }
}