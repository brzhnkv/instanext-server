package com.brzhnkv.instanext.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.sql.Blob;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ClientDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ClientDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<Client> selectAllClients() {
        final String sql = "SELECT id, username, token FROM client";
        return jdbcTemplate.query(sql, mapClientFomDb());
    }

   /* public Optional<Client> selectClientById(UUID id) {
        final String sql = "SELECT id, username FROM client WHERE id = ?";
        @Deprecated
        Client client = jdbcTemplate.queryForObject(
                sql,
                new Object[]{id},
                (resultSet, i) -> {
                    UUID clientId = UUID.fromString(resultSet.getString("id"));
                    String username = resultSet.getString("username");
                    String token = resultSet.getString("token");
                    byte[] clientFile = resultSet.getBytes("client_file");
                    return new Client(clientId, username, token, clientFile);
                });
                return Optional.ofNullable(client);
    }*/

    int insertClient(UUID clientId, Client client) {
        String sql = "" +
                "INSERT INTO client (" +
                " id, " +
                " username, " +
                " token, " +
                " client_file, " +
                " cookie_file) " +
                "VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(
                sql,
                clientId,
                client.getUsername(),
                client.getToken(),
                client.getClientFile(),
                client.getCookieFile()
        );
    }

    public Optional<Client> selectClientByUsernameAndToken(String username, String token) {
        final String sql = "SELECT id, username, token, client_file, cookie_file FROM client WHERE username = ? AND token = ?";
        @Deprecated
        Client client = jdbcTemplate.queryForObject(
                sql,
                new Object[]{username, token},
                (resultSet, i) -> {
                    UUID clientId = UUID.fromString(resultSet.getString("id"));
                    String clientUsername = resultSet.getString("username");
                    String clientToken = resultSet.getString("token");
                    byte[] clientFile = resultSet.getBytes("client_file");
                    byte[] cookieFile = resultSet.getBytes("cookie_file");
                    return new Client(clientId, username, token, clientFile, cookieFile);
                });
                return Optional.ofNullable(client);
    }



    int deleteClientByUsername(String username) {
        String sql = "" +
                "DELETE FROM client " +
                "WHERE username = ?";
        return jdbcTemplate.update(sql, username);
    }

    private RowMapper<Client> mapClientFomDb() {
        return (resultSet, i) -> {
            String clientIdStr = resultSet.getString("id");
            UUID clientId = UUID.fromString(clientIdStr);

            String username = resultSet.getString("username");
            String token = resultSet.getString("token");
            byte[] clientFile = resultSet.getBytes("client_file");
            byte[] cookieFile = resultSet.getBytes("cookie_file");

            return new Client(
                    clientId,
                    username,
                    token,
                    clientFile,
                    cookieFile);
        };
    }
}
