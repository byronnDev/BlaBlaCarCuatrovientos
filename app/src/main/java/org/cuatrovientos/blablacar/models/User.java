package org.cuatrovientos.blablacar.models;

import java.util.ArrayList;

public class User {
    private String name;
    private String surname;
    private String mail;
    private String phone;
    private int O2Points;
    private ArrayList<Route> Routes;//rutas de las que es propietario
    private ArrayList<Route> routesSubscribed;//rutas a las que esta apuntado
    private ArrayList<Route> routesBaned;//rutas en las que esta baneado;

    public User() {
        routesSubscribed = new ArrayList<>();
    }

    public User(String name, String surname, String mail, String phone) {
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.phone = phone;
        this.O2Points = 0;
        this.Routes = new ArrayList<Route>();
        this.routesSubscribed = new ArrayList<Route>();
        this.routesBaned = new ArrayList<Route>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getO2Points() {
        return O2Points;
    }

    public void setO2Points(int o2Points) {
        O2Points = o2Points;
    }

    public ArrayList<Route> getRoutes() {
        return Routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        Routes = routes;
    }

    public ArrayList<Route> getRoutesSubscribed() {
        return routesSubscribed;
    }

    public void setRoutesSubscribed(ArrayList<Route> routesSubscribed) {
        this.routesSubscribed = routesSubscribed;
    }

    public ArrayList<Route> getRoutesBaned() {
        return routesBaned;
    }
    public void apuntarRuta(Route ruta) {
        routesSubscribed.add(ruta);
    }
    public void setRoutesBaned(ArrayList<Route> routesBaned) {
        this.routesBaned = routesBaned;
    }
}
