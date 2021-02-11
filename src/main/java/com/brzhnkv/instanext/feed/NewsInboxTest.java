package com.brzhnkv.instanext.feed;

import com.brzhnkv.instanext.Main;
import org.junit.Assert;
import org.junit.Test;

import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.requests.news.NewsInboxRequest;
import com.github.instagram4j.instagram4j.responses.news.NewsInboxResponse;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class NewsInboxTest {
    public Logger logger = LoggerFactory.getLogger(Main.class);
    private final SerializeTestUtil serializeTestUtil;

    public NewsInboxTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testName() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        NewsInboxResponse response = new NewsInboxRequest().execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
        response.getOld_stories().forEach(story -> {
            logger.debug(story.get("args").toString());
        });
        logger.debug("Success");
    }
}
