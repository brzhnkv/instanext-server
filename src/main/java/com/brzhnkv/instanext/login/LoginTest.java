package com.brzhnkv.instanext.login;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.brzhnkv.instanext.Main;
import org.junit.Assert;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.requests.accounts.AccountsCurrentUserRequest;
import com.github.instagram4j.instagram4j.responses.IGResponse;
import lombok.extern.slf4j.Slf4j;
import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class LoginTest {
    public static Logger logger = LoggerFactory.getLogger(Main.class);
    private final SerializeTestUtil serializeTestUtil;

    public LoginTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    public void testName(String username, String password)
            throws Exception {
        IGClient client = IGClient.builder()
                .username(username)
                .password(password)
                .client(serializeTestUtil.formTestHttpClient())
                .onLogin((cli, response) -> {
                    cli.sendRequest(new AccountsCurrentUserRequest()).join();
                })
                .login();
        logger.debug(client.toString());
        Assert.assertNotNull(client.getSelfProfile());
        logger.debug("Success");
    }

    public static void postLoginResponsesHandler(List<CompletableFuture<?>> responses) {
        responses.stream()
                .map(res -> res.thenApply(IGResponse.class::cast))
                .forEach(res -> {
                    res.thenAccept(igRes -> {
                        logger.info("{} : {}", igRes.getClass().getName(), igRes.getStatus());
                    });
                });
    }
}
