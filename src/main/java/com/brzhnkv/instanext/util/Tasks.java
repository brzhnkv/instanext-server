package com.brzhnkv.instanext.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.brzhnkv.instanext.Notification;
import com.brzhnkv.instanext.NotificationDispatcher;
import com.brzhnkv.instanext.direct.DirectThreadBroadcastTest;
import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.actions.feed.FeedIterable;
import com.github.instagram4j.instagram4j.actions.feed.FeedIterator;
import com.github.instagram4j.instagram4j.actions.users.UserAction;
import com.github.instagram4j.instagram4j.models.IGBaseModel;
import com.github.instagram4j.instagram4j.models.user.Profile;
import com.github.instagram4j.instagram4j.requests.direct.DirectInboxRequest;
import com.github.instagram4j.instagram4j.requests.direct.DirectThreadsBroadcastRequest;
import com.github.instagram4j.instagram4j.requests.direct.DirectThreadsBroadcastRequest.BroadcastMediaSharePayload;
import com.github.instagram4j.instagram4j.requests.feed.FeedTagRequest;
import com.github.instagram4j.instagram4j.requests.users.UsersUsernameInfoRequest;
import com.github.instagram4j.instagram4j.responses.IGResponse;
import com.github.instagram4j.instagram4j.responses.direct.DirectInboxResponse;
import com.github.instagram4j.instagram4j.responses.feed.FeedTagResponse;
import com.github.instagram4j.instagram4j.utils.IGUtils;

import ch.qos.logback.core.joran.action.Action;

