package com.ivik.passwordmanager;

public class Account {
    private String password;
    private String username;
    private String webpage;
    private String app;

    public Account(String password, String username, String webpage, String app) {
        this.password = password;
        this.username = username;
        this.webpage = webpage;
        this.app = app;
    }

    public Account(String password, String username, String webpage) {
        this.password = password;
        this.username = username;
        this.webpage = webpage;
        this.app = null;
    }

    public String getWebpage() {
        return webpage;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String toString() {
        return password + " " + username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
