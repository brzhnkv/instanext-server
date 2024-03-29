package com.brzhnkv.instanext.upload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Assert;
import org.junit.Test;

import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.exceptions.IGResponseException;
import com.github.instagram4j.instagram4j.models.location.Location.Venue;
import com.github.instagram4j.instagram4j.models.media.UserTags.UserTagPayload;
import com.github.instagram4j.instagram4j.requests.IGRequest;
import com.github.instagram4j.instagram4j.requests.locationsearch.LocationSearchRequest;
import com.github.instagram4j.instagram4j.requests.media.MediaConfigureTimelineRequest;
import com.github.instagram4j.instagram4j.requests.media.MediaConfigureTimelineRequest.MediaConfigurePayload;
import com.github.instagram4j.instagram4j.requests.upload.RuploadPhotoRequest;
import com.github.instagram4j.instagram4j.responses.media.MediaResponse.MediaConfigureTimelineResponse;
import com.github.instagram4j.instagram4j.responses.media.RuploadPhotoResponse;

public class UploadPhotoTest {
    private final SerializeTestUtil serializeTestUtil;

    public UploadPhotoTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void uploadTest()
            throws IGLoginException, IOException, IGResponseException, ClassNotFoundException {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        File file = new File("src/main/resources/4to5ratio.jpg");
        byte[] imgData = Files.readAllBytes(file.toPath());
        IGRequest<RuploadPhotoResponse> uploadReq = new RuploadPhotoRequest(imgData, "1");
        Venue location = new LocationSearchRequest(0d, 0d, "london bridge").execute(client).join()
                .getVenues().get(0);
        String id = client.sendRequest(uploadReq).join().getUpload_id();
        IGRequest<MediaConfigureTimelineResponse> configReq = new MediaConfigureTimelineRequest(
                new MediaConfigurePayload().upload_id(id).caption("This is test").location(location)
                        .usertags(new UserTagPayload(18428658l, 0.5, 0.5)));
        MediaConfigureTimelineResponse response = client.sendRequest(configReq).join();

        Assert.assertEquals("ok", response.getStatus());
    }
}
