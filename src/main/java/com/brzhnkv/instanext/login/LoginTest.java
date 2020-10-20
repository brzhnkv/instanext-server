package com.brzhnkv.instanext.login;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.requests.accounts.AccountsCurrentUserRequest;
import com.github.instagram4j.instagram4j.responses.IGResponse;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import lombok.extern.slf4j.Slf4j;
import com.brzhnkv.instanext.serialize.SerializeTestUtil;

@Slf4j
@RunWith(JUnitParamsRunner.class)
public class LoginTest {
    @Test
    @FileParameters("src/main/resources/login.csv")
    public void testName(String username, String password)
            throws Exception {
        IGClient client = IGClient.builder()
                .username(username)
                .password(password)
                .client(SerializeTestUtil.formTestHttpClient())
                .onLogin((cli, response) -> {
                    cli.sendRequest(new AccountsCurrentUserRequest()).join();
                })
                .login();
        log.debug(client.toString());
        Assert.assertNotNull(client.getSelfProfile());
        log.debug("Success");
    }

    public static void postLoginResponsesHandler(List<CompletableFuture<?>> responses) {
        responses.stream()
                .map(res -> res.thenApply(IGResponse.class::cast))
                .forEach(res -> {
                    res.thenAccept(igRes -> {
                        log.info("{} : {}", igRes.getClass().getName(), igRes.getStatus());
                    });
                });
    }
}
