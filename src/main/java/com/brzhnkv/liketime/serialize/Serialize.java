package com.brzhnkv.liketime.serialize;

import com.brzhnkv.liketime.Main;
import com.brzhnkv.liketime.user.User;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.utils.IGUtils;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
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

    public static IGClient deserializeUser(String username) {

        String clientPath = username + "/clientFile";
        String cookiePath = username + "/cookieFile";

        Bucket bucket = StorageClient.getInstance().bucket();
        byte[] clientBytes = bucket.get(clientPath).getContent();
        byte[] cookieBytes = bucket.get(cookiePath).getContent();

        ByteArrayInputStream clientFile = new ByteArrayInputStream(clientBytes);
        ByteArrayInputStream cookieFile = new ByteArrayInputStream(cookieBytes);
        ObjectInputStream oIn = null;
        try {
            oIn = new ObjectInputStream(cookieFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        IGClient client = null;
        try {
            client = IGClient.from(clientFile,
                    formTestHttpClient(SerializableCookieJar.class.cast(oIn.readObject())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            cookieFile.close();
            oIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            os.writeObject(lib);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] clientFileBytes = outputStream.toByteArray();

        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        ObjectOutputStream os2 = null;
        try {
            os2 = new ObjectOutputStream(outputStream2);
            os2.writeObject(jar);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] cookieFileBytes = outputStream2.toByteArray();

        try {
            outputStream.close();
            os.close();
            outputStream2.close();
            os2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bucket bucket = StorageClient.getInstance().bucket();

        String blobStringClient = username + "/clientFile";
        String blobStringCookie = username + "/cookieFile";
        bucket.create(blobStringClient, clientFileBytes);
        bucket.create(blobStringCookie, cookieFileBytes);

        return new User(token, password, profilePicUrl);
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
