package com.example.beachapp;

public class User {
    private String userID;
    private String username;
    private String email;
    private String password;
    public User() {
        userID="";
        username="";
        email="";
        password="";
    }
    public User(String userID, String username, String email,String password) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.password=password;
    }
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password){
        this.password=password;
        }
}


