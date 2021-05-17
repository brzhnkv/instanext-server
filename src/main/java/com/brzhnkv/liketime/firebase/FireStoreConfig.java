package com.brzhnkv.liketime.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FireStoreConfig {

    @Bean
    public Firestore getFireStore() throws IOException {
        String fileName = "liketime-server-firebase-adminsdk.json";
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream serviceAccount = classLoader.getResourceAsStream(fileName);

        // Use a service account
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setStorageBucket("liketime-server.appspot.com")
                .build();
        FirebaseApp.initializeApp(options);

        Firestore db = FirestoreClient.getFirestore();

        return db;
    }
}
