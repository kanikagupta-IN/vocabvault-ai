CREATE TABLE IF NOT EXISTS flashcards (
    id BIGSERIAL PRIMARY KEY,
    word VARCHAR(120) NOT NULL,
    target_language VARCHAR(60) NOT NULL,
    category VARCHAR(80) NOT NULL,
    definition TEXT NOT NULL,
    example_sentence TEXT NOT NULL,
    mnemonic_image_prompt TEXT NOT NULL,
    image_base64 TEXT,
    audio_base64 TEXT,
    repetition_count INT NOT NULL DEFAULT 0,
    easiness_factor DOUBLE PRECISION NOT NULL DEFAULT 2.5,
    interval_days INT NOT NULL DEFAULT 1,
    next_review_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_flashcards_lang_cat ON flashcards (target_language, category);