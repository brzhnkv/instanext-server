package com.brzhnkv.instanext.actions;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.brzhnkv.instanext.Main;
import org.junit.Assert;
import org.junit.Test;

import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.actions.timeline.TimelineAction.SidecarInfo;
import com.github.instagram4j.instagram4j.actions.timeline.TimelineAction.SidecarPhoto;
import com.github.instagram4j.instagram4j.actions.timeline.TimelineAction.SidecarVideo;
import com.github.instagram4j.instagram4j.requests.media.MediaCommentRequest;
import com.github.instagram4j.instagram4j.requests.media.MediaConfigureSidecarRequest.MediaConfigureSidecarPayload;
import com.github.instagram4j.instagram4j.responses.IGResponse;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Slf4j
public class TimelineActionTest {
    public Logger logger = LoggerFactory.getLogger(Main.class);
    private final SerializeTestUtil serializeTestUtil;

    public TimelineActionTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testFeed() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        client.actions().timeline().feed().stream().limit(2).forEach(res -> {
            Assert.assertEquals("ok", res.getStatus());
            res.getFeed_items().forEach(item -> {
                logger.debug("{} : {} : {}", item.getUser().getUsername(), item.getPk(),
                        item.getCaption().getText());
            });
        });
        logger.debug("Success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testPhoto() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        IGResponse response = client.actions().timeline()
                .uploadPhoto(new File("src/examples/resources/cover.jpg"), "My Caption")
                .thenCompose(res -> new MediaCommentRequest(res.getMedia().getId(), "First comment!").execute(client))
                .join();
        Assert.assertEquals("ok", response.getStatus());
        logger.debug("Success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testVideo() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        IGResponse response =
                client.actions().timeline().uploadVideo(new File("src/examples/resources/test.mp4"),
                        new File("src/examples/resources/cover.jpg"), "Nice photo").join();
        Assert.assertEquals("ok", response.getStatus());
        logger.debug("Success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testAlbum() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        List<SidecarInfo> info =
                Arrays.asList(SidecarPhoto.from(new File("src/examples/resources/cover.jpg")),
                        SidecarVideo.from(new File("src/examples/resources/test.mp4"),
                                new File("src/examples/resources/cover.jpg")));
        IGResponse response = client.actions().timeline()
                .uploadAlbum(info, new MediaConfigureSidecarPayload().caption("Nice album!"))
                .join();
        Assert.assertEquals("ok", response.getStatus());
        logger.debug("Success");
    }
}
