package org.cuatrovientos.blablacar.utils;

import org.cuatrovientos.blablacar.models.Route;
import org.cuatrovientos.blablacar.models.User;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;


public class Utils {
    public static ArrayList<User> getDummyData() {
        ArrayList<User> userList = new ArrayList<>();
        userList.add(new User("test","testo","test@test.com","Qwerty123.","344567094"));
        userList.add(new User("test1","testo","test1@test.com","Qwerty123.","344745098"));
        userList.add(new User("test2","testo","test2@test.com","Qwerty123.","344567094"));
        userList.add(new User("test3","testo","test3@test.com","Qwerty123.","347973294"));
        return userList;
    }

    public static ArrayList<Route> getDummyDataRoutes() {
        ArrayList<Route> routeList = new ArrayList<>();
        Route route1 = new Route("test@test.com", "Ciudad A", "Ciudad B", new Date(), 3);
        Route route2 = new Route("test1@test.com", "Ciudad C", "Ciudad D", new Date(), 2);
        Route route3 = new Route("test2@test.com", "Ciudad E", "Ciudad F", new Date(), 4);

        RealmList<String> usuariosBaneados = route1.getUsuariosBaneados();
        usuariosBaneados.add("test@test.com");
        route1.setUsuariosBaneados(usuariosBaneados);
        // Agregar las rutas a la lista
        routeList.add(route1);
        routeList.add(route2);
        routeList.add(route3);
        return routeList;
    }
}
