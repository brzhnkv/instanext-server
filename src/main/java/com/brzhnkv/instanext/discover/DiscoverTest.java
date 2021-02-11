package com.brzhnkv.instanext.discover;

import com.brzhnkv.instanext.Main;
import org.junit.Assert;
import org.junit.Test;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.models.discover.SectionalMediaGridItem;
import com.github.instagram4j.instagram4j.requests.discover.DiscoverTopicalExploreRequest;
import com.github.instagram4j.instagram4j.responses.discover.DiscoverTopicalExploreResponse;

import lombok.extern.slf4j.Slf4j;
import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class DiscoverTest {
    public Logger logger = LoggerFactory.getLogger(Main.class);
    private final SerializeTestUtil serializeTestUtil;

    public DiscoverTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testName() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        DiscoverTopicalExploreResponse response =
                new DiscoverTopicalExploreRequest().execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
        response.getSectional_items().forEach(
                item -> logger.debug("{} : {}", item.getLayout_type(), item.getClass().getName()));
        response.getSectional_items().stream().filter(i -> i instanceof SectionalMediaGridItem)
                .map(SectionalMediaGridItem.class::cast)
                .forEach(item -> item.getMedias().forEach(media -> logger.debug(media.getId())));
        logger.debug(response.getNext_max_id());
        response = new DiscoverTopicalExploreRequest(response.getNext_max_id(), null)
                .execute(client).join();
        logger.debug(response.getNext_max_id());
        logger.debug("Success");
    }
}
