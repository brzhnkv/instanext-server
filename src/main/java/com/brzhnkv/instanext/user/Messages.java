package com.brzhnkv.instanext.user;

import java.util.List;

public class Messages {

    private List<String> statusMessages;
    private List<String> logMessages;

    private boolean status;


    public Messages() {}

    public Messages(boolean status, List<String> statusMessages, List<String> logMessages) {
        this.statusMessages = statusMessages;
        this.logMessages = logMessages;
        this.status = status;
    }

    public List<String> getStatusMessages() {
        return statusMessages;
    }

    public List<String> getLogMessages() {
        return logMessages;
    }

    public void setStatusMessages(List<String> messages) {
        this.statusMessages = messages;
    }

    public void setLogMessages(List<String> messages) {
        this.logMessages = messages;
    }

    public void clearMessages() {
        statusMessages.clear();
        logMessages.clear();
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
