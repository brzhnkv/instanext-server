package com.brzhnkv.instanext.feed;

import com.brzhnkv.instanext.Main;
import org.junit.Assert;
import org.junit.Test;

import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.requests.feed.FeedSavedRequest;
import com.github.instagram4j.instagram4j.responses.feed.FeedSavedResponse;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class FeedSavedTest {
    public Logger logger = LoggerFactory.getLogger(Main.class);
    private final SerializeTestUtil serializeTestUtil;

    public FeedSavedTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testName() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        FeedSavedResponse response = new FeedSavedRequest().execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
        response.getItems()
                .forEach(item -> logger.debug("{} : {}", item.getId(), item.getClass().getName()));
        logger.debug("Success");
    }
}
