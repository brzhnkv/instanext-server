package com.brzhnkv.liketime.util;

import com.brzhnkv.liketime.Main;
import com.brzhnkv.liketime.user.TempUser;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineMedia;
import com.github.instagram4j.instagram4j.requests.media.MediaActionRequest;
import com.github.instagram4j.instagram4j.requests.media.MediaActionRequest.MediaAction;
import com.github.instagram4j.instagram4j.responses.IGResponse;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public final class Utility {
	public static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void likePost(IGClient client, TimelineMedia post) {
			String media_id = post.getId();
			IGResponse response = null;
			response = new MediaActionRequest(media_id, MediaAction.LIKE).execute(client).exceptionally(e -> {
				TempUser.postsDeletedCount++;
				return null;
			}).join();
			//Assert.assertEquals("ok", response.getStatus());
	}

	public static void unlikePost(IGClient client, TimelineMedia post) {
			String media_id = post.getId();
			IGResponse response = new MediaActionRequest(media_id, MediaAction.UNLIKE).execute(client).exceptionally(e -> {
				TempUser.postsDeletedCount++;
				return null;
			}).join();
			//Assert.assertEquals("ok", response.getStatus());
	}


	public static void savePost(IGClient client, TimelineMedia post) {
			String media_id = post.getId();
			IGResponse response = new MediaActionRequest(media_id, MediaAction.SAVE).execute(client).exceptionally(e -> {
				TempUser.postsDeletedCount++;
				return null;
			}).join();
			//Assert.assertEquals("ok", response.getStatus());
	}

	public static void unsavePost(IGClient client, TimelineMedia post) {
			String media_id = post.getId();
			IGResponse response = new MediaActionRequest(media_id, MediaAction.UNSAVE).execute(client).exceptionally(e -> {
				TempUser.postsDeletedCount++;
				return null;
			}).join();
			//Assert.assertEquals("ok", response.getStatus());
	}


}