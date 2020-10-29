package com.example.UserAuthentication.Service.Impl;

import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class FirebaseInitializer {

	@Autowired
	Environment env;

	@PostConstruct
	private void startDB() {

		try {

			InputStream serviceAccount = this.getClass().getClassLoader()
					.getResourceAsStream(env.getProperty("firebase.key.json"));

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl(env.getProperty("firebase.db.url")).build();

			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("startDB error");
		}

	}

	public Firestore getFirebase() {
		return FirestoreClient.getFirestore();
	}

}
