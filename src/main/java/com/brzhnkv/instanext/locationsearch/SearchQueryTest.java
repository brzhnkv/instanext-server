package com.brzhnkv.instanext.locationsearch;

import com.brzhnkv.instanext.Main;
import org.junit.Assert;
import org.junit.Test;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.requests.locationsearch.LocationSearchRequest;
import com.github.instagram4j.instagram4j.responses.locationsearch.LocationSearchResponse;

import lombok.extern.slf4j.Slf4j;
import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class SearchQueryTest {
    public Logger logger = LoggerFactory.getLogger(Main.class);
    private final SerializeTestUtil serializeTestUtil;

    public SearchQueryTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testName() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        LocationSearchResponse response =
                new LocationSearchRequest(0d, 0d, "mcdonalds").execute(client).join();
        response.getVenues().forEach(v -> logger.debug("{} : {}", v.getName(), v.getExternal_id()));
        Assert.assertEquals("ok", response.getStatus());
        logger.debug("Success");
    }
}
