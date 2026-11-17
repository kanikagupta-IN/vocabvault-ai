package com.vocabvault.model;

import com.vocabvault.dto.AiGeneratedFlashcardData;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "flashcards")
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;
    private String targetLanguage;
//    private String englishTranslation;
    private String category;
    private String definition;

    @Column(length = 500)
    private String exampleSentence;
    private String imagePrompt;
//    private String imageBase64;
//    private String audioBase64;
    int repetitionCount;
    int intervalDays;


    private String imagePath;
    private String audioPath;
//    private int masteryScore = 1; // 1 (New) to 5 (Mastered)
    private LocalDateTime nextReviewAt;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Flashcard() {}

    public Flashcard(AiGeneratedFlashcardData data, String imagePath, String audioPath) {
        this.word = data.word();
        this.targetLanguage = data.targetLanguage();
//        this.englishTranslation = data.englishTranslation();
        this.category = data.category();
        this.definition = data.definition();
        this.exampleSentence = data.exampleSentence();
        this.imagePrompt = data.imagePrompt();
        this.imagePath = imagePath;
        this.audioPath = audioPath;
        this.nextReviewAt = LocalDateTime.now().plusDays(1);
    }

    // Standard Getters & Setters
    public Long getId() { return id; }
    public String getWord() { return word; }
    public String getTargetLanguage() { return targetLanguage; }
//    public String getEnglishTranslation() { return englishTranslation; }
    public String getCategory() { return category; }
    public String getDeinition() { return definition; }
    public String getExampleSentence() { return exampleSentence; }
    public String getImagePrompt() { return imagePrompt; }
    public String getImagePath() { return imagePath; }
    public String getAudioPath() { return audioPath; }
//    public int getMasteryScore() { return masteryScore; }
//    public void setMasteryScore(int masteryScore) { this.masteryScore = masteryScore; }
    public LocalDateTime getNextReviewAt() { return nextReviewAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
