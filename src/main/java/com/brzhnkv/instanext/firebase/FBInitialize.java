package com.brzhnkv.instanext.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;

@Service
public class FBInitialize {
    @PostConstruct
    public void initialize() {
        try {
            //String bucketName = environment.getRequiredProperty("FIREBASE_BUCKET_NAME");
            String projectId = "liketime-app";
            FileInputStream serviceAccount =
                    new FileInputStream("./liketime-app-firebase-adminsdk.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://databaseName.firebaseio.com")
                    // Or other region, e.g. <databaseName>.europe-west1.firebasedatabase.app
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}