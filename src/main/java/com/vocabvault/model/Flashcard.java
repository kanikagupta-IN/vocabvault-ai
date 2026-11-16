package com.vocabvault.model;

import com.vocabvault.dto.FlashcardData;
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
    private String englishTranslation;
    private String category;

    @Column(length = 500)
    private String exampleSentence;

    private String imagePath;
    private String audioPath;
    private int masteryScore = 1; // 1 (New) to 5 (Mastered)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Flashcard() {}

    public Flashcard(FlashcardData data, String imagePath, String audioPath) {
        this.word = data.word();
        this.targetLanguage = data.targetLanguage();
        this.englishTranslation = data.englishTranslation();
        this.category = data.category();
        this.exampleSentence = data.exampleSentence();
        this.imagePath = imagePath;
        this.audioPath = audioPath;
    }

    // Standard Getters & Setters
    public Long getId() { return id; }
    public String getWord() { return word; }
    public String getTargetLanguage() { return targetLanguage; }
    public String getEnglishTranslation() { return englishTranslation; }
    public String getCategory() { return category; }
    public String getExampleSentence() { return exampleSentence; }
    public String getImagePath() { return imagePath; }
    public String getAudioPath() { return audioPath; }
    public int getMasteryScore() { return masteryScore; }
    public void setMasteryScore(int masteryScore) { this.masteryScore = masteryScore; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
