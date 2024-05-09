package org.cuatrovientos.blablacar.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.cuatrovientos.blablacar.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Route extends RealmObject {
    @PrimaryKey
    private int id_ruta;
    private String lugarInicio;
    private String lugarFin;
    private Date horaSalida;
    private Date fechaCreacion;
    private int huecos;
    private String propietoario;//el mail del usuario creador de la ruta
    private String usuariosApuntados;//los correos de los usuarios que se apuntan separados por ";"
    private String usuariosBaneados;//los correos de los usuarios baneados en esta ruta

    public Route(String propietario,String lugarInicio, String lugarFin, Date horaSalida, int huecos) {
        this.id_ruta= MyApplication.rutaID.getAndIncrement();
        this.lugarInicio = lugarInicio;
        this.lugarFin = lugarFin;
        this.horaSalida = horaSalida;
        this.huecos = huecos;
        this.fechaCreacion = new Date();
        this.propietoario = propietario;
        this.usuariosApuntados = "";
        this.usuariosBaneados = "";
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

    public Date getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Date horaSalida) {
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

    public String getPropietoario() {
        return propietoario;
    }

    public void setPropietoario(String propietoario) {
        this.propietoario = propietoario;
    }

    public String getUsuariosApuntados() {
        return usuariosApuntados;
    }

    public void setUsuariosApuntados(String usuariosApuntados) {
        this.usuariosApuntados = usuariosApuntados;
    }

    public String getUsuariosBaneados() {
        return usuariosBaneados;
    }

    public void setUsuariosBaneados(String usuariosBaneados) {
        this.usuariosBaneados = usuariosBaneados;
    }

    public ArrayList<String> getUsuariosApuntadosEnArrayList(){
        ArrayList<String> usuariosApuntadosList = new ArrayList<>();

        if (usuariosApuntados != null && !usuariosApuntados.isEmpty()) {

            usuariosApuntadosList = new ArrayList<>(Arrays.asList(usuariosApuntados.split(";")));
        }

        return usuariosApuntadosList;
    }

    public void addUsuariosApuntados(String nuevoUsuario){
        this.usuariosApuntados = this.usuariosApuntados + nuevoUsuario + ";";

    }

    public ArrayList<String> getUsuariosBaneadosEnArrayList(){
        ArrayList<String> usuariosBaneadosList = new ArrayList<>();

        if (usuariosBaneados != null && !usuariosBaneados.isEmpty()) {

            usuariosBaneadosList = new ArrayList<>(Arrays.asList(usuariosBaneados.split(";")));
        }

        return usuariosBaneadosList;
    }
    public void addUsuariosBaneados(String nuevoUsuario){
        this.usuariosBaneados = this.usuariosBaneados + nuevoUsuario + ";";

    }
}
