package com.brzhnkv.instanext.serialize;

import java.io.*;

import com.brzhnkv.instanext.Main;
import com.brzhnkv.instanext.client.Client;
import com.brzhnkv.instanext.client.ClientService;
import org.junit.Assert;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.exceptions.IGResponseException;
import com.github.instagram4j.instagram4j.utils.IGUtils;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;


@Service
public class SerializeTestUtil {

    public static Logger logger = LoggerFactory.getLogger(Main.class);
    private final ClientService clientService;

    public SerializeTestUtil(ClientService clientService) {
        this.clientService = clientService;
    }


    public String serializeLogin(String username, String password)
            throws ClassNotFoundException, IOException {

        SerializableCookieJar jar = new SerializableCookieJar();
        IGClient lib = new IGClient.Builder().username(username).password(password)
                .client(formTestHttpClient(jar))
                .onLogin((cli, lr) -> Assert.assertEquals("ok", lr.getStatus()))
                .login();
        logger.info("Serializing. . .");
        String token = lib.getCsrfToken();
        serialize(lib, jar, username, token);

        ByteArrayInputStream clientFile = new ByteArrayInputStream(clientService
                .getClientByUsernameAndToken(username, token)
                .get()
                .getClientFile());
        ByteArrayInputStream cookieFile = new ByteArrayInputStream(clientService
                .getClientByUsernameAndToken(username, token)
                .get()
                .getCookieFile());

        IGClient saved = IGClient.from(clientFile,
                formTestHttpClient(deserialize(cookieFile, SerializableCookieJar.class)));
        logger.info(saved.toString());

        Assert.assertEquals(saved, lib);

        return token;
    }

    // logging interceptor
    private final HttpLoggingInterceptor loggingInterceptor =
            new HttpLoggingInterceptor((msg) -> {
                logger.debug(msg);
            }).setLevel(Level.BODY);

    public IGClient getClientFromSerialize(String username, String token)
            throws ClassNotFoundException, FileNotFoundException, IOException {

        ByteArrayInputStream clientFile = new ByteArrayInputStream(clientService
                .getClientByUsernameAndToken(username, token)
                .get()
                .getClientFile());
        ByteArrayInputStream cookieFile = new ByteArrayInputStream(clientService
                .getClientByUsernameAndToken(username, token)
                .get()
                .getCookieFile());

        IGClient client = IGClient.from(clientFile,
                formTestHttpClient(deserialize(cookieFile, SerializableCookieJar.class)));

        return client;
    }


    public void serialize(Object lib, Object jar, String username, String token) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(outputStream);
        os.writeObject(lib);
        byte[] clientFileBytes = outputStream.toByteArray();

        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        ObjectOutputStream os2 = new ObjectOutputStream(outputStream2);
        os2.writeObject(jar);
        byte[] cookieFileBytes = outputStream2.toByteArray();

        clientService.addNewClient(new Client(username, token, clientFileBytes, cookieFileBytes));

        outputStream.close();
        os.close();

        outputStream2.close();
        os2.close();
    }


    public <T> T deserialize(InputStream inputStream, Class<T> clazz) throws IOException, ClassNotFoundException {

        ObjectInputStream oIn = new ObjectInputStream(inputStream);

        T t = clazz.cast(oIn.readObject());

        inputStream.close();
        oIn.close();

        return t;
    }

    public OkHttpClient formTestHttpClient() {
        return IGUtils.defaultHttpClientBuilder().addInterceptor(loggingInterceptor)
                .build();
    }

    public OkHttpClient formTestHttpClient(SerializableCookieJar jar) {
        return IGUtils.defaultHttpClientBuilder().cookieJar(jar)
                .addInterceptor(loggingInterceptor).build();
    }
}
