package com.brzhnkv.instanext;

import java.util.HashSet;
import java.util.Set;

import com.brzhnkv.instanext.util.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationDispatcher {
    public Logger logger = LoggerFactory.getLogger(Main.class);
    private Set<String> listeners = new HashSet<>();
    private SimpMessagingTemplate template;
    private SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);

    public String getSessionId() {
        return this.headerAccessor.getSessionId();
    }

    public NotificationDispatcher(SimpMessagingTemplate template) {
        this.template = template;

    }

    public void add(String sessionId) {
        listeners.add(sessionId);
    }

    public void remove(String sessionId) {
        listeners.remove(sessionId);
    }


    public void dispatch(String user, Notification notification, String path) {
   /*     SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(getSessionId());
        headerAccessor.setLeaveMutable(true);
*/

        // logger.info("Sending notification to " + getSessionId());
        logger.info("Sending notification to " + user);
        path = "/queue/" + path;
        template.convertAndSendToUser(
                user,
                path,
                notification);
    }

  //  @Scheduled(fixedDelay = 5000) //WORKS!!
    public void send(String user) throws InterruptedException {
        String path = "/queue/status";
        if (getSessionId() != null) {
            template.convertAndSendToUser(
                    user,
                    path,
                    new Notification("lol"));

            Thread.sleep(3000);
            template.convertAndSendToUser(
                    user,
                    path,
                    new Notification("lol2"));
        }
    }

    @EventListener
    public void sessionConnectionHandler(SessionConnectEvent event) {
        logger.info("Incoming connection ====>");
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String username = sha.getFirstNativeHeader("sessionId");
        String token = sha.getFirstNativeHeader("token");

        User user = new User(username, token);
        logger.info(user.getUsername() + " | " + user.getToken() + " || " + user.toString());
        headerAccessor.setSessionId(username);
    }


    @EventListener
    public void sessionDisconnectionHandler(SessionDisconnectEvent event) {

       /* String sessionId = event.getSessionId();
        logger.info("Disconnecting " + sessionId + "!");
        remove(sessionId);*/
    }
}
