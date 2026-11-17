package com.vocabvault.dto;

public record AiGeneratedFlashcardData(
//        Long id,
        String word,
        String targetLanguage,
//        String englishTranslation,
        String category,
        String definition,
        String exampleSentence,
        String imagePrompt
//        String imageBase64,
//        String audioBase64,
//        int repetitionCount,
//        int intervalDays,
//        LocalDateTime nextReviewAt
) {
}
