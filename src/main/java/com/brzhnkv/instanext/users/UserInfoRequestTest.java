package com.brzhnkv.instanext.users;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.exceptions.IGResponseException;
import com.github.instagram4j.instagram4j.requests.users.UsersInfoRequest;
import com.github.instagram4j.instagram4j.requests.users.UsersUsernameInfoRequest;
import com.github.instagram4j.instagram4j.responses.IGResponse;

import com.brzhnkv.instanext.serialize.SerializeTestUtil;

public class UserInfoRequestTest {
    private final SerializeTestUtil serializeTestUtil;

    public UserInfoRequestTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testLogin()
            throws IGLoginException, IGResponseException, ClassNotFoundException,
            FileNotFoundException, IOException {
        IGClient lib = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        UsersUsernameInfoRequest req = new UsersUsernameInfoRequest("seattlegoldgrills");
        UsersInfoRequest req2 = new UsersInfoRequest(18428658l);
        IGResponse response = lib.sendRequest(req).join(), res = lib.sendRequest(req2).join();
        Assert.assertEquals("ok", response.getStatus());
        Assert.assertEquals("ok", res.getStatus());
    }
}
