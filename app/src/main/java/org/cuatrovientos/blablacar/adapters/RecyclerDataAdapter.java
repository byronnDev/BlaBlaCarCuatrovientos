package org.cuatrovientos.blablacar.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.Route;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import io.realm.RealmList;

public class RecyclerDataAdapter extends RecyclerView.Adapter<RecyclerDataAdapter.RecyclerDataHolder> {
    List<Route> listaDeElementos;
    OnItemClickListener itemListener;

    public RecyclerDataAdapter(RealmList<String> usuariosApuntados) {
        // Constructor vacío, no utilizado en este contexto
    }

    public RecyclerDataAdapter(List<Route> lista, OnItemClickListener listener) {
        listaDeElementos = lista;
        itemListener = listener;
    }

    @NonNull
    @Override
    public RecyclerDataAdapter.RecyclerDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.routes_item, null, false);
        return new RecyclerDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerDataAdapter.RecyclerDataHolder holder, int position) {
        holder.assignData(listaDeElementos.get(position), itemListener);
    }

    @Override
    public int getItemCount() {
        return listaDeElementos.size();
    }

    public class RecyclerDataHolder extends RecyclerView.ViewHolder {
        TextView tvLugarInicio;
        TextView tvLugarFin;
        TextView tvHuecos;
        TextView tvHoraSalida;

        public RecyclerDataHolder(@NonNull View itemView) {
            super(itemView);
            tvLugarInicio = itemView.findViewById(R.id.tvLugarInicio);
            tvLugarFin = itemView.findViewById(R.id.tvLugarFin);
            tvHuecos = itemView.findViewById(R.id.tvHuecos);
            tvHoraSalida = itemView.findViewById(R.id.tvHoraSalida);
        }

        public void assignData(Route route, OnItemClickListener listener) {
            tvLugarInicio.setText(route.getLugarInicio());
            tvLugarFin.setText(route.getLugarFin());
            tvHuecos.setText(String.valueOf(route.getHuecos()));

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String formattedTime = sdf.format(route.getHoraSalida());
            tvHoraSalida.setText(formattedTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(route);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Route route);
    }
}