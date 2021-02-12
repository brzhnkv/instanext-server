package com.brzhnkv.instanext.util;

import com.brzhnkv.instanext.*;

import com.brzhnkv.instanext.client.ClientService;
import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.actions.feed.FeedIterator;
import com.github.instagram4j.instagram4j.models.user.Profile;
import com.github.instagram4j.instagram4j.requests.direct.DirectThreadsBroadcastRequest;
import com.github.instagram4j.instagram4j.requests.direct.DirectThreadsBroadcastRequest.BroadcastMediaSharePayload;
import com.github.instagram4j.instagram4j.requests.feed.FeedTagRequest;
import com.github.instagram4j.instagram4j.responses.IGResponse;
import com.github.instagram4j.instagram4j.responses.feed.FeedTagResponse;
import com.github.instagram4j.instagram4j.utils.IGUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class Tasks {

	public Logger logger = LoggerFactory.getLogger(Main.class);

	private final NotificationDispatcher dispatcher;
	private final ClientService clientService;
	private final SerializeTestUtil serializeTestUtil;

	private int postsAlreadyLikedCount;
	private int postsLikedCount;
	private int postsSavedCount;
	private int postsSent;

	private String username;
	private String token;

	@Autowired
	public Tasks(NotificationDispatcher dispatcher, ClientService clientService, SerializeTestUtil serializeTestUtil) {
		this.serializeTestUtil = serializeTestUtil;
		this.clientService = clientService;
		this.dispatcher = dispatcher;
		this.postsAlreadyLikedCount = 0;
		this.postsLikedCount = 0;
		this.postsSavedCount = 0;
		this.postsSent = 0;
		this.username = "galinabraz";
		this.token = "lTj9agKZKUXu81ad4cIEYgNWtBAR9hnq";
	}

	public NotificationDispatcher getDispatcher() {
		return dispatcher;
	}

	public void authSession(String username, String token) throws Exception {
		IGClient client = serializeTestUtil.getClientFromSerialize(username, token);


		String profile_pic_url = client.getSelfProfile().getProfile_pic_url();
		logger.info(client.getCsrfToken());

		HashMap<String, String> data = new HashMap<String, String>();
		//data.put("username", username);
		data.put("userProfilePic", profile_pic_url);
		//data.put("token", token);
		dispatcher.dispatch(new Notification(data), "auth");
	}

	public void authLogin(String username, String password) throws Exception {
		logger.info(username);
		logger.info(password);
		String token = serializeTestUtil.serializeLogin(username, password);

		IGClient client = serializeTestUtil.getClientFromSerialize(username, token);
		String profile_pic_url = client.getSelfProfile().getProfile_pic_url();

		HashMap<String, String> data = new HashMap<String, String>();
		data.put("username", username);
		data.put("token", token);
		data.put("userProfilePic", profile_pic_url);
		dispatcher.dispatch(new Notification(data), "login");

		//authSession(username, token);
	}


	public void test() throws Exception {
		//IGClient client = SerializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");

		dispatcher.dispatch(new Notification("Задание выполняется."), "status");



/*
		List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.path("/files/")
					.path(dbFile.getId())
					.toUriString();

			return new ResponseFile(
					dbFile.getName(),
					fileDownloadUri,
					dbFile.getType(),
					dbFile.getData().length);
		}).collect(Collectors.toList());*/
/*
		IGClient client = SerializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
		String token = client.getCsrfToken();


		logger.info("Token: " + token);
		// wQPPePHdZiqX78WR4aMmb7sMlvrfifTn*/

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		dispatcher.dispatch(new Notification("ss"), "status");
	}

	public void getAllFollowers() throws Exception {
		IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");

		String username = "bowsmaker";
		
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
				logger.info("exception occured" + e);
			}

			String actionLog = "Пользователь добавлен в файл: " + u;
			dispatcher.dispatch(new Notification(actionLog), "status");
		});
		out.close();

	}
	
	
	public void sendMediaToGroup(String username, String token, String tag) throws Exception {
		postsSent = 0;

		IGClient client = serializeTestUtil.getClientFromSerialize(username, token);

		dispatcher.dispatch(new Notification("Задание выполняется."), "status");
		String base_url = "https://www.instagram.com/p/";
		String thread_id = "340282366841710300949128130629108875237";

		FeedIterator<FeedTagRequest, FeedTagResponse> iter = new FeedIterator<>(client, new FeedTagRequest(tag));
		FeedTagResponse response = null;
		while (iter.hasNext()) {
			response = iter.next();
			IGClient client2 = serializeTestUtil.getClientFromSerialize(username, token);
			// Actions here
			response.getItems().forEach(post -> {
				String url_code = base_url + IGUtils.toCode(Long.valueOf(post.getPk()));
				String media_id = post.getId();
				IGResponse response_thread = new DirectThreadsBroadcastRequest(
						new BroadcastMediaSharePayload(media_id, "", thread_id)).execute(client2).join();
				Assert.assertEquals("ok", response_thread.getStatus());
				postsSent++;
				String actionLog = "Пост отправлен в группу: " + url_code;
				dispatcher.dispatch(new Notification(actionLog), "log");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			// Recommended to wait in between iterations
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String log_info = "Всего публикаций отправлено: " + postsSent;
		dispatcher.dispatch(new Notification(log_info), "status");
		log_info = "Задание выполнено!" + " Публикаций отправлено: " + postsSent;
		dispatcher.dispatch(new Notification(log_info), "status");
	}

    public void likeByTag(String username, String token, String tag) throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize(username, token);

        postsLikedCount = 0;
        postsAlreadyLikedCount = 0;

        int likeDelay = 3000; // 3sec
        int skipDelay = 1000; // 1sec
        int feedIterationDelay = 5000; // 5sec
        int cycleDelay = 180000; // 3min

        dispatcher.dispatch(new Notification("Задание выполняется."), "status");

        String base_url = "https://www.instagram.com/p/";

        for (int i = 0; i < 3; i++) {
            // form a FeedIterator for FeedTagRequest

            FeedIterator<FeedTagRequest, FeedTagResponse> iter = new FeedIterator<>(client, new FeedTagRequest(tag));
            FeedTagResponse response = null;
            while (iter.hasNext()) {
                IGClient client2 = serializeTestUtil.getClientFromSerialize(username, token);
                try {
                    response = iter.next();
                    // Actions here
                    response.getItems().forEach(post -> {
                        String url_code = base_url + IGUtils.toCode(Long.valueOf(post.getPk()));
                        if (!post.isHas_liked()) {
                            Utility.likePost(client2, post);
                            postsLikedCount++;
                            String likedLog = "Лайк поставлен: " + url_code;
                            dispatcher.dispatch(new Notification(likedLog), "log");
                            try {
                                Thread.sleep(likeDelay);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            postsAlreadyLikedCount++;
                            String skipped_like_log = "Лайк уже стоит, пропускаю: " + url_code;
                            dispatcher.dispatch(new Notification(skipped_like_log), "log");
                            try {
                                Thread.sleep(skipDelay);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Recommended to wait in between iterations
                try {
                    Thread.sleep(feedIterationDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            String logInfo = "Проход #" + i + ". Постов с лайками до начала работы задания в этом проходе: " + postsAlreadyLikedCount + ". Всего лайков поставлено: " + postsLikedCount;
            dispatcher.dispatch(new Notification(logInfo), "status");
            postsAlreadyLikedCount = 0;

            logger.info(logInfo);
            try {
                Thread.sleep(cycleDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("Задание выполнено!");
    }

    public void saveByTag(String username, String token, String tag) throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize(username, token);

		postsSavedCount = 0;
        int saveDelay = 2000; // 2sec
        int feedIterationDelay = 5000; // 5sec
        int cycleDelay = 180000; // 3min

        dispatcher.dispatch(new Notification("Задание выполняется."), "status");

        String base_url = "https://www.instagram.com/p/";

        for (int i = 0; i < 1; i++) {
            // form a FeedIterator for FeedTagRequest
            FeedIterator<FeedTagRequest, FeedTagResponse> iter = new FeedIterator<>(client, new FeedTagRequest(tag));
            FeedTagResponse response = null;
            while (iter.hasNext()) {
                IGClient client2 = serializeTestUtil.getClientFromSerialize(username, token);
                response = iter.next();
                // Actions here
                response.getItems().forEach(post -> {
                    String url_code = base_url + IGUtils.toCode(Long.valueOf(post.getPk()));
                    Utility.savePost(client2, post);
                    String likedLog = "Пост сохранён: " + url_code;
                    dispatcher.dispatch(new Notification(likedLog), "log");
                    postsSavedCount++;
                    try {
                        Thread.sleep(saveDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                // Recommended to wait in between iterations
                try {
                    Thread.sleep(feedIterationDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            String logInfo = "Задание выполнено. Постов сохранено: " + postsSavedCount;
            dispatcher.dispatch(new Notification(logInfo), "status");

            logger.info(logInfo);
            try {
                Thread.sleep(cycleDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("Задание выполнено!");
    }

	public void likeAndSave(String username, String token, String tag) throws Exception {
		IGClient client = serializeTestUtil.getClientFromSerialize(username, token);

		postsLikedCount = 0;
		postsAlreadyLikedCount = 0;
		postsSavedCount = 0;
		dispatcher.dispatch(new Notification("Задание выполняется."), "status");

		String base_url = "https://www.instagram.com/p/";

		for (int i = 0; i < 2; i++) {
			// form a FeedIterator for FeedTagRequest
			FeedIterator<FeedTagRequest, FeedTagResponse> iter = new FeedIterator<>(client, new FeedTagRequest(tag));
			FeedTagResponse response = null;
			while (iter.hasNext()) {
				IGClient client2 = serializeTestUtil.getClientFromSerialize(username, token);
				response = iter.next();
				// Actions here
				response.getItems().forEach(post -> {
					String url_code = base_url + IGUtils.toCode(Long.valueOf(post.getPk()));
					if (!post.isHas_liked()) {
						Utility.likePost(client2, post);
						postsLikedCount++;
						Utility.savePost(client2, post);
						String likedLog = "Лайк поставлен / пост сохранён: " + url_code;
						dispatcher.dispatch(new Notification(likedLog), "log");
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
						dispatcher.dispatch(new Notification(skipped_like_log), "log");
					}
				});
				// Recommended to wait in between iterations
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			try {
				Thread.sleep(180000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		String logInfo = "Постов с лайками до начала работы задания: " + postsAlreadyLikedCount;
		dispatcher.dispatch(new Notification(logInfo), "status");
		logInfo = "Лайков поставлено: " + postsLikedCount;
		dispatcher.dispatch(new Notification(logInfo), "status");
		int total_posts = postsLikedCount + postsAlreadyLikedCount;

		logInfo = "Всего публикаций: " + total_posts;
		dispatcher.dispatch(new Notification(logInfo), "status");
		logInfo = "Задание выполнено!" + " Лайков поставлено: " + postsLikedCount;
		dispatcher.dispatch(new Notification(logInfo), "status");

		logger.info("Постов с лайками до начала работы задания: {}", postsAlreadyLikedCount);
		logger.info("Лайков поставлено: {}", postsLikedCount);
		logger.info("Всего публикаций: {}", postsLikedCount + postsAlreadyLikedCount);
		logger.info("Задание выполнено!");
	}
}
