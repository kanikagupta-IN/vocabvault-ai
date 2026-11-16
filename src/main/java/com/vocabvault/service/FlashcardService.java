package com.vocabvault.service;

import com.vocabvault.dto.FlashcardData;
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
        FlashcardData data = chatClient.prompt()
                .user(u -> u.text("""
                        Create a language flashcard for the {language} word '{word}'.
                        Provide the English translation, a single-word topic category,
                        a short example sentence in {language}, and a descriptive image prompt.
                        """)
                        .param("language", targetLanguage)
                        .param("word", word))
                .call()
                .entity(FlashcardData.class);

        // 2. Generate image via local Stable Diffusion
        var imageResponse = imageModel.call(new ImagePrompt(data.imagePrompt()));
        String base64Img = imageResponse.getResult().getOutput().getB64Json();
        String imageFilename = UUID.randomUUID() + ".png";
        Files.write(storageDir.resolve(imageFilename), Base64.getDecoder().decode(base64Img));

        // 3. Generate pronunciation audio via local Kokoro/Piper TTS
        String textToSpeak = data.word() + ". " + data.exampleSentence();
        byte[] audioBytes = speechService.generateSpeechMp3(textToSpeak);
        String audioFilename = UUID.randomUUID() + ".mp3";
        Files.write(storageDir.resolve(audioFilename), audioBytes);

        // 4. Save entity to PostgreSQL
        Flashcard card = new Flashcard(data, "/media/" + imageFilename, "/media/" + audioFilename);
        return repository.save(card);
    }

    public String askDatabaseWithPrompt(String userPrompt) {
        return chatClient.prompt()
                .system("""
                        You are a helpful language tutor assistant.
                        Always use the 'queryFlashcards' tool to fetch real flashcards from the database
                        when the user asks to study, review, or find words.
                        Present the retrieved words, translations, example sentences, and media paths clearly.
                        """)
                .user(userPrompt)
                .tools(databaseTools)
                .call()
                .content();
    }
}