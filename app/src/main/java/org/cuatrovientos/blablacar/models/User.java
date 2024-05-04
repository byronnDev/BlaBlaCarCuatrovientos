package org.cuatrovientos.blablacar.models;

public class


User {
    private String mail;
    private String userName;

    public User(String mail) {
        this.mail = mail;
        String[] mailPart = mail.split("@");
        String namePart = mailPart[0];
        this.userName = namePart;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
