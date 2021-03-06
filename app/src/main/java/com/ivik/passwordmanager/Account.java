package com.ivik.passwordmanager;

public class Account {
    private String password;
    private String username;

    public Account(String password, String username) {
        this.password = password;
        this.username = username;
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
