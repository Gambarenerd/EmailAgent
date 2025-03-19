package org.example.controller;

import org.example.agent.AgentOrchestrator;
import org.example.service.OllamaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgentController {

    private final OllamaService ollamaService;
    private final AgentOrchestrator orchestrator;


    public AgentController(OllamaService ollamaService, AgentOrchestrator orchestrator) {
        this.ollamaService = ollamaService;
        this.orchestrator = orchestrator;
    }

    @GetMapping("/test-model")
    public String testModel(@RequestParam String prompt) {
        return ollamaService.generateResponse(prompt);
    }

    @GetMapping("/test-tool")
    public String testTool(@RequestParam String prompt) {
        return ollamaService.generateResponseWithTool(prompt);
    }

    @GetMapping("/check-email")
    public String checkEmail(@RequestParam String request) {
        return orchestrator.checkAndProcessEmail(request);
    }
}
