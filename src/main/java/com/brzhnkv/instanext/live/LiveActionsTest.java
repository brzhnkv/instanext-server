package com.brzhnkv.instanext.live;

import com.brzhnkv.instanext.Main;
import org.junit.Assert;
import org.junit.Test;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.requests.live.LiveBroadcastCommentRequest;
import com.github.instagram4j.instagram4j.requests.live.LiveBroadcastGetCommentRequest;
import com.github.instagram4j.instagram4j.requests.live.LiveBroadcastGetViewerListRequest;
import com.github.instagram4j.instagram4j.requests.live.LiveBroadcastHeartbeatRequest;
import com.github.instagram4j.instagram4j.requests.live.LiveBroadcastLikeRequest;
import com.github.instagram4j.instagram4j.requests.live.LiveBroadcastQuestionsRequest;
import com.github.instagram4j.instagram4j.requests.live.LiveWaveRequest;
import com.github.instagram4j.instagram4j.responses.IGResponse;

import lombok.extern.slf4j.Slf4j;
import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class LiveActionsTest {
    public Logger logger = LoggerFactory.getLogger(Main.class);
    private final SerializeTestUtil serializeTestUtil;
    private static final String BROADCAST_ID = "17845796883218625";

    public LiveActionsTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testComment() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        LiveBroadcastCommentRequest IGRequest =
                new LiveBroadcastCommentRequest(BROADCAST_ID, "Test");

        IGResponse response = IGRequest.execute(client).join();

        Assert.assertEquals("ok", response.getStatus());
        logger.debug("Success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testHeartbeat() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        LiveBroadcastHeartbeatRequest IGRequest = new LiveBroadcastHeartbeatRequest(BROADCAST_ID);
        IGResponse response = IGRequest.execute(client).join();

        Assert.assertEquals("ok", response.getStatus());
        logger.debug("success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testGetComment() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        LiveBroadcastGetCommentRequest IGRequest = new LiveBroadcastGetCommentRequest(BROADCAST_ID);
        IGResponse response = IGRequest.execute(client).join();

        Assert.assertEquals("ok", response.getStatus());
        logger.debug("success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testLike() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        LiveBroadcastLikeRequest IGRequest = new LiveBroadcastLikeRequest(BROADCAST_ID, 69);
        IGResponse response = IGRequest.execute(client).join();

        Assert.assertEquals("ok", response.getStatus());
        logger.debug("success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testGetViewerList() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        LiveBroadcastGetViewerListRequest IGRequest =
                new LiveBroadcastGetViewerListRequest(BROADCAST_ID);
        IGResponse response = IGRequest.execute(client).join();

        Assert.assertEquals("ok", response.getStatus());
        logger.debug("success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testBroadcastQuestions() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        LiveBroadcastQuestionsRequest IGRequest =
                new LiveBroadcastQuestionsRequest(BROADCAST_ID, "What is the meaning of life?");
        IGResponse response = IGRequest.execute(client).join();

        Assert.assertEquals("ok", response.getStatus());
        logger.debug("success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testWave() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        LiveWaveRequest IGRequest = new LiveWaveRequest(BROADCAST_ID, "");
        IGResponse response = IGRequest.execute(client).join();

        Assert.assertEquals("ok", response.getStatus());
        logger.debug("success");
    }
}
