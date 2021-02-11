package com.brzhnkv.instanext;


import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import com.github.instagram4j.instagram4j.IGClient;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;

@Slf4j
public class TestTemplate {
    public Logger logger = LoggerFactory.getLogger(Main.class);
    private final SerializeTestUtil serializeTestUtil;
	
	  private String username = "galinabraz"; 
	  private String password = "lapushok2010";
	  
	  public IGClient client = new IGClient(username, password);

    public TestTemplate(SerializeTestUtil serializeTestUtil) {
        this.serializeTestUtil = serializeTestUtil;
    }

    public void testName(String username, String password) throws Exception {
    	serializeTestUtil.serializeLogin(username, password);

        String clientClassPath = "classpath:/client" + username + ".ser";
        String cookieClassPath = "classpath:/cookie" + username + ".ser";
        int count = 0;
        while(count != 3) {
            try {
                client = serializeTestUtil.getClientFromSerialize(clientClassPath, cookieClassPath);

                break;
            } catch (FileNotFoundException e) {
                File igClientFile = new File(clientClassPath);
                File igCookieFile = new File(cookieClassPath);
                logger.info("3423523453463463");
                count++;
            }
        }

        logger.debug(client.getSessionId());
        logger.debug("Success");
    }
}
