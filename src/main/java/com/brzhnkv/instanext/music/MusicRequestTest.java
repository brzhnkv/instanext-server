package com.brzhnkv.instanext.music;

import com.brzhnkv.instanext.Main;
import org.junit.Assert;
import org.junit.Test;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.requests.music.MusicBrowseRequest;
import com.github.instagram4j.instagram4j.requests.music.MusicGenresIdRequest;
import com.github.instagram4j.instagram4j.requests.music.MusicGetGenresRequest;
import com.github.instagram4j.instagram4j.requests.music.MusicSearchRequest;
import com.github.instagram4j.instagram4j.requests.music.MusicTrackLyricsRequest;
import com.github.instagram4j.instagram4j.requests.music.MusicTrendingRequest;
import com.github.instagram4j.instagram4j.responses.IGResponse;
import com.github.instagram4j.instagram4j.responses.music.MusicBrowseResponse;
import com.github.instagram4j.instagram4j.responses.music.MusicGetResponse;
import com.github.instagram4j.instagram4j.responses.music.MusicTrackLyricsResponse;
import com.github.instagram4j.instagram4j.responses.music.MusicTrackResponse;

import lombok.extern.slf4j.Slf4j;
import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class MusicRequestTest {
    public Logger logger = LoggerFactory.getLogger(Main.class);
    private final SerializeTestUtil serializeTestUtil;

    public MusicRequestTest(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testBrowse() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        MusicBrowseResponse response = new MusicBrowseRequest().execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
        response.getItems().stream()
                .flatMap(m -> m.getPreview_items().stream())
                .forEach(m -> logger.debug("{} : {} : {}", m.getId(), m.getTitle(),
                        m.getProgressive_download_url()));
        logger.debug("Success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testTrend() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        MusicTrackResponse response = new MusicTrendingRequest().execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
        response.getItems().stream()
                .forEach(m -> logger.debug("{} : {} : {}", m.getId(), m.getTitle(),
                        m.getProgressive_download_url()));
        logger.debug("Success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testSearch() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        MusicTrackResponse response = new MusicSearchRequest("the box").execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
        response = new MusicSearchRequest("the box", response.getPage_info().getNext_max_id())
                .execute(client).join();
        logger.debug("Success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testMoods() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        MusicGetResponse response = new MusicGetGenresRequest().execute(client).join();
        response.getIds().forEach(logger::debug);
        Assert.assertEquals("ok", response.getStatus());
        logger.debug("Success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testGenreId() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        IGResponse response = new MusicGenresIdRequest("pop").execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
        logger.debug("Success");
    }

    @Test
    // Run SerializeTestUtil.serializeLogin first to generate saved sessions
    public void testLyrics() throws Exception {
        IGClient client = serializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        MusicTrackLyricsResponse response =
                new MusicTrackLyricsRequest("319999298968664").execute(client).join();
        Assert.assertEquals("ok", response.getStatus());
        response.getLyrics().getPhrases().forEach(s -> logger.debug(s.getPhrase()));
        logger.debug("Success");
    }
}
