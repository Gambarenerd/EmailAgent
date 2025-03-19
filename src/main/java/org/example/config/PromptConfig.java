package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class PromptConfig {

    private final Map<String, Map<String, String>> prompts;

    public PromptConfig() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.prompts = objectMapper.readValue(new ClassPathResource("prompts.json").getInputStream(), Map.class);
    }

    public String getPrompt(String category, String key) {
        return prompts.getOrDefault(category, Map.of()).getOrDefault(key, "Prompt non trovato");
    }

    @Override
    public String toString() {
        return "PromptConfig{" +
                "prompts=" + prompts +
                '}';
    }
}