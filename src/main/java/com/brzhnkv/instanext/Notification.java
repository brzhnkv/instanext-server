package com.brzhnkv.instanext;

public class Notification {

    public String status;
    public String task;   

    public Notification() {
    }

    public Notification(String status) {
        this.status = status;
    }
    
    public void setStatus(String status) {
    	this.status = status;
    }
    public void setTask(String task) {
    	this.task = task;
    }
}