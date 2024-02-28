package org.cuatrovientos.blablacar.models;

public class LogedUser {

    public static User logedUser;

    public static User getLogedUser(){
        return logedUser;
    }
    public static void setLogedUser(User loged){
        logedUser = loged;
    }
}
