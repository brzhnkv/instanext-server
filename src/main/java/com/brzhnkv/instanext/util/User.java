package com.brzhnkv.instanext.util;

public class User {
    String username;
    String token;

    public User(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
