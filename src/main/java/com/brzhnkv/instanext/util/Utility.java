package com.brzhnkv.instanext.util;

import org.junit.Assert;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineMedia;
import com.github.instagram4j.instagram4j.requests.media.MediaActionRequest;
import com.github.instagram4j.instagram4j.requests.media.MediaActionRequest.MediaAction;
import com.github.instagram4j.instagram4j.responses.IGResponse;


public final class Utility {
	
    
    public static void likePost(IGClient client, TimelineMedia post) {
    	String media_id = post.getId();
    	IGResponse response = null;
   
		do {
			response = new MediaActionRequest(media_id, MediaAction.LIKE).execute(client).join();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (post.isHas_liked());
		
		Assert.assertEquals("ok", response.getStatus());
	}

	public static void unlikePost(IGClient client, TimelineMedia post) {
		String media_id = post.getId();
		
		IGResponse response = new MediaActionRequest(media_id, MediaAction.UNLIKE).execute(client).join();
		Assert.assertEquals("ok", response.getStatus());
	}

	public static void savePost(IGClient client, TimelineMedia post) {
		String media_id = post.getId();
		
		IGResponse response = new MediaActionRequest(media_id, MediaAction.SAVE).execute(client).join();
		Assert.assertEquals("ok", response.getStatus());
	}
	
	public static void unsavePost(IGClient client, TimelineMedia post) {
		String media_id = post.getId();
		
		IGResponse response = new MediaActionRequest(media_id, MediaAction.UNSAVE).execute(client).join();
		Assert.assertEquals("ok", response.getStatus());
	}
	
	
}