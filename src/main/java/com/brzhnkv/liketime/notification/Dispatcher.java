package com.brzhnkv.liketime.notification;

import com.brzhnkv.liketime.Main;
import com.brzhnkv.liketime.notification.Notification;
import com.brzhnkv.liketime.user.TempUser;
import com.brzhnkv.liketime.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Service
public class Dispatcher {
    public Logger logger = LoggerFactory.getLogger(Main.class);

    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private UserService userService;

    public Dispatcher() {}


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
        if (TempUser.taskIsRunning)
            userService.updateMessages(username, true, TempUser.getTempStatusMessage(), TempUser.getTempLogMessage());
        else userService.updateMessages(username, false, TempUser.getTempStatusMessage(), TempUser.getTempLogMessage());
    }


    @EventListener
    public void sessionDisconnectionHandler(SessionDisconnectEvent event) {
        String username = event.getUser().getName();
        logger.info("Disconnecting " + username + "!");
    }
}
