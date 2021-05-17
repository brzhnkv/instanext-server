package com.brzhnkv.liketime.task;

import com.brzhnkv.liketime.Main;
import com.brzhnkv.liketime.notification.Notification;
import com.brzhnkv.liketime.notification.Dispatcher;
import com.brzhnkv.liketime.serialize.Serialize;
import com.brzhnkv.liketime.user.TempUser;
import com.brzhnkv.liketime.util.Utility;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.actions.feed.FeedIterator;
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineMedia;
import com.github.instagram4j.instagram4j.requests.direct.DirectThreadsBroadcastRequest;
import com.github.instagram4j.instagram4j.requests.feed.FeedTagRequest;
import com.github.instagram4j.instagram4j.responses.IGResponse;
import com.github.instagram4j.instagram4j.responses.feed.FeedTagResponse;
import com.github.instagram4j.instagram4j.utils.IGUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Component
public class Task {
    public Logger logger = LoggerFactory.getLogger(Main.class);

    @Autowired
    private Dispatcher dispatcher;


    public Task() {}


    public void likeByTag(String username, String tag) {
        IGClient client = Serialize.deserializeUser(username);

        TempUser.clear();
        int likeDelay = 3000; // 3sec
        int skipDelay = 1000; // 1sec
        int feedIterationDelay = 5000; // 5sec
        int cycleDelay = 180000; // 3min

        String taskName = "[лайк : #" + tag + "] ";
        String l = "";
        String s = taskName + "задание выполняется | идет подбор постов...";
        TempUser.addTempStatusMessage(s);
        dispatcher.dispatch(username, new Notification(s), "status");

        String base_url = "https://www.instagram.com/p/";

        Set<TimelineMedia> posts = new HashSet<>();

        FeedIterator<FeedTagRequest, FeedTagResponse> iter = new FeedIterator<>(client, new FeedTagRequest(tag));
        FeedTagResponse response = null;
        while (iter.hasNext()) {
            try {
                client = Serialize.deserializeUser(username);
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

        s = taskName + "задание выполняется | публикаций в обработке: " + posts.size();
        TempUser.addTempStatusMessage(s);
        dispatcher.dispatch(username, new Notification(s), "status");

        logger.info("10 sec to delete post");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (TimelineMedia post : posts) {
            if (post.isHas_liked()) {
                TempUser.postsAlreadyLikedCount++;
                continue;
            }
            ;
            client = Serialize.deserializeUser(username);
            String url_code = base_url + IGUtils.toCode(Long.valueOf(post.getPk()));

            Utility.likePost(client, post);
            TempUser.postsLikedCount++;
            l = "лайк поставлен: " + url_code;
            TempUser.addTempLogMessage(l);
            dispatcher.dispatch(username, new Notification(l), "log");
            // delay
            //Random random = new Random();
            //int  r = random.ints(5000,(12000+1)).findFirst().getAsInt();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int handledPosts = TempUser.postsLikedCount + TempUser.postsAlreadyLikedCount;
        l = "публикаций обработано: " + handledPosts
                + " | удаленные посты во время работы программы: " + TempUser.postsDeletedCount
                + " | лайки до начала работы: " + TempUser.postsAlreadyLikedCount
                + " | поставленные лайки: " + TempUser.postsLikedCount;
        TempUser.addTempLogMessage(l);
        dispatcher.dispatch(username, new Notification(l), "log");

        s = taskName + "задание выполнено";
        TempUser.addTempStatusMessage(s);
        TempUser.taskIsRunning = false;
        dispatcher.dispatch(username, new Notification(s), "status");
    }


    public void saveByTag(String username, String tag) {
        IGClient client = Serialize.deserializeUser(username);
        TempUser.clear();

        int saveDelay = 2000; // 2sec
        int feedIterationDelay = 5000; // 5sec
        int cycleDelay = 180000; // 3min

        String taskName = "[сохранение : #" + tag + "] ";
        String l = "";
        String s = taskName + "задание выполняется | идет подбор постов...";
        TempUser.addTempStatusMessage(s);
        dispatcher.dispatch(username, new Notification(s), "status");

        String base_url = "https://www.instagram.com/p/";

        Set<TimelineMedia> posts = new HashSet<>();

        FeedIterator<FeedTagRequest, FeedTagResponse> iter = new FeedIterator<>(client, new FeedTagRequest(tag));
        FeedTagResponse response = null;
        while (iter.hasNext()) {
            try {
                client = Serialize.deserializeUser(username);
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

        s = taskName + "задание выполняется | публикаций в обработке: " + posts.size();
        TempUser.addTempStatusMessage(s);
        dispatcher.dispatch(username, new Notification(s), "status");

        for (TimelineMedia post : posts) {
            client = Serialize.deserializeUser(username);
            String url_code = base_url + IGUtils.toCode(Long.valueOf(post.getPk()));

            Utility.savePost(client, post);
            TempUser.postsSavedCount++;

            l = "пост сохранён: " + url_code;
            TempUser.addTempLogMessage(l);
            dispatcher.dispatch(username, new Notification(l), "log");

            // delay
            //Random random = new Random();
            //int  r = random.ints(5000,(12000+1)).findFirst().getAsInt();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        l = "публикаций сохранено: " + TempUser.postsSavedCount
                + " | удаленные посты во время работы программы: " + TempUser.postsDeletedCount;
        TempUser.addTempLogMessage(l);
        dispatcher.dispatch(username, new Notification(l), "log");

        s = taskName + "задание выполнено";
        TempUser.addTempStatusMessage(s);
        TempUser.taskIsRunning = false;
        dispatcher.dispatch(username, new Notification(s), "status");
    }

    public void likeAndSave(String username, String tag) {
        IGClient client = Serialize.deserializeUser(username);
        TempUser.clear();

        String taskName = "[лайк + сохранение : #" + tag + "] ";
        String l = "";
        String s = taskName + "задание выполняется | идет подбор постов...";
        TempUser.addTempStatusMessage(s);
        dispatcher.dispatch(username, new Notification(s), "status");

        String base_url = "https://www.instagram.com/p/";

        Set<TimelineMedia> posts = new HashSet<>();

        FeedIterator<FeedTagRequest, FeedTagResponse> iter = new FeedIterator<>(client, new FeedTagRequest(tag));
        FeedTagResponse response = null;
        while (iter.hasNext()) {
            try {
                client = Serialize.deserializeUser(username);
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

        s = taskName + "задание выполняется | публикаций в обработке: " + posts.size();
        TempUser.addTempStatusMessage(s);
        dispatcher.dispatch(username, new Notification(s), "status");

        for (TimelineMedia post : posts) {
            client = Serialize.deserializeUser(username);
            String url_code = base_url + IGUtils.toCode(Long.valueOf(post.getPk()));
            if (!post.isHas_liked()) {
                Utility.likePost(client, post);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Utility.savePost(client, post);
                TempUser.postsLikedCount++;
                TempUser.postsSavedCount++;

                l = "лайк поставлен, пост сохранен: " + url_code;
                TempUser.addTempLogMessage(l);
                dispatcher.dispatch(username, new Notification(l), "log");
            } else {
                Utility.savePost(client, post);
                TempUser.postsSavedCount++;
                l = "пропуск лайка, пост сохранен: " + url_code;
                TempUser.addTempLogMessage(l);
                dispatcher.dispatch(username, new Notification(l), "log");
            }


            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        TempUser.postsDeletedCount /= 2;

        int handledPosts = TempUser.postsLikedCount + TempUser.postsAlreadyLikedCount;
        l = "публикаций обработано: " + handledPosts
                + " | удаленные посты во время работы программы: " + TempUser.postsDeletedCount
                + " | лайки до начала работы: " + TempUser.postsAlreadyLikedCount
                + " | поставленные лайки: " + TempUser.postsLikedCount;
        TempUser.addTempLogMessage(l);
        dispatcher.dispatch(username, new Notification(l), "log");

        s = taskName + "задание выполнено";
        TempUser.addTempStatusMessage(s);
        TempUser.taskIsRunning = false;
        dispatcher.dispatch(username, new Notification(s), "status");
    }


    public void sendMediaToGroup(String username, String tag) {
        IGClient client = Serialize.deserializeUser(username);
        TempUser.clear();

        String taskName = "[самолет : #" + tag + "] ";
        String l = "";
        String s = taskName + "задание выполняется | идет подбор постов...";
        TempUser.addTempStatusMessage(s);
        dispatcher.dispatch(username, new Notification(s), "status");

        String base_url = "https://www.instagram.com/p/";
        String thread_id = "340282366841710300949128130629108875237";

        Set<TimelineMedia> posts = new HashSet<>();

        FeedIterator<FeedTagRequest, FeedTagResponse> iter = new FeedIterator<>(client, new FeedTagRequest(tag));
        FeedTagResponse response = null;
        while (iter.hasNext()) {
            try {
                client = Serialize.deserializeUser(username);
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

        s = taskName + "задание выполняется | публикаций в обработке: " + posts.size();
        TempUser.addTempStatusMessage(s);
        dispatcher.dispatch(username, new Notification(s), "status");


        for (TimelineMedia post : posts) {
            client = Serialize.deserializeUser(username);
            String url_code = base_url + IGUtils.toCode(Long.valueOf(post.getPk()));

            String media_id = post.getId();
            IGResponse response_thread = new DirectThreadsBroadcastRequest(
                    new DirectThreadsBroadcastRequest.BroadcastMediaSharePayload(media_id, "", thread_id))
                    .execute(client).exceptionally(e -> {
                        TempUser.postsDeletedCount++;
                        TempUser.postsSentCount--;
                        return null;
                    }).join();
            TempUser.postsSentCount++;

            l = "пост отправлен в группу: " + url_code;
            TempUser.addTempLogMessage(l);
            dispatcher.dispatch(username, new Notification(l), "log");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        l = "публикаций отправлено: " + TempUser.postsSentCount
                + " | удаленные посты во время работы программы: " + TempUser.postsDeletedCount;
        TempUser.addTempLogMessage(l);
        dispatcher.dispatch(username, new Notification(l), "log");

        s = taskName + "задание выполнено";
        TempUser.addTempStatusMessage(s);
        TempUser.taskIsRunning = false;
        dispatcher.dispatch(username, new Notification(s), "status");
    }

///////////////////////////////// TEST ////////////////////////////////

    public void test(String username) {
        TempUser.clear();

        String s = "задание выполняется | публикаций в обработке: 120";
        TempUser.addTempStatusMessage(s);
        logger.info(TempUser.getTempStatusMessage().toString());
        dispatcher.dispatch(username, new Notification(s), "status");
        dispatcher.dispatch(username, new Notification("true"), "running");


        String l = String.valueOf(TempUser.postsLikedCount);
        TempUser.addTempLogMessage(l);
        dispatcher.dispatch(username, new Notification(l), "log");


        TempUser.postsLikedCount = 48;
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // dispatcher.send(user);
        TempUser.addTempLogMessage("лайк поставлен");
        dispatcher.dispatch(username, new Notification("лайк поставлен"), "log");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TempUser.addTempLogMessage("сохранение поставлено");
        dispatcher.dispatch(username, new Notification("сохранение поставлено"), "log");

        l = String.valueOf(TempUser.postsLikedCount);
        TempUser.addTempLogMessage(l);
        dispatcher.dispatch(username, new Notification(l), "log");
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

        s = "задание выполнено | публикаций обработано: 120";

        TempUser.addTempStatusMessage(s);
        dispatcher.dispatch(username, new Notification(s), "status");
    }

    /*public void getAllFollowers() throws Exception {
        IGClient client = Serialize.deserializeUser(new User("username", "token"));

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

    }*/


  /*  public void testLikers(String mediaId) throws Exception {
        IGClient client = Serialize.deserializeUser(new User("username", "token"));
        FeedUsersResponse response =
                new MediaGetLikersRequest(mediaId).execute(client).join();
        logger.info(response.getUsers().size() + "");
        Assert.assertEquals("ok", response.getStatus());
    }*/

   /* public void saveLikersToCSV(IGClient client, String account) throws IOException {


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
    }*/

   /* public void followLikersFromCSV(String username, String token, IGClient client) throws IOException {
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

       *//* List<Long> pks = new ArrayList<>();

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
        });*//*

        accounts.forEach(pk -> {
            IGResponse response = new FriendshipsActionRequest(pk, FriendshipsActionRequest.FriendshipsAction.CREATE)
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

    }*/
}

