package org.cuatrovientos.blablacar.models;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
//seguridad de passwords
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

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
        //TODO hasear la password
        this.pass = hashPassword(pass);
    }
    //geters and setters
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
    public String getPass() {
        return pass;

    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public int getO2Points() {
        return o2Points;
    }
    public void setO2Points(int o2Points) {
        this.o2Points = o2Points;
    }
    public String getRoutesPropietario() {
        return routesPropietario;
    }
    public void setRoutesPropietario(String routesPropietario) {
        this.routesPropietario = routesPropietario;
    }
    public String getRoutesSubscribed() {
        return routesSubscribed;
    }
    public void setRoutesSubscribed(String routesSubscribed) {
        this.routesSubscribed = routesSubscribed;
    }
    public String getRoutesBaned() {
        return routesBaned;
    }
    public void setRoutesBaned(String routesBaned) {
        this.routesBaned = routesBaned;
    }
    //haseamos la pass con rsha256
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear la contraseña", e);
        }
    }

    //comprobamos si la password es correcta
    public boolean checkPass(String pass) {
        return this.pass.equals(pass);
    }
    //comvertir las rutas en un arraylist
    public ArrayList<String> getRoutesPropietarioEnArrayList(){
        ArrayList<String> routesPropietarioList = new ArrayList<>();

        if (routesPropietario != null && !routesPropietario.isEmpty()) {

            routesPropietarioList = new ArrayList<>(Arrays.asList(routesPropietario.split(";")));
        }

        return routesPropietarioList;
    }
    //convertir las rutas en un arraylist
    public ArrayList<String> getRoutesSubscribedEnArrayList(){
        ArrayList<String> routesSubscribedList = new ArrayList<>();

        if (routesSubscribed != null && !routesSubscribed.isEmpty()) {

            routesSubscribedList = new ArrayList<>(Arrays.asList(routesSubscribed.split(";")));
        }

        return routesSubscribedList;
    }
    //convertir rutas en las que esta baneado;
    public ArrayList<String> getRoutesBanedEnArrayList(){
        ArrayList<String> routesBanedList = new ArrayList<>();

        if (routesBaned != null && !routesBaned.isEmpty()) {

            routesBanedList = new ArrayList<>(Arrays.asList(routesBaned.split(";")));
        }

        return routesBanedList;
    }
    //añadir rutas a las que esta apuntado
    public void addRoutesSubscribed(String newRoute){
        this.routesSubscribed = this.routesSubscribed + newRoute + ";";
    }

    //añadir rutas de las que es propietario
    public void addRoutesPropietario(String newRoute){
        this.routesPropietario = this.routesPropietario + newRoute + ";";
    }

    //añadir rutas en las que esta baneado
    public void addRoutesBaned(String newRoute){
        this.routesBaned = this.routesBaned + newRoute + ";";
    }



}
