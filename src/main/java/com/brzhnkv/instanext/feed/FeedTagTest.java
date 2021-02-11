package com.brzhnkv.instanext.feed;

import com.brzhnkv.instanext.Main;
import org.junit.Test;

import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.requests.feed.FeedTagRequest;
import com.github.instagram4j.instagram4j.responses.feed.FeedTagResponse;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class FeedTagTest {
    public Logger logger = LoggerFactory.getLogger(Main.class);
    private final SerializeTestUtil serializeTestUtil;

    public FeedTagTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testName() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        FeedTagResponse response = client.sendRequest(new FeedTagRequest("love")).join();
        logger.debug("Items : {} Story : {}", response.getItems().size(),
                response.getStory().getItems().size());
        response = new FeedTagRequest("love", response.getNext_max_id()).execute(client).join();
        logger.debug("Items : {} Story : {}", response.getItems().size(),
                response.getStory().getItems().size());
        logger.debug("Success");
    }
}
