package com.vocabvault.service;

import com.vocabvault.dto.AiGeneratedFlashcardData;
import com.vocabvault.model.Flashcard;
import com.vocabvault.repository.FlashcardRepository;
import com.vocabvault.tools.FlashcardDatabaseTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.UUID;

@Service
public class FlashcardService {

    private final ChatClient chatClient;
    private final ImageModel imageModel;
    private final OpenSourceSpeechService speechService;
    private final FlashcardRepository repository;
    private final FlashcardDatabaseTools databaseTools;
    private final Path storageDir;

    public FlashcardService(
            ChatClient.Builder chatClientBuilder,
            ImageModel imageModel,
            OpenSourceSpeechService speechService,
            FlashcardRepository repository,
            FlashcardDatabaseTools databaseTools,
            @Value("${app.media.storage-dir}") String storageDir) throws IOException {

        this.chatClient = chatClientBuilder.build();
        this.imageModel = imageModel;
        this.speechService = speechService;
        this.repository = repository;
        this.databaseTools = databaseTools;
        this.storageDir = Path.of(storageDir);
        Files.createDirectories(this.storageDir);
    }

    public Flashcard createFlashcard(String word, String targetLanguage) throws IOException {
        // 1. Generate structured flashcard text via Ollama
        // Step A: Generate structured definition & visual prompt via llama-server
        AiGeneratedFlashcardData data = chatClient.prompt()
                .system("""
                        You are an expert language tutor. Given a vocabulary word and target language,
                        provide a clear English definition, a natural example sentence in the target language
                        with English translation, a single-word topic category, and a vivid visual scene
                        description (under 25 words, no text inside the image) to help memorize the word.
                        """)
                .user(u -> u.text("Word: {word}\nTarget Language: {language}")
                        .param("language", targetLanguage)
                        .param("word", word))
                .call()
                .entity(AiGeneratedFlashcardData.class);

        // Step B: Generate illustrative mnemonic image via open-source LocalAI Stable Diffusion
        var imageResponse = imageModel.call(new ImagePrompt(data.imagePrompt()));
        String base64Img = imageResponse.getResult().getOutput().getB64Json();
        String imageFilename = UUID.randomUUID() + ".png";
        Files.write(storageDir.resolve(imageFilename), Base64.getDecoder().decode(base64Img));

        // Step C: Generate native pronunciation audio via open-source local Kokoro TTS
        String textToSpeak = data.word() + ". " + data.exampleSentence();
        byte[] audioBytes = speechService.generateSpeechMp3(textToSpeak);
        String audioFilename = UUID.randomUUID() + ".mp3";
        Files.write(storageDir.resolve(audioFilename), audioBytes);

        // Step D: Store entity in PostgreSQL
        Flashcard card = new Flashcard(data, "/media/" + imageFilename, "/media/" + audioFilename);
        return repository.save(card);
    }

    public String askDatabaseWithPrompt(String userPrompt) {
        return chatClient.prompt()
                .system("""
                        You are a database assistant for VocabVault AI.
                        Use the 'queryFlashcards' tool to retrieve vocabulary data from PostgreSQL
                        based on the user's request.
                        Current timestamp function in PostgreSQL is CURRENT_TIMESTAMP.
                        Summarize the extracted flashcards clearly for the learner.
                        """)
                .user(userPrompt)
                .tools(databaseTools)
                .call()
                .content();
    }
}