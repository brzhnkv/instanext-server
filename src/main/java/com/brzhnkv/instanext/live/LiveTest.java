package com.brzhnkv.instanext.live;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import com.brzhnkv.instanext.Main;
import org.junit.Test;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.exceptions.IGResponseException;
import com.github.instagram4j.instagram4j.requests.live.LiveBroadcastGetCommentRequest;
import com.github.instagram4j.instagram4j.requests.live.LiveBroadcastGetLikeCountRequest;
import com.github.instagram4j.instagram4j.requests.live.LiveBroadcastGetViewerListRequest;
import com.github.instagram4j.instagram4j.requests.live.LiveCreateRequest;
import com.github.instagram4j.instagram4j.requests.live.LiveEndBroadcastRequest;
import com.github.instagram4j.instagram4j.requests.live.LiveGetQuestionsRequest;
import com.github.instagram4j.instagram4j.requests.live.LiveQuestionActivateRequest;
import com.github.instagram4j.instagram4j.requests.live.LiveQuestionDeactivateRequest;
import com.github.instagram4j.instagram4j.requests.live.LiveStartRequest;
import com.github.instagram4j.instagram4j.responses.IGResponse;
import com.github.instagram4j.instagram4j.responses.live.LiveBroadcastGetCommentResponse;
import com.github.instagram4j.instagram4j.responses.live.LiveBroadcastGetViewerListResponse;
import com.github.instagram4j.instagram4j.responses.live.LiveBroadcastLikeResponse.LiveBroadcastGetLikeCountResponse;
import com.github.instagram4j.instagram4j.responses.live.LiveCreateResponse;

import lombok.extern.slf4j.Slf4j;
import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class LiveTest {
    public Logger logger = LoggerFactory.getLogger(Main.class);
    private final SerializeTestUtil serializeTestUtil;

    public LiveTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void test()
            throws IGLoginException, IGResponseException, ClassNotFoundException,
            FileNotFoundException, IOException {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        LiveCreateRequest liveCreate = new LiveCreateRequest();
        LiveStartRequest liveStart;
        LiveCreateResponse createResponse = client.sendRequest(liveCreate).join();
        System.out.printf("Url:\n%s\nKey:\n%s\nBroadcast Id:\n%s\n",
                createResponse.getBroadcastUrl(), createResponse.getBroadcastKey(),
                createResponse.getBroadcast_id());
        Scanner in = new Scanner(System.in);
        System.out.println("Type 's' to start");
        in.nextLine();
        String id = createResponse.getBroadcast_id();
        liveStart = new LiveStartRequest(id, true);
        client.sendRequest(liveStart);

        System.out.println("Currently streaming. Type 'end' to end.");
        String input;
        long like_ts = 0, comment_ts = 0;
        while (!(input = in.nextLine()).equals("end")) {
            try {
                switch (input.split(" ")[0]) {
                    default:
                        logger.info("Unknown. Type 'end' to end.");
                        break;
                    case "viewers":
                        LiveBroadcastGetViewerListRequest vrequest =
                                new LiveBroadcastGetViewerListRequest(id);
                        LiveBroadcastGetViewerListResponse vresponse =
                                vrequest.execute(client).join();
                        logger.info("Viewers List ({})", vresponse.getUsers().size());
                        vresponse.getUsers().forEach(profile -> logger.info("{} ({})",
                                profile.getUsername(), profile.getPk()));
                        break;
                    case "comments":
                        LiveBroadcastGetCommentRequest crequest =
                                new LiveBroadcastGetCommentRequest(id, comment_ts);
                        LiveBroadcastGetCommentResponse cresponse = crequest.execute(client).join();
                        if (cresponse.getComments().size() > 0)
                            comment_ts = cresponse.getComments()
                                    .get(cresponse.getComments().size() - 1).getCreated_at();
                        break;
                    case "questions":
                        LiveGetQuestionsRequest qrequest = new LiveGetQuestionsRequest();
                        IGResponse qresponse = qrequest.execute(client).join();
                        break;
                    case "likes":
                        LiveBroadcastGetLikeCountRequest lrequest =
                                new LiveBroadcastGetLikeCountRequest(id, like_ts);
                        LiveBroadcastGetLikeCountResponse lResponse =
                                lrequest.execute(client).join();
                        logger.info("Total likes : {}", lResponse.getLikes());
                        like_ts = lResponse.getLike_ts();
                        break;
                    case "activate":
                        LiveQuestionActivateRequest actRequest =
                                new LiveQuestionActivateRequest(id, input.split(" ")[1]);
                        actRequest.execute(client).join();
                        break;
                    case "deactivate":
                        LiveQuestionDeactivateRequest deactRequest =
                                new LiveQuestionDeactivateRequest(id, input.split(" ")[1]);
                        deactRequest.execute(client).join();
                        break;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        client.sendRequest(new LiveEndBroadcastRequest(id)).join();
        in.close();
    }

}
