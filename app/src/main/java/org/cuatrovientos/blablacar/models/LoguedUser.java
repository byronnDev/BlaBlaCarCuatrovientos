package org.cuatrovientos.blablacar.models;

public class LoguedUser {

    private static User user;

    private LoguedUser() {
        // Constructor privado para evitar la creación de instancias de LoggedUser
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User newUser) {
        user = newUser;
    }
}
