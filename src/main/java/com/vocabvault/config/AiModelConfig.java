package com.vocabvault.config;

import org.springframework.ai.image.*;
//import org.springframework.ai.image.ImageGeneration;
//import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

public class AiModelConfig {
    @Bean
    public ImageModel localStableDiffusionModel(
            @Value("${app.ai.stable-diffusion-url}") String sdUrl) {

        RestClient restClient = RestClient.create(sdUrl);

        return request -> {
            String prompt = request.getInstructions().get(0).getText();

            var payload = Map.of(
                    "prompt", prompt + ", flat vector illustration, clean educational style",
                    "steps", 20,
                    "width", 512,
                    "height", 512
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restClient.post()
                    .body(payload)
                    .retrieve()
                    .body(Map.class);

            @SuppressWarnings("unchecked")
            List<String> images = (List<String>) response.get("images");
            Image image = new Image(null, images.get(0)); // Base64 image string
            return new ImageResponse(List.of(new ImageGeneration(image)));
        };
    }
}
