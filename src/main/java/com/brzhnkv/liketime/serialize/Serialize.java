package com.brzhnkv.liketime.serialize;

import com.brzhnkv.liketime.Main;
import com.brzhnkv.liketime.user.User;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.utils.IGUtils;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class Serialize {

    public static Logger logger = LoggerFactory.getLogger(Main.class);

    public static IGClient deserializeUser(User user)
            throws ClassNotFoundException, IOException {

        ByteArrayInputStream clientFile = new ByteArrayInputStream(user.getClientFile());
        ByteArrayInputStream cookieFile = new ByteArrayInputStream(user.getCookieFile());
        ObjectInputStream oIn = new ObjectInputStream(cookieFile);

        IGClient client = IGClient.from(clientFile,
                formTestHttpClient(SerializableCookieJar.class.cast(oIn.readObject())));

        cookieFile.close();
        oIn.close();

        return client;
    }


    public static User serializeUser(String username, String password) throws IGLoginException {
        int proxyPort = 8000;
        String proxyHost = "213.139.221.170";
        final String proxyUsername = "4GZKVg";
        final String proxyPassword = "MxULAY";
        Authenticator proxyAuthenticator = new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) {
                String credential = Credentials.basic(proxyUsername, proxyPassword);
                return response.request().newBuilder()
                        .header("Proxy-Authorization", credential)
                        .build();
            }
        };

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        OkHttpClient httpClient = new OkHttpClient.Builder().proxy(proxy).proxyAuthenticator(proxyAuthenticator).build();
        SerializableCookieJar jar = new SerializableCookieJar();
        IGClient lib = null;
        lib = new IGClient.Builder().username(username).password(password)
                .client(formTestHttpClient(jar))
                .onLogin((cli, lr) -> Assert.assertEquals("ok", lr.getStatus()))
                // .client(httpClient)
                .login();
        logger.info("Serializing. . .");
        String token = lib.getCsrfToken();
        String profilePicUrl = lib.getSelfProfile().getProfile_pic_url();
        //serialize
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.writeObject(lib);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] clientFileBytes = outputStream.toByteArray();

        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        ObjectOutputStream os2 = null;
        try {
            os2 = new ObjectOutputStream(outputStream2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os2.writeObject(jar);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] cookieFileBytes = outputStream2.toByteArray();

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            outputStream2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new User(password, token, profilePicUrl, clientFileBytes, cookieFileBytes);
    }

    private static OkHttpClient formTestHttpClient() {
        int proxyPort = 8000;
        String proxyHost = "213.139.221.170";
        final String proxyUsername = "4GZKVg";
        final String proxyPassword = "MxULAY";
        Authenticator proxyAuthenticator = new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                String credential = Credentials.basic(proxyUsername, proxyPassword);
                return response.request().newBuilder()
                        //      .header("Proxy-Authorization", credential)
                        .build();
            }
        };

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));

        return IGUtils.defaultHttpClientBuilder().addInterceptor(loggingInterceptor)
                //    .proxy(proxy)
                //   .proxyAuthenticator(proxyAuthenticator)
                .build();
    }

    private static OkHttpClient formTestHttpClient(SerializableCookieJar jar) {
        int proxyPort = 8000;
        String proxyHost = "213.139.221.170";
        final String proxyUsername = "4GZKVg";
        final String proxyPassword = "MxULAY";
        Authenticator proxyAuthenticator = new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                String credential = Credentials.basic(proxyUsername, proxyPassword);
                return response.request().newBuilder()
                        //        .header("Proxy-Authorization", credential)
                        .build();
            }
        };

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        return IGUtils.defaultHttpClientBuilder().cookieJar(jar)
                .addInterceptor(loggingInterceptor)
                //   .proxy(proxy)
                //   .proxyAuthenticator(proxyAuthenticator)
                .build();
    }

    private static final HttpLoggingInterceptor loggingInterceptor =
            new HttpLoggingInterceptor((msg) -> {
                logger.debug(msg);
            }).setLevel(HttpLoggingInterceptor.Level.BODY);
}
