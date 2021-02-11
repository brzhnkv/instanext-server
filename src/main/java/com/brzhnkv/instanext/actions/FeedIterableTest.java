package com.brzhnkv.instanext.actions;

import com.brzhnkv.instanext.Main;
import org.junit.Test;

import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.actions.feed.FeedIterator;
import com.github.instagram4j.instagram4j.requests.feed.FeedTimelineRequest;
import com.github.instagram4j.instagram4j.responses.feed.FeedTimelineResponse;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class FeedIterableTest {
    public Logger logger = LoggerFactory.getLogger(Main.class);
    private final SerializeTestUtil serializeTestUtil;

    public FeedIterableTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testIterator() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        // form a FeedIterator for FeedTimelineRequest
        FeedIterator<FeedTimelineRequest, FeedTimelineResponse> iter =
                new FeedIterator<>(client, new FeedTimelineRequest());
        // setting a limit of 2 responses (initial and one paginated)
        int limit = 2;
        while (iter.hasNext() && limit-- > 0) {
            FeedTimelineResponse response = iter.next();
            // Actions here
            response.getFeed_items().forEach(m -> logger.debug(m.getCaption().getText()));
            // Recommended to wait in between iterations
        }

        logger.debug("Success");
    }

}
