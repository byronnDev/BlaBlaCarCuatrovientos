package org.cuatrovientos.blablacar.models;

public class LoguedUser {

    public static class StaticLogedUser{
        private static User user;



        public static User getUser() {
            return user;
        }

        public static void setUser(User newUser) {
            user = newUser;
        }
    }

}
