package com.brzhnkv.instanext;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import com.brzhnkv.instanext.util.Tasks;
import com.brzhnkv.instanext.util.Utility;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.actions.feed.FeedIterator;
import com.github.instagram4j.instagram4j.requests.feed.FeedTagRequest;
import com.github.instagram4j.instagram4j.responses.feed.FeedTagResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class SocketController {
	
	private final NotificationDispatcher dispatcher;
	
    @Autowired
    public SocketController(NotificationDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
    @MessageMapping("/start")
    public void start(StompHeaderAccessor stompHeaderAccessor) {
        dispatcher.add(stompHeaderAccessor.getSessionId());
        log.info("start called");
    }
    @MessageMapping("/stop")
    public void stop(StompHeaderAccessor stompHeaderAccessor) {
        dispatcher.remove(stompHeaderAccessor.getSessionId());
        log.info("stop called");
    }
    @MessageMapping("/notify")
    public void notify(StompHeaderAccessor stompHeaderAccessor) {
    	Notification notification = new Notification();
    	notification.setStatus("in progress");
    	dispatcher.dispatch(notification, "status");
    	try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	notification.setStatus("done");
    	notification.setTask("task is sfjskf");
    	dispatcher.dispatch(notification, "status");
    	
    }
    @MessageMapping("/likeandsave")
    public void likeAndSave(StompHeaderAccessor stompHeaderAccessor, @RequestBody String message) throws Exception {
    	String tag = message;
    	log.info(tag);
    	dispatcher.add(stompHeaderAccessor.getSessionId());
    	Tasks task = new Tasks(dispatcher);
    	task.likeAndSave(tag);
    	dispatcher.remove(stompHeaderAccessor.getSessionId());
    }
    @MessageMapping("/sendmediatogroup")
    public void sendMediaToGroup(StompHeaderAccessor stompHeaderAccessor, @RequestBody String message) throws Exception {
    	String tag = message;
    	log.info(tag);
    	dispatcher.add(stompHeaderAccessor.getSessionId());
    	Tasks task = new Tasks(dispatcher);
    	task.sendMediaToGroup(tag);
    	dispatcher.remove(stompHeaderAccessor.getSessionId());
    }
    @MessageMapping("/test")
    public void test(StompHeaderAccessor stompHeaderAccessor) throws Exception {
    	dispatcher.add(stompHeaderAccessor.getSessionId());
    	Tasks task = new Tasks(dispatcher);
    	task.test();
    	dispatcher.remove(stompHeaderAccessor.getSessionId());
    }
}