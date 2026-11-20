**Tech stack:**

• Java 17
• Springboot 3.4.3
• Spring AI 1.0.0-M6
• PostgreSQL,
• open-source Dockerized models 
	-llama.cpp llama-server for LLM chat/SQL extraction
	-Kokoro-FastAPI for open-source OpenAI-compatible TTS
	-LocalAI / stable-diffusion.cpp for open-source OpenAI-compatible image generation

**Architecture Overview**

• LLM & Database Extraction (llama.cpp llama-server): Runs Qwen2.5-7B-Instruct locally. Spring AI's ChatClient connects to llama-server's /v1/chat/completions endpoint. Instead of standard Spring Data JPA queries for extracting flashcards, the user asks natural-language questions (e.g., "Show me Spanish food words due for review today"), and Spring AI uses Function Calling (@Tool) + schema-grounded JdbcTemplate execution to translate prompts into safe read-only SELECT SQL queries against PostgreSQL.
• Text-to-Speech (Kokoro-FastAPI): An open-source TTS container exposing an OpenAI-compatible /v1/audio/speech endpoint wired directly into Spring AI's OpenAiAudioSpeechModel.
• Image Generation (LocalAI with stablediffusion): An open-source container exposing an OpenAI-compatible /v1/images/generations endpoint wired directly into Spring AI's OpenAiImageModel.

**Steps to test:**

1. Launch all containers
docker compose up --build
2. Generate a flashcard (Definition + Image + Audio + DB Save)
curl -X POST "http://localhost:8080/api/flashcards?word=mariposa&language=Spanish"
3. Extract flashcards from PostgreSQL via an AI prompt:
curl -X POST "http://localhost:8080/api/flashcards/ask" \
  -H "Content-Type: application/json" \
  -d '{"prompt": "Find all Spanish flashcards due for review right now and quiz me on their definitions."}'
4. Play pronunciation audio or view the generated image in a browser:
	• Audio: http://localhost:8080/api/flashcards/1/audio
	• Image: http://localhost:8080/api/flashcards/1/image
	
