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
        holder.assignData(listaDeElementos.get(position),itemListener);
    }

    @Override
    public int getItemCount() {

        return listaDeElementos.size();

    }

    public class RecyclerDataHolder extends RecyclerView.ViewHolder {

        TextView id;



        public RecyclerDataHolder(@NonNull View itemView) {

            super(itemView);

            id = itemView.findViewById(R.id.RoutesitemTxtIdruta);


        }

        public void assignData(Route contenido, OnItemClickListener onItemClickListener){
            String rutasId = String.valueOf(contenido.getId());
            rutasId = "Ruta " + rutasId;



            id.setText(rutasId);






            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //si en el evento onItemClick pusimos que pasara la posicion en la que se encuentra el item lo pondremos
                    //como parametro aqui poniendo getAdapterPosition(), como esta puesto aqui

                    onItemClickListener.onItemClick(contenido);
                }
            });
        }

    }

    public interface OnItemClickListener{

        void onItemClick(Route conten);
    }
}
