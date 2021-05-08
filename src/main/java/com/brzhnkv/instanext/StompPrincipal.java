package com.brzhnkv.instanext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.security.Principal;


class StompPrincipal implements Principal {

    String name;
    String token;


    StompPrincipal(String name, String token) {
        this.name = name;
        this.token = token;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getToken() { return token; }
}