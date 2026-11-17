package com.vocabvault.controller;

import com.vocabvault.dto.StudyQueryRequest;
import com.vocabvault.model.Flashcard;
import com.vocabvault.service.FlashcardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController {

    private final FlashcardService flashcardService;

    public FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

//    @PostMapping
//    public Flashcard create(@RequestBody CreateRequest request) throws IOException {
//        return flashcardService.createFlashcard(request.word(), request.language());
//    }
//
//    @PostMapping("/ai-query")
//    public String queryWithAi(@RequestBody StudyPromptRequest request) {
//        return flashcardService.askDatabaseWithPrompt(request.prompt());
//    }

    // Generate & save a new multimodal flashcard
    @PostMapping
    public ResponseEntity<Flashcard> createCard(
            @RequestParam String word,
            @RequestParam(defaultValue = "Spanish") String language) throws IOException {
        return ResponseEntity.ok(flashcardService.createFlashcard(word, language));
    }

    // Extract flashcards from PostgreSQL using natural-language AI prompts
    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> askDatabase(@RequestBody StudyQueryRequest request) {
        String answer = flashcardService.askDatabaseWithPrompt(request.prompt());
        return ResponseEntity.ok(Map.of("response", answer));
    }
}
