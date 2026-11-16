package com.vocabvault.controller;

import com.vocabvault.model.Flashcard;
import com.vocabvault.service.FlashcardService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController {

    private final FlashcardService flashcardService;

    public FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    public record CreateRequest(String word, String language) {}
    public record StudyPromptRequest(String prompt) {}

    @PostMapping
    public Flashcard create(@RequestBody CreateRequest request) throws IOException {
        return flashcardService.createFlashcard(request.word(), request.language());
    }

    @PostMapping("/ai-query")
    public String queryWithAi(@RequestBody StudyPromptRequest request) {
        return flashcardService.askDatabaseWithPrompt(request.prompt());
    }
}
