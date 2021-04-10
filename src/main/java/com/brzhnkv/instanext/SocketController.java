package com.brzhnkv.instanext;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import com.brzhnkv.instanext.util.Tasks;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class SocketController {
	public Logger logger = LoggerFactory.getLogger(Main.class);
	

	private final Tasks tasks;

    //private final SimpUserRegistry simpUserRegistry;

    private SimpMessagingTemplate template;


    @Autowired
    public SocketController(Tasks tasks, SimpMessagingTemplate template) {
        this.tasks = tasks;
       // this.simpUserRegistry = simpUserRegistry;
        this.template = template;
    }



    @MessageMapping("/like")
    public void likeByTag(StompHeaderAccessor stompHeaderAccessor, @RequestBody String taskDataJSON) throws Exception {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");
        String token = response.getString("token");
        String tag = response.getString("tag");

        tasks.likeByTag(username, token, tag);

    }
    @MessageMapping("/newlike")
    public void newLikeByTag(StompHeaderAccessor stompHeaderAccessor, @RequestBody String taskDataJSON) throws Exception {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");
        String token = response.getString("token");
        String tag = response.getString("tag");
        tasks.newLikeByTag(username, token, tag);
    }

    @MessageMapping("/save")
    public void saveByTag(StompHeaderAccessor stompHeaderAccessor, @RequestBody String taskDataJSON) throws Exception {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");
        String token = response.getString("token");
        String tag = response.getString("tag");

        tasks.saveByTag(username, token, tag);

    }
//    @MessageMapping("/likeandsave")
//    public void likeAndSave(StompHeaderAccessor stompHeaderAccessor, @RequestBody String message) throws Exception {
//    	String tag = message;
//    	logger.info(tag);
//		tasks.getDispatcher().add(stompHeaderAccessor.getSessionId());
//    	tasks.likeAndSave(tag);
//		tasks.getDispatcher().remove(stompHeaderAccessor.getSessionId());
//    }
@MessageMapping("/likeandsave")
public void likeAndSave(StompHeaderAccessor stompHeaderAccessor, @RequestBody String taskDataJSON) throws Exception {
    JSONObject response = new JSONObject(taskDataJSON);
    String username = response.getString("username");
    String token = response.getString("token");
    String tag = response.getString("tag");

    tasks.likeAndSave(username, token, tag);
}
    @MessageMapping("/sendmediatogroup")
    public void sendMediaToGroup(StompHeaderAccessor stompHeaderAccessor, @RequestBody String taskDataJSON) throws Exception {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");
        String token = response.getString("token");
        String tag = response.getString("tag");

    	tasks.sendMediaToGroup(username, token, tag);
    }
    @MessageMapping("/test")
    public void test(StompHeaderAccessor stompHeaderAccessor, @RequestBody String taskDataJSON) throws Exception {
        JSONObject response = new JSONObject(taskDataJSON);
        String username = response.getString("username");
        String token = response.getString("token");
        String user = stompHeaderAccessor.getUser().getName();
        
       // tasks.test(user, token);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        template.convertAndSendToUser(
                user,
                "/queue/status",
                new Notification("Simple message"));


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
    @MessageMapping("/auth/session")
    public void auth_session(StompHeaderAccessor stompHeaderAccessor, @RequestBody String cookie) throws Exception {

    	logger.info(cookie);
    	//tasks.authSession();

    }
    @MessageMapping("/auth/login")
    public void auth_login(StompHeaderAccessor stompHeaderAccessor, @RequestBody String loginDataJSON) throws Exception {

    	JSONObject response = new JSONObject(loginDataJSON);
    	String username = response.getString("username");
    	String password = response.getString("password");
    	tasks.authLogin(username, password);

    }
    @MessageMapping("/auth/addaccount")
    public void auth_add_account(StompHeaderAccessor stompHeaderAccessor, @RequestBody String loginDataJSON) throws Exception {
        String user = stompHeaderAccessor.getUser().getName();
        JSONObject response = new JSONObject(loginDataJSON);
        String username = response.getString("username");
        String password = response.getString("password");
        tasks.authAddAccount(user, username, password);
    }
}