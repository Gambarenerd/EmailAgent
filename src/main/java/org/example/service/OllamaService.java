package org.example.service;

import jakarta.annotation.PostConstruct;
import org.example.tools.CalendarTool;
import org.example.tools.DateTimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.stereotype.Service;


@Service
public class OllamaService {

    private final OllamaChatModel chatModel;
    private final ToolCallback[] tools;
    private final CalendarTool calendarTool;
    private final DateTimeTools dateTimeTools;
    private final MessageChatMemoryAdvisor chatMemoryAdvisor;

    private ToolCallback[] calendarTools;

    private final static String LLAMA_MODEL = "llama3.3";
    private final static String DEEPSEEK_MODEL = "deepseek-r1:70b";
    private final static String GEMMA_MODEL = "gemma2:27b";
    private final static String MISTRAL_LARGE_MODEL = "mistral-large:latest";

    public OllamaService(OllamaChatModel chatModel, ToolCallback[] tools, CalendarTool calendarTool, ChatMemory chatMemory, DateTimeTools dateTimeTools, MessageChatMemoryAdvisor chatMemoryAdvisor) {
        this.chatModel = chatModel;
        this.tools = tools;
        this.calendarTool = calendarTool;
        this.dateTimeTools = dateTimeTools;
        this.chatMemoryAdvisor = chatMemoryAdvisor;
    }

    @PostConstruct
    public void setupToolLists() {
        calendarTools = ToolCallbacks.from(calendarTool, dateTimeTools);
    }

    public String generateResponse(String promptText) {
        ChatResponse response = chatModel.call(
                new Prompt(
                        promptText,
                        OllamaOptions.builder()
                                .model(DEEPSEEK_MODEL)
                                .temperature(0.4)
                                .build()
                ));

        return response.getResult().getOutput().getText();
    }

    public String generateResponseWithTool(String userPrompt) {
        return ChatClient.create(chatModel)
                .prompt()
                .system("Sei un agente intelligente in grado di eseguire dei task come controllare e rispondere alle email, controllare la data di oggi. " +
                        "Sei in grado di scegliere quale Tool utilizzare analizzando le istruzioni ricevute.")
                .user(userPrompt)
                .options(OllamaOptions.builder().model(MISTRAL_LARGE_MODEL).temperature(0.4).build())
                .tools(tools)
                .call()
                .content();
    }

    public String processEmail(String userPrompt, String systemPrompt) {
        System.out.println("[OllamaService:processEmail] processing email");

        return ChatClient.create(chatModel)
                .prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .advisors(chatMemoryAdvisor)
                .options(OllamaOptions.builder().model(LLAMA_MODEL).temperature(0.4).build())
                .tools(tools)
                .call()
                .content();
    }

    public String checkCalendar(String userPrompt, String systemPrompt) {
        System.out.println("[OllamaService:checkCalendar] checking calendar");

        return ChatClient.create(chatModel)
                .prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .advisors(chatMemoryAdvisor)
                .options(OllamaOptions.builder().model(LLAMA_MODEL).temperature(0.4).build())
                .tools(calendarTools)
                .call()
                .content();
    }

    public String generateFriendlyResponse() {
        return ChatClient.create(chatModel)
                .prompt()
                .user("Genera una risposta amichevole per il mittente dell'email sulla base delle informazioni precedentemente estratte." +
                        "Se nel giorno richiesto e presente un altro appuntamento informa il mittente." +
                        "Se non ci sono appuntamenti per il giorno richiesto informa il mittente.")
                .advisors(chatMemoryAdvisor)
                .options(OllamaOptions.builder().model(LLAMA_MODEL).temperature(0.4).build())
                .tools(tools)
                .call()
                .content();
    }
}
