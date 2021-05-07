package com.brzhnkv.instanext.user;

import com.brzhnkv.instanext.Notification;
import com.brzhnkv.instanext.serialize.SerializableCookieJar;
import com.brzhnkv.instanext.util.Serialize;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import okhttp3.*;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }


    @RequestMapping(path = "/messages", method = RequestMethod.POST)
    @PostMapping
    public Map<String,List<String>> getMessages(@RequestBody String usernameJSON) {
        JSONObject response = new JSONObject(usernameJSON);
        String username = response.getString("username");
        return new HashMap(userService.getMessages(username));
    }

    @RequestMapping(path = "/status", method = RequestMethod.POST)
    @PostMapping
    public Map<String,Boolean> getStatus(@RequestBody String usernameJSON) {
        JSONObject response = new JSONObject(usernameJSON);
        String username = response.getString("username");
        boolean status = userService.getStatus(username);
        Map<String,Boolean> data = new HashMap();
        data.put("status", status);
        return data;
    }

    @PostMapping
    public Map<String, String> registerNewUser(@RequestBody String loginDataJSON) {
        JSONObject response = new JSONObject(loginDataJSON);
        String username = response.getString("username");
        String password = response.getString("password");
        HashMap<String, String> data = new HashMap<String, String>();

        if (userService.isUserLoggedIn(username, password)) {
            User user = userService.getUser(username);
            data.put("username", username);
            data.put("token", user.getToken());
            data.put("userProfilePic", user.getProfilePicUrl());
            return data;
        }

        User serializedUser = Serialize.serializeUser(username, password);
        userService.addNewUser(serializedUser);

        data.put("username", username);
        data.put("token", serializedUser.getToken());
        data.put("userProfilePic", serializedUser.getProfilePicUrl());

        return data;
    }

    @DeleteMapping(path = "{username}")
    public void deleteUser(@PathVariable("username") String username) {
        userService.deleteUser(username);
    }


}
