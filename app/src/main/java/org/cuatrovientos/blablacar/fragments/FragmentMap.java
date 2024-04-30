package org.cuatrovientos.blablacar.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cuatrovientos.blablacar.R;


public class FragmentMap extends Fragment {



    public FragmentMap() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //TODO
        //vincular los items del xml a variables

        return view;
    }

    public void renderData(int idRuta) {
        //TODO
        //crear un objeto de tipo Ruta y construirlo, luego el resto de la logica de la clase






    }
}