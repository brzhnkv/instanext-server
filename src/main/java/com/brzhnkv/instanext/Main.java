package com.brzhnkv.instanext;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;


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


	// TODO
	/*
		allowedUsersList
	 */

	public Logger logger = LoggerFactory.getLogger(Main.class);


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

}
