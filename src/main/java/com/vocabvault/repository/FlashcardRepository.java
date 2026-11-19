package com.vocabvault.repository;

import com.vocabvault.model.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

//    @Query("""
//        SELECT f FROM Flashcard f
//        WHERE (:language IS NULL OR LOWER(f.targetLanguage) = LOWER(:language))
//          AND (:category IS NULL OR LOWER(f.category) = LOWER(:category))
//          AND (:maxMastery IS NULL OR f.masteryScore <= :maxMastery)
//        ORDER BY f.masteryScore ASC, f.createdAt DESC
//    """)
    @Query("""
        SELECT f FROM Flashcard f
        WHERE (:language IS NULL OR LOWER(f.targetLanguage) = LOWER(:language))
          AND (:category IS NULL OR LOWER(f.category) = LOWER(:category))
        ORDER BY f.createdAt DESC
    """)
    List<Flashcard> searchByFilters(
            @Param("language") String language,
            @Param("category") String category
//            @Param("maxMastery") Integer maxMastery
    );
}