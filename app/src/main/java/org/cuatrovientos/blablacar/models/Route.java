package org.cuatrovientos.blablacar.models;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Route {
    private int id_ruta;
    private String lugarInicio;//son dos coordenadas, separadas por coma
    private String lugarFin;//son dos coordenadas, separadas por coma
    private String horaSalida;
    private Date fechaCreacion;
    private int huecos;
    private User propietoario;
    private ArrayList<User> usuariosApuntados;
    private ArrayList<User> usuariosBaneados;
    private FirebaseFirestore db;

    public Route() {
        usuariosApuntados = new ArrayList<User>();
    }

    public Route(int id, String lugarInicio, String lugarFin, String horaSalida, int huecos, User usuarioPropietario) {
        this.id_ruta = id;
        this.lugarInicio = lugarInicio;
        this.lugarFin = lugarFin;
        this.horaSalida = horaSalida;
        this.fechaCreacion =  new Date(); //Formato EEE MMM dd HH:mm:ss zzz yyyy
                                            //EEE: Día de la semana abreviado (por ejemplo, "Mon" para lunes).
                                            //MMM: Mes abreviado (por ejemplo, "Jan" para enero).
                                            //dd: Día del mes en formato numérico (por ejemplo, "03" para el tercer día del mes).
                                            //HH: Hora en formato de 24 horas.
                                            //mm: Minuto.
                                            //ss: Segundo.
                                            //zzz: Zona horaria.
                                            //yyyy: Año.
        this.propietoario = usuarioPropietario;
        this.huecos = huecos;
        this.usuariosApuntados = new ArrayList<User>();
        this.usuariosBaneados = new ArrayList<User>();
    }

    public Route(String lugarInicio, String lugarFin, String horaSalida, int huecos, User usuarioPropietario) {
        this.id_ruta = 0;
        this.lugarInicio = lugarInicio;
        this.lugarFin = lugarFin;
        this.horaSalida = horaSalida;
        this.fechaCreacion =  new Date(); //Formato EEE MMM dd HH:mm:ss zzz yyyy
        //EEE: Día de la semana abreviado (por ejemplo, "Mon" para lunes).
        //MMM: Mes abreviado (por ejemplo, "Jan" para enero).
        //dd: Día del mes en formato numérico (por ejemplo, "03" para el tercer día del mes).
        //HH: Hora en formato de 24 horas.
        //mm: Minuto.
        //ss: Segundo.
        //zzz: Zona horaria.
        //yyyy: Año.
        this.propietoario = usuarioPropietario;
        this.huecos = huecos;
        this.usuariosApuntados = new ArrayList<User>();
        this.usuariosBaneados = new ArrayList<User>();


    }
    public void apuntarUsuario(User usuario) {
        if (usuariosApuntados.size() < huecos) {
            usuariosApuntados.add(usuario);
        }
    }
    public void insertToDatabase(){
        db = FirebaseFirestore.getInstance();
        CollectionReference routesRef = db.collection("routes");
        // Añadir la ruta a la colección "routes"
        routesRef.add(this)
        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    // La operación se realizó con éxito
                    Log.d(TAG, "Ruta añadida correctamente con ID: " + task.getResult().getId());
                } else {
                    // Hubo un error al agregar la ruta
                    Log.w(TAG, "Error al añadir ruta", task.getException());
                }
            }
        });
    }

    public int getId_ruta() {
        return id_ruta;
    }

    public void setId_ruta(int id_ruta) {
        this.id_ruta = id_ruta;
    }

    public String getLugarInicio() {
        return lugarInicio;
    }

    public void setLugarInicio(String lugarInicio) {
        this.lugarInicio = lugarInicio;
    }

    public String getLugarFin() {
        return lugarFin;
    }

    public void setLugarFin(String lugarFin) {
        this.lugarFin = lugarFin;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getHuecos() {
        return huecos;
    }

    public void setHuecos(int huecos) {
        this.huecos = huecos;
    }

    public User getPropietoario() {
        return propietoario;
    }

    public void setPropietoario(User propietoario) {
        this.propietoario = propietoario;
    }

    public ArrayList<User> getUsuariosApuntados() {
        return usuariosApuntados;
    }

    public void setUsuariosApuntados(ArrayList<User> usuariosApuntados) {
        this.usuariosApuntados = usuariosApuntados;
    }

    public ArrayList<User> getUsuariosBaneados() {
        return usuariosBaneados;
    }

    public void setUsuariosBaneados(ArrayList<User> usuariosBaneados) {
        this.usuariosBaneados = usuariosBaneados;
    }
}
