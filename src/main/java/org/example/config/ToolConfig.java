package org.example.config;

import org.example.tools.DateTimeTools;
import org.example.tools.GmailTool;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolConfig {

    @Bean
    public ToolCallback[] toolCallbacks( DateTimeTools dateTimeTools, GmailTool gmailTool) {
        return ToolCallbacks.from(dateTimeTools, gmailTool);
    }
}