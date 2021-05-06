package com.brzhnkv.instanext;

import com.brzhnkv.instanext.task.Task;
import com.brzhnkv.instanext.user.TempUser;
import com.brzhnkv.instanext.user.User;
import com.brzhnkv.instanext.user.UserService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import com.brzhnkv.instanext.util.Tasks;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class SocketController {
	public Logger logger = LoggerFactory.getLogger(Main.class);
	

	private final Tasks tasks;
    private final NotificationDispatcher dispatcher;
    private final UserService userService;

    //private final SimpUserRegistry simpUserRegistry;

    private SimpMessagingTemplate template;


    @Autowired
    public SocketController(Tasks tasks, SimpMessagingTemplate template, NotificationDispatcher dispatcher, UserService userService) {
        this.tasks = tasks;
        this.template = template;
        this.dispatcher = dispatcher;
        this.userService = userService;
    }



    @MessageMapping("/like")
    public void likeByTag(StompHeaderAccessor stompHeaderAccessor, @RequestBody String taskDataJSON) throws Exception {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");
        String tag = response.getString("tag");
        String taskName = "[лайк : #" + tag + "] ";

        User user = userService.getUser(username);
        userService.setStatus(username, true);
        userService.clearMessages(username);
        TempUser.clear();

        Task task = new Task(dispatcher);
        task.likeByTag(user, username, tag);

        userService.updateUserMessages(username, TempUser.getTempStatusMessage(), TempUser.getTempLogMessage());
        userService.setStatus(username, false);


        dispatcher.dispatch(username, new Notification(taskName + "задание выполнено"), "status");
    }


    @MessageMapping("/save")
    public void saveByTag(StompHeaderAccessor stompHeaderAccessor, @RequestBody String taskDataJSON) throws Exception {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");
        String tag = response.getString("tag");
        String taskName = "[лайк : #" + tag + "] ";

        User user = userService.getUser(username);
        userService.setStatus(username, true);
        userService.clearMessages(username);
        TempUser.clear();

        Task task = new Task(dispatcher);
        task.saveByTag(user, username, tag);

        userService.updateUserMessages(username, TempUser.getTempStatusMessage(), TempUser.getTempLogMessage());
        userService.setStatus(username, false);


        dispatcher.dispatch(username, new Notification(taskName + "задание выполнено"), "status");

    }


    @MessageMapping("/likeandsave")
    public void likeAndSave(StompHeaderAccessor stompHeaderAccessor, @RequestBody String taskDataJSON) throws Exception {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");
        String tag = response.getString("tag");
        String taskName = "[лайк + сохранение : #" + tag + "] ";

        User user = userService.getUser(username);
        userService.setStatus(username, true);
        userService.clearMessages(username);
        TempUser.clear();

        Task task = new Task(dispatcher);
        task.likeAndSave(user, username, tag);

        userService.updateUserMessages(username, TempUser.getTempStatusMessage(), TempUser.getTempLogMessage());
        userService.setStatus(username, false);

        dispatcher.dispatch(username, new Notification(taskName + "задание выполнено"), "status");
    }


    @MessageMapping("/sendmediatogroup")
    public void sendMediaToGroup(StompHeaderAccessor stompHeaderAccessor, @RequestBody String taskDataJSON) throws Exception {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");
        String token = response.getString("token");
        String tag = response.getString("tag");
        String taskName = "[самолет : #" + tag + "] ";

        User user = userService.getUser(username);
        userService.setStatus(username, true);
        userService.clearMessages(username);
        TempUser.clear();

        Task task = new Task(dispatcher);
        task.likeAndSave(user, username, tag);

        userService.updateUserMessages(username, TempUser.getTempStatusMessage(), TempUser.getTempLogMessage());
        userService.setStatus(username, false);

        dispatcher.dispatch(username, new Notification(taskName + "задание выполнено"), "status");
    }


    @MessageMapping("/test")
    public void test(StompHeaderAccessor stompHeaderAccessor, @RequestBody String taskDataJSON) throws Exception {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");

        //String user = stompHeaderAccessor.getUser().getName();
        User user = userService.getUser(username);
        userService.setStatus(username, true);
        userService.clearMessages(username);

        //tasks.test(user, token);
        Task task = new Task(dispatcher);
        task.test(user, username);


        userService.setStatus(username, false);
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
}