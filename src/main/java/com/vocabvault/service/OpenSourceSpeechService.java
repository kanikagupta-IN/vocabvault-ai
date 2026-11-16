package com.vocabvault.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class OpenSourceSpeechService {

    private final RestClient restClient;

    public OpenSourceSpeechService(@Value("${app.ai.tts-url}") String ttsUrl) {
        this.restClient = RestClient.create(ttsUrl);
    }

    public byte[] generateSpeechMp3(String textToSpeak) {
        var payload = Map.of(
                "model", "kokoro",
                "input", textToSpeak,
                "voice", "af_bella",
                "response_format", "mp3"
        );

        return restClient.post()
                .body(payload)
                .retrieve()
                .body(byte[].class);
    }
}
