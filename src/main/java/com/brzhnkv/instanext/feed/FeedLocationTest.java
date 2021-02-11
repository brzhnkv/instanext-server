package com.brzhnkv.instanext.feed;

import com.brzhnkv.instanext.Main;
import org.junit.Assert;
import org.junit.Test;

import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.requests.feed.FeedLocationRequest;
import com.github.instagram4j.instagram4j.responses.feed.FeedLocationResponse;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class FeedLocationTest {
    public Logger logger = LoggerFactory.getLogger(Main.class);
    private final SerializeTestUtil serializeTestUtil;

    public FeedLocationTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testName() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        FeedLocationResponse response =
                new FeedLocationRequest(106048159426257l).execute(client).join();
        logger.debug(response.getLocation().getName());
        Assert.assertEquals("ok", response.getStatus());
        logger.debug("Success");
    }

}
