package com.brzhnkv.instanext;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationDispatcher {
	private Set<String> listeners = new HashSet<>();
	private SimpMessagingTemplate template;
	public NotificationDispatcher(SimpMessagingTemplate template) {
	    this.template = template;
	}
	
	  public void add(String sessionId) {
	        listeners.add(sessionId);
	    }

	    public void remove(String sessionId) {
	        listeners.remove(sessionId);
	    }
	    
	    //@Scheduled(fixedDelay = 10000)
	    public void dispatch(Notification notification, String path) {
	        for (String listener : listeners) {
	            log.info("Sending notification to " + listener);

	            SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
	            headerAccessor.setSessionId(listener);
	            headerAccessor.setLeaveMutable(true);

	            path = "/notification/" + path;
	            //int value = (int) Math.round(Math.random() * 100d);
	            template.convertAndSendToUser(
	                    listener,
	                    path,
	                    notification,
	                    headerAccessor.getMessageHeaders());
	        }
	    }

	    @EventListener
	    public void sessionDisconnectionHandler(SessionDisconnectEvent event) {
	        String sessionId = event.getSessionId();
	        log.info("Disconnecting " + sessionId + "!");
	        remove(sessionId);
	    }
}
