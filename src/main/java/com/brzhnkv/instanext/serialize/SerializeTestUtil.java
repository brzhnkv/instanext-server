package com.brzhnkv.instanext.serialize;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Optional;

import com.brzhnkv.instanext.Main;
import com.brzhnkv.instanext.client.Client;
import com.brzhnkv.instanext.client.ClientService;
import com.brzhnkv.instanext.user.User;
import com.brzhnkv.instanext.user.UserService;
import okhttp3.*;
import org.junit.Assert;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.utils.IGUtils;


import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class SerializeTestUtil {

    public static Logger logger = LoggerFactory.getLogger(Main.class);
   // private final ClientService clientService;
    private final UserService userService;

    public SerializeTestUtil(ClientService clientService, UserService userService) {
      //  this.clientService = clientService;
        this.userService = userService;
    }


    public String serializeLogin(String username, String password)
            throws ClassNotFoundException, IOException {



        int proxyPort = 8000;
        String proxyHost = "213.139.221.170";
        final String proxyUsername = "4GZKVg";
        final String proxyPassword = "MxULAY";
        Authenticator proxyAuthenticator = new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                String credential = Credentials.basic(proxyUsername, proxyPassword);
                return response.request().newBuilder()
                      //  .header("Proxy-Authorization", credential)
                        .build();
            }
        };

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        OkHttpClient httpClient = new OkHttpClient.Builder().proxy(proxy).proxyAuthenticator(proxyAuthenticator).build();
        SerializableCookieJar jar = new SerializableCookieJar();
        IGClient lib = null;
        try {
            lib = new IGClient.Builder().username(username).password(password)
                    .client(formTestHttpClient(jar))
                    .onLogin((cli, lr) -> Assert.assertEquals("ok", lr.getStatus()))
                   // .client(httpClient)
                    .login();
        } catch (IGLoginException e) {
            e.printStackTrace();
        }
        logger.info("Serializing. . .");
        String token = lib.getCsrfToken();
        serialize(lib, jar, username, password, token);


        return token;
    }

    // logging interceptor
    private final HttpLoggingInterceptor loggingInterceptor =
            new HttpLoggingInterceptor((msg) -> {
                logger.debug(msg);
            }).setLevel(Level.BODY);

    public IGClient getClientFromSerialize(String username, String token)
            throws ClassNotFoundException, FileNotFoundException, IOException {

        User userByUsernameAndToken = userService.getUserByUsernameAndToken(username, token).get();

        ByteArrayInputStream clientFile = new ByteArrayInputStream(userByUsernameAndToken.getClientFile());
        ByteArrayInputStream cookieFile = new ByteArrayInputStream(userByUsernameAndToken.getCookieFile());

        IGClient client = IGClient.from(clientFile,
                formTestHttpClient(deserialize(cookieFile, SerializableCookieJar.class)));

        return client;
    }


    public void serialize(Object lib, Object jar, String username, String password, String token) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(outputStream);
        os.writeObject(lib);
        byte[] clientFileBytes = outputStream.toByteArray();

        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        ObjectOutputStream os2 = new ObjectOutputStream(outputStream2);
        os2.writeObject(jar);
        byte[] cookieFileBytes = outputStream2.toByteArray();

       // clientService.addNewClient(new Client(username, token, clientFileBytes, cookieFileBytes));



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

    public OkHttpClient formTestHttpClient(SerializableCookieJar jar) {
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
}
