package com.brzhnkv.liketime.user;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;


    public UserController() {}


    @PostMapping
    public Map<String, String> createUser(@RequestBody String loginDataJSON) {
        JSONObject response = new JSONObject(loginDataJSON);
        String username = response.getString("username");
        String password = response.getString("password");
        HashMap<String, String> data = new HashMap<>();

        User user;
        String errorMessage;
        try {
            user = userService.createUser(username, password);
        } catch (ExecutionException e) {
            errorMessage = e.getMessage();
            data.put("error", "true");
            data.put("errorMessage", errorMessage);
            return data;
        } catch (InterruptedException e) {
            errorMessage = e.getMessage();
            data.put("error", "true");
            data.put("errorMessage", errorMessage);
            return data;
        } catch (IGLoginException e) {
            errorMessage = e.getMessage();
            data.put("error", "true");
            data.put("errorMessage", errorMessage);
            return data;
        }

        if (user != null) {
            data.put("error", "false");
            data.put("username", username);
            data.put("token", user.getToken());
            data.put("userProfilePic", user.getProfilePicUrl());
        } else {
            data.put("error", "true");
            data.put("errorMessage", "Wrong password");
        }
        return data;
    }

    @DeleteMapping(path = "{username}")
    public Map<String, String> deleteUser(@PathVariable("username") String username) {
        HashMap<String, String> data = new HashMap<String, String>();
        try {
            userService.deleteUser(username);
            data.put("error", "false");
            return data;
        } catch (ExecutionException e) {
            data.put("error", "true");
            data.put("errorMessage", e.getMessage());
            return data;
        } catch (InterruptedException e) {
            data.put("error", "true");
            data.put("errorMessage", e.getMessage());
            return data;
        }
    }
}
