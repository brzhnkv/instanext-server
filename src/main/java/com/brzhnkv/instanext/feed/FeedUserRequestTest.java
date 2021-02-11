package com.brzhnkv.instanext.feed;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.brzhnkv.instanext.Main;
import org.junit.Assert;
import org.junit.Test;

import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.exceptions.IGResponseException;
import com.github.instagram4j.instagram4j.requests.feed.FeedUserRequest;
import com.github.instagram4j.instagram4j.responses.feed.FeedUserResponse;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class FeedUserRequestTest {
    public Logger logger = LoggerFactory.getLogger(Main.class);
    private final SerializeTestUtil serializeTestUtil;

    public FeedUserRequestTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions //
    // 2336729204844097508_18428658
    public void testFeedRequest()
            throws IGResponseException, IGLoginException, ClassNotFoundException,
            FileNotFoundException, IOException {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        FeedUserRequest req = new FeedUserRequest(client.getSelfProfile().getPk());
        FeedUserResponse response = client.sendRequest(req).join();
        response.getItems().forEach(item -> {
            logger.debug("{} : {}", item.getId(), item.getClass().getName());
        });
        Assert.assertEquals("ok", response.getStatus());
    }
}
