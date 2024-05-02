package org.cuatrovientos.blablacar.models;

import java.util.ArrayList;
import java.util.Date;

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

    public Route() {
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
