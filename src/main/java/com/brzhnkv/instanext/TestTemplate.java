package com.brzhnkv.instanext;


import com.brzhnkv.instanext.serialize.SerializeTestUtil;
import com.github.instagram4j.instagram4j.IGClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestTemplate {
	
	private String username = "galinabraz";
	private String password = "lapushok2010";
	
	public IGClient client = new IGClient(username, password);
	SerializeTestUtil ser = new SerializeTestUtil();
    public void testName() throws Exception {
    	ser.serializeLogin(username, password);
        client = SerializeTestUtil.getClientFromSerialize("igclient.ser", "cookie.ser");
        log.debug(client.getSessionId());
        log.debug("Success");
    }
}
