package com.brzhnkv.instanext.util;

import com.brzhnkv.instanext.*;

import com.brzhnkv.instanext.client.ClientService;
import com.brzhnkv.instanext.friendships.FriendshipsTest;
import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.actions.feed.FeedIterable;
import com.github.instagram4j.instagram4j.actions.feed.FeedIterator;
import com.github.instagram4j.instagram4j.models.friendships.Friendship;
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineMedia;
import com.github.instagram4j.instagram4j.models.user.Profile;
import com.github.instagram4j.instagram4j.models.user.User;
import com.github.instagram4j.instagram4j.requests.direct.DirectThreadsBroadcastRequest;
import com.github.instagram4j.instagram4j.requests.direct.DirectThreadsBroadcastRequest.BroadcastMediaSharePayload;
import com.github.instagram4j.instagram4j.requests.feed.FeedTagRequest;
import com.github.instagram4j.instagram4j.requests.feed.FeedUserRequest;
import com.github.instagram4j.instagram4j.requests.friendships.FriendshipsActionRequest;
import com.github.instagram4j.instagram4j.requests.friendships.FriendshipsActionRequest.FriendshipsAction;
import com.github.instagram4j.instagram4j.requests.friendships.FriendshipsShowRequest;
import com.github.instagram4j.instagram4j.requests.media.MediaGetLikersRequest;
import com.github.instagram4j.instagram4j.requests.users.UsersUsernameInfoRequest;
import com.github.instagram4j.instagram4j.responses.IGResponse;
import com.github.instagram4j.instagram4j.responses.feed.FeedTagResponse;
import com.github.instagram4j.instagram4j.responses.feed.FeedUserResponse;
import com.github.instagram4j.instagram4j.responses.feed.FeedUsersResponse;
import com.github.instagram4j.instagram4j.responses.friendships.FriendshipStatusResponse;
import com.github.instagram4j.instagram4j.utils.IGUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
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
        dispatcher.dispatch(username, new Notification(data), "auth");
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
        dispatcher.dispatch("guest", new Notification(data), "login");

        //authSession(username, token);
    }

    public void authAddAccount(String user, String username, String password) throws Exception {
        logger.info(username);
        logger.info(password);
        String token = serializeTestUtil.serializeLogin(username, password);

        IGClient client = serializeTestUtil.getClientFromSerialize(username, token);
        String profile_pic_url = client.getSelfProfile().getProfile_pic_url();

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", username);
        data.put("token", token);
        data.put("userProfilePic", profile_pic_url);
        dispatcher.dispatch(user, new Notification(data), "login/addaccount");

        //authSession(username, token);
    }


    public void test(String user, String token) throws Exception {
        //IGClient client = SerializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");

        dispatcher.dispatch(user, new Notification("Задание выполняется."), "status");
        dispatcher.dispatch(user, new Notification("true"), "running");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dispatcher.send(user);

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
     //   IGClient client = serializeTestUtil.getClientFromSerialize(user, token);
      //  followLikersFromCSV(user, token, client);

        dispatcher.dispatch(user, new Notification("Задание выполнено"), "status");
        dispatcher.dispatch(user, new Notification("false"), "running");
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
            dispatcher.dispatch(username, new Notification(actionLog), "status");
        });
        out.close();

    }


    public void sendMediaToGroup(String username, String token, String tag) throws Exception {
        postsSent = 0;

        IGClient client = serializeTestUtil.getClientFromSerialize(username, token);

        dispatcher.dispatch(username, new Notification("Задание выполняется."), "status");
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
                dispatcher.dispatch(username, new Notification(actionLog), "log");
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
        dispatcher.dispatch(username, new Notification(log_info), "status");
        log_info = "Задание выполнено!" + " Публикаций отправлено: " + postsSent;
        dispatcher.dispatch(username, new Notification(log_info), "status");
    }
