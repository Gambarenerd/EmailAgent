package org.example.agent;

import org.example.config.PromptConfig;
import org.example.service.OllamaService;
import org.springframework.stereotype.Service;

@Service
public class AgentOrchestrator {

    private final OllamaService ollamaService;
    private final PromptConfig promptConfig;

    public AgentOrchestrator(OllamaService ollamaService, PromptConfig promptConfig) {
        this.ollamaService = ollamaService;
        this.promptConfig = promptConfig;
    }

    public String checkAndProcessEmail(String request) {
        String extractedInfoEmail = ollamaService.processEmail(
                request,
                promptConfig.getPrompt("system","check_email"));

        String extractedInfoCalendar = ollamaService.checkCalendar(
                promptConfig.getPrompt("user", "check_calendar"),
                promptConfig.getPrompt("system", "check_calendar"));

        String emailProcessed = ollamaService.processEmail(
                promptConfig.getPrompt("user", "send_reply"),
                promptConfig.getPrompt("system","send_reply"));;

        String setEvent = ollamaService.checkCalendar(
                promptConfig.getPrompt("user", "set_calendar"),
                promptConfig.getPrompt("system", "set_calendar"));

        return extractedInfoCalendar;
    }
}