package org.cuatrovientos.blablacar.models;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    private String name;
    private String surname;
    @PrimaryKey
    private String mail;
    private String pass;
    private String phone;
    private int o2Points;
    private String routesPropietario;//rutas de las que es propietario
    private String routesSubscribed;//rutas a las que esta apuntado
    private String routesBaned;//rutas en las que esta baneado;

    public User(String name, String surname, String mail,String pass, String phone) {
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.phone = phone;
        this.o2Points = 0;
        this.routesPropietario = "";
        this.routesSubscribed = "";
        this.routesBaned = "";
        //hasear la password
        this.pass = pass;
    }
}
