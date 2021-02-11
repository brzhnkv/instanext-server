package com.brzhnkv.instanext.media;

import com.brzhnkv.instanext.Main;
import org.junit.Assert;
import org.junit.Test;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.requests.media.MediaGetStoryPollVotersRequest;
import com.github.instagram4j.instagram4j.requests.media.MediaGetStoryQuestionResponsesRequest;
import com.github.instagram4j.instagram4j.requests.media.MediaListReelMediaViewerRequest;
import com.github.instagram4j.instagram4j.responses.IGResponse;
import com.github.instagram4j.instagram4j.responses.media.MediaGetStoryPollVotersResponse;
import com.github.instagram4j.instagram4j.responses.media.MediaGetStoryQuestionResponsesResponse;

import lombok.extern.slf4j.Slf4j;
import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
// id 2364185211807618943
// question 17958669613337332
// poll 17958171793339158
public class ReelStickerResponseTest {
    public Logger logger = LoggerFactory.getLogger(Main.class);
    private final SerializeTestUtil serializeTestUtil;

    public ReelStickerResponseTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testMediaViewer() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        IGResponse response =
                new MediaListReelMediaViewerRequest("2364185211807618943").execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
        logger.debug("Success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testStoryQuestionResponses() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        MediaGetStoryQuestionResponsesResponse response =
                new MediaGetStoryQuestionResponsesRequest("2364185211807618943",
                        "17958669613337332").execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
        response.getResponder_info().getResponders().forEach(responder -> logger.debug("{} : {}",
                responder.getResponse(), responder.getUser().getPk()));
        logger.debug("Success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testStoryPollResponses() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        MediaGetStoryPollVotersResponse response =
                new MediaGetStoryPollVotersRequest("2364185211807618943", "17958171793339158")
                        .execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
        response.getVoter_info().getVoters().forEach(responder -> logger.debug("{} : {}",
                responder.getVote(), responder.getUser().getPk()));
        logger.debug("Success");
    }
}
