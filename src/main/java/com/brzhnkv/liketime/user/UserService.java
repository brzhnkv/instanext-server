package com.brzhnkv.liketime.user;

import com.brzhnkv.liketime.Main;
import com.brzhnkv.liketime.serialize.Serialize;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    public Logger logger = LoggerFactory.getLogger(Main.class);

    private final Firestore db;

    @Autowired
    public UserService(Firestore db) {
        this.db = db;
    }

    public User createUser(String username, String password) throws ExecutionException, InterruptedException, IGLoginException {
        DocumentReference docRef = db.collection("users").document(username);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        User user = null;
        if (document.exists()) {
            // convert document to POJO
            user = document.toObject(User.class);
            if (user.getPassword() == password) return user;
            else return null;
        } else {
            user = Serialize.serializeUser(username, password);
            ApiFuture<WriteResult> futureCreateUser = db.collection("users").document(username).set(user);
            WriteResult result = futureCreateUser.get();
            return user;
        }
    }

    public User getUser(String username) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("users").document(username);
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // block on response
        DocumentSnapshot document = future.get();
        User user = null;
        if (document.exists()) {
            // convert document to POJO
            user = document.toObject(User.class);
            return user;
        } else {
            logger.info("user with username " + username + " does not exist");
            return null;
        }
    }

    public void deleteUser(String username) throws ExecutionException, InterruptedException {
        WriteBatch batch = db.batch();

        DocumentReference user = db.collection("users").document(username);
        DocumentReference messages = db.collection("messages").document(username);
        batch.delete(user);
        batch.delete(messages);

        ApiFuture<List<WriteResult>> future = batch.commit();
        for (WriteResult result : future.get()) {
            logger.info("Delete time : " + result.getUpdateTime());
        }
    }

    void closeDB() throws Exception {
        db.close();
    }

    public Messages getMessages(String username) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("messages").document(username);
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // block on response
        DocumentSnapshot document = future.get();
        Messages messages = null;
        if (document.exists()) {
            // convert document to POJO
            messages = document.toObject(Messages.class);
            return messages;
        } else {
            logger.info("empty");
            return null;
        }
    }

    public void updateMessages(String username, boolean status, List<String> statusMessages, List<String> logMessages)  {
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", status);
        updates.put("statusMessages", statusMessages);
        updates.put("logMessages", logMessages);
        try {
            db.collection("messages").document(username).update(updates).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void updateMessages(String username, Messages messages) {
        try {
            db.collection("messages").document(username).set(messages).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
