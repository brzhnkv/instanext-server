package com.brzhnkv.instanext;

import com.brzhnkv.instanext.client.Client;
import com.brzhnkv.instanext.client.ClientService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import java.io.*;
import java.util.Arrays;
import java.util.Base64;


@Slf4j
@EnableAsync
@RestController
@SpringBootApplication
@EnableScheduling
@EnableWebSocketMessageBroker
@EnableWebSocket
@EnableWebMvc
@EnableConfigurationProperties
public class Main {

	// private String username = "reactnextjs";
	// private String password = "reactnextjs!";

	// private String username = "galinabraz";
	//private String password = "lapushok2010";

	//private String username = "bowsmaker";
	//private String password = "lapushok2010";
	public Logger logger = LoggerFactory.getLogger(Main.class);

    private final ClientService clientService;
    public Main(ClientService clientService) {
        this.clientService = clientService;
    }


    public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}



	@GetMapping("/")
	public String deploy() {
		return "Welcome to LikeTime Server";
	}



	@RequestMapping(path = "/test", method = RequestMethod.POST)
	public ResponseEntity authUser(@RequestBody String authData){


		JSONObject obj = new JSONObject(authData);
		String username = obj.getString("username");
		String password = obj.getString("password");

		logger.info(username);
		logger.info(password);


		ResponseEntity response;
		response = new ResponseEntity("Success", HttpStatus.OK);

		return response;
	}

    @RequestMapping(path = "/test1", method = RequestMethod.GET)
    public String test() throws IOException {

        logger.info("works");


		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource resource = resolver.getResource("igclient.ser");
		Resource resource2 = resolver.getResource("cookie.ser");

		File to = resource.getFile(), cookFile = resource2.getFile();
		InputStream fileIn = resource.getInputStream();
		InputStream cookieIS = resource2.getInputStream();

		byte[] buffer = new byte[1024];
		fileIn.read(buffer);

		byte[] clientFile = buffer;

		String username = "";
		String token = "";

       // clientService.addNewClient(new Client("keklol2", "234234355453", clientFile, cookieFile));

		byte[] clientFileFromDB = clientService.getClientByUsernameAndToken(username, token).get().getClientFile();
		byte[] cookieFileFromDB = clientService.getClientByUsernameAndToken(username, token).get().getCookieFile();

		ByteArrayInputStream clientInputStream = new ByteArrayInputStream(clientFileFromDB);
		ByteArrayInputStream cookieInputStream = new ByteArrayInputStream(cookieFileFromDB);

		String s = new String(clientFileFromDB);
		logger.info(s);

        return "test1";
    }

}
