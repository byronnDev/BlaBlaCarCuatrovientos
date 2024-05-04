package org.cuatrovientos.blablacar.models;

import java.util.Date;

public class Route {
    private int id;
    private String lugarInicio;
    private String lugarFin;
    private String horaSalida;
    private Date fechaCreacion;
    private int huecos;

    public Route(int id, String lugarInicio) {
        this.id = id;
        this.lugarInicio = lugarInicio;
        this.lugarFin = "42.82434579444012, -1.6598648266999774";
        this.horaSalida = "7:00";
        this.fechaCreacion =  new Date(); //Formato EEE MMM dd HH:mm:ss zzz yyyy
                                            //EEE: Día de la semana abreviado (por ejemplo, "Mon" para lunes).
                                            //MMM: Mes abreviado (por ejemplo, "Jan" para enero).
                                            //dd: Día del mes en formato numérico (por ejemplo, "03" para el tercer día del mes).
                                            //HH: Hora en formato de 24 horas.
                                            //mm: Minuto.
                                            //ss: Segundo.
                                            //zzz: Zona horaria.
                                            //yyyy: Año.

    }
    public Route(int id, String lugarInicio, String lugarFin, String horaSalida, int huecos) {
        this.id = id;
        this.lugarInicio = lugarInicio;
        this.lugarFin = lugarFin;
        this.horaSalida = horaSalida;
        this.huecos = huecos;
        this.fechaCreacion = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getHuecos() {
        return huecos;
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


}
