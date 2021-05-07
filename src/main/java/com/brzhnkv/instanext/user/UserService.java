package com.brzhnkv.instanext.user;

import com.brzhnkv.instanext.Main;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void createUser(User user, String username) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> future = db.collection("users").document(username).set(user);
        WriteResult result = future.get();
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

    public void updateMessages(String username, boolean status, List<String> statusMessages, List<String> logMessages) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("statusMessages", statusMessages);
        updates.put("logMessages", logMessages);
        updates.put("status", status);
        ApiFuture<WriteResult> writeResult = db.collection("messages").document(username).update(updates);
    }




    public boolean isUserLoggedIn(String username, String password) {
        Optional<User> userByUsername = userRepository.findByUsername(username);
        if (userByUsername.isPresent())
            if (userByUsername.get().getPassword().equals(password)) return true;
            else throw new IllegalStateException("invalid password");

        return false;
    }

    public Optional<User> getUserByUsernameAndToken(String username, String token) {
        Optional<User> userByUsernameAndToken = userRepository.findByUsernameAndToken(username, token);

        return userByUsernameAndToken;
    }


    @Transactional
    public void clearMessages(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("user with username " + username + " does not exist"));
        user.clear();
    }

    @Transactional
    public boolean getStatus(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("user with username " + username + " does not exist"));
        boolean status = user.getStatus();
        return status;
    }

    @Transactional
    public void setStatus(String username, boolean status) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("user with username " + username + " does not exist"));
        user.setStatus(status);
    }
}
