package com.brzhnkv.liketime.user;

import java.util.ArrayList;
import java.util.List;

public class TempUser {


    public static int postsAlreadyLikedCount = 0;
    public static int postsLikedCount = 0;
    public static int postsSavedCount = 0;
    public static int postsSentCount = 0;
    public static int postsDeletedCount = 0;

    public static boolean taskIsRunning = false;

    private static List<String> tempStatusM = new ArrayList<>();
    private static List<String> tempLogM = new ArrayList<>();

    public TempUser() {
    }

    public static void addTempStatusMessage(String m) {
        tempStatusM.add(m);
    }


    public static void addTempLogMessage(String m) {
        tempLogM.add(m);
    }


    public static List<String> getTempStatusMessage() {
        return tempStatusM;
    }


    public static List<String> getTempLogMessage() {
        return tempLogM;
    }

    public static void clear() {
        postsAlreadyLikedCount = 0;
        postsLikedCount = 0;
        postsSavedCount = 0;
        postsSentCount = 0;
        postsDeletedCount = 0;
        taskIsRunning = true;
        tempStatusM.clear();
        tempLogM.clear();
    }
}
