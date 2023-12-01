//package com.fp.backend.system.config.firebase;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import javax.annotation.PostConstruct;
//import java.io.IOException;
//import java.io.InputStream;
//
//@Slf4j
//@Component
//public class FCMInitializer {
//    @Value("${fcm.certification}")
//    private String credential;
//
//    @PostConstruct
//    public void initalize() throws IOException{
//        ClassPathResource resource = new ClassPathResource(credential);
//
//        try (InputStream is = resource.getInputStream()){
//            FirebaseOptions options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(is))
//                    .build();
//
//            if(FirebaseApp.getApps().isEmpty()){
//                FirebaseApp.initializeApp(options);
//                log.info("FirebaseApp initialization complete");
//            }
//        }
//    }
//
//}
