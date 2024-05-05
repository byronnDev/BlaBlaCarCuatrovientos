package org.cuatrovientos.blablacar.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.Route;

import java.util.List;

public class RecyclerDataAdapter extends RecyclerView.Adapter<RecyclerDataAdapter.RecyclerDataHolder>{
    List<Route> listaDeElementos;
    OnItemClickListener itemListener;

    public RecyclerDataAdapter(List<Route> lista, OnItemClickListener listener){
        listaDeElementos = lista;
        itemListener = listener;
    }

    @NonNull
    @Override
    public RecyclerDataAdapter.RecyclerDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.routes_item,null,false);
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

    public RecyclerDataHolder(@NonNull View itemView) {
        super(itemView);
        tvLugarInicio = itemView.findViewById(R.id.tvLugarInicio);
        tvLugarFin = itemView.findViewById(R.id.tvLugarFin);
        tvHuecos = itemView.findViewById(R.id.tvHuecos);
    }

    public void assignData(Route route, OnItemClickListener listener) {
        tvLugarInicio.setText(tvLugarInicio.getText() + route.getLugarInicio());
        tvLugarFin.setText(tvLugarFin.getText() + route.getLugarFin());
        tvHuecos.setText(tvHuecos.getText() + String.valueOf(route.getHuecos()));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(route, getAdapterPosition());
            }
        });
    }
}

    public interface OnItemClickListener {
        void onItemClick(Route conten);

        void onItemClick(Route route, int position);
    }
}