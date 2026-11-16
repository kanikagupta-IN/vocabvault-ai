package com.vocabvault.dto;

import java.time.LocalDateTime;

public record FlashcardData(
        Long id,
        String word,
        String targetLanguage,
        String category,
        String definition,
        String exampleSentence,
        String imageBase64,
        String audioBase64,
        int repetitionCount,
        int intervalDays,
        LocalDateTime nextReviewAt
) {
}
