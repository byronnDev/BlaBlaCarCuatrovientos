package org.cuatrovientos.blablacar.utils;

import org.cuatrovientos.blablacar.models.User;

import java.util.ArrayList;

import io.realm.Realm;


public class Utils {
    public static ArrayList<User> getDummyData() {
        ArrayList<User> userList = new ArrayList<>();
        userList.add(new User("test","testo","test@test.com","Qwerty123.","344567094"));
        userList.add(new User("test1","testo","test1@test.com","Qwerty123.","344745098"));
        userList.add(new User("test2","testo","test2@test.com","Qwerty123.","344567094"));
        userList.add(new User("test3","testo","test3@test.com","Qwerty123.","347973294"));
        return userList;
    }
}
