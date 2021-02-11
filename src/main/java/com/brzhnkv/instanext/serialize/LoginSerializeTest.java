package com.brzhnkv.instanext.serialize;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.brzhnkv.instanext.Main;
import org.junit.Test;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.exceptions.IGResponseException;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class LoginSerializeTest {
    public Logger logger = LoggerFactory.getLogger(Main.class);
    private final SerializeTestUtil serializeTestUtil;

    public LoginSerializeTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testName()
            throws IGLoginException, IGResponseException, ClassNotFoundException,
            FileNotFoundException, IOException {
        

        IGClient lib = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        logger.debug(lib.toString());
    }
}
