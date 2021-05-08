package com.brzhnkv.liketime.user;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


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

    @RequestMapping(path = "/messages", method = RequestMethod.POST)
    @PostMapping
    public Map<String, List<String>> getMessages(@RequestBody String usernameJSON) {
        JSONObject response = new JSONObject(usernameJSON);
        String username = response.getString("username");

        HashMap<String, List<String>> data = new HashMap<>();
        Messages messages;
        try {
            messages = userService.getMessages(username);
            data.put("status", Collections.singletonList(String.valueOf(messages.getStatus())));
            data.put("statusMessages", messages.getStatusMessages());
            data.put("logMessages", messages.getLogMessages());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data;
    }

/*    @RequestMapping(path = "/status", method = RequestMethod.POST)
    @PostMapping
    public Map<String,Boolean> getStatus(@RequestBody String usernameJSON) {
        JSONObject response = new JSONObject(usernameJSON);
        String username = response.getString("username");
        boolean status = userService.getStatus(username);
        Map<String,Boolean> data = new HashMap();
        data.put("status", status);
        return data;
    }*/


}
