package com.brzhnkv.liketime;

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