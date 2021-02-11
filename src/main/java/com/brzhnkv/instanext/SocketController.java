package com.brzhnkv.instanext;

import com.brzhnkv.instanext.client.ClientService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.brzhnkv.instanext.util.Tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@Slf4j
@Controller
public class SocketController {
	public Logger logger = LoggerFactory.getLogger(Main.class);
	

	private final Tasks tasks;


    @Autowired
    public SocketController(Tasks tasks) {
        this.tasks = tasks;
    }
    @MessageMapping("/start")
    public void start(StompHeaderAccessor stompHeaderAccessor) {
		tasks.getDispatcher().add(stompHeaderAccessor.getSessionId());
        logger.info("start called");
    }
    @MessageMapping("/stop")
    public void stop(StompHeaderAccessor stompHeaderAccessor) {
		tasks.getDispatcher().remove(stompHeaderAccessor.getSessionId());
        logger.info("stop called");
    }
    @MessageMapping("/notify")
    public void notify(StompHeaderAccessor stompHeaderAccessor) {
    	Notification notification = new Notification();
    	notification.setStatus("in progress");
		tasks.getDispatcher().dispatch(notification, "status");
    	try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	notification.setStatus("done");
    	notification.setTask("task is sfjskf");
		tasks.getDispatcher().dispatch(notification, "status");
    	
    }
    @MessageMapping("/likeandsave")
    public void likeAndSave(StompHeaderAccessor stompHeaderAccessor, @RequestBody String message) throws Exception {
    	String tag = message;
    	logger.info(tag);
		tasks.getDispatcher().add(stompHeaderAccessor.getSessionId());
    	tasks.likeAndSave(tag);
		tasks.getDispatcher().remove(stompHeaderAccessor.getSessionId());
    }
    @MessageMapping("/sendmediatogroup")
    public void sendMediaToGroup(StompHeaderAccessor stompHeaderAccessor, @RequestBody String message) throws Exception {
    	String tag = message;
    	logger.info(tag);
		tasks.getDispatcher().add(stompHeaderAccessor.getSessionId());
    	tasks.sendMediaToGroup(tag);
		tasks.getDispatcher().remove(stompHeaderAccessor.getSessionId());
    }
    @MessageMapping("/test")
    public void test(StompHeaderAccessor stompHeaderAccessor) throws Exception {
		tasks.getDispatcher().add(stompHeaderAccessor.getSessionId());
    	tasks.test();
		//tasks.getDispatcher().remove(stompHeaderAccessor.getSessionId());
    }
    @MessageMapping("/auth/session")
    public void auth_session(StompHeaderAccessor stompHeaderAccessor, @RequestBody String cookie) throws Exception {
		tasks.getDispatcher().add(stompHeaderAccessor.getSessionId());
    	logger.info(cookie);
    	//tasks.authSession();
		tasks.getDispatcher().remove(stompHeaderAccessor.getSessionId());
    }
    @MessageMapping("/auth/login")
    public void auth_login(StompHeaderAccessor stompHeaderAccessor, @RequestBody String loginDataJSON) throws Exception {
		tasks.getDispatcher().add(stompHeaderAccessor.getSessionId());
    	JSONObject response = new JSONObject(loginDataJSON);
    	String username = response.getString("username");
    	String password = response.getString("password");
    	tasks.authLogin(username, password);
		tasks.getDispatcher().remove(stompHeaderAccessor.getSessionId());
    }
}