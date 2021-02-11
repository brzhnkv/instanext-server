package com.brzhnkv.instanext.actions;

import com.brzhnkv.instanext.Main;
import org.junit.Test;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.actions.users.UserAction;
import com.github.instagram4j.instagram4j.models.friendships.Friendship;
import com.github.instagram4j.instagram4j.requests.friendships.FriendshipsActionRequest.FriendshipsAction;
import lombok.extern.slf4j.Slf4j;
import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class UsersActionTest {
    public Logger logger = LoggerFactory.getLogger(Main.class);
    private final SerializeTestUtil serializeTestUtil;

    public UsersActionTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testFriendship() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        UserAction user = client.actions().users().findByUsername("kimkardashian").join();
        Friendship friendship = user.getFriendship().join();
        logger.debug("Is following : {}", friendship.isFollowing());
        
        user.action(
                friendship.isFollowing() ? FriendshipsAction.DESTROY : FriendshipsAction.CREATE)
                .join();
        logger.debug("Success");
    }
}
