package com.brzhnkv.instanext.highlights;

import com.brzhnkv.instanext.Main;
import org.junit.Assert;
import org.junit.Test;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.requests.highlights.HighlightsCreateReelRequest;
import com.github.instagram4j.instagram4j.requests.highlights.HighlightsDeleteReelRequest;
import com.github.instagram4j.instagram4j.requests.highlights.HighlightsEditReelRequest;
import com.github.instagram4j.instagram4j.requests.highlights.HighlightsUserTrayRequest;
import com.github.instagram4j.instagram4j.responses.IGResponse;

import lombok.extern.slf4j.Slf4j;
import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class HighlightsTest {
    public Logger logger = LoggerFactory.getLogger(Main.class);
    private final SerializeTestUtil serializeTestUtil;

    public HighlightsTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testName() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        IGResponse response = new HighlightsUserTrayRequest(18428658L).execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
        logger.debug("Success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testCreate() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        IGResponse response =
                new HighlightsCreateReelRequest("EXAMPLE", "2342701632890283954_18428658",
                        "2343246908405232496_18428658").execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
        logger.debug("Success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testDelete() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        IGResponse response = new HighlightsDeleteReelRequest("highlight:17850277421192731")
                .execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
        logger.debug("Success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testEdit() throws Exception {
        // highlight:17922135847428707
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        IGResponse response = new HighlightsEditReelRequest("highlight:17922135847428707", "NEW",
                "2342701632890283954_18428658", new String[] {"2364174303060359549_18428658"}, null)
                        .execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
        logger.debug("Success");
    }
}
