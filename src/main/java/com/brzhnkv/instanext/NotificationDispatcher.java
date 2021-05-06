package com.brzhnkv.instanext;

import com.brzhnkv.instanext.user.TempUser;
import com.brzhnkv.instanext.user.User;
import com.brzhnkv.instanext.user.UserRepository;
import com.brzhnkv.instanext.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NotificationDispatcher {
    public Logger logger = LoggerFactory.getLogger(Main.class);

    private SimpMessagingTemplate template;
    private final UserService userService;

    public NotificationDispatcher(SimpMessagingTemplate template, UserService userService) {
        this.template = template;
        this.userService = userService;
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
        String path = "/queue/log";

        template.convertAndSendToUser(
                user,
                path,
                new Notification("лайк поставлен"));

        Thread.sleep(3000);
        template.convertAndSendToUser(
                user,
                path,
                new Notification("сохранение поставлено"));
    }

    @EventListener
    public void sessionConnectionHandler(SessionConnectEvent event) {
        String username = event.getUser().getName();
        logger.info("User " + username + " connected!");
        userService.updateUserMessages(username, TempUser.getTempStatusMessage(), TempUser.getTempLogMessage());
    }


    @EventListener
    public void sessionDisconnectionHandler(SessionDisconnectEvent event) {
        String username = event.getUser().getName();
        userService.updateUserMessages(username, TempUser.getTempStatusMessage(), TempUser.getTempLogMessage());

        logger.info("Disconnecting " + username + "!");
    }
}
