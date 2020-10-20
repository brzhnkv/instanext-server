package com.brzhnkv.instanext;

import java.util.List;

import org.junit.Assert;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.actions.feed.FeedIterator;
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineMedia;
import com.github.instagram4j.instagram4j.requests.feed.FeedTagRequest;
import com.github.instagram4j.instagram4j.requests.media.MediaActionRequest;
import com.github.instagram4j.instagram4j.requests.media.MediaActionRequest.MediaAction;
import com.github.instagram4j.instagram4j.responses.IGResponse;
import com.github.instagram4j.instagram4j.responses.feed.FeedTagResponse;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@EnableAsync
@RestController
@SpringBootApplication
@EnableScheduling
public class InstanextApplication {

	// private String username = "reactnextjs";
	// private String password = "reactnextjs!";

	 private String username = "galinabraz";
	 private String password = "lapushok2010";

	//private String username = "bowsmaker";
	//private String password = "lapushok2010";

	private void likePost(IGClient client, String media_id) {
		IGResponse response = new MediaActionRequest(media_id, MediaAction.LIKE).execute(client).join();
		Assert.assertEquals("ok", response.getStatus());
	}

	private void unlikePost(IGClient client, String media_id) {
		IGResponse response = new MediaActionRequest(media_id, MediaAction.UNLIKE).execute(client).join();
		Assert.assertEquals("ok", response.getStatus());
	}

	private void savePost(IGClient client, String media_id) {
		IGResponse response = new MediaActionRequest(media_id, MediaAction.SAVE).execute(client).join();
		Assert.assertEquals("ok", response.getStatus());
	}

	@GetMapping("/load")
	public String index() throws Exception {
		IGClient client = IGClient.builder().username(username).password(password).login();
		if (client.isLoggedIn()) {
			return "logged in";
		} else
			return "This is Home page";
	}

	@GetMapping("/login")
	public String login() throws Exception {
		TestTemplate test = new TestTemplate();
		test.testName();
		return "This is Home page";
	}

	int likedWasCount = 0;
	int likedCount = 0;

	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/test")
	public void test() throws Exception {
		SimpMessagingTemplate template;
		
	}
	
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/gettag")
	public List<TimelineMedia> getTag() throws Exception {
		IGClient client = SerializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");

		// form a FeedIterator for FeedTimelineRequest
		FeedIterator<FeedTagResponse> iter = new FeedIterator<>(client, new FeedTagRequest("нaчнисeгoдня"));
		FeedTagResponse response = null;
		while (iter.hasNext()) {
			response = iter.next();
			// Actions here
			response.getItems().forEach(m -> {
				String media_id = m.getId();
				log.info(media_id);
				
				if (!m.isHas_liked()) {
					likePost(client, media_id);
					likedCount++;
					savePost(client, media_id);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					likedWasCount++;
					log.info("Skipped");
				}
			});
			// Recommended to wait in between iterations
			Thread.sleep(3000);
		}

		log.info("Лайков пропущено: {}", likedWasCount);
		log.info("Лайков поставлено: {}", likedCount);
		log.info("Всего публикаций: {}", likedCount + likedWasCount);
		log.info("Success");
		return response.getItems();
	}

	public static void main(String[] args) {
		SpringApplication.run(InstanextApplication.class, args);
	}

	
	 @Bean public WebMvcConfigurer corsConfigurer() { return new
	 WebMvcConfigurer() {
	 
	 @Override public void addCorsMappings(CorsRegistry registry) {
	 registry.addMapping("/**").allowedMethods("*"); } }; }
	 

}
