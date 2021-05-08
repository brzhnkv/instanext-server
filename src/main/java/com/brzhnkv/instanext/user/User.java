package com.brzhnkv.instanext.user;

import com.google.cloud.firestore.Blob;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


public class User {

    private String token;
    private String password;
    private String profilePicURL;

    private Blob clientFile;
    private Blob cookieFile;


    public User() {
    }

    public User(String token, String password, String profilePicURL, byte[] clientFile, byte[] cookieFile) {
        this.token = token;
        this.password = password;
        this.profilePicURL = profilePicURL;

        this.clientFile = Blob.fromBytes(clientFile);
        this.cookieFile = Blob.fromBytes(cookieFile);
    }

    public String getToken() {
        return token;
    }

    public String getPassword() {
        return password;
    }

    public String getProfilePicUrl() {
        return profilePicURL;
    }

    public byte[] getClientFile() {
        return clientFile.toBytes();
    }

    public byte[] getCookieFile() {
        return cookieFile.toBytes();
    }
}