import org.junit.Assert;
import com.github.instagram4j.instagram4j.responses.feed.FeedUsersResponse;
import com.github.instagram4j.instagram4j.requests.feed.FeedUserRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Tasks {

	private final NotificationDispatcher dispatcher;
	private int postsAlreadyLikedCount;
	private int postsLikedCount;
	private int postsSavedCount;
	private int postsSent;

	@Autowired
	public Tasks(NotificationDispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.postsAlreadyLikedCount = 0;
		this.postsLikedCount = 0;
		this.postsSavedCount = 0;
		this.postsSent = 0;
	}

	public void test() throws Exception {
		IGClient client = SerializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");

		String username = "bowsmaker";// pk 14175586339
		String profile_pic_url = client.getSelfProfile().getProfile_pic_url();

		
		
	
		final List<Profile> result = client.actions().users().findByUsername(username)
                .thenApply(userAction -> userAction.followersFeed().stream()
                        .flatMap(feedUsersResponse -> feedUsersResponse.getUsers().stream()).collect(Collectors.toList())
                ).join();
		
		BufferedWriter out = new BufferedWriter(new FileWriter("src/main/resources/followers.csv", true));
		result.forEach(user -> {
			String u = user.getUsername();
			try {
				  out.write(u);
				  out.newLine(); 
			  } catch (IOException e) {
				  System.out.println("exception occured" + e); 
			  }
			  		  
			  String actionLog = "Пользователь добавлен в файл: " + u;
			  dispatcher.dispatch(new Notification(actionLog), "status");
		});
		out.close();
		
		/*
		 * while (followersResult == null || maxId != null) { try { followersResult =
		 * instagram.sendRequest(new InstagramGetUserFollowersRequest(user_pk,maxId));
		 * if (followersResult != null) { if (followersResult.getStatus().equals("ok"))
		 * { maxId = followersResult.getNext_max_id();
		 * followers.addAll(followersResult.getUsers()); }else{ break; } } } catch
		 * (IOException e) { Thread.sleep(5000); } }
		 */
		
//		  List<FeedUsersResponse> followers = new ArrayList<>(); while
//		  (response.getNext_max_id() != null) {
//		  
//		  response.getUsers().forEach(user -> { 
//			  String u = user.getUsername();
//		  
//		  
//		  
//		  try { Thread.sleep(3000); } catch (InterruptedException e) {
//		  e.printStackTrace(); }
//		  
//		  }); response = iter.next(); // Recommended to wait in between iterations
//		  Thread.sleep(5000); }
//		 

		// String followings_count =
		// String.valueOf(client.actions().account().currentUser().join().getUser().getFollowing_count());
		// String followers_count =
		// String.valueOf(client.actions().account().currentUser().get().getUser().getFollower_count());

		dispatcher.dispatch(new Notification(username), "status");
		dispatcher.dispatch(new Notification(""), "status");
//		  dispatcher.dispatch(new Notification(profile_pic_url), "status");
//		  dispatcher.dispatch(new Notification(followings_count), "status");
//		  dispatcher.dispatch(new Notification(followers_count), "status");

	}

	public void sendMediaToGroup(String tag) throws Exception {
		IGClient client = SerializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");

		String base_url = "https://www.instagram.com/p/";
		String thread_id = "340282366841710300949128130629108875237";

		FeedIterator<FeedTagResponse> iter = new FeedIterator<>(client, new FeedTagRequest(tag));
		FeedTagResponse response = null;
		while (iter.hasNext()) {
			response = iter.next();
			IGClient client2 = SerializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
			// Actions here
			response.getItems().forEach(post -> {
				String url_code = base_url + IGUtils.toCode(Long.valueOf(post.getPk()));
				String media_id = post.getId();
				IGResponse response_thread = new DirectThreadsBroadcastRequest(
						new BroadcastMediaSharePayload(media_id, "", thread_id)).execute(client2).join();
				Assert.assertEquals("ok", response_thread.getStatus());
				postsSent++;
				String actionLog = "Пост отправлен в группу: " + url_code;
				dispatcher.dispatch(new Notification(actionLog), "status");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			// Recommended to wait in between iterations
			Thread.sleep(5000);
		}
		String log_info = "Всего публикаций отправлено: " + postsSent;
		dispatcher.dispatch(new Notification(log_info), "status");
		log_info = "Задание выполнено!";
		dispatcher.dispatch(new Notification(log_info), "status");
	}

	public void likeAndSave(String tag) throws Exception {
		IGClient client = SerializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");

		dispatcher.dispatch(new Notification("Задание Выполняется."), "status");

		String base_url = "https://www.instagram.com/p/";
		// form a FeedIterator for FeedTagRequest
		FeedIterator<FeedTagResponse> iter = new FeedIterator<>(client, new FeedTagRequest(tag));
		FeedTagResponse response = null;
		while (iter.hasNext()) {
			IGClient client2 = SerializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
			response = iter.next();
			// Actions here
			response.getItems().forEach(post -> {
				String url_code = base_url + IGUtils.toCode(Long.valueOf(post.getPk()));
				if (!post.isHas_liked()) {
					Utility.likePost(client2, post);
					postsLikedCount++;
					Utility.savePost(client2, post);
					String likedLog = "Лайк поставлен / пост сохранён: " + url_code;
					dispatcher.dispatch(new Notification(likedLog), "status");
					postsSavedCount++;
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					postsAlreadyLikedCount++;
					String skipped_like_log = "Лайк уже стоит, пропускаю: " + url_code;
					dispatcher.dispatch(new Notification(skipped_like_log), "status");
				}
			});
			// Recommended to wait in between iterations
			Thread.sleep(5000);
			
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String logInfo = "Постов с лайками до начала работы задания: " + postsAlreadyLikedCount;
		dispatcher.dispatch(new Notification(logInfo), "status");
		logInfo = "Лайков поставлено: " + postsLikedCount;
		dispatcher.dispatch(new Notification(logInfo), "status");
		int total_posts = postsLikedCount + postsAlreadyLikedCount;

		logInfo = "Всего публикаций: " + total_posts;
		dispatcher.dispatch(new Notification(logInfo), "status");
		logInfo = "Задание выполнено!";
		dispatcher.dispatch(new Notification(logInfo), "status");

		log.info("Постов с лайками до начала работы задания: {}", postsAlreadyLikedCount);
		log.info("Лайков поставлено: {}", postsLikedCount);
		log.info("Всего публикаций: {}", postsLikedCount + postsAlreadyLikedCount);
		log.info("Задание выполнено!");
	}
}
