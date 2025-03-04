package org.example.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OllamaService {

    private final OllamaChatModel chatModel;

    public OllamaService(OllamaChatModel chatModel){
        this.chatModel = chatModel;
    }

    public String generateResponse(String promptText) {
        ChatResponse response = chatModel.call(
                new Prompt(
                        promptText,
                        OllamaOptions.builder()
                                .model("llama3.3")
                                .temperature(0.4)
                                .build()
                ));

        return response.getResult().getOutput().getText();
    }
}
