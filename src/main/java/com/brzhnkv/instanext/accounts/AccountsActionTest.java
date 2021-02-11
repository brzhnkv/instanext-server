package com.brzhnkv.instanext.accounts;

import java.io.File;

import com.brzhnkv.instanext.Main;
import org.junit.Assert;
import org.junit.Test;

import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.requests.accounts.AccountsActionRequest.AccountsAction;
import com.github.instagram4j.instagram4j.requests.accounts.AccountsCurrentUserRequest;
import com.github.instagram4j.instagram4j.requests.accounts.AccountsEditProfileRequest;
import com.github.instagram4j.instagram4j.requests.accounts.AccountsEditProfileRequest.AccountsEditProfilePayload;
import com.github.instagram4j.instagram4j.requests.accounts.AccountsSetBiographyRequest;
import com.github.instagram4j.instagram4j.responses.IGResponse;
import com.github.instagram4j.instagram4j.responses.accounts.AccountsUserResponse;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class AccountsActionTest {

    private final SerializeTestUtil serializeTestUtil;

    public Logger logger = LoggerFactory.getLogger(Main.class);

    public AccountsActionTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testCurrent() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        AccountsUserResponse response = new AccountsCurrentUserRequest().execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
        logger.debug("Success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testEdit() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        IGResponse response = new AccountsEditProfileRequest(
                new AccountsEditProfilePayload("username", "user@email.com")
                        .first_name("First name").biography("Cool!")).execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
        logger.debug("Success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testSetBiography() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        IGResponse response = new AccountsSetBiographyRequest("Test boi!").execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testChangeProfilePicture() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        IGResponse response = client.actions().account().setProfilePicture(new File("src/examples/resources/profile.jpg")).join();
        Assert.assertEquals("ok", response.getStatus());
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testAction() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        IGResponse response = client.actions().account().action(AccountsAction.SET_PRIVATE).join();
        Assert.assertEquals("ok", response.getStatus());
    }
}
