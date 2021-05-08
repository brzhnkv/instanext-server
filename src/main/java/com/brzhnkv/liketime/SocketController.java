package com.brzhnkv.liketime;

import com.brzhnkv.liketime.task.Task;
import com.brzhnkv.liketime.user.Messages;
import com.brzhnkv.liketime.user.TempUser;
import com.brzhnkv.liketime.user.User;
import com.brzhnkv.liketime.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
public class SocketController {
    public Logger logger = LoggerFactory.getLogger(Main.class);

    private final NotificationDispatcher dispatcher;
    private final UserService userService;

    @Autowired
    public SocketController(NotificationDispatcher dispatcher, UserService userService) {
        this.dispatcher = dispatcher;
        this.userService = userService;
    }


    @MessageMapping("/like")
    public void likeByTag(StompHeaderAccessor stompHeaderAccessor, @RequestBody String taskDataJSON) throws Exception {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");
        String tag = response.getString("tag");

        User user = userService.getUser(username);
        startTask(username);

        Task task = new Task(dispatcher);
        task.likeByTag(user, username, tag);

        userService.updateMessages(username, false, TempUser.getTempStatusMessage(), TempUser.getTempLogMessage());
    }


    @MessageMapping("/save")
    public void saveByTag(StompHeaderAccessor stompHeaderAccessor, @RequestBody String taskDataJSON) throws Exception {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");
        String tag = response.getString("tag");

        User user = userService.getUser(username);
        startTask(username);

        Task task = new Task(dispatcher);
        task.saveByTag(user, username, tag);

        userService.updateMessages(username, false, TempUser.getTempStatusMessage(), TempUser.getTempLogMessage());
    }


    @MessageMapping("/likeandsave")
    public void likeAndSave(StompHeaderAccessor stompHeaderAccessor, @RequestBody String taskDataJSON) throws Exception {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");
        String tag = response.getString("tag");

        User user = userService.getUser(username);
        startTask(username);

        Task task = new Task(dispatcher);
        task.likeAndSave(user, username, tag);

        userService.updateMessages(username, false, TempUser.getTempStatusMessage(), TempUser.getTempLogMessage());
    }


    @MessageMapping("/sendmediatogroup")
    public void sendMediaToGroup(StompHeaderAccessor stompHeaderAccessor, @RequestBody String taskDataJSON) throws Exception {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");
        String tag = response.getString("tag");

        User user = userService.getUser(username);
        startTask(username);

        Task task = new Task(dispatcher);
        task.sendMediaToGroup(user, username, tag);

        userService.updateMessages(username, false, TempUser.getTempStatusMessage(), TempUser.getTempLogMessage());
    }


    @MessageMapping("/test")
    public void test(StompHeaderAccessor stompHeaderAccessor, @RequestBody String taskDataJSON) throws Exception {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");

        //String user = stompHeaderAccessor.getUser().getName();
        User user = userService.getUser(username);
        startTask(username);

        //tasks.test(user, token);
        Task task = new Task(dispatcher);
        task.test(user, username);


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