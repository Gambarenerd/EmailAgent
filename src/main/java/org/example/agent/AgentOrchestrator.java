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
        System.out.println("[AgentOrchestrator] AgentOrchestrator started with request: " + request);

        String extractedInfoEmail = ollamaService.processEmail(
                request,
                promptConfig.getPrompt("system","check_email"));
        System.out.println("[AgentOrchestrator] Informazioni estratte da processEmail: " + extractedInfoEmail);

        String extractedInfoCalendar = ollamaService.checkCalendar(
                promptConfig.getPrompt("user", "check_calendar"),
                promptConfig.getPrompt("system", "check_calendar"));
        System.out.println("[AgentOrchestrator] Informazioni estratte da checkCalendar: " + extractedInfoCalendar);

        String emailProcessed = ollamaService.processEmail(
                promptConfig.getPrompt("user", "send_reply"),
                promptConfig.getPrompt("system","send_reply"));;
        System.out.println("[AgentOrchestrator] Email processata: " + emailProcessed);

        String setEvent = ollamaService.checkCalendar(
                promptConfig.getPrompt("user", "set_calendar"),
                promptConfig.getPrompt("system", "set_calendar"));
        System.out.println("[AgentOrchestrator] Set Calendar Event: " + setEvent);

        return extractedInfoCalendar;
    }
}