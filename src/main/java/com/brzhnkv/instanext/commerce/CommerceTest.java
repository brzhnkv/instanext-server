package com.brzhnkv.instanext.commerce;

import com.brzhnkv.instanext.Main;
import org.junit.Assert;
import org.junit.Test;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.requests.commerce.CommerceDestinationRequest;
import com.github.instagram4j.instagram4j.requests.commerce.CommerceProductsDetailsRequest;
import com.github.instagram4j.instagram4j.responses.commerce.CommerceDestinationResponse;
import com.github.instagram4j.instagram4j.responses.commerce.CommerceProductsDetailsResponse;

import lombok.extern.slf4j.Slf4j;
import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class CommerceTest {
    public Logger logger = LoggerFactory.getLogger(Main.class);
    private final SerializeTestUtil serializeTestUtil;

    public CommerceTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testDestination() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        CommerceDestinationResponse response =
                new CommerceDestinationRequest().execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
        response.getSectional_items().stream().map(item -> item.getMedias())
                .forEach(media -> media.forEach(item -> logger.debug(item.getId())));
        logger.debug("Success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testProductDetails() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        CommerceProductsDetailsResponse response =
                new CommerceProductsDetailsRequest("2562765907136094", "7734459462").execute(client)
                        .join();
        Assert.assertEquals("ok", response.getStatus());
        logger.debug(response.getProduct_item().getName());
        logger.debug("Success");
    }
}
