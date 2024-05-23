package org.cuatrovientos.blablacar.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.Route;
import org.cuatrovientos.blablacar.models.User;

import java.util.List;

import io.realm.RealmList;

public class RecyclerDataAdapterUsersSubscribed extends RecyclerView.Adapter<RecyclerDataAdapterUsersSubscribed.RecyclerDataHolder>{
    List<String> listaDeElementos;
    OnItemClickListener itemListener;
    Boolean isPorpietario;

    public RecyclerDataAdapterUsersSubscribed(List<String> usuariosApuntados) {

    }

    public RecyclerDataAdapterUsersSubscribed( Boolean isPorpietario,List<String> lista, OnItemClickListener listener){
        this.isPorpietario = isPorpietario;
        listaDeElementos = lista;
        itemListener = listener;
    }



    @NonNull
    @Override
    public RecyclerDataAdapterUsersSubscribed.RecyclerDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usuarioa_puntado_layout,null,false);
        return new RecyclerDataHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerDataAdapterUsersSubscribed.RecyclerDataHolder holder, int position) {
        holder.assignData(listaDeElementos.get(position), itemListener);
    }

    @Override
    public int getItemCount() {
        return listaDeElementos.size();
    }

    public class RecyclerDataHolder extends RecyclerView.ViewHolder {
        TextView tvMailusuarioApuntado;
        Button btnBan;

        public RecyclerDataHolder(@NonNull View itemView) {
            super(itemView);
            //SETEAR BOTON
            tvMailusuarioApuntado = itemView.findViewById(R.id.tvmail);
            btnBan = itemView.findViewById(R.id.btnBan);
            if (!isPorpietario){
                btnBan.setVisibility(View.GONE);
            }else{
                btnBan.setVisibility(View.VISIBLE);
            }
        }
        public void assignData(String user1, OnItemClickListener listener) {
//            tvMailusuarioApuntado.setText();

            tvMailusuarioApuntado.setText(user1);
            //TODO setear vien, lo que regresa es un Date
            //tvHoraSalida.setText(route.getHoraSalida());
            btnBan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //baneamos al usuario
                    listener.onItemClick(user1);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String conten);

    }
}