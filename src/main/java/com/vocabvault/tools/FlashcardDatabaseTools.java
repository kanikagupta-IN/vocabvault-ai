package com.vocabvault.tools;

import com.vocabvault.model.Flashcard;
import com.vocabvault.repository.FlashcardRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FlashcardDatabaseTools {

    private final FlashcardRepository repository;

    public FlashcardDatabaseTools(FlashcardRepository repository) {
        this.repository = repository;
    }

    @Tool(description = "Extract flashcards from the PostgreSQL database based on natural language filters like language, topic category, or mastery level.")
    public List<Flashcard> queryFlashcards(
            @ToolParam(description = "Target language such as Spanish, French, German. Pass null if not specified.") String language,
            @ToolParam(description = "Topic category such as Food, Travel, Verbs, Animals. Pass null if not specified.") String category
//            @ToolParam(description = "Maximum mastery score (1 to 5). Use 2 if the user asks for hard, difficult, or weak words. Pass null if not specified.") Integer maxMastery
    ) {
//        return repository.searchByFilters(language, category, maxMastery);
        return repository.searchByFilters(language, category);
    }
}
