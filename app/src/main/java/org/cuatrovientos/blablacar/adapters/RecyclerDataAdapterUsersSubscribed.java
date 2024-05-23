package org.cuatrovientos.blablacar.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.User;

import io.realm.RealmList;
import io.realm.RealmResults;

public class RecyclerDataAdapterUsersSubscribed extends RecyclerView.Adapter<RecyclerDataAdapterUsersSubscribed.ViewHolder> {

    private RealmResults<User> dataList;
    private OnItemClickListener listener;

    public RecyclerDataAdapterUsersSubscribed(RealmList<String> usuariosApuntados) {
    }

    public interface OnItemClickListener {
        void onItemClick(User item);
    }

    public RecyclerDataAdapterUsersSubscribed(RealmResults<User> dataList, OnItemClickListener listener) {
        if (dataList == null || listener == null) {
            throw new IllegalArgumentException("dataList and listener must not be null");
        }
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.usuarioa_puntado_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User item = dataList.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void updateData(RealmResults<User> newData) {
        this.dataList = newData;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mail = itemView.findViewById(R.id.tvmail);
        }

        public void bind(final User item, final OnItemClickListener listener) {
            mail.setText(item.getMail());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}