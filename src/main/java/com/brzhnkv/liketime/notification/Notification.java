package com.brzhnkv.liketime.notification;

import java.util.HashMap;

public class Notification {

    public String status;
    public String task;   
    public HashMap<String, String> data = new HashMap<String, String>();

    public Notification() {
    }

    public Notification(String status) {
        this.status = status;
    }
    
    public Notification(HashMap<String, String> data) {
    	this.data = data;
    }
    
    public void setStatus(String status) {
    	this.status = status;
    }
    public void setTask(String task) {
    	this.task = task;
    }
}