package com.brzhnkv.instanext.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.UUID;


public class Client {

    private final UUID id;

    @NotBlank
    private final String username;

    @NotBlank
    private final String token;

    private final byte[] clientFile;
    private final byte[] cookieFile;


    public Client(@JsonProperty("username") String username,
                  @JsonProperty("token") String token,
                  @JsonProperty("client_file") byte[] clientFile,
                  @JsonProperty("cookie_file") byte[] cookieFile) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.token = token;

        this.clientFile = clientFile;
        this.cookieFile = cookieFile;
    }

    public Client(@JsonProperty("id") UUID id,
                  @JsonProperty("username") String username,
                  @JsonProperty("token") String token,
                  @JsonProperty("client_file") byte[] clientFile,
                  @JsonProperty("cookie_file") byte[] cookieFile) {
        this.id = id;
        this.username = username;
        this.token = token;

        this.clientFile = clientFile;
        this.cookieFile = cookieFile;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public byte[] getClientFile() {
        return clientFile;
    }

    public byte[] getCookieFile() {
        return cookieFile;
    }
}
