package com.brzhnkv.instanext.login;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Callable;

import org.junit.Assert;
import org.junit.Test;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.IGClient.Builder.LoginHandler;
import com.github.instagram4j.instagram4j.utils.IGChallengeUtils;

import com.brzhnkv.instanext.serialize.SerializeTestUtil;


public class ChallengeLoginTest {
    private final SerializeTestUtil serializeTestUtil;

    public ChallengeLoginTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    public void testChallengeLogin(String username, String password) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Callable that returns inputted code from System.in
        Callable<String> inputCode = () -> {
            System.out.print("Please input code: ");
            return scanner.nextLine();
        };

        // handler for challenge login
        LoginHandler challengeHandler = (client, response) -> {
            // included utility to resolve challenges
            // may specify retries. default is 3
            return IGChallengeUtils.resolveChallenge(client, response, inputCode);
        };

        IGClient client = IGClient.builder()
                .username(username)
                .password(password)
                .client(serializeTestUtil.formTestHttpClient())
                .onChallenge(challengeHandler)
                .login();

        Assert.assertNotNull(client.getSelfProfile());
    }
}
