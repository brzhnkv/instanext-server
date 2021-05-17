package com.brzhnkv.liketime;

import com.brzhnkv.liketime.task.Task;
import com.brzhnkv.liketime.user.Messages;
import com.brzhnkv.liketime.user.TempUser;
import com.brzhnkv.liketime.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@CrossOrigin
@Controller
public class SocketController {
    public Logger logger = LoggerFactory.getLogger(Main.class);


    @Autowired
    private UserService userService;

    public SocketController() {}


    @MessageMapping("/like")
    public void likeByTag(@RequestBody String taskDataJSON) {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");
        String tag = response.getString("tag");

        startTask(username);
        Task task = new Task();
        task.likeByTag(username, tag);

        userService.updateMessages(username, false, TempUser.getTempStatusMessage(), TempUser.getTempLogMessage());
    }


    @MessageMapping("/save")
    public void saveByTag(@RequestBody String taskDataJSON) {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");
        String tag = response.getString("tag");

        startTask(username);
        Task task = new Task();
        task.saveByTag(username, tag);

        userService.updateMessages(username, false, TempUser.getTempStatusMessage(), TempUser.getTempLogMessage());
    }


    @MessageMapping("/likeandsave")
    public void likeAndSave(@RequestBody String taskDataJSON) {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");
        String tag = response.getString("tag");

        startTask(username);
        Task task = new Task();
        task.likeAndSave(username, tag);

        userService.updateMessages(username, false, TempUser.getTempStatusMessage(), TempUser.getTempLogMessage());
    }


    @MessageMapping("/sendmediatogroup")
    public void sendMediaToGroup(@RequestBody String taskDataJSON) {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");
        String tag = response.getString("tag");

        startTask(username);
        Task task = new Task();
        task.sendMediaToGroup(username, tag);

        userService.updateMessages(username, false, TempUser.getTempStatusMessage(), TempUser.getTempLogMessage());
    }


    @MessageMapping("/test")
    public void test(@RequestBody String taskDataJSON) {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");

        startTask(username);
        Task task = new Task();
        task.test(username);


        userService.updateMessages(username, false, TempUser.getTempStatusMessage(), TempUser.getTempLogMessage());
      /*  template.convertAndSendToUser(
                user,
                "/queue/status",
                new Notification("Simple message"));

*/
         /*
        dispatcher.dispatch(new Notification("Задание выполняется."), "status");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dispatcher.send();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dispatcher.dispatch(new Notification("ss"), "status");
        */


    }

    private void startTask(String username) {
        Messages messages = new Messages(true);
        userService.updateMessages(username, messages);
    }
}