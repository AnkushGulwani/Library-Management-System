package com.ankush.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.UUID;

@Service
public class ChatService {

    private static final String PROJECT_ID = "librarychatbot-yxwc";
    private static final String LANGUAGE_CODE = "en-US";

    public String getBotResponse(String message) throws Exception {

        String sessionId = UUID.randomUUID().toString();

        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream("src/main/resources/librarychatbot-yxwc-56ee557cdf48.json"));

        SessionsSettings sessionsSettings =
                SessionsSettings.newBuilder()
                        .setCredentialsProvider(
                                FixedCredentialsProvider.create(credentials))
                        .build();

        try (SessionsClient sessionsClient =
                     SessionsClient.create(sessionsSettings)) {

            SessionName session =
                    SessionName.of(PROJECT_ID, sessionId);

            TextInput.Builder textInput =
                    TextInput.newBuilder()
                            .setText(message)
                            .setLanguageCode(LANGUAGE_CODE);

            QueryInput queryInput =
                    QueryInput.newBuilder()
                            .setText(textInput)
                            .build();

            DetectIntentResponse response =
                    sessionsClient.detectIntent(session, queryInput);

            return response.getQueryResult().getFulfillmentText();
        }
    }
}