/*
    // Hello, thank you for your work on this library, it's great, but I keep suffering from one problem here
    // I don't know, whether this problem is on your side or on instagram side, or maybe it's my bad code
    // I need to find medias by tag and like them all, with no skipping any of them
    // tag can be random, effect the same, here I took tag with 20 posts
    // So here is my code:


    IGClient client = serializeTestUtil.getClientFromSerialize(username, token);
    List<TimelineMedia> posts = FeedIterable.of(client, new FeedTagRequest("onedayinperu"))
            .stream()
            .flatMap(tagResponse -> {
                return tagResponse.getItems().stream();
            })
            .collect(Collectors.toList());

        logger.info("{} ", posts.size());  // displays correct size of 20 items, great


        AtomicInteger i = new AtomicInteger(0);

        posts.forEach(post -> {
            Utility.likePost(client, post);
            i.getAndIncrement();
            logger.info("{} liked", i);

            // delay
            try {
             Thread.sleep(3000);
            } catch (InterruptedException e) {
             e.printStackTrace();
            }
        });  // after this function is complete, all 20 posts successfully liked as it says
            // with response of 200 and status "ok", but
            //  actually not all of them liked, some is missing, and if I unlike all post by the hand and execute this code again, I'll get the same result with the same posts liked and not liked
            // I hope you can help me here and tell what I'm doing wrong, and sorry for my bad English...

log output:
    Response for https://i.instagram.com/api/v1/media/1874442164333159488_319876461/like/ with body (truncated) : {"status":"ok"}
    1 liked
    Response for https://i.instagram.com/api/v1/media/1874440026144093709_319876461/like/ with body (truncated) : {"status":"ok"}
    2 liked
    Response for https://i.instagram.com/api/v1/media/1821156522265587853_205516749/like/ with body (truncated) : {"status":"ok"}
    3 liked
    Response for https://i.instagram.com/api/v1/media/1781176659303624180_1119521471/like/ with body (truncated) : {"status":"ok"}
    4 liked
    Response for https://i.instagram.com/api/v1/media/1138051765741463400_7590424/like/ with body (truncated) : {"status":"ok"}
    5 liked
    Response for https://i.instagram.com/api/v1/media/1137934777090221184_7590424/like/ with body (truncated) : {"status":"ok"}
    6 liked
    Response for https://i.instagram.com/api/v1/media/1137745874152498338_7590424/like/ with body (truncated) : {"status":"ok"}
    7 liked
    Response for https://i.instagram.com/api/v1/media/1137365298010427093_633859386/like/ with body (truncated) : {"status":"ok"}
    8 liked
    Response for https://i.instagram.com/api/v1/media/1137326252026147254_31284011/like/ with body (truncated) : {"status":"ok"}
    9 liked
    Response for https://i.instagram.com/api/v1/media/336204398478951125_3239287/like/ with body (truncated) : {"status":"ok"}
    10 liked
    Response for https://i.instagram.com/api/v1/media/336203488818299595_3239287/like/ with body (truncated) : {"status":"ok"}
    11 liked
    Response for https://i.instagram.com/api/v1/media/336202604684182209_3239287/like/ with body (truncated) : {"status":"ok"}
    12 liked
    Response for https://i.instagram.com/api/v1/media/336201323953455790_3239287/like/ with body (truncated) : {"status":"ok"}
    13 liked
    Response for https://i.instagram.com/api/v1/media/336200310357951137_3239287/like/ with body (truncated) : {"status":"ok"}
    14 liked
    Response for https://i.instagram.com/api/v1/media/336199297509032597_3239287/like/ with body (truncated) : {"status":"ok"}
    15 liked
    Response for https://i.instagram.com/api/v1/media/336198486506799750_3239287/like/ with body (truncated) : {"status":"ok"}
    16 liked
    Response for https://i.instagram.com/api/v1/media/336194788783227466_3239287/like/ with body (truncated) : {"status":"ok"}
    17 liked
    Response for https://i.instagram.com/api/v1/media/336193357468598835_3239287/like/ with body (truncated) : {"status":"ok"}
    18 liked
    Response for https://i.instagram.com/api/v1/media/336192409354568229_3239287/like/ with body (truncated) : {"status":"ok"}
    19 liked
    Response for https://i.instagram.com/api/v1/media/336191535328723479_3239287/like/ with body (truncated) : {"status":"ok"}
    20 liked

    I thought maybe the medias gets duplicated somehow, but they are all different. I suggest that there is something with LIKE action
*/


    public void testLikers(String mediaId) throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize(username, token);
        FeedUsersResponse response =
                new MediaGetLikersRequest(mediaId).execute(client).join();
        logger.info(response.getUsers().size() + "");
        Assert.assertEquals("ok", response.getStatus());
    }

    public void saveLikersToCSV(IGClient client, String account) throws IOException {


        Set<Long> likers = new HashSet<>();
        // get user's pk

        long pk = new UsersUsernameInfoRequest(account).execute(client).join().getUser().getPk();

        // set array of user's last 18 posts
        List<TimelineMedia> posts = FeedIterable.of(client, new FeedUserRequest(pk))
                .stream()
                .limit(1)
                .flatMap(FeedUserResponse -> {
                    return FeedUserResponse.getItems().stream();
                })
                .collect(Collectors.toList());


        posts.stream().forEach(post -> {
            FeedUsersResponse response = new MediaGetLikersRequest(post.getId()).execute(client).join();
            logger.info(response.getUsers().size() + "");
            response.getUsers().forEach(u -> {
                if (u.is_private()) return;
                likers.add(u.getPk());
                // logger.info(u.getUsername());
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        PrintStream out = new PrintStream(new FileOutputStream(account + ".csv"));
        Iterator hashSetIterator = likers.iterator();
        while (hashSetIterator.hasNext()) {
            out.println(hashSetIterator.next());
        }
    }

    public void followLikersFromCSV(String username, String token, IGClient client) throws IOException {
       // saveLikersToCSV(client, "barashka_aktau");

        List<Long> accounts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("barashka_aktau.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                accounts.add(Long.valueOf(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

       /* List<Long> pks = new ArrayList<>();

        accounts.forEach(acc -> {
            User user = new UsersUsernameInfoRequest(acc).execute(client).join().getUser();

            long pk = user.getPk();
            logger.info(String.valueOf(pk));
            pks.add(pk);
            try {
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });*/

        accounts.forEach(pk -> {
            IGResponse response = new FriendshipsActionRequest(pk, FriendshipsAction.CREATE)
                    .execute(client).join();
            Assert.assertEquals("ok", response.getStatus());
              dispatcher.dispatch(username, new Notification("success"), "status");

            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        //  if (new FriendshipsActionRequest(pk, FriendshipsActionRequest.FriendshipsAction.))

    }

    public void newLikeByTag(String username, String token, String tag) throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize(username, token);

        //saveLikersToCSV(client, "barashka_aktau");
        followLikersFromCSV(username, token, client);
       /* String account = "brzhnkv";

        long pk = new UsersUsernameInfoRequest(account).execute(client).join().getUser().getPk();
        IGResponse response = new FriendshipsActionRequest(pk, FriendshipsActionRequest.FriendshipsAction.CREATE)
                .execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
*/


        logger.info("Done");

        postsLikedCount = 0;
        postsAlreadyLikedCount = 0;

        int likeDelay = 3000; // 3sec
        int skipDelay = 1000; // 1sec
        int feedIterationDelay = 5000; // 5sec
        int cycleDelay = 180000; // 3min

        dispatcher.dispatch(username, new Notification("Задание выполняется."), "status");

        String base_url = "https://www.instagram.com/p/";



       /* FeedUserRequest req = new FeedUserRequest(client.getSelfProfile().getPk());
        FeedUserResponse response = client.sendRequest(req).join();
        response.getItems().forEach(item -> {
            logger.info("{} : {}", item.getLike_count(), item.getClass().getName());

            FeedUsersResponse response2 =
                    new MediaGetLikersRequest(item.getId()).execute(client).join();
            logger.info(response2.getUsers().size() + "");
            Assert.assertEquals("ok", response2.getStatus());
        });
        Assert.assertEquals("ok", response.getStatus());*/
      /*  List<TimelineMedia> followers = FeedIterable.of(client, new FeedTagRequest("onedayinperu"))
                .stream()
                .flatMap(followersPageResponse -> {
                    return followersPageResponse.getItems().stream();
                })
                .collect(Collectors.toList());

        logger.info("{} ", followers.size());
        AtomicInteger i = new AtomicInteger(0);
        followers.forEach(post -> {

                    Utility.likePost(client, post);
                    i.getAndIncrement();
                    logger.info("{} liked", i);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                });*/
        // form a FeedIterator for FeedTimelineRequest
  /*      FeedIterable.of(client, new FeedTagRequest("onedayinperu"))
                .stream()

                .forEach(res -> {
                    logger.info("{} {}", res.getStatus(), res.getItems().size());
                    res.getItems().stream().map(r -> );
                });*/


    }

    public void likeByTag(String username, String token, String tag) throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize(username, token);

        postsLikedCount = 0;
        postsAlreadyLikedCount = 0;

        int likeDelay = 3000; // 3sec
        int skipDelay = 1000; // 1sec
        int feedIterationDelay = 5000; // 5sec
        int cycleDelay = 180000; // 3min

        dispatcher.dispatch(username, new Notification("Задание выполняется."), "status");

        String base_url = "https://www.instagram.com/p/";

        Set<TimelineMedia> posts = new HashSet<>();

        FeedIterator<FeedTagRequest, FeedTagResponse> iter = new FeedIterator<>(client, new FeedTagRequest(tag));
        FeedTagResponse response = null;
        while (iter.hasNext()) {
            try {
                IGClient client2 = serializeTestUtil.getClientFromSerialize(username, token);
                response = iter.next();
                if (response.getItems() != null) {
                    response.getItems().forEach(post -> {
                        posts.add(post);
                    });
                }
                if (response.getRanked_items() != null) {
                    response.getRanked_items().forEach(post -> {
                        posts.add(post);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        int size = posts.size();
        String str = "Постов: " + size;
        logger.info(str);
        dispatcher.dispatch(username, new Notification(str), "status");

        for (TimelineMedia post : posts) {
            if (post.isHas_liked()) continue;
            IGClient client2 = serializeTestUtil.getClientFromSerialize(username, token);
            String url_code = base_url + IGUtils.toCode(Long.valueOf(post.getPk()));

            Utility.likePost(client2, post);
            postsLikedCount++;
            String likedLog = "Лайк поставлен: " + url_code;
            dispatcher.dispatch(username, new Notification(likedLog), "log");
            // delay
            //Random random = new Random();
            //int  r = random.ints(5000,(12000+1)).findFirst().getAsInt();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String finalStr = "Задание выполнено! Всего постов: " + size + ". Лайков поставлено: " + postsLikedCount;
        logger.info(finalStr);
        dispatcher.dispatch(username, new Notification(finalStr), "status");
    }

    public void saveByTag(String username, String token, String tag) throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize(username, token);

        postsSavedCount = 0;
        int saveDelay = 2000; // 2sec
        int feedIterationDelay = 5000; // 5sec
        int cycleDelay = 180000; // 3min

        dispatcher.dispatch(username, new Notification("Задание выполняется."), "status");

        String base_url = "https://www.instagram.com/p/";

        Set<TimelineMedia> posts = new HashSet<>();

        FeedIterator<FeedTagRequest, FeedTagResponse> iter = new FeedIterator<>(client, new FeedTagRequest(tag));
        FeedTagResponse response = null;
        while (iter.hasNext()) {
            try {
                IGClient client2 = serializeTestUtil.getClientFromSerialize(username, token);
                response = iter.next();
                if (response.getItems() != null) {
                    response.getItems().forEach(post -> {
                        posts.add(post);
                    });
                }
                if (response.getRanked_items() != null) {
                    response.getRanked_items().forEach(post -> {
                        posts.add(post);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        int size = posts.size();
        String str = "Постов: " + size;
        logger.info(str);
        dispatcher.dispatch(username, new Notification(str), "status");

        for (TimelineMedia post : posts) {
            IGClient client2 = serializeTestUtil.getClientFromSerialize(username, token);
            String url_code = base_url + IGUtils.toCode(Long.valueOf(post.getPk()));

            Utility.savePost(client2, post);

            String likedLog = "Пост сохранён: " + url_code;
            dispatcher.dispatch(username, new Notification(likedLog), "log");
            // delay
            //Random random = new Random();
            //int  r = random.ints(5000,(12000+1)).findFirst().getAsInt();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String finalStr = "Задание выполнено! Всего постов: " + size;
        logger.info(finalStr);
        dispatcher.dispatch(username, new Notification(finalStr), "status");
    }

    public void likeAndSave(String username, String token, String tag) throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize(username, token);

        postsLikedCount = 0;
        postsAlreadyLikedCount = 0;
        postsSavedCount = 0;

        dispatcher.dispatch(username, new Notification("Задание выполняется."), "status");

        String base_url = "https://www.instagram.com/p/";

        Set<TimelineMedia> posts = new HashSet<>();

        FeedIterator<FeedTagRequest, FeedTagResponse> iter = new FeedIterator<>(client, new FeedTagRequest(tag));
        FeedTagResponse response = null;
        while (iter.hasNext()) {
            try {
                response = iter.next();
                if (response.getItems() != null) {
                    response.getItems().forEach(post -> {
                        posts.add(post);
                    });
                }
                if (response.getRanked_items() != null) {
                    response.getRanked_items().forEach(post -> {
                        posts.add(post);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int size = posts.size();
        String str = "Постов: " + size;
        logger.info(str);
        dispatcher.dispatch(username, new Notification(str), "status");

        for (TimelineMedia post : posts) {
            if (post.isHas_liked()) continue;
            IGClient client2 = serializeTestUtil.getClientFromSerialize(username, token);
            String url_code = base_url + IGUtils.toCode(Long.valueOf(post.getPk()));

            Utility.likePost(client2, post);
            Utility.savePost(client2, post);

            String likedLog = "Лайк поставлен, пост сохранен: " + url_code;
            dispatcher.dispatch(username, new Notification(likedLog), "log");
            // delay
            //Random random = new Random();
            //int  r = random.ints(5000,(12000+1)).findFirst().getAsInt();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String finalStr = "Задание выполнено! Всего постов: " + size;
        logger.info("Задание выполнено!");
        dispatcher.dispatch(username, new Notification(finalStr), "status");
    }
}
