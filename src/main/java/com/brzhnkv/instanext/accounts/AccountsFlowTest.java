package com.brzhnkv.instanext.accounts;

import java.util.concurrent.CompletableFuture;

import com.brzhnkv.instanext.Main;
import org.junit.Assert;
import org.junit.Test;

import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.responses.IGResponse;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Slf4j
public class AccountsFlowTest {

    private final SerializeTestUtil serializeTestUtil;

    public Logger logger = LoggerFactory.getLogger(Main.class);

    public AccountsFlowTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    public void testPreLoginFlow() {
        IGClient client = new IGClient("", "", serializeTestUtil.formTestHttpClient());
        client.actions().simulate().preLoginFlow().stream()
                .map(CompletableFuture::join)
                .map(IGResponse.class::cast)
                .forEach(res -> {
                    logger.info("{} : {}", res.getClass().getName(), res.getStatus());
                    Assert.assertEquals("ok", res.getStatus());
                });
        logger.info("Success");
    }

    @Test
    public void testPostLoginFlow() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        long start = System.currentTimeMillis();
        client.actions().simulate().postLoginFlow().parallelStream()
                .map(CompletableFuture::join)
                .map(IGResponse.class::cast)
                .forEach(res -> {
                    logger.info("{} : {}", res.getClass().getName(), res.getStatus());

                    Assert.assertEquals("ok", res.getStatus());
                });
        logger.info("Success Took {}", System.currentTimeMillis() - start);
    }
}
